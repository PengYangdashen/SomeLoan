package com.sd.pallentloan;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sd.pallentloan.R;
import com.sd.pallentloan.menu.ResideMenu;
import com.sd.pallentloan.utils.ActivityList;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;

public class IndexActivity extends BaseActivity implements OnClickListener {

	private LinearLayout layoutLeftMenu;
	private ResideMenu resideMenu;
	private ImageView index_img;
	private TextView index_txt;
	private ImageView person_img;
	private TextView person_txt, index_left_menu_txt_name;
	private ImageView more_img;
	private TextView more_txt;
	private int rzstatus;
	private int invest_status;
	
	private List<Fragment> fragments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		
		initView();
		Intent intert = getIntent();
		int id = intert.getIntExtra("id", -1);
		if (id == 0) {
			System.out.println("id=" + id);
		} else if (id == 1) {
			switchFragment(1);
			index_img.setImageResource(R.drawable.home_icon_nor);
			index_txt.setTextColor(getResources().getColor(R.color.textcolor));
			person_img.setImageResource(R.drawable.persion_icon_chose);
			person_txt.setTextColor(getResources().getColor(R.color.yellor));
			more_img.setImageResource(R.drawable.more_icon_nor);
			more_txt.setTextColor(getResources().getColor(R.color.textcolor));
		} else {
			switchFragment(0);
		}
		
	}

	private void initView() {
		resideMenu = new ResideMenu(this);
//		resideMenu.setBackground(R.drawable.app_menu_bg);
		resideMenu.attachToActivity(this);
		
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new PersonFragment());
		fragments.add(new MoreFragment());

		// resideMenu.setScaleValue(0.6f);
		// 禁止使用右侧菜单
		resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		resideMenu.setDirectionDisable(ResideMenu.DIRECTION_LEFT);
		LayoutInflater mInflater = LayoutInflater.from(this);
		layoutLeftMenu = resideMenu.getLayoutLeftMenu();

		View convertView = mInflater.inflate(R.layout.activity_index_left,
				layoutLeftMenu);
		TextView tv_contact = (TextView) convertView
				.findViewById(R.id.tv_contact);
		tv_contact.setOnClickListener(this);
		convertView.findViewById(R.id.tv_invite).setOnClickListener(this);
		convertView.findViewById(R.id.tv_money).setOnClickListener(this);
		convertView.findViewById(R.id.tv_refere).setOnClickListener(this);
		tv_refere = (TextView) convertView.findViewById(R.id.tv_refere);
		tv_refere.setOnClickListener(this);
		LinearLayout linear_aboutus = (LinearLayout) convertView
				.findViewById(R.id.linear_aboutus);
		linear_aboutus.setOnClickListener(this);
		linear_aboutus.setVisibility(View.GONE);
		resideMenu.setMenuListener(new ResideMenu.OnMenuListener() {
			@Override
			public void openMenu() {
			}

			@Override
			public void closeMenu() {
			}
		});
		findViewById(R.id.index).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		LinearLayout index_home = (LinearLayout) findViewById(R.id.index_home);
		index_home.setOnClickListener(this);
		LinearLayout index_person = (LinearLayout) findViewById(R.id.index_person);
		index_person.setOnClickListener(this);
		LinearLayout index_more = (LinearLayout) findViewById(R.id.index_more);
		index_more.setOnClickListener(this);
		index_img = (ImageView) findViewById(R.id.index_img);
		index_txt = (TextView) findViewById(R.id.index_txt);
		person_img = (ImageView) findViewById(R.id.person_img);
		person_txt = (TextView) findViewById(R.id.person_txt);
		more_img = (ImageView) findViewById(R.id.more_img);
		more_txt = (TextView) findViewById(R.id.more_txt);
		initData();
		index_left_menu_txt_name = (TextView) convertView
				.findViewById(R.id.index_left_menu_txt_name);
		sp = getApplicationContext().getSharedPreferences("config", 0x0000);
		String phone = sp.getString("phone", "");
		index_left_menu_txt_name.setText(phone);
		// setText();
	}

	public final void setText() {
		// if (sp.getInt("rzstatus", 0)==rzstatus) {
		//
		// }
		// int rzstatus = sp.getInt("rzstatus", 0);
		// int invest_status = sp.getInt("invest_status", 0);
		if (rzstatus == 1) {
			if (invest_status == 1) {
				tv_refere.setText("切换到投资");
			} else {
				tv_refere.setText("申请投资人");
			}

		} else {
			tv_refere.setText("申请投资人");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.index_home:
			switchFragment(0);
			index_img.setImageResource(R.drawable.home_icon_chose);
			index_txt.setTextColor(getResources().getColor(R.color.yellor));
			person_img.setImageResource(R.drawable.persion_icon_nor);
			person_txt.setTextColor(getResources().getColor(R.color.textcolor));
			more_img.setImageResource(R.drawable.more_icon_nor);
			more_txt.setTextColor(getResources().getColor(R.color.textcolor));
			break;
		case R.id.index_person:
			switchFragment(1);
			index_img.setImageResource(R.drawable.home_icon_nor);
			index_txt.setTextColor(getResources().getColor(R.color.textcolor));
			person_img.setImageResource(R.drawable.persion_icon_chose);
			person_txt.setTextColor(getResources().getColor(R.color.yellor));
			more_img.setImageResource(R.drawable.more_icon_nor);
			more_txt.setTextColor(getResources().getColor(R.color.textcolor));
			break;
		case R.id.index_more:
//			resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			switchFragment(2);
			index_img.setImageResource(R.drawable.home_icon_nor);
			index_txt.setTextColor(getResources().getColor(R.color.textcolor));
			person_img.setImageResource(R.drawable.persion_icon_nor);
			person_txt.setTextColor(getResources().getColor(R.color.textcolor));
			more_img.setImageResource(R.drawable.more_icon_chose);
			more_txt.setTextColor(getResources().getColor(R.color.yellor));
			break;
		case R.id.tv_contact://
			// Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
			// + "82341685"));
			// startActivity(intent);

			
			break;
		
		default:
			break;
		}
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
	private SharedPreferences sp;
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
                   fragmentTransaction.add(R.id.index_fragment,fragment);
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
    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}
}
