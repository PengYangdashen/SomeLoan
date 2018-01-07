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

	/* 自定义ACTION常数，作为广播的Intent Filter识别常数 */
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
			Log.d(TAG, "今日共需还款人数：" + repayPersonList.size());
			Log.d(TAG, "今日已还款人数：" + repayPersonListDone.size());
			Log.d(TAG, "今日未还款人数：" + repayPersonListToDo.size());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new MyAdapter(MainActivity.this, list);
		lvShow.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		tvTotal.setText("人数总计：" + list.size());
	}

	protected String doPost(String url, Handler handler2, int type) {
		System.out.println(url);
		Message msg = Message.obtain();
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setUseCaches(false);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);

			// if (param != null && !param.trim().equals("")) {
			// // 获取URLConnection对象对应的输出流
			// out = new PrintWriter(conn.getOutputStream());
			// // 发送请求参数
			// out.print(param);
			// // flush输出流的缓冲
			// out.flush();
			// }
			// 定义BufferedReader输入流来读取URL的响应
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
			// 网络错误异常
			msg.what = CODE_NET_ERROR;
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// 网络错误异常
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
			/* 建立自定义Action常数的Intent(给PendingIntent参数之用) */
			Intent itSend = new Intent(SMS_SEND_ACTIOIN);
			Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);

			/* sentIntent参数为传送后接受的广播信息PendingIntent */
			PendingIntent mSendPI = PendingIntent.getBroadcast(
					getApplicationContext(), 0, itSend, 0);

			/* deliveryIntent参数为送达后接受的广播信息PendingIntent */
			PendingIntent mDeliverPI = PendingIntent.getBroadcast(
					getApplicationContext(), 0, itDeliver, 0);
			for (Person person : repayPersonListToDo) {
				if ("JBD".equalsIgnoreCase(person.getPlatform())) {
					SmsManager.getDefault().sendTextMessage(
							person.getPhoneNumber(),
							null,
							"【人人闪贷】 优质用户" + person.getName() + "您好，您在人人闪贷贷款的"
									+ person.getRepaymoney()
									+ "元今天到期，请您尽快于app内还款，提前还款可以加大放款额度",
							mSendPI, mDeliverPI);
				} else {
					SmsManager.getDefault().sendTextMessage(
							person.getPhoneNumber(),
							null,
							"【粒粒贷】 优质用户" + person.getName() + "您好，您在粒粒贷贷款的"
									+ person.getRepaymoney()
									+ "元今天到期，请您尽快于app内还款，提前还款可以加大放款额度",
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
	            String mobile = sms.getOriginatingAddress();//发送短信的手机号码  
	            String content = sms.getMessageBody(); //短信内容  
	            Date date = new Date(sms.getTimestampMillis());  
	            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	            String time = format.format(date);  //得到发送时间  
	              
	        }  
			
			if (SMS_SEND_ACTIOIN.equals(intent.getAction())) {
				switch (getResultCode()) {
				case RESULT_OK:

					break;

				//短信发送失败
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

					break;

				//发送失败，无线处于关闭状态
				case SmsManager.RESULT_ERROR_RADIO_OFF:

					break;

				//发送失败，没有指定的PDU
				case SmsManager.RESULT_ERROR_NULL_PDU:

					break;

				//发送失败，无服务
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					
					break;
				default:
					break;
				}
			} else if (SMS_DELIVERED_ACTION.equals(intent.getAction())) {
				switch (getResultCode()) {
				//自由空间
				case SmsManager.STATUS_ON_ICC_FREE:

					break;

				//接收且已读
				case SmsManager.STATUS_ON_ICC_READ:

					break;

				//存储且已发送
				case SmsManager.STATUS_ON_ICC_SENT:

					break;

				//接收但未读
				case SmsManager.STATUS_ON_ICC_UNREAD:
					
					break;
					
				//存储但未发送
				case SmsManager.STATUS_ON_ICC_UNSENT:
					
					break;
				default:
					break;
				}
			}
		}

	}
}
