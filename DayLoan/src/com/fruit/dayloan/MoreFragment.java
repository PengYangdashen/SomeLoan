package com.fruit.dayloan;

import com.fruit.dayloan.utils.Config;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoreFragment extends Fragment implements OnClickListener {
	private View view;
	private Activity ac;
	private int rzstatus;
	private android.app.AlertDialog phoneDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_more, container, false);
			initView();
			// initData();
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		Log.d("MoreFragment", "onCreateView -> 系统时间：" + System.currentTimeMillis());
		return view;
	}

	private void initView() {
		ac = getActivity();
		SharedPreferences sp = ac.getSharedPreferences("config", 0x0000);
		String phone = sp.getString("phone", "");
		rzstatus = sp.getInt("rzstatus", 0);

		LinearLayout market_linear = (LinearLayout) view
				.findViewById(R.id.market_linear);
		market_linear.setOnClickListener(this);
		LinearLayout invite_linear = (LinearLayout) view
				.findViewById(R.id.invite_linear);
		invite_linear.setOnClickListener(this);
		LinearLayout problem_linear = (LinearLayout) view
				.findViewById(R.id.problem_linear);
		problem_linear.setOnClickListener(this);
		LinearLayout my_money_linear = (LinearLayout) view
				.findViewById(R.id.my_money_linear);
		my_money_linear.setOnClickListener(this);
		LinearLayout contact_linear = (LinearLayout) view
				.findViewById(R.id.contact_linear);
		contact_linear.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		ConnectionDetector cd = new ConnectionDetector(ac);
		switch (v.getId()) {
		case R.id.market_linear:
			if (cd.isConnectingToInternet()) {
				Intent intent = new Intent(ac, NewsActivity.class);
				startActivity(intent);
			}

			break;
		case R.id.invite_linear:
			if (cd.isConnectingToInternet()) {
				startActivity(new Intent(ac, InviteActivity.class));

			}
			break;
		case R.id.problem_linear:
			Intent intent = new Intent(ac, ProblemActivity.class);
			intent.putExtra("title", "常见问题");
			intent.putExtra("url", Config.PROLEM_CODE);
			startActivity(intent);
			break;
		case R.id.my_money_linear:
			if (cd.isConnectingToInternet()) {
				Intent intent4 = new Intent(ac, MyMoneyActivity.class);
				startActivity(intent4);
			}
			break;
		case R.id.contact_linear:
			this.phoneDialog = new android.app.AlertDialog.Builder(ac).create();
			LayoutInflater lay = LayoutInflater.from(((Context) ac));
			final View inflate = lay.inflate(R.layout.layout_dialog,
					(ViewGroup) null);
			final TextView textView = (TextView) inflate
					.findViewById(R.id.dialog_ok);
			((TextView) inflate.findViewById(R.id.dialog_cancle))
					.setOnClickListener((View.OnClickListener) new View.OnClickListener() {
						public void onClick(final View view) {
							phoneDialog.dismiss();
						}
					});
			inflate.findViewById(R.id.wx_problem).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 打开微信
							phoneDialog.dismiss();
							try {
								Intent intent = new Intent(Intent.ACTION_MAIN);
								ComponentName cmp = new ComponentName(
										"com.tencent.mm",
										"com.tencent.mm.ui.LauncherUI");
								intent.addCategory(Intent.CATEGORY_LAUNCHER);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setComponent(cmp);
								startActivity(intent);
							} catch (ActivityNotFoundException e) {
								Toast.makeText(ac, "检查到您手机没有安装微信，请安装后使用该功能",
										Toast.LENGTH_LONG).show();
							}

						}
					});
			inflate.findViewById(R.id.qq_problem).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 打开扣扣进行客服沟通
							phoneDialog.dismiss();
							try {
								String url = "mqqwpa://im/chat?chat_type=wpa&uin="
										+ Config.RRSDQQ + "&version=1";
								startActivity(new Intent(Intent.ACTION_VIEW,
										Uri.parse(url)));
							} catch (ActivityNotFoundException e) {
								Toast.makeText(ac, "检查到您手机没有安装QQ，请安装后使用该功能",
										Toast.LENGTH_LONG).show();
							}
						}
					});
			inflate.findViewById(R.id.tel_problem).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 拨打电话
							phoneDialog.dismiss();
							startActivity(new Intent(Intent.ACTION_DIAL, Uri
									.parse("tel:0755-83201440")));

						}
					});
			textView.setOnClickListener((View.OnClickListener) new View.OnClickListener() {
				public void onClick(final View view) {
				}
			});
			this.phoneDialog.show();
			this.phoneDialog.setCanceledOnTouchOutside(true);
			this.phoneDialog.setContentView(inflate);

			break;
		}
	}
}
