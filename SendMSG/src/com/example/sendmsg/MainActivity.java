package com.example.sendmsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final int CODE_NET_ERROR = 10010;
	private static final int FROM_INIT = 1;
	private static final int FROM_ALL = 2;
	private static final int FROM_DONE = 3;
	private static final int FROM_TODO = 4;
	protected static final String TAG = "--SendMsg--";

	/* �Զ���ACTION��������Ϊ�㲥��Intent Filterʶ���� */
	private static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
	private static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";

	private Button btnSend;
	private Button btnAll;
	private Button btnDone;
	private Button btnToDo;
	private ListView lvShow;
	private TextView tvTotal;

	private List<Person> repayPersonList;
	private List<Person> repayPersonListDone;
	private List<Person> repayPersonListToDo;

	private MyBroadcastReceiver mReceiver01;
	private MyBroadcastReceiver mReceiver02;

	private String result;
	private String urlStr = "http://www.shandkj.com/servlet/current/JBDUserAction?function=ShowJRJK";
	private MyAdapter adapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String reString = msg.obj.toString();
			Log.w(TAG, reString);

			switch (msg.what) {
			case FROM_ALL:
			case FROM_INIT:
				System.out.println("FROM_INIT");
				getList(reString, repayPersonList);

				break;

			case FROM_DONE:
				System.out.println("FROM_DONE");
				getList(reString, repayPersonListDone);

				break;

			case FROM_TODO:
				System.out.println("FROM_TODO");
				getList(reString, repayPersonListToDo);

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.w(TAG, "onCreate");
		btnSend = (Button) findViewById(R.id.btn_sendmsg);
		btnAll = (Button) findViewById(R.id.btn_all);
		btnDone = (Button) findViewById(R.id.btn_done);
		btnToDo = (Button) findViewById(R.id.btn_todo);
		tvTotal = (TextView) findViewById(R.id.tv_total);

		btnSend.setOnClickListener(this);
		btnAll.setOnClickListener(this);
		btnDone.setOnClickListener(this);
		btnToDo.setOnClickListener(this);

		lvShow = (ListView) findViewById(R.id.lv_result);
		repayPersonList = new ArrayList<Person>();
		repayPersonListDone = new ArrayList<Person>();
		repayPersonListToDo = new ArrayList<Person>();

		new Thread() {
			@Override
			public void run() {
				Log.w(TAG, "doPost");
				result = doPost(urlStr, handler, FROM_INIT);
			};
		}.start();

	}

	protected void getList(String reString, List<Person> list) {
		try {
			JSONObject jsonObject = new JSONObject(reString);
			JSONObject jsonObjectList = jsonObject.getJSONObject("list");
			Log.w(TAG, jsonObjectList.getString("numPerPage"));
			JSONArray jsonArray = jsonObjectList.getJSONArray("data");
			Log.w(TAG, jsonArray.toString());

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				Log.w(TAG, json.getString("sfyhw"));
				Person person = new Person();
				person.setName(json.getString("realname"));
				person.setPhoneNumber(json.getString("mobilephone"));
				person.setPlatform(json.getString("username").substring(0, 3));
				person.setRepaymoney(json.getString("sjsh_money"));
				person.setStatus(json.getInt("sfyhw") + "");
				Log.w(TAG, person.toString());
				repayPersonList.add(person);
				if (json.getInt("sfyhw") == 0) {
					repayPersonListToDo.add(person);
				} else {
					repayPersonListDone.add(person);
				}

			}
			Log.d(TAG, "���չ��軹��������" + repayPersonList.size());
			Log.d(TAG, "�����ѻ���������" + repayPersonListDone.size());
			Log.d(TAG, "����δ����������" + repayPersonListToDo.size());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new MyAdapter(MainActivity.this, list);
		lvShow.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		tvTotal.setText("�����ܼƣ�" + list.size());
	}

	protected String doPost(String url, Handler handler2, int type) {
		System.out.println(url);
		Message msg = Message.obtain();
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setUseCaches(false);
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);

			// if (param != null && !param.trim().equals("")) {
			// // ��ȡURLConnection�����Ӧ�������
			// out = new PrintWriter(conn.getOutputStream());
			// // �����������
			// out.print(param);
			// // flush������Ļ���
			// out.flush();
			// }
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			msg.what = type;
			System.out.println(result);
			// } else {
			// msg.what = Config.CODE_URL_ERROR;
			// }
		} catch (Exception e) {
			// ��������쳣
			msg.what = CODE_NET_ERROR;
			e.printStackTrace();
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ��������쳣
				msg.what = CODE_NET_ERROR;
				ex.printStackTrace();
			}
		}
		msg.obj = result;
		handler.sendMessage(msg);
		return "process OK";
	}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_all:
			repayPersonList.clear();
			repayPersonListDone.clear();
			repayPersonListToDo.clear();
			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(urlStr, handler, FROM_INIT);
				};
			}.start();
			break;

		case R.id.btn_done:
			repayPersonList.clear();
			repayPersonListDone.clear();
			repayPersonListToDo.clear();
			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(urlStr, handler, FROM_DONE);
				};
			}.start();
			break;

		case R.id.btn_todo:
			repayPersonList.clear();
			repayPersonListDone.clear();
			repayPersonListToDo.clear();
			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(urlStr, handler, FROM_TODO);
				};
			}.start();
			break;

		case R.id.btn_sendmsg:
			/* �����Զ���Action������Intent(��PendingIntent����֮��) */
			Intent itSend = new Intent(SMS_SEND_ACTIOIN);
			Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);

			/* sentIntent����Ϊ���ͺ���ܵĹ㲥��ϢPendingIntent */
			PendingIntent mSendPI = PendingIntent.getBroadcast(
					getApplicationContext(), 0, itSend, 0);

			/* deliveryIntent����Ϊ�ʹ����ܵĹ㲥��ϢPendingIntent */
			PendingIntent mDeliverPI = PendingIntent.getBroadcast(
					getApplicationContext(), 0, itDeliver, 0);
			for (Person person : repayPersonListToDo) {
				if ("JBD".equalsIgnoreCase(person.getPlatform())) {
					SmsManager.getDefault().sendTextMessage(
							person.getPhoneNumber(),
							null,
							"������������ �����û�" + person.getName() + "���ã������������������"
									+ person.getRepaymoney()
									+ "Ԫ���쵽�ڣ�����������app�ڻ����ǰ������ԼӴ�ſ���",
							mSendPI, mDeliverPI);
				} else {
					SmsManager.getDefault().sendTextMessage(
							person.getPhoneNumber(),
							null,
							"���������� �����û�" + person.getName() + "���ã����������������"
									+ person.getRepaymoney()
									+ "Ԫ���쵽�ڣ�����������app�ڻ����ǰ������ԼӴ�ſ���",
							mSendPI, mDeliverPI);
				}
			}
			break;

		default:
			break;
		}
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			Object[] pduses= (Object[])intent.getExtras().get("pdus");  
	        for(Object pdus: pduses){  
	            byte[] pdusmessage = (byte[])pdus;  
	            SmsMessage sms = SmsMessage.createFromPdu(pdusmessage);  
	            String mobile = sms.getOriginatingAddress();//���Ͷ��ŵ��ֻ�����  
	            String content = sms.getMessageBody(); //��������  
	            Date date = new Date(sms.getTimestampMillis());  
	            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	            String time = format.format(date);  //�õ�����ʱ��  
	              
	        }  
			
			if (SMS_SEND_ACTIOIN.equals(intent.getAction())) {
				switch (getResultCode()) {
				case RESULT_OK:

					break;

				//���ŷ���ʧ��
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

					break;

				//����ʧ�ܣ����ߴ��ڹر�״̬
				case SmsManager.RESULT_ERROR_RADIO_OFF:

					break;

				//����ʧ�ܣ�û��ָ����PDU
				case SmsManager.RESULT_ERROR_NULL_PDU:

					break;

				//����ʧ�ܣ��޷���
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					
					break;
				default:
					break;
				}
			} else if (SMS_DELIVERED_ACTION.equals(intent.getAction())) {
				switch (getResultCode()) {
				//���ɿռ�
				case SmsManager.STATUS_ON_ICC_FREE:

					break;

				//�������Ѷ�
				case SmsManager.STATUS_ON_ICC_READ:

					break;

				//�洢���ѷ���
				case SmsManager.STATUS_ON_ICC_SENT:

					break;

				//���յ�δ��
				case SmsManager.STATUS_ON_ICC_UNREAD:
					
					break;
					
				//�洢��δ����
				case SmsManager.STATUS_ON_ICC_UNSENT:
					
					break;
				default:
					break;
				}
			}
		}

	}
}
