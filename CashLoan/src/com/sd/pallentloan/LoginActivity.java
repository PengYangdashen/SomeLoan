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
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class LoginActivity extends BaseActivity implements OnClickListener {
	final long startTime = System.currentTimeMillis();
	private EditText phone;
	private EditText pwd;
	private String phoneStr;
	private String pwdStr;
	private String msgNet;
	private EditText et_phoneCode;
	private String realCode;
	private ImageView iv_showCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}

	private void initView() {
		phone = (EditText) findViewById(R.id.phone);
		pwd = (EditText) findViewById(R.id.pwd);
		Button login = (Button) findViewById(R.id.login_btn);
		login.setOnClickListener(this);
		TextView regist = (TextView) findViewById(R.id.regist);
		regist.setOnClickListener(this);
		TextView forget_pwd = (TextView) findViewById(R.id.forget_pwd);
		forget_pwd.setOnClickListener(this);
		et_phoneCode = (EditText) findViewById(R.id.et_phoneCodes);
		iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
		// 将验证码用图片的形式显示出来
		iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
		realCode = Code.getInstance().getCode().toLowerCase();
		iv_showCode.setOnClickListener(this);
	}

	private void showDialog(String Message) {
		// TODO Auto-generated method stub
		new AlertDialog(LoginActivity.this).builder().setMsg(Message)
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_showCode:
			iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
			realCode = Code.getInstance().getCode().toLowerCase();
			Log.v("TAG", "realCode" + realCode);
			break;
		case R.id.login_btn:
			String phoneCode = et_phoneCode.getText().toString().toLowerCase();
			phoneStr = phone.getText().toString().trim();
			pwdStr = pwd.getText().toString();
			if (TextUtils.isEmpty(phoneStr)) {
				showDialog("手机号码不能为空");
				// Toast.makeText(LoginActivity.this, "手机号码不能为空",
				// Toast.LENGTH_SHORT).show();
				return;
			}

			/** 去除手机号码中的空格 */
			if (phoneStr.contains(" ")) {
				phoneStr = phoneStr.replaceAll(" ", "");
			}

			/** 检查手机号码格式是否正确 */
			if (!EditUtils.isMobileNO(phoneStr)) {
				showDialog("手机号码格式不正确");
				// Toast.makeText(LoginActivity.this, "手机号码格式不正确",
				// Toast.LENGTH_SHORT).show();
				return;
			}
			if (pwdStr.contains(" ")) {
				showDialog("密码不能为空！");
				return;
			}
			if (phoneCode.contains(" ")) {
				showDialog("验证码不能为空！");
				return;
			}

			if (phoneCode.equals(realCode)) {
				ConnectionDetector cd = new ConnectionDetector(
						LoginActivity.this);
				if (cd.isConnectingToInternet()) {
					login();
				}

			} else {
				iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
				realCode = Code.getInstance().getCode().toLowerCase();
				showDialog("验证码不正确");
				et_phoneCode.setText("");
				return;
			}

			break;
		case R.id.regist:
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.forget_pwd:
			startActivity(new Intent(LoginActivity.this,
					ForgetPwdOneActivity.class));
			break;
		case R.id.problem:
			// Intent intents = new Intent(LoginActivity.this,
			// ProblemActivity.class);
			// intents.putExtra("title", "常见问题");
			// intents.putExtra("url",
			// "http://www.lvzbao.com/androidHtml/sdcjwt_app.html");
			// startActivity(intents);
			break;
		default:
			break;
		}
	}

	private void login() {

		String url = Config.URL + Config.LOGIN_CODE + "&userName=" + phoneStr
				+ "&pwd=" + pwdStr;
		HttpUtils.doGetAsyn(url, mHandler, Config.CODE_LOGIN);

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.CODE_URL_ERROR:
				Toast.makeText(LoginActivity.this, "url错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				break;

			case Config.CODE_LOGIN:
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					int error = json.getInt("err");

					// {"ui":"22","username":"GSD22","wxid":false,
					// "status":0,"name":"","err":-8,"idno":"","msg":"登录成功",
					// "business":1,"mobile":"176*****768"}

					Toast.makeText(LoginActivity.this, json.getString("msg"),
							Toast.LENGTH_SHORT).show();
					if (error == -8) {
						SharedPreferences sp = getSharedPreferences("config",
								MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString("phone", json.getString("mobile"));
						editor.putString("userid", json.getString("ui"));
						editor.commit();
						Intent intent = new Intent(LoginActivity.this,
								IndexActivity.class);
						startActivity(intent);
					} else {
						iv_showCode.setImageBitmap(Code.getInstance()
								.createBitmap());
						realCode = Code.getInstance().getCode().toLowerCase();
						et_phoneCode.setText("");
						pwd.setText("");
					}

				} catch (JSONException e) {
					Toast.makeText(LoginActivity.this, "数据解析错误",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		};
	};

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}

}
