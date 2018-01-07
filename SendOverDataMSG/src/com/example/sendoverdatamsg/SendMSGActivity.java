package com.example.sendoverdatamsg;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendMSGActivity extends Activity {

	private TextView tvSendTo;
	private TextView tvbBack;
	private EditText etMSG;
	private Button btnSend;

	private Person person;

	/* 自定义ACTION常数，作为广播的Intent Filter识别常数 */
	private static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
	private static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";

	private MyBroadcastReceiver mReceiver01;
	private MyBroadcastReceiver mReceiver02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendmsg);

		tvSendTo = (TextView) findViewById(R.id.tv_sendto);
		tvbBack = (TextView) findViewById(R.id.tv_back);
		etMSG = (EditText) findViewById(R.id.et_msg);
		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* 建立自定义Action常数的Intent(给PendingIntent参数之用) */
				Intent itSend = new Intent(SMS_SEND_ACTIOIN);
				Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);

				/* sentIntent参数为传送后接受的广播信息PendingIntent */
				PendingIntent mSendPI = PendingIntent.getBroadcast(
						getApplicationContext(), 0, itSend, 0);

				/* deliveryIntent参数为送达后接受的广播信息PendingIntent */
				PendingIntent mDeliverPI = PendingIntent.getBroadcast(
						getApplicationContext(), 0, itDeliver, 0);
				String MSG = etMSG.getText().toString();

				try {
					SmsManager sms = SmsManager.getDefault();
//					Class smClass = SmsManager.class;
//					// 通过反射查到了SmsManager有个叫做mSubId的属性
//					Field field = smClass.getDeclaredField("mSubId");
//					field.setAccessible(true);
//					field.set(sms, 1);// 0:默认卡1发送；1：默认卡2发送
					sms.sendTextMessage(person.getMobilePhone(), null, MSG,
							mSendPI, mDeliverPI);
					finish();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		tvbBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		person = (Person) getIntent().getSerializableExtra("person");
		tvSendTo.setText("发送到： " + person.getName());
		double allmoney = person.getRepaymoney() + person.getInterest();
		String platform;
		if (person.getUserId().substring(0, 3).equalsIgnoreCase("JBD")) {
			platform = "人人闪貸";
		} else {
			platform = "粒粒貸";
		}
		etMSG.setText(person.getName() + "，这里是" + platform + "，您在我们这里借钱"
				+ person.getRepaymoney() + "，逾期" + person.getOverdue() + "天，欠钱"
				+ Double.toString(allmoney) + "请您尽快还钱或与我们联系！");
	}
	
//	public static SmsManager getSmsManagerForSubscriptionId(int subId) {
//        // TODO(shri): Add javadoc link once SubscriptionManager is made public api
//        synchronized(sLockObject) {
//            SmsManager smsManager = sSubInstances.get(subId);
//            if (smsManager == null) {
//                smsManager = new SmsManager(subId);
//                sSubInstances.put(subId, smsManager);
//            }
//            return smsManager;
//        }
//    }   

	@Override
	protected void onResume() {
		/* 自定义IntentFilter为SENT_SMS_ACTIOIN Receiver */
		IntentFilter mFilter01;
		mFilter01 = new IntentFilter(SMS_SEND_ACTIOIN);
		mReceiver01 = new MyBroadcastReceiver();
		registerReceiver(mReceiver01, mFilter01);

		/* 自定义IntentFilter为DELIVERED_SMS_ACTION Receiver */
		mFilter01 = new IntentFilter(SMS_DELIVERED_ACTION);
		mReceiver02 = new MyBroadcastReceiver();
		registerReceiver(mReceiver02, mFilter01);
		super.onResume();
	}

	@Override
	protected void onPause() {
		/* 取消注册自定义Receiver */
		unregisterReceiver(mReceiver01);
		unregisterReceiver(mReceiver02);
		super.onPause();
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// //实现此段功能需注册接收短信接收权限
			// Object[] pduses= (Object[])intent.getExtras().get("pdus");
			// for(Object pdus: pduses){
			// byte[] pdusmessage = (byte[])pdus;
			// SmsMessage sms = SmsMessage.createFromPdu(pdusmessage);
			// String mobile = sms.getOriginatingAddress();//发送短信的手机号码
			// String content = sms.getMessageBody(); //短信内容
			// Date date = new Date(sms.getTimestampMillis());
			// SimpleDateFormat format = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// String time = format.format(date); //得到发送时间
			//
			// }
			//
			if (SMS_SEND_ACTIOIN.equals(intent.getAction())) {
				switch (getResultCode()) {
				case RESULT_OK:
					Toast.makeText(SendMSGActivity.this, "短信发送成功！",
							Toast.LENGTH_LONG).show();
					break;

				// 短信发送失败
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(SendMSGActivity.this, "短信发送失败！",
							Toast.LENGTH_LONG).show();

					break;

				// 发送失败，无线处于关闭状态
				case SmsManager.RESULT_ERROR_RADIO_OFF:

					break;

				// 发送失败，没有指定的PDU
				case SmsManager.RESULT_ERROR_NULL_PDU:

					break;

				// 发送失败，无服务
				case SmsManager.RESULT_ERROR_NO_SERVICE:

					break;
				default:
					break;
				}
			} else if (SMS_DELIVERED_ACTION.equals(intent.getAction())) {
				switch (getResultCode()) {
				// 自由空间
				case SmsManager.STATUS_ON_ICC_FREE:

					break;

				// 接收且已读
				case SmsManager.STATUS_ON_ICC_READ:

					break;

				// 存储且已发送
				case SmsManager.STATUS_ON_ICC_SENT:

					break;

				// 接收但未读
				case SmsManager.STATUS_ON_ICC_UNREAD:

					break;

				// 存储但未发送
				case SmsManager.STATUS_ON_ICC_UNSENT:

					break;
				default:
					break;
				}
			}
		}

	}
}
