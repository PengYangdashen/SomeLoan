package com.example.sendoverdatamsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DataListActivity extends Activity implements OnClickListener {

	private String TAG = getClass().getSimpleName();
	DecimalFormat df = new DecimalFormat("#.00");  
	private String result;

	private static final int CODE_NET_ERROR = 10010;
	private static final int FROM_INIT = 1;

	private TextView tvAdmintor;
	private TextView tvBack;
	private ListView lvOverdue;
	private Button btnPrevious;
	private Button btnNext;

	private String from;
	private int curPage = 1;
	private String wuUrl = "http://cms.shandkj.com/servlet/current/JBDcms2Action?function=getYqWYCList&user_id=194&curPage=";
	private String zhouUrl = "http://cms.shandkj.com/servlet/current/JBDcms2Action?function=getYqZYList&user_id=194&curPage=";
	private String liuUrl = "http://cms.shandkj.com/servlet/current/JBDcms2Action?function=getYqLYLList&user_id=194&curPage=";
	private String yangUrl = "http://cms.shandkj.com/servlet/current/JBDcms2Action?function=getYqYSMList&user_id=194&curPage=";
	private String curUrl;

	private List<Person> list;
	private PersonAdapter adapter;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String reString = msg.obj.toString();
			Log.w(TAG, reString);
			switch (msg.what) {
			case FROM_INIT:
				System.out.println("FROM_INIT");
				getList(reString);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_list);
		from = getIntent().getStringExtra("from");
		Log.d(TAG, from);
		initview();
		if ("wuyaochen".equalsIgnoreCase(from)) {
			tvAdmintor.setText("��ҫ��");
			curUrl = wuUrl;
			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(curUrl + curPage, handler, FROM_INIT);
				};
			}.start();
		} else if ("zhouyun".equalsIgnoreCase(from)) {
			tvAdmintor.setText("����");
			curUrl = zhouUrl;
			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(curUrl + curPage, handler, FROM_INIT);
				};
			}.start();
		} else if ("liuyulong".equalsIgnoreCase(from)){
			tvAdmintor.setText("������");
			curUrl = liuUrl;
			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(curUrl + curPage, handler, FROM_INIT);
				};
			}.start();
		} else {
			tvAdmintor.setText("������");
			curUrl = yangUrl;
			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(curUrl + curPage, handler, FROM_INIT);
				};
			}.start();
		}

	}

	protected void getList(String reString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(reString);
			JSONObject jsonObjectList = jsonObject.getJSONObject("list");
			Log.w(TAG, jsonObjectList.getString("numPerPage"));
			JSONArray jsonArray = jsonObjectList.getJSONArray("data");
			Log.w(TAG, jsonArray.toString());

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				Log.w(TAG, json.getString("sfyhw"));
				if (json.getInt("sfyhw") == 0) {
					Person person = new Person();
					person.setName(json.getString("cardusername"));
					person.setInterest(Double.parseDouble(df.format(json.getDouble("yuq_lx"))));
					person.setMobilePhone(json.getString("mobilephone"));
					person.setMsg(json.getString("msg"));
					person.setOverdue(json.getInt("yuq_ts"));
					person.setRepaymoney(json.getDouble("sjsh_money"));
					person.setUserId(json.getString("username"));
					Log.w(TAG, person.toString());
					list.add(person);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list.size() == 0) {
			curPage ++;
			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(curUrl + curPage, handler, FROM_INIT);
				};
			}.start();
			return;
		}
		adapter = new PersonAdapter(DataListActivity.this, list);
		lvOverdue.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void initview() {
		tvAdmintor = (TextView) findViewById(R.id.tv_admintor);
		tvBack = (TextView) findViewById(R.id.tv_back);
		btnNext = (Button) findViewById(R.id.btn_next);
		btnPrevious = (Button) findViewById(R.id.btn_previous);
		lvOverdue = (ListView) findViewById(R.id.lv_overdue);

		tvBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);

		lvOverdue.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//��һ�ַ���
//				Intent intent = new Intent(DataListActivity.this, SendMSGActivity.class);
//				intent.putExtra("person", list.get(position));
//				startActivity(intent);
				
				//�ڶ��ַ���
//				Person  person = list.get(position);
//				double allmoney = person.getRepaymoney() + person.getInterest();
//				String platform;
//				if (person.getUserId().substring(0, 3).equalsIgnoreCase("JBD")) {
//					platform = "�������J";
//				} else {
//					platform = "�����J";
//				}
//				String smsBody = person.getName() + "��������" + platform + "���������������Ǯ"
//						+ person.getRepaymoney() + "������" + person.getOverdue() + "�죬ǷǮ"
//						+ Double.toString(allmoney) + "�������컹Ǯ����������ϵ��";
//				sendSMS(person.getMobilePhone(), smsBody);
				//�����ַ���
//				new WindowUtil(DataListActivity.this, list.get(position)).showChooseView();
				
				//�����ַ���
				Intent intent = new Intent(DataListActivity.this, ChooseActivity.class);
				intent.putExtra("person", list.get(position));
				startActivity(intent);
			}
			
		});

		list = new ArrayList<Person>();

	}
	
	private void sendSMS(String mobilePhone, String smsBody) {

	Uri smsToUri = Uri.parse("smsto:" + mobilePhone);

	Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

	intent.putExtra("sms_body", smsBody);

	startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		System.out.println("curPage=" + curPage);
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;

		case R.id.btn_previous:
			curPage--;
			if (curPage == 0) {
				Toast.makeText(DataListActivity.this, "���Ѿ��ǵ�һҳ��!", Toast.LENGTH_SHORT).show();
				curPage = 1;
				return;
			}
			list.clear();

			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(curUrl + curPage, handler, FROM_INIT);
				};
			}.start();
			break;
			
		case R.id.btn_next:
			curPage++;
			list.clear();
			new Thread() {
				@Override
				public void run() {
					Log.w(TAG, "doPost");
					result = doPost(curUrl + curPage, handler, FROM_INIT);
				};
			}.start();
			break;
			
		default:
			break;
		}
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

}
