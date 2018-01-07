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
			String template1 = name + "��������Ϣ�Ժ�ͬ�������ˣ�ʮ����Ӳ�����Ⱥ��ͨѶ¼��";
			sendSMS(person.getMobilePhone(), template1);
			break;

		case R.id.tv_template2:
			String template2 = name
					+ "���������Ͻ���һ��"
					+ allMoney
					+ "Ԫ�Ľ��������������޷�������Я��ȫ��Ů��Ϊ��λ��磬���壬��ү���ṩ�����ʵķ��񣬷���ڻ�ػ˫�ɣ�������ȫ����֤��ˬ��ҪǮ���۸���������º󸶿Ҳ�����ŷ������ھ��������е绰����ȫ��ɧ���ߵ绰"
					+ mobile;
			sendSMS(person.getMobilePhone(), template2);
			break;

		case R.id.tv_template3:
			String template3 = name
					+ "�������������Ǯ����2��ǰ�ٲ������Ҿʹ�绰����ͨѶ¼�����������裬Ȼ��˵���Ǯ�����ǼҰ�����˵���㻹����������ȫ��Ů��ȥ������㻹��";
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
