package com.sd.pallentloan;

import org.json.JSONException;
import org.json.JSONObject;

import com.sd.pallentloan.GetMoneyRecordActivity;
import com.sd.pallentloan.utils.AlertDialog;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.view.MyProgressDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyMoneyActivity extends BaseActivity implements OnClickListener {

	private String userId;
	private String ketje = "0.00";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_money);
		initView();
	}

	private void initView() {
		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		userId = sp.getString("userid", "");
		invest_status = sp.getInt("invest_status", 0);

		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("我的财富");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		TextView tx_money = (TextView) findViewById(R.id.tx_money);
		tx_money.setOnClickListener(this);
		all_money_value = (TextView) findViewById(R.id.all_money_value);
		money_value_txt1 = (TextView) findViewById(R.id.money_value_txt1);
		money_value_txt2 = (TextView) findViewById(R.id.money_value_txt2);
		money_value_txt3 = (TextView) findViewById(R.id.money_value_txt3);
		TextView money_value_txt4 = (TextView) findViewById(R.id.money_value_txt4);
		money_value_txt5 = (TextView) findViewById(R.id.money_value_txt5);
		TextView ask_money_txt = (TextView) findViewById(R.id.ask_money_txt);
		ask_money_txt.setOnClickListener(this);
		TextView get_money_txt = (TextView) findViewById(R.id.get_money_txt);
		get_money_txt.setOnClickListener(this);
		RelativeLayout invest_money = (RelativeLayout) findViewById(R.id.invest_money);
		RelativeLayout invest = (RelativeLayout) findViewById(R.id.invest);
		TextView money_txt3 = (TextView) findViewById(R.id.money_txt3);
//		if (invest_status != 0) {
//			invest.setVisibility(View.VISIBLE);
//			invest_money.setVisibility(View.VISIBLE);
			money_txt3.setVisibility(View.INVISIBLE);
//		}
		HttpUtils.doGetAsyn(Config.ASKMONEYINIT_CORD + "&userid=" + userId,
				mHandler, Config.CODE_ASKMONEYINIT);
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			String result = msg.obj.toString();
			switch (msg.what) {
			case Config.CODE_URL_ERROR:
				Toast.makeText(MyMoneyActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(MyMoneyActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				break;

			case Config.CODE_ASKMONEYINIT:

				try {
					JSONObject json = new JSONObject(result);
					if (json.has("err")) {
						// int err = json.getInt("err");
						Toast.makeText(MyMoneyActivity.this,
								json.getString("msg"), Toast.LENGTH_SHORT)
								.show();
						//{"tjjl":"55.00","totalInvestmentSum":10,"totalIncome":75,"tjli":130,"ketje":"55.00"}
					} else {
						ketje = json.getString("ketje");//可提取金额
						double tjli = json.getDouble("tjli");//总赚取金额
						String tjjl = json.getString("tjjl");//推荐认证收益
//						double totalIncome = json.getDouble("totalIncome");//投资收益
//						int totalInvestmentSum = json.getInt("totalInvestmentSum");//邀请人投资收益
						
						all_money_value.setText("￥" + tjli);//总赚取金额
						money_value_txt2.setText("￥" + tjjl);//推荐用户认证
						money_value_txt5.setText("￥" + ketje);//可提取金额
//						if (invest_status != 0) {
							money_value_txt1.setText("￥"
									+ json.getString("totalIncome"));//投资收益
							money_value_txt3.setText("￥"
									+ json.getString("totalInvestmentSum"));//推荐用户投资
							
//						}

					}

				} catch (JSONException e) {
					Toast.makeText(MyMoneyActivity.this, "数据解析错误",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				break;
			case Config.CODE_ASKMONEY:
				dialog.dismiss();
				try {
					JSONObject jsonObject2 = new JSONObject(result);
					int err = jsonObject2.getInt("err");
					Toast.makeText(MyMoneyActivity.this,
							jsonObject2.getString("msg"), Toast.LENGTH_SHORT)
							.show();
					if (err == 0) {
						Intent intent = new Intent(MyMoneyActivity.this,
								GetMoneyRecordActivity.class);
						startActivity(intent);
						// finish();
					} else if (err == -5) {
						new AlertDialog(MyMoneyActivity.this).builder()
								.setMsg("您还未绑定银行卡")
								.setNegativeButton("确定", new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												MyMoneyActivity.this,
												BindCard2Activity.class);
										startActivity(intent);
										finish();
									}
								}).show();
					}
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
	private TextView all_money_value;
	private TextView money_value_txt2;
	private TextView money_value_txt5;
	private MyProgressDialog dialog;
	private TextView money_value_txt1;
	private TextView money_value_txt3;
	private int invest_status;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tx_money:
			startActivity(new Intent(this,GetMoneyRecordActivity.class));
			break;
		case R.id.backpress:
			finish();
			break;
		case R.id.ask_money_txt:// 申请提现
			if (Double.parseDouble(ketje) < 100) {
				AlertDialog alert = new AlertDialog(MyMoneyActivity.this);
				alert.builder();
				alert.setMsg("您可提现的金额不足100，快邀请好友赚钱吧!");
				alert.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// finish();
					}
				});
				alert.show();
			} else {
				
				AlertDialog alert = new AlertDialog(MyMoneyActivity.this);
				alert.builder();
				alert.setMsg("您可提现的金额为" + ketje + "元");
				alert.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// finish();
						// alert
					}
				});
				alert.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog = new MyProgressDialog(MyMoneyActivity.this, "");
						dialog.show();
						HttpUtils.doGetAsyn(Config.ASKMONEY_CORD + "&userid="
								+ userId, mHandler, Config.CODE_ASKMONEY);
					}
				});
				alert.show();
			}
			break;

		case R.id.get_money_txt:// 马上赚钱
			startActivity(new Intent(this, InviteActivity.class));
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
