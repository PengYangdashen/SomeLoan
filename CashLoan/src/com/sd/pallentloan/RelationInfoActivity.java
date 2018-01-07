package com.sd.pallentloan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts.Phones;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.pallentloan.R;
import com.sd.pallentloan.utils.ActionSheetDialog;
import com.sd.pallentloan.utils.AlertDialog;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.utils.ActionSheetDialog.OnSheetItemClickListener;
import com.sd.pallentloan.utils.ActionSheetDialog.SheetItemColor;
import com.sd.pallentloan.view.MyProgressDialog;

public class RelationInfoActivity extends BaseActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relation_info);
		initView();
	}

	private void initView() {
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("人际关系");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		relation_name = (TextView) findViewById(R.id.relation_name);
		relation_phone = (TextView) findViewById(R.id.relation_phone);
		relation2 = (TextView) findViewById(R.id.relation);
		relation_name2 = (TextView) findViewById(R.id.relation_name2);
		relation_phone2 = (TextView) findViewById(R.id.relation_phone2);
		relation3 = (TextView) findViewById(R.id.relation2);
		TextView commit_txt = (TextView) findViewById(R.id.commit_txt);
		commit_txt.setOnClickListener(this);
		findViewById(R.id.left).setOnClickListener(this);
		findViewById(R.id.right).setOnClickListener(this);
	}

	private String relation, relation1, name1, name2, phone1, phone2;
	private TextView relation_name;
	private TextView relation_phone;
	private TextView relation3;
	private TextView relation_name2;
	private TextView relation_phone2;
	private TextView relation2;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = msg.obj.toString();
			dialog.dismiss();
			switch (msg.what) {
			case Config.CODE_URL_ERROR:
				Toast.makeText(RelationInfoActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(RelationInfoActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_RELATION_INFO:
				try {
					JSONObject jsonObject = new JSONObject(result);
					int erro = jsonObject.getInt("err");
					if (erro == 1) {
						finish();
						startActivity(new Intent(RelationInfoActivity.this,
								MyInfoActivity.class));
						Toast.makeText(RelationInfoActivity.this, "绑定联系人成功",
								Toast.LENGTH_SHORT).show();
						finish();
					} else {
						showDialog(jsonObject.getString("msg"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		};
	};
	private MyProgressDialog dialog;
	private String number;

	private void showDialog(String Message) {
		// TODO Auto-generated method stub
		new AlertDialog(RelationInfoActivity.this).builder().setMsg(Message)
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n|-");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
			break;
		case R.id.commit_txt:
			dialog = new MyProgressDialog(this, "");
			String name = relation_name.getText().toString();
			String phone = relation_phone2.getText().toString();
			if (name.equals("点击获取")) {
				showDialog("请先添加直系亲属");
				return;
			}
			if (phone.equals("点击获取")) {
				showDialog("请先添加同事或朋友");
				return;
			}
			dialog.show();
			SharedPreferences sp = getSharedPreferences("config", 0x0000);
			String UserId = sp.getString("userid", "");
			String url;
			try {
				url = Config.RELATION_INFO_CORD
						+ "&userid="
						+ UserId
						+ "&contactname="
						+ URLEncoder.encode(name, "UTF-8")
						+ "&contactname2="
						+ URLEncoder.encode(
								relation_name2.getText().toString(), "UTF-8")
						+ "&contactRelationship="
						+ URLEncoder.encode(relation, "UTF-8")
						+ "&contactRelationship2="
						+ URLEncoder.encode(relation1, "UTF-8")
						+ "&contactPhone="
						+ replaceBlank(relation_phone.getText().toString())
						+ "&contactPhone2="
						+ replaceBlank(relation_phone2.getText().toString());
				HttpUtils.doGetAsyn(url, mHandler, Config.CODE_RELATION_INFO);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case R.id.left:
			new ActionSheetDialog(RelationInfoActivity.this)
					.builder()
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItem("父母", SheetItemColor.GRAY,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									relation = "父母";
									// startActivityForResult(new Intent(
									// RelationInfoActivity.this,
									// PhoneNumberActivity.class), 1001);

									Intent i = new Intent();
									i.setAction(Intent.ACTION_PICK);
									i.setData(ContactsContract.Contacts.CONTENT_URI);
									startActivityForResult(i, 1001);
								}
							})
					.addSheetItem("配偶", SheetItemColor.GRAY,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									relation = "配偶";
									// startActivityForResult(new Intent(
									// RelationInfoActivity.this,
									// PhoneNumberActivity.class), 1001);

									Intent i = new Intent();
									i.setAction(Intent.ACTION_PICK);
									i.setData(ContactsContract.Contacts.CONTENT_URI);
									startActivityForResult(i, 1001);
								}
							})
					.addSheetItem("姐妹", SheetItemColor.GRAY,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									relation = "姐妹";
									// startActivityForResult(new Intent(
									// RelationInfoActivity.this,
									// PhoneNumberActivity.class), 1001);

									Intent i = new Intent();
									i.setAction(Intent.ACTION_PICK);
									i.setData(ContactsContract.Contacts.CONTENT_URI);
									startActivityForResult(i, 1001);
								}
							})
					.addSheetItem("兄弟", SheetItemColor.GRAY,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									relation = "兄弟";
									// startActivityForResult(new Intent(
									// RelationInfoActivity.this,
									// PhoneNumberActivity.class), 1001);

									Intent i = new Intent();
									i.setAction(Intent.ACTION_PICK);
									i.setData(ContactsContract.Contacts.CONTENT_URI);
									startActivityForResult(i, 1001);
								}
							}).show();
			break;
		case R.id.right:
			new ActionSheetDialog(RelationInfoActivity.this)
					.builder()
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItem("同事", SheetItemColor.GRAY,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									relation1 = "同事";

									Intent i = new Intent();
									i.setAction(Intent.ACTION_PICK);
									i.setData(ContactsContract.Contacts.CONTENT_URI);
									startActivityForResult(i, 1002);

									// startActivityForResult(new Intent(
									// RelationInfoActivity.this,
									// PhoneNumberActivity.class), 1002);
								}
							})
					.addSheetItem("朋友", SheetItemColor.GRAY,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									relation1 = "朋友";
									Intent i = new Intent();
									i.setAction(Intent.ACTION_PICK);
									i.setData(ContactsContract.Contacts.CONTENT_URI);
									startActivityForResult(i, 1002);

									// startActivityForResult(new Intent(
									// RelationInfoActivity.this,
									// PhoneNumberActivity.class), 1002);
								}
							})
					.addSheetItem("同学", SheetItemColor.GRAY,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									relation1 = "同学";
									Intent i = new Intent();
									i.setAction(Intent.ACTION_PICK);
									i.setData(ContactsContract.Contacts.CONTENT_URI);
									startActivityForResult(i, 1002);

									// startActivityForResult(new Intent(
									// RelationInfoActivity.this,
									// PhoneNumberActivity.class), 1002);
								}
							}).show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case 1001:
			// Bundle b = data.getExtras();
			// String name = b.getString("name");
			// String phone = b.getString("phone");
			//
			// if (!(resultCode == RESULT_OK)) {
			// return;
			// }
			// if (data == null) {
			// return;
			// }
			// Uri uri = data.getData();
			// Cursor cursor = getContentResolver().query(uri, null, null, null,
			// null);
			// cursor.moveToPosition(-1);
			// String name = cursor.getString(cursor
			// .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// String contactId = cursor.getString(cursor
			// .getColumnIndex(ContactsContract.Contacts._ID));
			// Cursor phones = getContentResolver().query(
			// ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			// null,
			// ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
			// + contactId, null, null);
			// while (phones.moveToNext()) {
			// number = phones
			// .getString(phones
			// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			// }
			//
			// relation_name.setText(name);
			// relation_phone.setText(number);
			// relation2.setText(relation);

			if (data == null) {
				return;
			}
			Uri contactData = data.getData();
			if (contactData == null) {
				return;
			}
			Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
			String name = "";
			if (cursor.moveToFirst()) {
				name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String hasPhone = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				String id = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				if (hasPhone.equalsIgnoreCase("1")) {
					hasPhone = "true";
				} else {
					hasPhone = "false";
				}
				if (Boolean.parseBoolean(hasPhone)) {
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + id, null, null);
					while (phones.moveToNext()) {
						number = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					phones.close();
				}
			}

			relation_name.setText(name);
			relation_phone.setText(number);
			relation2.setText(relation);

			break;
		case 1002:

			if (!(resultCode == RESULT_OK)) {
				return;
			}
			if (data == null) {
				return;
			}
			Uri uri2 = data.getData();
			if(uri2==null){
				return;
			}
//			Cursor cursor2 = getContentResolver().query(uri2, null, null, null,
//					null);
//			cursor2.moveToFirst();
//			name2 = cursor2.getString(cursor2
//					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//			String contactId2 = cursor2.getString(cursor2
//					.getColumnIndex(ContactsContract.Contacts._ID));
//			Cursor phones2 = getContentResolver().query(
//					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//					null,
//					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
//							+ contactId2, null, null);
//			while (phones2.moveToNext()) {
//				phone2 = phones2
//						.getString(phones2
//								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//			}
			
			
			Cursor cursor2 = getContentResolver().query(uri2, null, null, null, null);
			String name2 = "";
			if (cursor2.moveToFirst()) {
				name2 = cursor2
						.getString(cursor2
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String hasPhone = cursor2
						.getString(cursor2
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				String id = cursor2.getString(cursor2
						.getColumnIndex(ContactsContract.Contacts._ID));
				if (hasPhone.equalsIgnoreCase("1")) {
					hasPhone = "true";
				} else {
					hasPhone = "false";
				}
				if (Boolean.parseBoolean(hasPhone)) {
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + id, null, null);
					while (phones.moveToNext()) {
						phone2 = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					phones.close();
				}
			}

			// Bundle b2 = data.getExtras();
			// String name2 = b2.getString("name");
			// String phone2 = b2.getString("phone");
			relation_name2.setText(name2);
			relation_phone2.setText(phone2);
			relation3.setText(relation1);
			break;
		default:
			break;
		}

	}
	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}
}
