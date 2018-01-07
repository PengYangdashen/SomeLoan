package com.fruit.dayloan;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.fruit.dayloan.IndexActivity;
import com.fruit.dayloan.ProblemActivity;
import com.fruit.dayloan.R;
import com.fruit.dayloan.menu.ResideMenu;
import com.fruit.dayloan.utils.ActivityList;
import com.fruit.dayloan.utils.AlertDialog;
import com.fruit.dayloan.utils.BaseActivity;
import com.fruit.dayloan.utils.Config;
import com.fruit.dayloan.utils.TimeUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

public class IndexActivity extends BaseActivity{

	private int rzstatus;
	private int invest_status;

	private RadioGroup rgBottom;
	private RadioButton rbHome;
	private RadioButton rbMe;
	private RadioButton rbMore;
	
	private HomeFragment homeFragment;
	private PersonFragment personFragment;
	private MoreFragment moreFragment;
	private List<Fragment> fragments;
	
	/**
	 * 为了控制底部button图标大小
	 */
	private List<RadioButton> buttons;
	private Drawable[] drs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_3);

		Log.d("Index3Activity", "onCreate -> 系统时间：" + System.currentTimeMillis());
		
		initView();
		Intent intert = getIntent();
		int No = intert.getIntExtra("fragment", -1);
		if (No == 1) {
			System.out.println("fragment=" + No);
//			rgBottom.clearCheck();
			rbMe.setChecked(true);
//	        FragmentTransaction fragmentTransaction =
//	                getSupportFragmentManager().beginTransaction();
//	        fragmentTransaction.show(personFragment);
//	        fragmentTransaction.commit();
		} else {
			System.out.println("fragment=" + No);
			switchFragment(0);
		}

	}

	private void initView() {
		Log.d("Index3Activity", "new 出3个碎片之前的时间：" + TimeUtils.parseDate(System.currentTimeMillis()));
		homeFragment = new HomeFragment();
		personFragment = new PersonFragment();
		moreFragment = new MoreFragment();
		Log.d("Index3Activity", "new 出之后的当前时间：" + TimeUtils.parseDate(System.currentTimeMillis()));
		fragments = new ArrayList<Fragment>();
		fragments.add(homeFragment);
		fragments.add(personFragment);
		fragments.add(moreFragment);
		rgBottom = (RadioGroup) findViewById(R.id.rg_bottom);
		rbHome = (RadioButton) findViewById(R.id.rb_home);
		rbMe = (RadioButton) findViewById(R.id.rb_me);
		rbMore = (RadioButton) findViewById(R.id.rb_more);
		buttons = new ArrayList<RadioButton>();
		buttons.add(rbHome);
		buttons.add(rbMe);
		buttons.add(rbMore);
		for (RadioButton rb : buttons) {
			drs = rb.getCompoundDrawables();
			// 定义一个Rect边界
			Rect r = new Rect(0, 0, drs[1].getMinimumWidth(), drs[1].getMinimumHeight());
			// 给drawable设置边界
			drs[1].setBounds(r);
			// 添加限制给控件
			rb.setCompoundDrawables(null, drs[1], null, null);
		}

		// 为底部按钮设置监听器
		rgBottom.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					switchFragment(0);
					Log.w("Index3Activity",
							"initview()  -->  rgBottom -> onCheckedChange -> home");
					break;

				case R.id.rb_me:
					switchFragment(1);
					
					Log.w("Index3Activity",
							"initview()  -->  rgBottom -> onCheckedChange -> me");
					break;

				case R.id.rb_more:
					switchFragment(2);
					Log.w("Index3Activity",
							"initview()  -->  rgBottom -> onCheckedChange -> more");
					break;

				default:
					Log.w("Index3Activity",
							"initview()  -->  rgBottom -> onCheckedChange -> default");
					break;
				}
			}
		});

	}

	private void initData() {
		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		String userId = sp.getString("userid", "");

		RequestParams params = new RequestParams();
		// params.add("clientData", "您好服务器端的post方法！！！");
		new AsyncHttpClient().post(Config.HOME_INIT + "&userid=" + userId,
				params, new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {

						super.onFailure(statusCode, headers, responseString,
								throwable);

					}

					@Override
					public void onFinish() {
						super.onFinish();
					}

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);

						try {
							rzstatus = Integer.parseInt(response
									.getString("rzstatus"));
							invest_status = Integer.parseInt(response
									.getString("invest_status"));
							if (rzstatus == 1) {
								if (invest_status == 1) {
									tv_refere.setText("切换到投资");
								} else {
									tv_refere.setText("申请投资人");
								}

							} else {
								tv_refere.setText("申请投资人");
							}
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
	}

	private boolean isExit = false;
	private TimerTask timeTask;
	private Timer timer = new Timer();
	private TextView tv_refere;

	// 监听返回键是否退出
	@Override
	public void onBackPressed() {
		if (isExit) {
			ActivityList.tuichu();
			this.finish();
			System.exit(0);
		} else {
			isExit = true;
			Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
			timeTask = new TimerTask() {

				@Override
				public void run() {
					isExit = false;
				}
			};
			timer.schedule(timeTask, 3000);
		}
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}
	
	
	/**
     * 点击切换fragment
     *
     * @param position
     */
    public void switchFragment(int position) {
        //开启事务
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        //遍历集合
        for (int i = 0; i <fragments.size() ; i++) {
            Fragment fragment = fragments.get(i);
            if (i==position){
                //显示fragment
                if (fragment.isAdded()){
                    //如果这个fragment已经被事务添加,显示
                    fragmentTransaction.show(fragment);
                }else{
                    //如果这个fragment没有被事务添加过,添加
                   fragmentTransaction.add(R.id.index3_fragment,fragment);
                }
            }else{
                //隐藏fragment
                if (fragment.isAdded()){
                    //如果这个fragment已经被事务添加,隐藏
                    fragmentTransaction.hide(fragment);
                }
            }
        }
        //提交事务
        fragmentTransaction.commit();
//        buttons.get(position).setChecked(true);
        System.out.println("switchFragment=" + position);
    }
}
