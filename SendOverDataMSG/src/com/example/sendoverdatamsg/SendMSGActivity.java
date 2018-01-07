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

	/* �Զ���ACTION��������Ϊ�㲥��Intent Filterʶ���� */
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
				/* �����Զ���Action������Intent(��PendingIntent����֮��) */
				Intent itSend = new Intent(SMS_SEND_ACTIOIN);
				Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);

				/* sentIntent����Ϊ���ͺ���ܵĹ㲥��ϢPendingIntent */
				PendingIntent mSendPI = PendingIntent.getBroadcast(
						getApplicationContext(), 0, itSend, 0);

				/* deliveryIntent����Ϊ�ʹ����ܵĹ㲥��ϢPendingIntent */
				PendingIntent mDeliverPI = PendingIntent.getBroadcast(
						getApplicationContext(), 0, itDeliver, 0);
				String MSG = etMSG.getText().toString();

				try {
					SmsManager sms = SmsManager.getDefault();
//					Class smClass = SmsManager.class;
//					// ͨ������鵽��SmsManager�и�����mSubId������
//					Field field = smClass.getDeclaredField("mSubId");
//					field.setAccessible(true);
//					field.set(sms, 1);// 0:Ĭ�Ͽ�1���ͣ�1��Ĭ�Ͽ�2����
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
		tvSendTo.setText("���͵��� " + person.getName());
		double allmoney = person.getRepaymoney() + person.getInterest();
		String platform;
		if (person.getUserId().substring(0, 3).equalsIgnoreCase("JBD")) {
			platform = "�������J";
		} else {
			platform = "�����J";
		}
		etMSG.setText(person.getName() + "��������" + platform + "���������������Ǯ"
				+ person.getRepaymoney() + "������" + person.getOverdue() + "�죬ǷǮ"
				+ Double.toString(allmoney) + "�������컹Ǯ����������ϵ��");
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
		/* �Զ���IntentFilterΪSENT_SMS_ACTIOIN Receiver */
		IntentFilter mFilter01;
		mFilter01 = new IntentFilter(SMS_SEND_ACTIOIN);
		mReceiver01 = new MyBroadcastReceiver();
		registerReceiver(mReceiver01, mFilter01);

		/* �Զ���IntentFilterΪDELIVERED_SMS_ACTION Receiver */
		mFilter01 = new IntentFilter(SMS_DELIVERED_ACTION);
		mReceiver02 = new MyBroadcastReceiver();
		registerReceiver(mReceiver02, mFilter01);
		super.onResume();
	}

	@Override
	protected void onPause() {
		/* ȡ��ע���Զ���Receiver */
		unregisterReceiver(mReceiver01);
		unregisterReceiver(mReceiver02);
		super.onPause();
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// //ʵ�ִ˶ι�����ע����ն��Ž���Ȩ��
			// Object[] pduses= (Object[])intent.getExtras().get("pdus");
			// for(Object pdus: pduses){
			// byte[] pdusmessage = (byte[])pdus;
			// SmsMessage sms = SmsMessage.createFromPdu(pdusmessage);
			// String mobile = sms.getOriginatingAddress();//���Ͷ��ŵ��ֻ�����
			// String content = sms.getMessageBody(); //��������
			// Date date = new Date(sms.getTimestampMillis());
			// SimpleDateFormat format = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// String time = format.format(date); //�õ�����ʱ��
			//
			// }
			//
			if (SMS_SEND_ACTIOIN.equals(intent.getAction())) {
				switch (getResultCode()) {
				case RESULT_OK:
					Toast.makeText(SendMSGActivity.this, "���ŷ��ͳɹ���",
							Toast.LENGTH_LONG).show();
					break;

				// ���ŷ���ʧ��
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(SendMSGActivity.this, "���ŷ���ʧ�ܣ�",
							Toast.LENGTH_LONG).show();

					break;

				// ����ʧ�ܣ����ߴ��ڹر�״̬
				case SmsManager.RESULT_ERROR_RADIO_OFF:

					break;

				// ����ʧ�ܣ�û��ָ����PDU
				case SmsManager.RESULT_ERROR_NULL_PDU:

					break;

				// ����ʧ�ܣ��޷���
				case SmsManager.RESULT_ERROR_NO_SERVICE:

					break;
				default:
					break;
				}
			} else if (SMS_DELIVERED_ACTION.equals(intent.getAction())) {
				switch (getResultCode()) {
				// ���ɿռ�
				case SmsManager.STATUS_ON_ICC_FREE:

					break;

				// �������Ѷ�
				case SmsManager.STATUS_ON_ICC_READ:

					break;

				// �洢���ѷ���
				case SmsManager.STATUS_ON_ICC_SENT:

					break;

				// ���յ�δ��
				case SmsManager.STATUS_ON_ICC_UNREAD:

					break;

				// �洢��δ����
				case SmsManager.STATUS_ON_ICC_UNSENT:

					break;
				default:
					break;
				}
			}
		}

	}
}
