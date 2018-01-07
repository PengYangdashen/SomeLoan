package com.example.contacts;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	/** 获取库Phone表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER = 1;

	/** 头像ID **/
	private static final int PHONES_PHOTO_ID = 2;

	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID = 3;

	private MyAdapter mAdapter;
	private ListView listview;
	private Button btn;
	private List<Contact> lists = new ArrayList<Contact>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.listview);
		btn = (Button) findViewById(R.id.btn);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPhoneContacts();
			}
		});

	}

	private void getPhoneContacts() {
		// rely=(RelativeLayout) findViewById(R.id.relationId);
		ContentResolver resolver = getContentResolver();
		List<Contact> list = new ArrayList<Contact>();
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		// 不为空
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				Contact cv = new Contact();
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME);

				// 得到联系人ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID);
				// 得到联系人头像ID
				Long imgid = phoneCursor.getLong(PHONES_PHOTO_ID);

				// 得到联系人头像Bitamp
				Bitmap contactPhoto = null;

				// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
				if (imgid > 0) {
					Uri uri = ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI, contactid);
					InputStream input = ContactsContract.Contacts
							.openContactPhotoInputStream(resolver, uri);
					contactPhoto = BitmapFactory.decodeStream(input);
				} else {
					contactPhoto = BitmapFactory.decodeResource(getResources(),
							R.drawable.ic_launcher);
				}

				cv.setName(contactName);
				// Log.i("info", "1111111"+contactName);
				cv.setMoble(phoneNumber);
				cv.setImg(contactPhoto);
				// Log.i("info", "img----"+contactPhoto);
				list.add(cv);
			}
			phoneCursor.close();
			lists.clear();
			lists.addAll(list);
			// Log.i("info", "lists----"+lists);
		}
		mAdapter = new MyAdapter(this, lists);
		listview.setAdapter(mAdapter);
	}
}
