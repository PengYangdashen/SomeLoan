package com.sd.pallentloan;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sd.pallentloan.R;
import com.sd.pallentloan.adapter.Out_MoneyAdapter;
import com.sd.pallentloan.pojo.MoneyPojo;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.Formatdou;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.utils.TimeUtils;
import com.sd.pallentloan.xlistview.XListView;
import com.sd.pallentloan.xlistview.XListView.IXListViewListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class OutMoneyRecordActivity extends BaseActivity implements
		OnClickListener, IXListViewListener {

	private XListView out_money_list;
	private String userId;
	private int curPage = 1;
	private boolean isloadMore;
	private ArrayList<MoneyPojo> arrayList;
	private Out_MoneyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out_money);
		initView();
	}

	private void initView() {

		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		userId = sp.getString("userid", "");

		type = getIntent().getStringExtra("type");
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("借款记录");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);

		out_money_list = (XListView) findViewById(R.id.out_money_list);
		out_money_list.setXListViewListener(this);
		out_money_list.setPullLoadEnable(false);
		out_money_list.setPullRefreshEnable(true);

		arrayList = new ArrayList<MoneyPojo>();
		adapter = new Out_MoneyAdapter(OutMoneyRecordActivity.this, arrayList);
		out_money_list.setAdapter(adapter);

		HttpUtils.doGetAsyn(Config.JKRECORD_CORD + "&userid=" + userId
				+ "&curPage=" + curPage, mHandler, Config.CODE_JKRECORD);

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.CODE_URL_ERROR:
				Toast.makeText(OutMoneyRecordActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(OutMoneyRecordActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;

			case Config.CODE_JKRECORD:
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					JSONObject listObject = json.getJSONObject("list");
					int totalRows = listObject.getInt("totalRows");
					int totalPages = listObject.getInt("totalPages");
					if (curPage < totalPages) {
						out_money_list.setPullLoadEnable(true);
					} else {
						out_money_list.setPullLoadEnable(false);
						// CustomToast.showShortToast(getActivity(),
						// "数据已经加载完毕了!");
					}

					// cl03_status":1,"jk_date":2,"cl_status":1,"create_date":"2017-02-09
					// 09:57:19","cl02_status":1,"jk_money":3300
					ArrayList<MoneyPojo> arrayList2 = new ArrayList<MoneyPojo>();
					JSONArray array = listObject.getJSONArray("data");
					if (array.length() == 0) {
						setDialog();
					} else {
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = (JSONObject) array.get(i);
							MoneyPojo pojo = new MoneyPojo();
							int jk_date = object.getInt("jk_date");
							if (jk_date==1) {
								pojo.setDate("借款期限:15天");
							}else{
								pojo.setDate("借款期限:30天");
							}
							String time = object.getString("create_date");
							// String[] strs=time.split("-");
							// pojo.setTime(strs[0]+"年"+strs[1]+"月"+strs[2]+"日"+);
							pojo.setTime(time);
							int id = object.getInt("id");
							pojo.setId(id);
							
							int one = object.getInt("cl_status");
							int two = object.getInt("cl02_status");
							int three = object.getInt("cl03_status");
							int sfyfk = object.getInt("sfyfk");
							int jksfwc = object.getInt("jksfwc");
							int spzt = object.getInt("spzt");

							String type = "";
							if(one==0||one==1&&two==0||one==1&&two==1&&three==0){
								type = "借款审核中";
							} 
							else if (one == 1 && two == 1 && three == 0
									&& spzt == 0) {
								type = "上传视频";
							} else if (one == 1 && two == 1 && three == 1
									&& sfyfk == 2) {
								type = "等待放款中";
							} else if (one == 1 && two == 1 && three == 1
									&& sfyfk == 1&& jksfwc == 0) {
								type = "请还款";
							} else if (one == 3 || two == 3 || three == 3) {
								type = "借款审核失败";
							} else if (one == 1 && two == 1 && three == 1
									&& jksfwc == 1) {
								type = "已还款";
							}

							if (one == 1 && two == 1) {
								pojo.setName("审批金额");
								pojo.setMoney(Formatdou.formatdou(object.getDouble("sjsh_money")) + "元");
							} else {
								pojo.setName("申请金额");
								pojo.setMoney(Formatdou.formatdou(object.getDouble("jk_money")) + "元");
							}
							
							pojo.setType(type);
							arrayList2.add(pojo);
						}

						if (isloadMore) {
							arrayList.addAll(arrayList2);

						} else {
							arrayList = arrayList2;

						}

						adapter.setArrayList(arrayList);
						adapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					Toast.makeText(OutMoneyRecordActivity.this, "数据解析错误",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;
			default:
				break;
			}
		};
	};
	private String type;

	private void setDialog() {
		new com.sd.pallentloan.utils.AlertDialog(OutMoneyRecordActivity.this)
				.builder().setMsg("您还没有借款记录")
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				}).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			if (TextUtils.isEmpty(type) || type == null) {
				finish();
				return;
			}
			if (type.equals("wantmoney")) {
				startActivity(new Intent(this, IndexActivity.class));
				// finish();
			} else {
				finish();
			}

			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (TextUtils.isEmpty(type) || type == null) {
				finish();
			} else if (type.equals("wantmoney")) {
				startActivity(new Intent(this, IndexActivity.class));
				// finish();
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onRefresh() {
		// adapter.initAd();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// list.clear();
				curPage = 1;
				isloadMore = false;
				HttpUtils
						.doGetAsyn(Config.JKRECORD_CORD + "&userid=" + userId
								+ "&curPage=" + curPage, mHandler,
								Config.CODE_JKRECORD);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				curPage += 1;
				isloadMore = true;
				HttpUtils
						.doGetAsyn(Config.JKRECORD_CORD + "&userid=" + userId
								+ "&curPage=" + curPage, mHandler,
								Config.CODE_JKRECORD);
				onLoad();
			}
		}, 2000);
	}

	protected void onLoad() {
		out_money_list.stopRefresh();
		out_money_list.stopLoadMore();
		out_money_list.setRefreshTime(TimeUtils.getDate());
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}

}
