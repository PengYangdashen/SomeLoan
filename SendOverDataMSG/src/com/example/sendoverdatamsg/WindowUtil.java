package com.example.sendoverdatamsg;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class WindowUtil implements OnClickListener {

	private Person person;
	private WindowManager mManager;
	private Context mContext;
	private LayoutInflater mInflater;
	private View view;
	private LayoutParams mParams;
	private boolean isShowing;

	public WindowUtil(Context context, Person person) {
		mContext = context;
		this.person = person;
		mParams = new WindowManager.LayoutParams();
	}

	public void showChooseView() {
		mManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.choose_dialog, null);
		TextView tvTemplate1 = (TextView) view.findViewById(R.id.tv_template1);
		TextView tvTemplate2 = (TextView) view.findViewById(R.id.tv_template2);
		TextView tvTemplate3 = (TextView) view.findViewById(R.id.tv_template3);
		Button btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		tvTemplate1.setOnClickListener(this);
		tvTemplate2.setOnClickListener(this);
		tvTemplate3.setOnClickListener(this);
		btnCancle.setOnClickListener(this);
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.type = WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL;
		mParams.y = 26;
		mParams.width = 630;
		mParams.height = 60;
		mParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

		mParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
				| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

		mParams.format = PixelFormat.RGBA_8888;
		
		mManager.addView(view, mParams);
		isShowing = true;
	}

	@Override
	public void onClick(View v) {
		String name = person.getName();
		String mobile = person.getMobilePhone();
		String allMoney = Double.toString(person.getRepaymoney()
				+ person.getInterest());

		switch (v.getId()) {
		case R.id.tv_template1:
			String template1 = name + "，以下信息稍后同发你亲人，十五分钟不处理，群发通讯录！";
			sendSMS(person.getMobilePhone(), template1);
			break;

		case R.id.tv_template2:
			String template2 = name
					+ "在粒粒贷上借了一笔"
					+ allMoney
					+ "元的借款，现因遇到困难无法偿还，携手全家女性为各位大哥，大叔，大爷，提供最优质的服务，服务口活，胸活，双飞，样样齐全，保证不爽不要钱，价格好商量，事后付款，也可上门服务，现在就拿起手中电话拨打全家骚热线电话"
					+ mobile;
			sendSMS(person.getMobilePhone(), template2);
			break;

		case R.id.tv_template3:
			String template3 = name
					+ "，给粒粒贷这点钱今天2点前再不处理，我就打电话给你通讯录挨个草他们妈，然后说你借钱给他们家办葬礼，说了你还不起让他们全家女性去卖身给你还！";
			sendSMS(person.getMobilePhone(), template3);
			break;

		case R.id.btn_cancle:
			hideView();
			break;
		default:
			break;
		}
	}

	private void hideView() {
		mManager.removeView(view);
		isShowing = false;
	}

	private void sendSMS(String mobilePhone, String smsBody) {

		Uri smsToUri = Uri.parse("smsto:" + mobilePhone);

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		mContext.startActivity(intent);

	}

}
