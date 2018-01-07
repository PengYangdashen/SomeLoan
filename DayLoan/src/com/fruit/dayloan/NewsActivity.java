package com.fruit.dayloan;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shandai.xlistview.XListView;
import com.shandai.xlistview.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fruit.dayloan.R;
import com.fruit.dayloan.adapter.MoneyRecordAdapter;
import com.fruit.dayloan.adapter.NewsAdapter;
import com.fruit.dayloan.pojo.MoneyPojo;
import com.fruit.dayloan.utils.BaseActivity;
import com.fruit.dayloan.utils.Config;
import com.fruit.dayloan.utils.HttpUtils;
import com.fruit.dayloan.utils.TimeUtils;
public class NewsActivity extends BaseActivity implements OnClickListener,
		IXListViewListener {

	private XListView news_list;
	private String userId;
	private int curPage = 1;
	private boolean isloadMore;
	private ArrayList<MoneyPojo> arrayList;
	private NewsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		initView();
	}

	private void initView() {
		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		userId = sp.getString("userid", "");

		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("消息中心");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);

		news_list = (XListView) findViewById(R.id.news_list);
		news_list.setXListViewListener(this);
		news_list.setPullLoadEnable(false);
		news_list.setPullRefreshEnable(true);

		arrayList = new ArrayList<MoneyPojo>();
		adapter = new NewsAdapter(NewsActivity.this, arrayList);
		news_list.setAdapter(adapter);

		HttpUtils.doGetAsyn(Config.NEWS_CORD + "&userid=" + userId+"&curPage="+curPage, mHandler,
				Config.CODE_NEWS);

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.CODE_URL_ERROR:
				Toast.makeText(NewsActivity.this, "url错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(NewsActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				break;

			case Config.CODE_NEWS:
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					JSONObject listObject = json.getJSONObject("list");
					int totalRows = listObject.getInt("totalRows");
					int totalPages = listObject.getInt("totalPages");
					if (curPage < totalPages) {
						news_list.setPullLoadEnable(true);
					} else {
						news_list.setPullLoadEnable(false);
						// CustomToast.showShortToast(getActivity(),
						// "数据已经加载完毕了!");
					}
					ArrayList<MoneyPojo> arrayList2 = new ArrayList<MoneyPojo>();
					JSONArray array = listObject.getJSONArray("data");
					if (array.length() == 0) {
						setDialog();
					} else {
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = (JSONObject) array.get(i);
							MoneyPojo pojo = new MoneyPojo();
							JSONObject time = object.getJSONObject("fb_time");
							pojo.setTime(TimeUtils.parseDate(Long
									.parseLong(time.getString("time"))));
							pojo.setName(object.getString("title"));
							pojo.setType(object.getString("neirong"));
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
					Toast.makeText(NewsActivity.this, "数据解析错误",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;
			default:
				break;
			}
		};
	};

	private void setDialog() {
		new com.fruit.dayloan.utils.AlertDialog(NewsActivity.this)
				.builder().setMsg("您目前暂无消息")
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				}).show();
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// list.clear();
				curPage = 1;
				isloadMore = false;
				HttpUtils.doGetAsyn(Config.NEWS_CORD + "&userid=" + userId+"&curPage="+curPage,
						mHandler, Config.CODE_NEWS);
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
				HttpUtils.doGetAsyn(Config.NEWS_CORD + "&userid=" + userId+"&curPage="+curPage,
						mHandler, Config.CODE_NEWS);
				onLoad();
			}
		}, 2000);
	}

	protected void onLoad() {
		news_list.stopRefresh();
		news_list.stopLoadMore();
		news_list.setRefreshTime(TimeUtils.getDate());
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
        	startActivity(new Intent(this, IndexActivity.class));
			finish();
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			startActivity(new Intent(this, IndexActivity.class));
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
