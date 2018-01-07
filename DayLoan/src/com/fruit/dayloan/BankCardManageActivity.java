package com.fruit.dayloan;

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

import com.fruit.dayloan.R;
import com.fruit.dayloan.utils.BaseActivity;
import com.fruit.dayloan.utils.Config;
import com.fruit.dayloan.utils.HttpUtils;
/**
 * 
 * 银行卡管理
 * @author Administrator
 *
 */
public class BankCardManageActivity extends BaseActivity implements OnClickListener {

	private TextView name_card, id_card, card_check, bank,idno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_band_card_manage);
		initView();
	}

	private void initView() {
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("银行卡管理");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		name_card = (TextView) findViewById(R.id.name_card);
		id_card = (TextView) findViewById(R.id.id_card);
		card_check = (TextView) findViewById(R.id.card_check);
		bank = (TextView) findViewById(R.id.bank);
	idno = (TextView) findViewById(R.id.idno);
		
		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		String UserId = sp.getString("userid", "");
		HttpUtils.doGetAsyn(Config.CARD_BANAME_CORD+"?userid="+UserId+"&type=1", mHandler,
				Config.CODE_CARD_MANAGE);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = msg.obj.toString();
			switch (msg.what) {
			case Config.CODE_URL_ERROR:
				Toast.makeText(BankCardManageActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(BankCardManageActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_CARD_MANAGE:
				try {
					JSONObject jsonObject = new JSONObject(result);
					String cardno = jsonObject.getString("cardno");
					String idnoStr = jsonObject.getString("idno");
					String realname = jsonObject.getString("realname");
					name_card.setText("**"+realname.substring((realname.length())-1,realname.length()));
					id_card.setText(cardno.substring(0,3)+"**********"+cardno.substring((cardno.length())-4,cardno.length()));
					idno.setText(idnoStr.substring(0,3)+"**********"+idnoStr.substring((idnoStr.length())-4,idnoStr.length()));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
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
