package com.sd.pallentloan;

import org.json.JSONException;
import org.json.JSONObject;

import com.sd.pallentloan.R;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Code;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.EditUtils;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.utils.TimeCount;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ForgetPwdOneActivity extends BaseActivity implements OnClickListener {
	private EditText phone, code;
	private String rephone;
	private String reCode;
	private EditText et_phoneCode;
	private ImageView iv_showCode;
	private String realCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		initView();
	}

	private void initView() {
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		codeBtn = (Button) findViewById(R.id.codeBtn);
		codeBtn.setOnClickListener(this);
		phone = (EditText) findViewById(R.id.phone);
		code = (EditText) findViewById(R.id.code);
		findViewById(R.id.one_step).setOnClickListener(this);

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
			phoneStr = phone.getText().toString();
			if (TextUtils.isEmpty(phoneStr)) {
				Toast.makeText(ForgetPwdOneActivity.this, "手机号码不能为空", 0).show();
				return;
			}

			/** 去除手机号码中的空格 */
			if (phoneStr.contains(" ")) {
				phoneStr = phoneStr.replaceAll(" ", "");
			}

			/** 检查手机号码格式是否正确 */
			if (!EditUtils.isMobileNO(phoneStr)) {
				Toast.makeText(ForgetPwdOneActivity.this, "手机号码格式不正确", 0)
						.show();
				return;
			}
			
			if (TextUtils.isEmpty(phoneCode)) {
				Toast.makeText(ForgetPwdOneActivity.this, "图形校验码不能为空", 0)
				.show();
					return;
			}
			
			
			if (phoneCode.equals(realCode)) {
				ConnectionDetector cd = new ConnectionDetector(
						ForgetPwdOneActivity.this);
				if (cd.isConnectingToInternet()) {
					String url = Config.URL + Config.FORGET_SEND_CODE + "&mobile="
							+ phoneStr + "&type=0";
					HttpUtils.doGetAsyn(url, mHandler, Config.CODE_FORGET_SEND_PWD);
				}

			} else {
				iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
				realCode = Code.getInstance().getCode().toLowerCase();
				Toast.makeText(ForgetPwdOneActivity.this, "图形校验码不正确", 0).show();
				et_phoneCode.setText("");
				return;
			}


			
			break;

		case R.id.backpress:
			finish();
			break;
		case R.id.one_step:
			phoneStr = phone.getText().toString();
			String codeStr = code.getText().toString();
			String urls = Config.URL + Config.CHECK_CODE + "&mobile="
					+ phoneStr + "&u_ip=" + codeStr + "&rephone=" + rephone
					+ "&reCode=" + reCode;
			HttpUtils.doGetAsyn(urls, mHandler, Config.CODE_CHECk_SEND_PWD);
			// startActivity(new Intent(ForgetPwdOneActivity.this, Forget));
			break;
		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.CODE_FORGET_SEND_PWD:
				String reString = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(reString);
					int error = jsonObject.optInt("error");
					
					if (error > 0) {
						Toast.makeText(ForgetPwdOneActivity.this,
								jsonObject.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					} else {
						rephone = jsonObject.optString("recivePhone");
						reCode = jsonObject.optString("randomCode");
						time = new TimeCount(180000, 1000, codeBtn, "重新获取");
						time.start();
						Toast.makeText(ForgetPwdOneActivity.this,
								"发送成功", Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;
			case Config.CODE_URL_ERROR:
				Toast.makeText(ForgetPwdOneActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(ForgetPwdOneActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				// if (time!=null) {
				// time.cancel();
				// }
				break;
			case Config.CODE_CHECk_SEND_PWD:
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					int error = json.optInt("error");
					
					if (error < 0) {
						Intent intent = new Intent(ForgetPwdOneActivity.this,
								ForgetPwdTwoActivity.class);
						intent.putExtra("phone", phoneStr);
						startActivity(intent);
						finish();
					}else {
						Toast.makeText(ForgetPwdOneActivity.this,
								json.getString("msg"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					Toast.makeText(ForgetPwdOneActivity.this, "数据解析错误",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		};
	};
	private Button codeBtn;
	private String phoneStr;
	private TimeCount time;

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
}
