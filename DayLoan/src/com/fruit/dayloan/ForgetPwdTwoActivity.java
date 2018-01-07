package com.fruit.dayloan;

import org.json.JSONException;
import org.json.JSONObject;

import com.fruit.dayloan.R;
import com.fruit.dayloan.utils.BaseActivity;
import com.fruit.dayloan.utils.Config;
import com.fruit.dayloan.utils.HttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ForgetPwdTwoActivity extends BaseActivity implements OnClickListener {

	private EditText new_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_two);
		initView();
	}

	private void initView() {
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		new_pwd = (EditText) findViewById(R.id.new_pwd);
		commit_btn = (Button) findViewById(R.id.commit_btn);
		commit_btn.setOnClickListener(this);
		phone = getIntent().getStringExtra("phone");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
			break;
		case R.id.commit_btn:
			String new_pwdStr = new_pwd.getText().toString();
			String url = Config.URL + Config.FORGET_CODE + "&loginPwd="
					+ new_pwdStr + "&phone=" + phone;
			HttpUtils.doGetAsyn(url, mHandler, Config.CODE_FORGET_PWD);
			break;
		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.CODE_FORGET_PWD:
				String reString = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(reString);
					int error = jsonObject.getInt("error");
					if (error == 3) {
						startActivity(new Intent(ForgetPwdTwoActivity.this,
								LoginActivity.class));
					}
					Toast.makeText(ForgetPwdTwoActivity.this,
							jsonObject.getString("msg"), Toast.LENGTH_SHORT)
							.show();
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;
			case Config.CODE_URL_ERROR:
				Toast.makeText(ForgetPwdTwoActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(ForgetPwdTwoActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	private Button commit_btn;
	private String phone;

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

}
