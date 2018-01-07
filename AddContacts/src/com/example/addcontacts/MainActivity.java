package com.example.addcontacts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private TextView tvMSG;
	
	private int count = 0;
	
	private String TAG = getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvMSG = (TextView) findViewById(R.id.tv_msg);
		Log.d(TAG, "into onCreate");
		AsyncTask<Void, Integer, Void> task = new AsyncTask<Void, Integer, Void>() {

			@Override
			protected void onPostExecute(Void result) {
				Log.d(TAG, "into onPostExecute!");
				super.onPostExecute(result);
			}

			@Override
			protected Void doInBackground(Void... params) {
				Log.d(TAG, "into doInBackground!");
				PrintWriter out = null;
				BufferedReader in = null;
				String result = "";
				try {
					URL realUrl = new URL("http://cms.shandkj.com/servlet/current/JBDcms2Action?function=getZYList");
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

					in = new BufferedReader(new InputStreamReader(
							conn.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
					JSONObject jsonObject = new JSONObject(result);
					JSONObject jsonObjectList = jsonObject.getJSONObject("list");
					Log.w(TAG, jsonObjectList.getString("numPerPage"));
					JSONArray jsonArray = jsonObjectList.getJSONArray("data");
					Log.w(TAG, jsonArray.toString());

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject json = jsonArray.getJSONObject(i);
						Log.w(TAG, json.getString("sfyhw"));
						if (json.getInt("sfyhw") == 0) {
//							Person person = new Person();
//							person.setName(json.getString("cardusername"));
//							person.setInterest(Double.parseDouble(df.format(json.getDouble("yuq_lx"))));
//							person.setMobilePhone(json.getString("mobilephone"));
//							person.setMsg(json.getString("msg"));
//							person.setOverdue(json.getInt("yuq_ts"));
//							person.setRepaymoney(json.getDouble("sjsh_money"));
//							person.setUserId(json.getString("username"));
//							Log.w(TAG, person.toString());
//							list.add(person);
							String nameString = json.getString("cardusername");
							String phoneString = json.getString("mobilephone");
							Log.d(TAG, "into json list! --> nameString:" + nameString + ",phoneString:" + phoneString);

							 //创建一个空的ContentValues
			                ContentValues contentValues = new ContentValues();
			                //向RawContacts.CONTENT_URI执行一个空值插入
			                //目的是获取系统返回的parseId
			                Uri uri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
			                long parseId = ContentUris.parseId(uri);
			                contentValues.clear();

			                //联系人绑定parseId
			                contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, parseId);
			                //设置内容类型
			                contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
			                //设置联系人名字
			                contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, nameString);
			                //向联系人Uri添加联系人名字
			                getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);
			                //清理contentValues的数据
			                contentValues.clear();

			                contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, parseId);
			                contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
			                //设置联系人的电话号码
			                contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneString);
			                //设置电话类型为手机
			                contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
			                //向联系人电话号码Uri添加电话号码
			                getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);
			                contentValues.clear();
							Log.d(TAG, "into onPostExecute!");

			                count ++;
			                publishProgress(count);
						}
					}
				} catch (Exception e) {
					// 网络错误异常
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
						ex.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				tvMSG.setText("当前已添加 " + count + " 个联系人");
				Log.d(TAG, "into onProgressUpdate!");

				super.onProgressUpdate(values);
			}
		};
		task.execute();
		Log.w(TAG, task.isCancelled() + "");
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
}
