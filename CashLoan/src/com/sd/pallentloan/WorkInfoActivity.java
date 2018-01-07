package com.sd.pallentloan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.pallentloan.R;
import com.sd.pallentloan.adapter.WorkAdapter;
import com.sd.pallentloan.pojo.PersonPojo;
import com.sd.pallentloan.pojo.SortModel;
import com.sd.pallentloan.utils.AlertDialog;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.HttpUtils;
import com.sd.pallentloan.view.MyProgressDialog;
public class WorkInfoActivity extends BaseActivity implements OnClickListener {

	private TextView work_sx;
	private EditText work_name;
	private EditText phone_fore;
	private EditText phone_back;
	private PopupWindow popupWindow;
	private LinearLayout work1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_info);
		initView();
	}

	private void initView() {
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("工作资料");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
		work1 = (LinearLayout) findViewById(R.id.work1);
		work1.setOnClickListener(this);
		// findViewById(R.id.work2).setOnClickListener(this);
		// findViewById(R.id.work3).setOnClickListener(this);
		findViewById(R.id.commit_txt).setOnClickListener(this);
		work_sx = (TextView) findViewById(R.id.work_sx);
		work_name = (EditText) findViewById(R.id.work_name);
		phone_fore = (EditText) findViewById(R.id.phone_fore);
		phone_back = (EditText) findViewById(R.id.phone_back);
		initPop2();
		dialog = new MyProgressDialog(WorkInfoActivity.this, "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			finish();
			break;
		case R.id.work1:
			popupWindow.showAsDropDown(work_sx);
			// popupWindow.setOnDismissListener(onDismissListener)
//			work_sx.setText("教育");
			break;
		// case R.id.work2:
		// break;
		// case R.id.work3:
		// break;
		case R.id.commit_txt:
			SharedPreferences sp = getSharedPreferences("config", 0x0000);
			String UserId = sp.getString("userid", "");
			String sxStr = work_sx.getText().toString();
			String name = work_name.getText().toString();
			String phone_foreStr = phone_fore.getText().toString();
			String phone_backStr = phone_back.getText().toString();
			if (TextUtils.isEmpty(sxStr)) {
				showDialog("请填公司性质");
				return;
			}
			if (TextUtils.isEmpty(name)) {
				showDialog("请填公司名字");
				return;
			}
			if (TextUtils.isEmpty(phone_foreStr)) {
				showDialog("请填公司电话区号");
				return;
			}
			if (TextUtils.isEmpty(phone_backStr)) {
				showDialog("请填公司电话号码");
				return;
			}
			dialog.show();
			String url;
			try {
				url = Config.WORK_INFO_CORD + "&companyName="
						+ URLEncoder.encode(name, "UTF-8") + "&companyline="
						+ URLEncoder.encode(sxStr, "UTF-8") + "&companyphone="
						+ phone_foreStr + phone_backStr + "&userid=" + UserId;
				HttpUtils.doGetAsyn(url, mHandler, Config.CODE_WORK_INFO);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = msg.obj.toString();
			dialog.dismiss();
			switch (msg.what) {
			case Config.CODE_URL_ERROR:
				Toast.makeText(WorkInfoActivity.this, "url错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(WorkInfoActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_WORK_INFO:
				try {
					JSONObject jsonObject = new JSONObject(result);
					int ero=jsonObject.getInt("err");
					if (ero==1) {
						startActivity(new Intent(WorkInfoActivity.this, MyInfoActivity.class));
						finish();
					} else {
						showDialog(jsonObject.getString("respMsg"));
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

	// <!-- //政府机关/社会团体 军事/公检法 教育/科研 医院 公共事业/邮电通讯/物流 建筑业 制造业 金融 商业/贸易 服务业
	// 媒体/体育/娱乐 专业事务所 农业牧渔/自由职业 其他 -->
	private void initPop2() {
		WindowManager windowManager = (WindowManager) 
				getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		View view = LayoutInflater.from(WorkInfoActivity.this).inflate(
				R.layout.pop_work, null);
		popupWindow = new PopupWindow((int) (display
				.getWidth() * 0.55), (int)(display.getHeight()*0.5));
		ColorDrawable cd = new ColorDrawable(0x000000);
		popupWindow.setBackgroundDrawable(cd);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setContentView(view);
		popupWindow.setFocusable(true);
		ListView work_list = (ListView) view.findViewById(R.id.work_list);
		ArrayList<PersonPojo> workList = new ArrayList<>();
		for (int i = 0; i < 14; i++) {
			PersonPojo pojo = new PersonPojo();
			switch (i) {
			case 0:
				pojo.setCardusername("政府机关/社会团体");
				break;
			case 1:
				pojo.setCardusername("军事/公检法");
				break;
			case 2:
				pojo.setCardusername("教育/科研");
				break;
			case 3:
				pojo.setCardusername("医院");
				break;
			case 4:
				pojo.setCardusername("公共事业/邮电通讯/物流");
				break;
			case 5:
				pojo.setCardusername("建筑业");
				break;
			case 6:
				pojo.setCardusername("制造业");
				break;
			case 7:
				pojo.setCardusername("金融");
				break;
			case 8:
				pojo.setCardusername("商业/贸易");
				break;
			case 9:
				pojo.setCardusername("服务业");
				break;
			case 10:
				pojo.setCardusername("媒体/体育/娱乐");
				break;
			case 11:
				pojo.setCardusername("专业事务所");
				break;
			case 12:
				pojo.setCardusername("农业牧渔/自由职业");
				break;
			case 13:
				pojo.setCardusername("其他");
				break;
			default:
				break;
			}
			workList.add(pojo);

			final WorkAdapter workAdapter = new WorkAdapter(WorkInfoActivity.this,
					workList);
			work_list.setAdapter(workAdapter);
			work_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					PersonPojo model = (PersonPojo) workAdapter.getItem(position);
					work_sx.setText(model.getCardusername());
					popupWindow.dismiss();
				}
			});
		}

		// FrameLayout.LayoutParams liParams =
		// (android.widget.FrameLayout.LayoutParams) ll2
		// .getLayoutParams();
		// int width = ScreenUtils.getScreenWidth(FoodActivity.this);
		// liParams.setMargins((int) width / 3, 0, 0, 0);
		// ll2.setLayoutParams(liParams);
	}

	private void showDialog(String Message) {
		// TODO Auto-generated method stub
		new AlertDialog(WorkInfoActivity.this).builder().setMsg(Message)
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
}
