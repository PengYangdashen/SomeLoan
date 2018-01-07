package com.sd.pallentloan;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.pallentloan.R;
import com.sd.pallentloan.utils.AlertDialog;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Code;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.EditUtils;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.utils.StreamUtils;
import com.sd.pallentloan.utils.TimeCount;
import com.sd.pallentloan.utils.HttpUtils.CallBack;
public class RegisterActivity extends BaseActivity implements OnClickListener {
	private EditText phone, pwd, code, adv_phone;
	private Button codeBtn;
	private String rephone;
	private String reCode;
	private ImageView iv_showCode;
	private String realCode;
	private EditText et_phoneCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
	}

	private void initView() {
		phone = (EditText) findViewById(R.id.phone);
		pwd = (EditText) findViewById(R.id.pwd);
		code = (EditText) findViewById(R.id.code);
		adv_phone = (EditText) findViewById(R.id.adv_phone);
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("注册");
		Button register_btn = (Button) findViewById(R.id.register_btn);
		register_btn.setOnClickListener(this);
		codeBtn = (Button) findViewById(R.id.codeBtn);
		codeBtn.setOnClickListener(this);
		TextView problem = (TextView) findViewById(R.id.problem);
		problem.setOnClickListener(this);
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		
		check = (CheckBox) findViewById(R.id.check);
		
		et_phoneCode = (EditText) findViewById(R.id.et_phoneCodes);
		iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
		// 将验证码用图片的形式显示出来
		iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
		realCode = Code.getInstance().getCode().toLowerCase();
		iv_showCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_showCode:
			iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
			realCode = Code.getInstance().getCode().toLowerCase();
			Log.v("TAG", "realCode" + realCode);
			break;
		case R.id.codeBtn:// 验证码
			String phoneCode = et_phoneCode.getText().toString().toLowerCase();
			String phoneStr = phone.getText().toString();
			if (TextUtils.isEmpty(phoneStr)) {
				Toast.makeText(RegisterActivity.this, "手机号码不能为空", 0).show();
				return;
			}

			/** 去除手机号码中的空格 */
			if (phoneStr.contains(" ")) {
				phoneStr = phoneStr.replaceAll(" ", "");
			}

			/** 检查手机号码格式是否正确 */
			if (!EditUtils.isMobileNO(phoneStr)) {
				Toast.makeText(RegisterActivity.this, "手机号码格式不正确", 0).show();
				return;
			}
			
			if (phoneCode.contains(" ")) {
				Toast.makeText(RegisterActivity.this, "图形验证码不能为空！", 0).show();
				return;
			}

			if (phoneCode.equals(realCode)) {
				ConnectionDetector cd = new ConnectionDetector(
						RegisterActivity.this);
				if (cd.isConnectingToInternet()) {
					
					String url = Config.URL + Config.SEND_CODE + "&mobile=" + phoneStr
							+ "&type=0";
					HttpUtils.doGetAsyn(url, mHandler, Config.CODE_SEND);
				}

			} else {
				iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
				realCode = Code.getInstance().getCode().toLowerCase();
				Toast.makeText(RegisterActivity.this, "图形验证码不正确！", 0).show();
				et_phoneCode.setText("");
				return;
			}
			break;
		case R.id.register_btn:
			if (!check.isChecked()) {
				new AlertDialog(RegisterActivity.this).builder().setMsg("请先勾选上协议")
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
				return;
			}
			register();
			break;
		case R.id.problem:
			Intent intents = new Intent(RegisterActivity.this, ProblemActivity.class);
			intents.putExtra("title", "注册协议");
			intents.putExtra("url", Config.REGISTERADDRES_CODE);
			startActivity(intents);
			break;
		case R.id.backpress:
			finish();
			break;
		default:
			break;
		}
	}

	private void register() {
		String phoneStr = phone.getText().toString();
		String codeStr = code.getText().toString();
		String pwdStr = pwd.getText().toString();
		String refferee = adv_phone.getText().toString();
		String url = Config.URL + Config.REGISTER + "&mobile=" + phoneStr
				+ "&u_ip=" + codeStr + "&pwd=" + pwdStr + "&refferee="
				+ refferee + "&rephone=" + rephone + "&reCode=" + reCode;
		HttpUtils.doGetAsyn(url, mHandler, Config.CODE_RGISTER);
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.CODE_SEND:
				String reString = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(reString);
					int error = jsonObject.optInt("error");
					
					if (error > 0) {
						Toast.makeText(RegisterActivity.this,
								jsonObject.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}else{
						new TimeCount(180000, 1000, codeBtn, "重新获取").start();
						rephone = jsonObject.optString("recivePhone");
						reCode = jsonObject.optString("randomCode");
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;
			case Config.CODE_URL_ERROR:
				Toast.makeText(RegisterActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(RegisterActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;

			case Config.CODE_RGISTER:
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					int error = json.getInt("error");
					if (error > 0) {
						Toast.makeText(RegisterActivity.this,
								json.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}else {
						Toast.makeText(RegisterActivity.this,
								"注册成功", Toast.LENGTH_SHORT)
								.show();
						SharedPreferences sp =	getSharedPreferences("config",MODE_PRIVATE);
						sp.edit().putString("phone","").commit();
						startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
						finish();
					}
				} catch (JSONException e) {
					Toast.makeText(RegisterActivity.this, "数据解析错误",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		};
	};
	private CheckBox check;

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

}
