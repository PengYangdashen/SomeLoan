package com.sd.pallentloan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.moblie.zmxy.antgroup.creditsdk.app.CreditApp;
import com.android.moblie.zmxy.antgroup.creditsdk.app.ICreditListener;
import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
import com.authreal.module.BaseResponse;
import com.example.credit.sdk.sp.CreditAuthHelper;
import com.example.credit.sdk.sp.DemoView;
import com.face.bsdk.crypt.Md5;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.sd.pallentloan.MyInfoActivity;
import com.sd.pallentloan.R;
import com.sd.pallentloan.utils.AlertDialog;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.Config;
import com.sd.pallentloan.utils.HttpUtils;
import com.google.gson.Gson;

public class MyInfoActivity extends BaseActivity implements OnClickListener,
		DemoView {

	private int rzstatus, isshenfen, islianxi, isjob, ismobile, istaobao, isjd,
			sfzmrz;
	private String realName, cardNo;
	private static final String TAG = "MoxieSDK";

	private String mApiKey = "e94901cb57464c868bd5f9393980af25";
	private String mobilephone;
	public static final String PARAM_CARRIER_EDITABLE = "carrier_editable";
	private ImageView shenfen_msg_img, person_guanxi_img, person_work_img,
			jd_icon, taobao_icon, mobile_icon, zm_icon;
	private TextView person_guanxi_txt, shenfen_msg_txt, person_work_txt,
			jd_txt, taobao_txt, mobile_txt, zm_txt;
	private String userId;
	public String packageSessionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info2);
		initView();
		initData();
	}

	private void initView() {
		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		userId = sp.getString("userid", "");
		// mUserId=userId + "+" + System.currentTimeMillis();
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("我的资料");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);

		findViewById(R.id.shenfen_msg_linear).setOnClickListener(this);
		findViewById(R.id.person_guanxi_linear).setOnClickListener(this);
		findViewById(R.id.person_work_linear).setOnClickListener(this);
		findViewById(R.id.mobile_linear).setOnClickListener(this);
		findViewById(R.id.taobao_linear).setOnClickListener(this);
		findViewById(R.id.jd_linear).setOnClickListener(this);
		// findViewById(R.id.emial_linear).setOnClickListener(this);
		findViewById(R.id.zhima_linear).setOnClickListener(this);
		shenfen_msg_img = (ImageView) findViewById(R.id.shenfen_msg_img);
		person_guanxi_img = (ImageView) findViewById(R.id.person_guanxi_img);
		person_work_img = (ImageView) findViewById(R.id.person_work_img);
		mobile_icon = (ImageView) findViewById(R.id.mobile_icon);
		taobao_icon = (ImageView) findViewById(R.id.taobao_icon);
		jd_icon = (ImageView) findViewById(R.id.jd_icon);
		person_guanxi_txt = (TextView) findViewById(R.id.person_guanxi_txt);
		shenfen_msg_txt = (TextView) findViewById(R.id.shenfen_msg_txt);
		person_work_txt = (TextView) findViewById(R.id.person_work_txt);
		mobile_txt = (TextView) findViewById(R.id.mobile_txt);
		taobao_txt = (TextView) findViewById(R.id.taobao_txt);
		jd_txt = (TextView) findViewById(R.id.jd_txt);
		zm_txt = (TextView) findViewById(R.id.zm_txt);
		zm_icon = (ImageView) findViewById(R.id.zm_icon);
	}

	private void initData() {
		HttpUtils.doGetAsyn(Config.HOME_INIT + "&userid=" + userId, mHandler,
				Config.CODE_HOME_INIT);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			String reString = msg.obj.toString();
			switch (msg.what) {
			case Config.CODE_HOME_INIT:
				Log.e("MainActivity", "result=" + reString);
				// String reString = msg.obj.toString();
				try {
					// {"mobilephone":"17620110768","username":"GSD22","phone":"17620***68","creditlimit":"5000",
					// "isyhbd":"0","isshenfen":"0","islianxi":"0",
					// "err":0,"isjob":"0","rzstatus":"0","usablecreditlimit":"500","lastdate":"2017-02-22 17:39:09.0"}
					JSONObject jsonObject = new JSONObject(reString);
					mobilephone = jsonObject.getString("mobilephone");
					realName = jsonObject.getString("rzname");
					cardNo = jsonObject.getString("rzcard2");
					rzstatus = Integer.parseInt(jsonObject
							.getString("rzstatus"));
					isshenfen = Integer.parseInt(jsonObject
							.getString("isshenfen"));
					isjob = Integer.parseInt(jsonObject.getString("isjob"));
					istaobao = Integer.parseInt(jsonObject
							.getString("istaobaoyz"));// istaobaoyz isjingdongyz
														// isyyshang
					islianxi = Integer.parseInt(jsonObject
							.getString("islianxi"));
					isjd = Integer.parseInt(jsonObject
							.getString("isjingdongyz"));
					ismobile = Integer.parseInt(jsonObject
							.getString("isyyshang"));
					sfzmrz = Integer.parseInt(jsonObject.getString("sfzmrz"));
					if (isshenfen == 1) {
						shenfen_msg_img
								.setImageResource(R.drawable.center_basedate_icon);
						shenfen_msg_txt.setText("已认证");
						shenfen_msg_txt.setTextColor(getResources().getColor(
								R.color.yellor));
					}

					// Toast.makeText(MyInfoActivity.this, "身份证+++"+isshenfen,
					// Toast.LENGTH_LONG)
					// .show();

					if (islianxi == 1) {
						person_guanxi_img
								.setImageResource(R.drawable.basedata_relation_icon);
						person_guanxi_txt.setText("已认证");
						person_guanxi_txt.setTextColor(getResources().getColor(
								R.color.yellor));
					}
					if (isjob == 1) {
						person_work_img
								.setImageResource(R.drawable.basedata_relation_icon);
						person_work_txt.setText("已认证");
						person_work_txt.setTextColor(getResources().getColor(
								R.color.yellor));
					}
					if (isjd == 1) {
						jd_icon.setImageResource(R.drawable.basedata_jd_icon);
						jd_txt.setTextColor(getResources().getColor(
								R.color.yellor));
						jd_txt.setText("已认证");
						//
					}
					if (ismobile == 1) {
						mobile_icon
								.setImageResource(R.drawable.basedata_phone_icon);
						mobile_txt.setText("已认证");
						mobile_txt.setTextColor(getResources().getColor(
								R.color.yellor));
					}
					if (istaobao == 1) {
						taobao_icon.setImageResource(R.drawable.taobao_icon);
						taobao_txt.setText("已认证");
						taobao_txt.setTextColor(getResources().getColor(
								R.color.yellor));
					}
					if (sfzmrz == 1) {
						zm_icon.setImageResource(R.drawable.zhima);
						zm_txt.setText("已认证");
						zm_txt.setTextColor(getResources().getColor(
								R.color.yellor));
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// if (dialog != null) {
				// dialog.dismiss();
				// }
				//
				System.out.println("+hrl+" + isshenfen);

				break;
			case Config.CODE_URL_ERROR:
				Toast.makeText(MyInfoActivity.this, "url错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(MyInfoActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case Config.CODE_AUTHMOBILE:
				// {"taskId":"d038878a-ffe8-11e6-a28e-00163e0c310d","message":"","searchId":"8541680557613895233","code":1,"function":"taobao"}
				try {
					JSONObject json = new JSONObject(reString);
					if (json.getInt("err") == 0) {
						HttpUtils.doGetAsyn(Config.HOME_INIT + "&userid="
								+ userId, mHandler, Config.CODE_HOME_INIT);
					} else {
						Toast.makeText(MyInfoActivity.this,
								json.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Config.CODE_ZMSTART:
				try {
					JSONObject json = new JSONObject(reString);
					if (json.getInt("error") == 0) {
						// Intent intent = new Intent(MyInfoActivity.this,
						// WebviewActivity.class);
						// intent.putExtra("url", json.getString("url"));
						// startActivity(intent);
						String params = json.getString("params");
						String appId = json.getString("app_id");
						String sign = json.getString("sign");
						zmAuth(params, appId, sign);
					} else {
						Toast.makeText(MyInfoActivity.this,
								json.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Config.CODE_ZMEND:
				try {
					JSONObject json = new JSONObject(reString);
					if (json.getInt("error") == 0) {
						HttpUtils.doGetAsyn(Config.HOME_INIT + "&userid="
								+ userId, mHandler, Config.CODE_HOME_INIT);
						sfzmrz = 1;
						zm_icon.setImageResource(R.drawable.zhima);
						zm_txt.setText("已认证");
						zm_txt.setTextColor(getResources().getColor(
								R.color.yellor));
					} else {
						Toast.makeText(MyInfoActivity.this,
								json.getString("msg"), Toast.LENGTH_SHORT)
								.show();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpress:
			Intent intents = new Intent(MyInfoActivity.this,
					IndexActivity.class);
			intents.putExtra("id", 1);
			startActivity(intents);
			finish();
			break;
		case R.id.shenfen_msg_linear:
//			if (rzstatus != 1) {// 请绑定银行卡
//				Intent intent = new Intent(MyInfoActivity.this,
//						BindCard2Activity.class);
//				startActivity(intent);
//				finish();
//				return;
//			}
//			System.out.println("+shenfen+" + isshenfen);
//			if (isshenfen != 1) {// 请填写身份信息 （人脸识别）
//				// Intent intent = new Intent(MyInfoActivity.this,
//				// BaseInfoActivity.class);
//				// startActivity(intent);
//
				mSAocrLiveness();
//
//			} else if (isshenfen == 1) {
//				setDialog("您已经提交了身份信息，无需再次提交");
//			}
			break;
		case R.id.person_guanxi_linear:
			// if (rzstatus!=1) {//请绑定银行卡
			// Intent intent = new Intent(MyInfoActivity.this,
			// BindCardActivity.class);
			// startActivity(intent);
			// }
			if (islianxi != 1) {// 请填写个人信息
				Intent intent = new Intent(MyInfoActivity.this,
						RelationInfoActivity.class);
				startActivity(intent);
			} else if (islianxi == 1) {
				setDialog("您已经提交了人际信息，无需再次提交");
			}
			break;
		case R.id.person_work_linear:
			if (isjob != 1) {
				Intent intent = new Intent(MyInfoActivity.this,
						WorkInfoActivity.class);
				startActivity(intent);
			} else if (isjob == 1) {
				setDialog("您已经提交了工作信息，无需再次提交");
			}
			break;
		case R.id.mobile_linear:// 手机运营商
			if (rzstatus != 1) {// 请绑定银行卡
				Intent intent = new Intent(MyInfoActivity.this,
						BindCard2Activity.class);
				startActivity(intent);
				finish();
				return;
			}
			// if (islianxi != 1 || isjob != 1 || isshenfen != 1) {
			// setDialog("请先绑定基本信息");
			// return;
			// }
			if (ismobile == 1) {
				setDialog("您已认证了手机运营商，无需再次认证！");
				return;
			}
			startAuth(MxParam.PARAM_FUNCTION_CARRIER);
			break;
		case R.id.taobao_linear:
			if (rzstatus != 1) {// 请绑定银行卡
				Intent intent = new Intent(MyInfoActivity.this,
						BindCard2Activity.class);
				startActivity(intent);
				finish();
				return;
			}
			// if (islianxi != 1 || isjob != 1 || isshenfen != 1) {
			// setDialog("请先绑定基本信息");
			// return;
			// }
			if (istaobao == 1) {
				setDialog("您已认证了淘宝，无需再次认证！");
				return;
			}
			startAuth(MxParam.PARAM_FUNCTION_TAOBAO);
			break;
		case R.id.jd_linear:
			if (rzstatus != 1) {// 请绑定银行卡
				Intent intent = new Intent(MyInfoActivity.this,
						BindCard2Activity.class);
				startActivity(intent);
				finish();
				return;
			}
			// if (islianxi != 1 || isjob != 1 || isshenfen != 1) {
			// setDialog("请先绑定基本信息");
			// return;
			// }
			if (isjd == 1) {
				setDialog("您已认证了京东，无需再次认证！");
				return;
			}
			startAuth(MxParam.PARAM_FUNCTION_JINGDONG);
			break;
		case R.id.emial_linear:
			// Intent intents = new Intent(MyInfoActivity.this,
			// IWantMoneyActivity.class);
			// startActivity(intents);
			// finish();

			setDialog("未开放！");
			break;
		case R.id.zhima_linear:

			if (rzstatus != 1) {// 请绑定银行卡
				Intent intent = new Intent(MyInfoActivity.this,
						BindCard2Activity.class);
				startActivity(intent);
				finish();
				return;
			}
			// if (islianxi != 1 || isjob != 1 || isshenfen != 1) {
			// setDialog("请先绑定基本信息");
			// return;
			// }
			if (sfzmrz == 1) {
				setDialog("您已认证了芝麻，无需再次认证！");
				return;
			}

			// zmAuth();

			try {
				HttpUtils.doGetAsyn(Config.ZMSTART_CORD + "&userid=" + userId
						+ "&realname=" + URLEncoder.encode(realName, "UTF-8")
						+ "&idno=" + cardNo, mHandler, Config.CODE_ZMSTART);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}

	private void startAuth(String type) {

		MxParam mxParam = new MxParam();
		mxParam.setUserId(userId);
		mxParam.setApiKey(mApiKey);
		if (type.equals(MxParam.PARAM_FUNCTION_CARRIER)) {
			mxParam.setFunction(MxParam.PARAM_FUNCTION_CARRIER);// 运营商
			HashMap<String, String> extendParam = new HashMap<String, String>();
			extendParam.put(MxParam.PARAM_CARRIER_IDCARD, cardNo); // 身份证
			extendParam.put(MxParam.PARAM_CARRIER_PHONE, mobilephone); // 手机号
			extendParam.put(MxParam.PARAM_CARRIER_NAME, realName); // 姓名
			extendParam.put(MxParam.PARAM_CARRIER_EDITABLE,
					MxParam.PARAM_COMMON_NO); // 是否允许用户修改以上信息
			mxParam.setExtendParams(extendParam);

		} else if (type.equals(MxParam.PARAM_FUNCTION_TAOBAO)) {
			mxParam.setFunction(MxParam.PARAM_FUNCTION_TAOBAO);// 淘宝
		} else if (type.equals(MxParam.PARAM_FUNCTION_JINGDONG)) { // 京东
			mxParam.setFunction(MxParam.PARAM_FUNCTION_JINGDONG);
		}

		MoxieSDK.getInstance().start(this, mxParam, new MoxieCallBack() {
			/**
			 * 
			 * 物理返回键和左上角返回按钮的back事件以及sdk退出后任务的状态都通过这个函数来回调
			 * 
			 * @param moxieContext
			 *            可以用这个来实现在魔蝎的页面弹框或者关闭魔蝎的界面
			 * @param moxieCallBackData
			 *            我们可以根据 MoxieCallBackData 的code来判断目前处于哪个状态，以此来实现自定义的行为
			 * @return 返回true表示这个事件由自己全权处理，返回false会接着执行魔蝎的默认行为(比如退出sdk)
			 * 
			 *         # 注意，假如设置了MxParam.setQuitOnLoginDone(MxParam.
			 *         PARAM_COMMON_YES);
			 *         登录成功后，返回的code是MxParam.ResultCode.IMPORTING
			 *         ，不是MxParam.ResultCode.IMPORT_SUCCESS
			 */
			@Override
			public boolean callback(MoxieContext moxieContext,
					MoxieCallBackData moxieCallBackData) {
				/**
				 * # MoxieCallBackData的格式如下： 1.1.没有进行账单导入，未开始！(后台没有通知) "code" :
				 * MxParam.ResultCode.IMPORT_UNSTART, "taskType" : "mail",
				 * "taskId" : "", "message" : "", "account" : "", "loginDone":
				 * false, "businessUserId": "" 1.2.平台方服务问题(后台没有通知) "code" :
				 * MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR, "taskType" :
				 * "mail", "taskId" : "", "message" : "", "account" : "xxx",
				 * "loginDone": false, "businessUserId": "" 1.3.魔蝎数据服务异常(后台没有通知)
				 * "code" : MxParam.ResultCode.MOXIE_SERVER_ERROR, "taskType" :
				 * "mail", "taskId" : "", "message" : "", "account" : "xxx",
				 * "loginDone": false, "businessUserId": ""
				 * 1.4.用户输入出错（密码、验证码等输错且未继续输入） "code" :
				 * MxParam.ResultCode.USER_INPUT_ERROR, "taskType" : "mail",
				 * "taskId" : "", "message" : "密码错误", "account" : "xxx",
				 * "loginDone": false, "businessUserId": "" 2.账单导入失败(后台有通知)
				 * "code" : MxParam.ResultCode.IMPORT_FAIL, "taskType" : "mail",
				 * "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account"
				 * : "xxx", "loginDone": false, "businessUserId": ""
				 * 3.账单导入成功(后台有通知) "code" : MxParam.ResultCode.IMPORT_SUCCESS,
				 * "taskType" : "mail", "taskId" :
				 * "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx",
				 * "loginDone": true, "businessUserId": "xxxx" 4.账单导入中(后台有通知)
				 * "code" : MxParam.ResultCode.IMPORTING, "taskType" : "mail",
				 * "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account"
				 * : "xxx", "loginDone": true, "businessUserId": "xxxx"
				 * 
				 * code : 表示当前导入的状态 taskType :
				 * 导入的业务类型，与MxParam.setFunction()传入的一致 taskId :
				 * 每个导入任务的唯一标识，在登录成功后才会创建 message : 提示信息 account : 用户输入的账号
				 * loginDone : 表示登录是否完成，假如是true，表示已经登录成功，接入方可以根据此标识判断是否可以提前退出
				 * businessUserId : 第三方被爬取平台本身的userId，非商户传入，例如支付宝的UserId
				 */
				if (moxieCallBackData != null) {
					Log.d("BigdataFragment", "MoxieSDK Callback Data : "
							+ moxieCallBackData.toString());
					switch (moxieCallBackData.getCode()) {
					/**
					 * 账单导入中
					 * 
					 * 如果用户正在导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口
					 * 魔蝎后台会向贵方后台推送Task通知和Bill通知 Task通知：登录成功/登录失败 Bill通知：账单通知
					 */
					case MxParam.ResultCode.IMPORTING:
						if (moxieCallBackData.isLoginDone()) {
							// 状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
							Log.d(TAG,
									"任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");

						} else {
							// 状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
							Log.d(TAG,
									"任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
						}
						break;
					/**
					 * 任务还未开始
					 * 
					 * 假如有弹框需求，可以参考
					 * {@link BigdataFragment#showDialog(MoxieContext)}
					 * 
					 * example: case MxParam.ResultCode.IMPORT_UNSTART:
					 * showDialog(moxieContext); return true;
					 * */
					case MxParam.ResultCode.IMPORT_UNSTART:
						Log.d(TAG, "任务未开始");
						break;
					case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
						Toast.makeText(MyInfoActivity.this, "导入失败(平台方服务问题)",
								Toast.LENGTH_SHORT).show();
						break;
					case MxParam.ResultCode.MOXIE_SERVER_ERROR:
						Toast.makeText(MyInfoActivity.this, "导入失败(魔蝎数据服务异常)",
								Toast.LENGTH_SHORT).show();
						break;
					case MxParam.ResultCode.USER_INPUT_ERROR:
						Toast.makeText(MyInfoActivity.this,
								"导入失败(" + moxieCallBackData.getMessage() + ")",
								Toast.LENGTH_SHORT).show();
						break;
					case MxParam.ResultCode.IMPORT_FAIL:
						Toast.makeText(MyInfoActivity.this, "导入失败",
								Toast.LENGTH_SHORT).show();
						break;
					case MxParam.ResultCode.IMPORT_SUCCESS:
						Log.d(TAG,
								"任务采集成功，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");

						// 根据taskType进行对应的处理
						switch (moxieCallBackData.getTaskType()) {

						case MxParam.PARAM_FUNCTION_CARRIER:
							Toast.makeText(MyInfoActivity.this, "运营商导入成功",
									Toast.LENGTH_SHORT).show();
							// 运营商导入
							HttpUtils.doGetAsyn(Config.AUTHMOBILE_CORD
									+ "&userid=" + userId + "&code="
									+ moxieCallBackData.getCode() + "&taskId="
									+ moxieCallBackData.getTaskId()
									+ "&outOrderId=" + "", mHandler,
									Config.CODE_AUTHMOBILE);
							break;
						case MxParam.PARAM_FUNCTION_TAOBAO:
							Toast.makeText(MyInfoActivity.this, "淘宝导入成功",
									Toast.LENGTH_SHORT).show();
							HttpUtils.doGetAsyn(Config.AUTHTAOBAO_CORD
									+ "&userid=" + userId + "&code="
									+ moxieCallBackData.getCode() + "&taskId="
									+ moxieCallBackData.getTaskId()
									+ "&outOrderId=" + "", mHandler,
									Config.CODE_AUTHMOBILE);
							break;
						case MxParam.PARAM_FUNCTION_JINGDONG:
							Toast.makeText(MyInfoActivity.this, "京东导入成功",
									Toast.LENGTH_SHORT).show();
							HttpUtils.doGetAsyn(
									Config.AUTHJD_CORD + "&userid=" + userId
											+ "&code="
											+ moxieCallBackData.getCode()
											+ "&taskId="
											+ moxieCallBackData.getTaskId()
											+ "&outOrderId=" + "", mHandler,
									Config.CODE_AUTHMOBILE);
							break;
						// .....
						default:
							Toast.makeText(MyInfoActivity.this, "导入成功",
									Toast.LENGTH_SHORT).show();
						}
						moxieContext.finish();
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean onError(MoxieContext moxieContext, int errorCode,
					Throwable th) {
				Log.e(TAG, "onError, throwable=" + th.getMessage());
				if (errorCode == MxParam.ErrorCode.SDK_OPEN_FAIL) {
					moxieContext.addView(getErrorView(moxieContext));
					return true;
				}
				return super.onError(moxieContext, errorCode, th);
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 用来清理数据或解除引用
		MoxieSDK.getInstance().clear();
	}

	@SuppressLint("InflateParams")
	private View getErrorView(final MoxieContext moxieContext) {
		View view = LayoutInflater.from(this).inflate(R.layout.error_layout,
				null);
		view.findViewById(R.id.error_tv).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						moxieContext.finish();
					}
				});
		return view;
	}

	/**
	 * 接收SDK的回调
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out
				.println("返回結果" + requestCode + "+" + resultCode + "+" + data);

		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case 0:
			Bundle b = data.getExtras(); // data为B中回传的Intent
			String result = b.getString("result"); // result即为回传的值(JSON格式)
			Log.e("MainActivity", "result=" + result);
			/**
			 * result的格式如下： 1.1.没有进行账单导入(后台没有通知) {"code" : -1, "function" :
			 * "mail", "searchId" : "", "taskId" : "} 1.2.服务不可用(后台没有通知) {"code"
			 * : -2, "function" : "mail", "searchId" : "", "taskId" : "}
			 * 1.3.任务创建失败(后台没有通知) {"code" : -3, "function" : "mail", "searchId"
			 * : "", "taskId" : "} 2.账单导入失败(后台有通知) {"code" : 0, "function" :
			 * "mail", "searchId" : "3550622685459407187", "taskId" :
			 * "ce6b3806-57a2-4466-90bd-670389b1a112"} 3.账单导入成功(后台有通知) {"code" :
			 * 1, "function" : "mail", "searchId" : "3550622685459407187",
			 * "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112"} 4.账单导入中(后台有通知)
			 * {"code" : 2, "function" : "mail", "searchId" :
			 * "3550622685459407187", "taskId" :
			 * "ce6b3806-57a2-4466-90bd-670389b1a112"}
			 */

			if (TextUtils.isEmpty(result)) {
				Toast.makeText(MyInfoActivity.this, "用户没有进行导入操作!",
						Toast.LENGTH_SHORT).show();
			} else {
				try {
					int code = 0;
					JSONObject jsonObject = new JSONObject(result);

					code = jsonObject.optInt("code");
					String taskId = jsonObject.optString("taskId");
					String searchId = jsonObject.optString("searchId");
					if (code == -1) {
						setDialog("用户没有进行导入操作!");
					} else if (code == 0) {
						setDialog("导入失败!");
					} else if (code == 1) {
						// setDialog(jsonObject.getString("message"));

						String type = "carrier";
						if (jsonObject.has("taskType")) {
							type = jsonObject.getString("taskType");
						} else if (jsonObject.has("function")) {
							type = jsonObject.getString("function");
						}
						switch (type) {

						case "carrier":
							// 运营商导入
							HttpUtils.doGetAsyn(Config.AUTHMOBILE_CORD
									+ "&userid=" + userId + "&code=" + code
									+ "&taskId=" + taskId + "&outOrderId="
									+ searchId, mHandler,
									Config.CODE_AUTHMOBILE);
							break;
						case "taobao":
							HttpUtils.doGetAsyn(Config.AUTHTAOBAO_CORD
									+ "&userid=" + userId + "&code=" + code
									+ "&taskId=" + taskId + "&outOrderId="
									+ searchId, mHandler,
									Config.CODE_AUTHMOBILE);
							break;
						case "jingdong":
							HttpUtils.doGetAsyn(Config.AUTHJD_CORD + "&userid="
									+ userId + "&code=" + code + "&taskId="
									+ taskId + "&outOrderId=" + searchId,
									mHandler, Config.CODE_AUTHMOBILE);
							break;
						default:
							break;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case 8001:
			CreditApp.onActivityResult(requestCode, resultCode, data);
			break;
		default:
			break;
		}
	}

	// S-A(OCR+活体)
	public void mSAocrLiveness() {
		String urlNotify = Config.URL
				+ "servlet/current/NotifyResultProcessorAction";
		AuthBuilder authBuilder = getAuthBuilder();

		authBuilder.isShowConfirm(true);// 2016/12/28 OCR之后需要确认身份证识别信息
		authBuilder.setPackageCode("TC008");
		// authBuilder.setPackageCode(getCodeS());// 如果有的话需要设置,可不设置

		authBuilder.ocrLiveness(this, urlNotify);

	}

	private AuthBuilder getAuthBuilder() {
		// SharedPreferences sp = getSharedPreferences("config", 0x0000);
		// String UserId = sp.getString("userid", "");
		/** 测试环境 */
		String partner_order_id = userId + "&" + System.currentTimeMillis(); // 商户订单号;
		String pubKey = "217fd592-4013-42be-a813-824bdab5598a"; // 商户号
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		String sign_time = simpleDateFormat.format(new Date());
		String security_key = "8e2440e3-90eb-4a82-a488-f1ead57eddfd"; // 商户密钥

		String singStr = "pub_key=" + pubKey + "|partner_order_id="
				+ partner_order_id + "|sign_time=" + sign_time
				+ "|security_key=" + security_key;

		Log.d("MainActivity", singStr);

		String sign = Md5.encrypt(singStr);

		OnResultListener listener = new OnResultListener() {
			@Override
			public void onResult(int op_type, String result) {

				BaseResponse response = new Gson().fromJson(result,
						BaseResponse.class);
				if (response != null
						&& !TextUtils.isEmpty(response.package_session_id)) {
					packageSessionId = response.package_session_id;
				}
				try {
					switch (op_type) {
					case AuthBuilder.OPTION_ERROR:// 流程终端异常----取消验证
						Toast.makeText(getApplicationContext(), "身份识别失败，请重试",
								Toast.LENGTH_SHORT).show();
						break;
					case AuthBuilder.OPTION_LIVENESS: // 活体检测结果回调
														// result+="活体检测";
						// Toast.makeText(getApplicationContext(), "OCR"+result,
						// Toast.LENGTH_SHORT).show();
						JSONObject object = new JSONObject(result);
						int code = object.getInt("ret_code");
						if (code == 000000) {
							isshenfen = 1;
							shenfen_msg_img
									.setImageResource(R.drawable.center_basedate_icon);
							shenfen_msg_txt.setText("已认证");
							shenfen_msg_txt.setTextColor(getResources()
									.getColor(R.color.yellor));

							Toast.makeText(getApplicationContext(), "身份识别成功",
									Toast.LENGTH_SHORT).show();
							System.out.println("+code+" + isshenfen);
						}
						break;
					case AuthBuilder.OPTION_OCR: // 身份证结果回调
													// 身份证扫描结果result="OCR结果";身份证扫描成功会弹出身份识别成功

						// Toast.makeText(getApplicationContext(), "OCR结果" +
						// result,
						// Toast.LENGTH_SHORT).show();
						//
						break;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("+结果返回+" + result);

			}

		};
		return new AuthBuilder(partner_order_id, pubKey, sign_time, sign,
				listener);
	}

	private void zmAuth(String params, String appId, String sign) {
		// TODO Auto-generated method stub
		// 测试数据，此部分数据，请由商户服务端生成下发，具体见开放平台商户对接文档
		// 请注意params、sign为encode过后的数据
		// String params =
		// "IsFyG77jip6mJt%2BgyeC8i4knR16LUEJXr9h0Tt7cp%2Bt6gFW9uQem599fnJGFEI0wHJCd84yQ8y89nBV5%2BKdLqEgwE7xRLrWj6fCbYvRnIjFIDthShR5nS1PY4W2abNxq%2BJTp%2BT2OJa9NOLIZq9HXOZg8%2BSVgTbkAt5dmrjmhnTdBCvV3zVsO7p83YN9js8ESPCZyWCNgyWOIPPeSePrBsZEXCs2ebxwQGN62%2FdeqaW1S9PKKEg7e8i%2F6PK2xZYjLFfNydoS1gqA%2BGDkhVeCsN6NZyWffotS0QWA9j9PXSN6nYN8HNIAma5%2BBq%2FbKDsxK0Af2CXg%2B3FJ3ArnnlHr6iLI96m6AsZYZlYcYFwbE25KZysohn5SqsVAF2AZriSEHEpiak9k6AIOduVmrlwuI6sdP%2FqoWM1oEgqnEV%2Faiz7ydODatVe5CC6pdJxP7ZxbyzOyMAB7um3uYjPQb0LTOsvOsRgCMQAPH8%2FJrMZvrHyv0q3C9NjnNQKDYlATYXJCa";
		// String appId = "1002180";
		// String sign =
		// "LpalhM1PJPKudoUCSZDBslOCMCG%2F8KIODWzby%2FxEaNV%2F3oAk5FGTXN4XTXDXf6qaYo6tsRmGjCvR%2FCr14KLwNb%2B7lIkVwSplGVV1b6ngQeEQPNjWrpyUolyR%2BsZ368vaJ%2BXJBh3jLg%2FyRGDD%2FNg6cNuQgt1JggKVZIed6Zmk%2FUg%3D";

		// extParams参数可以放置一些额外的参数，例如当biz_params参数忘记组织auth_code参数时，可以通过extParams参数带入auth_code。
		// 不过建议auth_code参数组织到biz_params里面进行加密加签。
		Map<String, String> extParams = new HashMap<>();
		// extParams.put("auth_code", "M_FACE");
		extParams.put("state", userId);

		try {
			// 请求授权
			CreditAuthHelper.creditAuth(MyInfoActivity.this, appId, params,
					sign, extParams, new ICreditListener() {
						private String paramsHttp;
						private String signHttp;

						@Override
						public void onComplete(Bundle result) {
							// toast message
							MyInfoActivity.this.toastMessage("授权成功");
							// 从result中获取params参数,然后解析params数据,可以获取open_id。

							if (result != null) {
								Set<String> keys = result.keySet();
								for (String key : keys) {
									if (key.equals("params")) {
										paramsHttp = result.getString(key);
									}
									if (key.equals("sign")) {
										signHttp = result.getString(key);
									}
									Log.d("ZHIMA_DemoActivity", key + " = "
											+ result.getString(key));
								}
							}
							try {
								paramsHttp = URLDecoder.decode(paramsHttp,
										"UTF-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// }

							// if(signHttp.indexOf("%")!=-1){
							try {
								signHttp = URLDecoder.decode(signHttp, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// }

							String params = "&params=" + paramsHttp + "&sign="
									+ signHttp;

							// HttpUtils.doPostAsyn(Config.ZMEND_CORD, params,
							// mHandler, Config.CODE_ZMEND);
							checkdata(params);

						}

						@Override
						public void onError(Bundle result) {
							// toast message
							MyInfoActivity.this.toastMessage("授权错误");
							Log.d("ZHIMA_DemoActivity",
									"DemoPresenterImpl.doCreditAuthRequest.onError.");
						}

						@Override
						public void onCancel() {
							// toast message
							MyInfoActivity.this.toastMessage("授权失败");
							Log.d("ZHIMA_DemoActivity",
									"DemoPresenterImpl.doCreditAuthRequest.onCancel.");
						}
					});
		} catch (Exception e) {
			Log.e("ZHIMA_DemoActivity",
					"DemoPresenterImpl.doCreditAuthRequest.exception="
							+ e.toString());
		}
	}

	private void checkdata(final String param) {
		// 启动子线程异步加载数据
		new Thread() {

			@Override
			public void run() {

				Message msg = Message.obtain();
				PrintWriter out = null;
				BufferedReader in = null;
				String result = "";
				try {
					URL realUrl = new URL(Config.ZMEND_CORD);
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
					if (param != null && !param.trim().equals("")) {
						// 获取URLConnection对象对应的输出流
						out = new PrintWriter(conn.getOutputStream());
						// 发送请求参数
						out.print(param);
						// flush输出流的缓冲
						out.flush();
					}
					// 定义BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(
							conn.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
					msg.what = Config.CODE_ZMEND;
					System.out.println(result);
					// } else {
					// msg.what = Config.CODE_URL_ERROR;
					// }
				} catch (Exception e) {
					// 网络错误异常
					msg.what = Config.CODE_NET_ERROR;
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
						msg.what = Config.CODE_NET_ERROR;
						ex.printStackTrace();
					}
				}
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	@Override
	public void toastMessage(final String msg) {
		// check context
		if (isFinishing() || TextUtils.isEmpty(msg)) {
			return;
		}
		// toast message
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MyInfoActivity.this, msg, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intents = new Intent(MyInfoActivity.this,
					IndexActivity.class);
			intents.putExtra("id", 1);
			startActivity(intents);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setDialog(String Message) {
		new AlertDialog(MyInfoActivity.this).builder().setMsg(Message)
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
