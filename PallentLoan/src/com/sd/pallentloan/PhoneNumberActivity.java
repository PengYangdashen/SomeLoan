package com.sd.pallentloan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sd.pallentloan.R;
import com.sd.pallentloan.adapter.SortAdapter;
import com.sd.pallentloan.pojo.SortModel;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.view.CharacterParser;
import com.sd.pallentloan.view.PinyinComparator;
import com.sd.pallentloan.view.SideBar;
import com.sd.pallentloan.view.SideBar.OnTouchingLetterChangedListener;

public class PhoneNumberActivity extends BaseActivity implements
		OnClickListener {

	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_number);
		initView();
	}

	private void initView() {
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("选择联系人");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				// Toast.makeText(getApplication(),
				// ((SortModel)adapter.getItem(position)).getName(),
				// Toast.LENGTH_SHORT).show();

				SortModel model = (SortModel) adapter.getItem(position);
				String name = model.getName();
				String phone = model.getPhone();
				Intent intent = new Intent(PhoneNumberActivity.this,
						RelationInfoActivity.class);
				intent.putExtra("name", name);
				intent.putExtra("phone", phone);
				setResult(RESULT_OK, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
				finish();

			}
		});
		SourceDateList = getRelationPhone();
		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);

	}

	private ArrayList<SortModel> getRelationPhone() {
		ArrayList<SortModel> list = new ArrayList<>();

		Uri uri = Uri.parse("content://com.android.contacts/contacts"); // 访问所有联系人
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null,
				null);
		while (cursor.moveToNext()) {
			SortModel model = new SortModel();
			int contactsId = cursor.getInt(0);
			StringBuilder sb = new StringBuilder("contactsId=");
			sb.append(contactsId);
			uri = Uri.parse("content://com.android.contacts/contacts/"
					+ contactsId + "/data"); // 某个联系人下面的所有数据
			Cursor dataCursor = resolver.query(uri, new String[] { "mimetype",
					"data1", "data2" }, null, null, null);
			while (dataCursor.moveToNext()) {
				String data = dataCursor.getString(dataCursor
						.getColumnIndex("data1"));
				String type = dataCursor.getString(dataCursor
						.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/name".equals(type)) { // 如果他的mimetype类型是name
					sb.append(", name=" + data);

					// 汉字转换成拼音
					String pinyin = characterParser.getSelling(data);
					String sortString = pinyin.substring(0, 1).toUpperCase();

					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						model.setSortLetters(sortString.toUpperCase());
					} else {
						model.setSortLetters("#");
					}
					model.setName(data);

				} else if ("vnd.android.cursor.item/email_v2".equals(type)) { // 如果他的mimetype类型是email
					sb.append(", email=" + data);
				} else if ("vnd.android.cursor.item/phone_v2".equals(type)) { // 如果他的mimetype类型是phone
					sb.append(", phone=" + data);
					model.setPhone(data);
				}

				list.add(model);
			}
			Log.i("TAG", sb.toString());
		}

//		// 去表中找 显示名称 电话号码
//		String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME,
//				Phone.NUMBER, Phone.CONTACT_ID };
//		/** 得到手机通讯录联系人信息 **/
//		ContentResolver resolver = getContentResolver();
//		// 获取手机联系人
//		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
//				PHONES_PROJECTION, null, null, null);
//		if (phoneCursor != null) {
//			while (phoneCursor.moveToNext()) {
//				SortModel model = new SortModel();
//				// 得到手机号码
//				String phoneNumber = phoneCursor
//						.getString(phoneCursor
//								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//				// 当手机号码为空的或者为空字段 跳过当前循环
//				if (TextUtils.isEmpty(phoneNumber))
//					continue;
//				// 得到联系人名称
//				String contactName = phoneCursor
//						.getString(phoneCursor
//								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//				// 汉字转换成拼音
//				String pinyin = characterParser.getSelling(contactName);
//				String sortString = pinyin.substring(0, 1).toUpperCase();
//
//				// 正则表达式，判断首字母是否是英文字母
//				if (sortString.matches("[A-Z]")) {
//					model.setSortLetters(sortString.toUpperCase());
//				} else {
//					model.setSortLetters("#");
//				}
//				model.setName(contactName);
//				model.setPhone(phoneNumber);
//				list.add(model);
//			}
//			phoneCursor.close();
//		}
		return list;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
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
