package com.sd.pallentloan;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.sd.pallentloan.R;
import com.sd.pallentloan.utils.AlertDialog;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.Formatdou;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.utils.TimeCount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class IWantMoneyActivity extends BaseActivity implements
		OnClickListener, OnSeekBarChangeListener {

	private TextView get_money_refere, back_money_txt, fuwuMoney;
	private TextView out_money;
	private TextView out_money_time;
	private TextView get_money_txt;
	private TextView card_band;
	private EditText code_value;
	private Button send_code;
	private CheckBox check;
	private int oMoney = 5000;
	private int time = 30;
	private String refeneMoney = "150";
	private String code;
	private String fuwu = "1350";
	private String getMoney;
	private String userId, phone, cardBank, msgcode;
	private String recivePhone;
	private String randomCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_want_money);
		initView();
	}

	private void initView() {
		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		userId = sp.getString("userid", "");
		Intent intent = getIntent();
		phone = intent.getStringExtra("phone");
		cardBank = intent.getStringExtra("cardBank");
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("我要借款");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		get_money_refere = (TextView) findViewById(R.id.get_money_refere);
		out_money = (TextView) findViewById(R.id.out_money);
		back_money_txt = (TextView) findViewById(R.id.back_money_txt);
		fuwuMoney = (TextView) findViewById(R.id.fuwuMoney);

		out_money_time = (TextView) findViewById(R.id.out_money_time);
		get_money_txt = (TextView) findViewById(R.id.get_money_txt);
		card_band = (TextView) findViewById(R.id.card_band);
		card_band.setText("银行卡号(" + cardBank + ")");

		code_value = (EditText) findViewById(R.id.code_value);
		send_code = (Button) findViewById(R.id.send_code);
		send_code.setOnClickListener(this);
		TextView fuwu = (TextView) findViewById(R.id.fuwu);
		fuwu.setOnClickListener(this);
		Button out_money_btn = (Button) findViewById(R.id.out_money_btn);
		out_money_btn.setOnClickListener(this);
		check = (CheckBox) findViewById(R.id.check);
		seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
		seekBar1.setOnSeekBarChangeListener(this);
		seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
		seekBar2.setOnSeekBarChangeListener(this);
		code_value.clearFocus();

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(code_value.getWindowToken(), 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
			break;
		case R.id.send_code:
			if (time == 0) {
				setDialog("借款时间不能为0天");
				return;
			}
			if (oMoney < 500) {
				setDialog("借款金额不能低于500元");
				return;
			}

			HttpUtils.doGetAsyn(Config.JKSEND_CORD + "&mobile=" + phone,
					mHandler, Config.CODE_JKSEND);
			break;
		case R.id.fuwu:
			Intent intent = new Intent(IWantMoneyActivity.this,
					ProblemActivity.class);
			intent.putExtra("title", "借款协议");
			intent.putExtra("url", Config.OUTMONEYPROTOL_CODE);
			startActivity(intent);
			break;
		case R.id.out_money_btn:
			code = code_value.getText().toString();

			if (time == 0) {
				setDialog("借款时间不能为0天");
				return;
			}
			if (oMoney < 500) {
				setDialog("借款金额不能低于500元");
				return;
			}

			// if (TextUtils.isEmpty(code)) {
			// setDialog("验证码不能为空");
			// return;
			// }

			// if (!code.equals(msgcode)) {
			// setDialog("验证码不一致");
			// return;
			// }

			if (!check.isChecked()) {
				setDialog("请先否选上协议！");
				return;
			}

			HttpUtils.doGetAsyn(Config.IWANTMONEY_CORD + "&userid=" + userId
					+ "&jk_money=" + oMoney + "&jk_date=" + time / 15
					+ "&annualrate=" + (30) + "&phone=" + phone, mHandler,
					Config.CODE_IWANTMONEY);

			// Intent intent = new Intent(IWantMoneyActivity.this,
			// OutMoneyRecordActivity.class);
			// startActivity(intent);
			// finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		switch (seekBar.getId()) {
		case R.id.seekBar1:
			// Toast.makeText(IWantMoneyActivity.this, ""+progress,
			// Toast.LENGTH_LONG).show();
			oMoney = progress * 100;
			out_money.setText("￥" + oMoney);
			if (progress < 5) {
				seekBar1.setProgress(5);
				// Toast.makeText(IWantMoneyActivity.this, "您最低需借500元",
				// Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.seekBar2:
			// Toast.makeText(IWantMoneyActivity.this, ""+progress,
			// Toast.LENGTH_LONG).show();
			time = progress * 15;
			out_money_time.setText(time + "天");
			if (progress == 0) {
				seekBar2.setProgress(1);
				// Toast.makeText(IWantMoneyActivity.this, "您最低需借15天",
				// Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}

		double money = 0.00;
		if (time == 30) {
			refeneMoney = Formatdou.formatdou((oMoney * 0.03));
			fuwu = Formatdou.formatdou(oMoney * 0.27);
			fuwuMoney.setText("服务费    ￥" + fuwu);
			money = oMoney * 0.3;
		} else if (time == 15) {
			refeneMoney = Formatdou.formatdou((oMoney * 0.03 * 0.5));
			fuwu = Formatdou.formatdou(oMoney * 0.27 * 0.5);
			fuwuMoney.setText("服务费    ￥" + fuwu);
			money = oMoney * 0.3 * 0.5;
		}

		get_money_refere.setText("利息    ￥" + refeneMoney);
		getMoney = Formatdou.formatdou(oMoney - money);
		get_money_txt.setText("实际到账   ￥" + getMoney);
		back_money_txt.setText(Html.fromHtml("应还金额   "
				+ "<font color=\"#FF532B\">" + "￥" + oMoney + "</font>"));

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			String reString = msg.obj.toString();
			switch (msg.what) {
			case Config.CODE_IWANTMONEY:
				try {
					JSONObject jsonObject = new JSONObject(reString);
					int ero = jsonObject.getInt("err");
					Toast.makeText(IWantMoneyActivity.this,
							jsonObject.getString("msg"), Toast.LENGTH_LONG)
							.show();
					if (ero == 1) {
						Intent intent = new Intent(IWantMoneyActivity.this,
								OutMoneyRecordActivity.class);
						intent.putExtra("type", "wantmoney");
						startActivity(intent);
						finish();
					}
					// else {
					// Toast.makeText(IWantMoneyActivity.this,
					// jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
					// }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case Config.CODE_JKSEND:
				try {
					JSONObject jsonObject = new JSONObject(reString);
					int ero = jsonObject.getInt("error");

					if (ero == 0) {
						Toast.makeText(IWantMoneyActivity.this, "发送成功",
								Toast.LENGTH_SHORT).show();
						new TimeCount(180000, 1000, send_code, "重新获取").start();
						// msgcode = jsonObject.getString("msgcode");
						code_value.setFocusable(true);
						recivePhone = jsonObject.getString("recivePhone");
						randomCode = jsonObject.getString("randomCode");
						return;
					}
					if (ero == 5) {
						recivePhone = jsonObject.getString("recivePhone");
						randomCode = jsonObject.getString("randomCode");
						Toast.makeText(IWantMoneyActivity.this,
								jsonObject.getString("msg"), Toast.LENGTH_SHORT)
								.show();
						code_value.setFocusable(true);
						return;
					}
					Toast.makeText(IWantMoneyActivity.this,
							jsonObject.getString("msg"), Toast.LENGTH_SHORT)
							.show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case Config.CODE_URL_ERROR:
				Toast.makeText(IWantMoneyActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(IWantMoneyActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	private SeekBar seekBar1;
	private SeekBar seekBar2;

	private void setDialog(String Message) {
		new AlertDialog(IWantMoneyActivity.this).builder().setMsg(Message)
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}

}
