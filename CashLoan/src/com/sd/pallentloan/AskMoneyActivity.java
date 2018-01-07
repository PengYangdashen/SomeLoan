package com.sd.pallentloan;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.pallentloan.R;
import com.sd.pallentloan.utils.AlertDialog;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.utils.TimeUtils;
import com.sd.pallentloan.view.MyProgressDialog;

public class AskMoneyActivity extends BaseActivity implements OnClickListener {

	private TextView money;
	private Button confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_money);
		initView();
	}

	private void initView() {
		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		userId = sp.getString("userid", "");
		allmoney = getIntent().getStringExtra("money");
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("申请提现");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		money = (TextView) findViewById(R.id.money);
		money.setText("￥"+allmoney);
		confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(this);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = msg.obj.toString();
			switch (msg.what) {
			case Config.CODE_URL_ERROR:
				Toast.makeText(AskMoneyActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(AskMoneyActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_ASKMONEY:
				dialog.dismiss();
				try {
					JSONObject jsonObject2 = new JSONObject(result);
					int err = jsonObject2.getInt("err");
					Toast.makeText(AskMoneyActivity.this, jsonObject2.getString("msg"),
							Toast.LENGTH_SHORT).show();
					if (err==0) {
						Intent intent =  new Intent(AskMoneyActivity.this, MyMoneyActivity.class);
						startActivity(intent);
						finish();
					}else if (err==-5) {
						new AlertDialog(AskMoneyActivity.this).builder().setMsg("您还未绑定银行卡")
						.setNegativeButton("确定", new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent =  new Intent(AskMoneyActivity.this, BindCard2Activity.class);
								startActivity(intent);
								finish();
							}
						}).show();
					}
					
//					else {
						
//											}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				break;
			default:
				break;
			}
		};
	};
	private String userId;
	private MyProgressDialog dialog;
	private String allmoney;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
			break;
		case R.id.confirm:
			dialog = new MyProgressDialog(AskMoneyActivity.this, "");
			dialog.show();
//			
				HttpUtils.doGetAsyn(Config.ASKMONEY_CORD + "&userid=" + userId, mHandler,
						Config.CODE_ASKMONEY);
//			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

}
