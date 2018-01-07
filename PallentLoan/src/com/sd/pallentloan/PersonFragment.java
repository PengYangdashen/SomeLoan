package com.sd.pallentloan;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.pallentloan.R;
public class PersonFragment extends Fragment implements OnClickListener {
	private View view;
	private Activity ac;
	private int rzstatus;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_person, container, false);
			initView();
//			initData();
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	private void initView() {
		ac = getActivity();
		SharedPreferences sp = ac.getSharedPreferences("config", 0x0000);
		String phone = sp.getString("phone", "");
		rzstatus = sp.getInt("rzstatus", 0);
		TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
		txt_name.setText(phone);
		LinearLayout msg_linear = (LinearLayout) view.findViewById(R.id.msg_linear);
		msg_linear.setOnClickListener(this);
		LinearLayout bank_card_linear = (LinearLayout) view.findViewById(R.id.bank_card_linear);
		bank_card_linear.setOnClickListener(this);
		LinearLayout money_out_linear = (LinearLayout) view.findViewById(R.id.money_out_linear);
		money_out_linear.setOnClickListener(this);
		LinearLayout my_money_linear = (LinearLayout)view.findViewById(R.id.my_money_linear);
		my_money_linear.setOnClickListener(this);
		LinearLayout money_record_linear = (LinearLayout) view.findViewById(R.id.money_record_linear);
		money_record_linear.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		ConnectionDetector cd = new ConnectionDetector(ac);
		switch (v.getId()) {
	case R.id.msg_linear:
		if (cd.isConnectingToInternet()) {
			Intent intent = new Intent(ac, MyInfoActivity.class);
			startActivity(intent);
		}
		
		break;
	case R.id.bank_card_linear:
		if (cd.isConnectingToInternet()) {
		if (rzstatus!=1) {
			Intent intent2 = new Intent(ac, BindCard2Activity.class);
			startActivity(intent2);
		}else {
			Intent intent2 = new Intent(ac, BankCardManageActivity.class);
			startActivity(intent2);
		}
		}
	break;
	case R.id.money_out_linear:
		if (cd.isConnectingToInternet()) {
		Intent intent3 = new Intent(ac, OutMoneyRecordActivity.class);
		intent3.putExtra("type", "person");
		startActivity(intent3);
		}
		break;
	case R.id.my_money_linear:
		if (cd.isConnectingToInternet()) {
		Intent intent4 = new Intent(ac, MyMoneyActivity.class);
		startActivity(intent4);
		}
		break;
	case R.id.money_record_linear:
		if (cd.isConnectingToInternet()) {
		Intent intent5 = new Intent(ac, MoneyRecordActivity.class);
		startActivity(intent5);
		}
		break;
	}
	}
}
