package com.fruit.dayloan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fruit.dayloan.R;
import com.fruit.dayloan.adapter.HomeAdapter;
import com.fruit.dayloan.pojo.PersonPojo;
import com.fruit.dayloan.utils.AlertDialog;
import com.fruit.dayloan.utils.Config;
import com.fruit.dayloan.utils.HttpUtils;
import com.fruit.dayloan.utils.StreamUtils;
import com.fruit.dayloan.utils.TimeTaskScroll;
public class HomeFragment extends Fragment {
	private View view;
	private int rzstatus;
	private int isshenfen;
	private int isjob,sfzmrz;;
	private int islianxi, ismobile, istaobao, isjd;
	private Activity ac;
	private TextView get_money;
	private ListView record_list;
	private ArrayList<PersonPojo> arrPojos;
	private HomeAdapter adapter;
	private String mobilephone, cardBank;
	private String jkid;
	private TextView out_money_txt;
	private int wdXiaoXi=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_home, container, false);
			initView();
			initData();
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	private void initView() {
		ac = getActivity();
		TextView title_txt_center = (TextView) view
				.findViewById(R.id.title_txt_center);
		title_txt_center.setText("人人闪贷");
		RelativeLayout backpress = (RelativeLayout) view
				.findViewById(R.id.backpress);
		backpress.setVisibility(View.INVISIBLE);
		news_warm = (ImageView) view
				.findViewById(R.id.news_warm);
		
		txt = (TextView) view.findViewById(R.id.txt);
		view.findViewById(R.id.title_right_rela).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ac, NewsActivity.class);
				startActivity(intent);
				
//				new AlertDialog(ac).builder().setMsg("正在开发中...")
//				.setNegativeButton("确定", new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						
//					}
//				}).show();
			}
		});
		view.findViewById(R.id.problem).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intents = new Intent(ac, ProblemActivity.class);
				intents.putExtra("title", "常见问题");
				intents.putExtra("url", Config.PROLEM_CODE);
				startActivity(intents);
			}
		});
		get_money = (TextView) view.findViewById(R.id.get_money);
		out_money_txt = (TextView) view.findViewById(R.id.out_money_txt);
		out_money_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ConnectionDetector cd = new ConnectionDetector(ac);
				if (cd.isConnectingToInternet()) {
				String type = out_money_txt.getText().toString();
				switch (type) {
				case "借款":
					if (rzstatus != 1) {// 请绑定银行卡
						setDialog("bank", "请先绑定银行卡！");
						return;
					}
					if (isshenfen != 1) {// 请填写个人信息
						setDialog("bank", "请先绑定个人资料！");
						return;
					}
					if (isjob != 1) {// 请填写工作信息
						setDialog("bank", "请先绑定工作信息！");
						return;
					}
					if (islianxi != 1) {// 请填写联系人信息
						setDialog("bank", "请先绑定联系人！");
						return;
					}
					if (ismobile != 1) {
						setDialog("bank", "请先进行手机运营商授权！");
						return;
					}
					if (isjd != 1) {
						if (istaobao!=1) {
							setDialog("bank", "请先进行淘宝或者京东授权！");
							return;
						}
					}
					if (istaobao != 1) {
						if (isjd!=1) {
							setDialog("bank", "请先进行淘宝或者京东授权！");
							return;
						}
					}
					if (sfzmrz!=1) {
						setDialog("bank", "请先进行芝麻认证！");
						return;	
					}
					Intent intent = new Intent(ac, IWantMoneyActivity.class);
					intent.putExtra("phone", mobilephone);
					intent.putExtra("cardBank", cardBank);
					startActivity(intent);
					break;
				case "查看借款详情":
					Intent intentc = new Intent(ac, OutMoneyRecordActivity.class);
					intentc.putExtra("type", "home");
					startActivity(intentc);
					
					break;
				case "上传视频":
					Intent intentv =new Intent(ac, CameraActivity.class);
					intentv.putExtra("jkid", jkid);
					startActivity(intentv);
//					setDialog("camera", "录制视频时请使用前置摄像头对着本人并说'我叫***,从光速贷借款**元'！");
					break;
				case "还款":
					Intent intents =new Intent(ac, BackMoney2Activity.class);
					intents.putExtra("jkid", jkid);
					startActivity(intents);
					break;
				case "等待放款中":
					Intent intento =new Intent(ac, OutMoneyRecordActivity.class);
					intento.putExtra("type", "home");
					startActivity(intento);
					break;
				default:
					break;
				}
			}
			}
		});
		record_list = (ListView) view.findViewById(R.id.record_list);

	};

	private void initData() {
		SharedPreferences sp = ac.getSharedPreferences("config", 0x0000);
		String userId = sp.getString("userid", "");
		String url = Config.HOME_INIT + "&userid=" + userId;
//		HttpUtils.doGetAsyn(Config.OUTMONEY_RECORD + "&type=0", mHandler,
//				Config.CODE_OUTMONEY_INIT);

		HttpUtils.doGetAsyn(url, mHandler, Config.CODE_HOME_INIT);
//		HttpUtils.doPostAsyn(Config.HOME_INIT,"&userid=" + userId, mHandler, Config.CODE_HOME_INIT);
//		HttpUtils.doPostAsyn(Config.OUTMONEY_RECORD ,"&type=0", mHandler, Config.CODE_OUTMONEY_INIT);
		checkdata();
	}

	private Handler mHandler = new Handler() {

		private int one;
		private int two;
		private int three;
				

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.CODE_HOME_INIT:
				String reString = msg.obj.toString();
				try {
					// {"mobilephone":"17620110768","username":"GSD22","phone":"17620***68","creditlimit":"5000",
					// "isyhbd":"0","isshenfen":"0","islianxi":"0",
					// "err":0,"isjob":"0","rzstatus":"0","usablecreditlimit":"500","lastdate":"2017-02-22 17:39:09.0"}
					JSONObject jsonObject = new JSONObject(reString);

					// "dataJK":{"id":8,"cl03_status":0,"yuq_ts":null,"cl_status":0,"sjds_money":420,"sjsh_money":600,"cl02_status":0,"jk_money":800}
					if (jsonObject.getString("dataJK")=="null") {
						out_money_txt.setText("借款");
					}else {
					JSONObject dataJK = jsonObject.getJSONObject("dataJK");
					if (dataJK.length() == 0) {
						out_money_txt.setText("借款");
					} else {
						one = dataJK.getInt("cl_status");
						two = dataJK.getInt("cl02_status");
						three = dataJK.getInt("cl03_status");
						int spzt = dataJK.getInt("spzt");
						int sfyfk = dataJK.getInt("sfyfk");
						if (one ==0) {
							out_money_txt.setText("查看借款详情");
						} else if (one == 1 && two == 1 &&  three == 0&&spzt == 0) {
							out_money_txt.setText("上传视频");
						} else if (one == 1 && two == 1 && three == 1&& spzt == 1&& sfyfk == 1) {
							out_money_txt.setText("还款");
						} else if (one == 1 && two == 1 && three == 1&& spzt == 1&& sfyfk == 2) {
							out_money_txt.setText("等待放款中");
						} else if (one==1&&two==0) {
							out_money_txt.setText("查看借款详情");
						}else if (one==1&&two==1&&three==0) {
							out_money_txt.setText("查看借款详情");
						}
						
						else if (one == 1 && two == 1 && spzt == 1) {
							out_money_txt.setText("查看借款详情");
						}
						else if (one == 3|| two == 3) {
							out_money_txt.setText("借款");
						}
//						else if (one == 1&& two == 1&& three == 3) {
//							out_money_txt.setText("上传视频");
//						}
						
						if (one==1&&two==1) {
							get_money.setText("￥"+dataJK.getString("sjsh_money"));
							txt.setText("审批金额");
						}else {
							get_money.setText("￥"+jsonObject.getString("creditlimit"));
							txt.setText("借款额度");
						}
					}
					jkid = dataJK.getInt("id")+"";
					
					}
					
					sfzmrz=Integer.parseInt(jsonObject
							.getString("sfzmrz"));
					rzstatus = Integer.parseInt(jsonObject
							.getString("rzstatus"));
					isshenfen = Integer.parseInt(jsonObject
							.getString("isshenfen"));
					isjob = Integer.parseInt(jsonObject.getString("isjob"));
					islianxi = Integer.parseInt(jsonObject
							.getString("islianxi"));
					istaobao = Integer.parseInt(jsonObject
							.getString("istaobaoyz"));// istaobaoyz isjingdongyz
														// isyyshang
					isjd = Integer.parseInt(jsonObject
							.getString("isjingdongyz"));
					ismobile = Integer.parseInt(jsonObject
							.getString("isyyshang"));

					mobilephone = jsonObject.getString("mobilephone");
					cardBank = jsonObject.getString("rzcard");

					wdXiaoXi = jsonObject.getInt("wdXiaoXi");
					if (wdXiaoXi==1) {
						news_warm.setVisibility(View.VISIBLE);
					}else {
						news_warm.setVisibility(View.INVISIBLE);
					}
					
					int invest_status = Integer.parseInt(jsonObject
							.getString("invest_status"));
					SharedPreferences sp = ac.getSharedPreferences("config",
							ac.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putInt("rzstatus", rzstatus);
					editor.putInt("invest_status", invest_status);
					editor.commit();
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;
			case Config.CODE_URL_ERROR:
				Toast.makeText(ac, "url错误", Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(ac, "网络错误", Toast.LENGTH_SHORT).show();
				break;


			default:
				break;
			}
		};
	};
	
	private void checkdata() {
		// 启动子线程异步加载数据
		new Thread() {

			@Override
			public void run() {
				
				
				Message msg = Message.obtain();
				PrintWriter out = null;
				BufferedReader in = null;
				String result = "";
				try {
					URL realUrl = new URL(Config.OUTMONEY_RECORD);
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
					 String param= "&type=0";
					if (param != null && !param.trim().equals("")) {
						// 获取URLConnection对象对应的输出流
						out = new PrintWriter(conn.getOutputStream());
						// 发送请求参数
						out.print(param);
						// flush输出流的缓冲
						out.flush();
					}
					// 定义BufferedReader输入流来读取URL的响应
					in = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
					msg.what = Config.CODE_OUTMONEY_INIT;
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
				mHandlers.sendMessage(msg);
				
//				Message msg = Message.obtain();
//				HttpURLConnection conn = null;
//				try {
//					// 本机地址用localhost, 但是如果用模拟器加载本机的地址时,可以用ip(10.0.2.2)来替换
//					URL url = new URL(Config.OUTMONEY_RECORD + "&type=0");
//					conn = (HttpURLConnection) url.openConnection();
//					conn.setRequestMethod("GET");// 设置请求方法
//					conn.setConnectTimeout(5000);// 设置连接超时
//					conn.setReadTimeout(5000);// 设置响应超时, 连接上了,但服务器迟迟不给响应
//					conn.connect();// 连接服务器
//
//					int responseCode = conn.getResponseCode();// 获取响应码
//					if (responseCode == 200) {
//						InputStream inputStream = conn.getInputStream();
//						String result = StreamUtils.readFromStream(inputStream);
//						msg.obj=result;
//							msg.what = Config.CODE_OUTMONEY_INIT;
//					}
//				} catch (MalformedURLException e) {
//					// url错误的异常
//					msg.what = Config.CODE_URL_ERROR;
//					e.printStackTrace();
//				} catch (IOException e) {
//					// 网络错误异常
//					msg.what = Config.CODE_NET_ERROR;
//					e.printStackTrace();
//				} finally {
//
//					mHandlers.sendMessage(msg);
//					if (conn != null) {
//						conn.disconnect();// 关闭网络连接
//					}
//				}
			}
		}.start();
	}
	
	
	private Handler mHandlers = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
			case Config.CODE_URL_ERROR:
				Toast.makeText(ac, "url错误", Toast.LENGTH_SHORT).show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(ac, "网络错误", Toast.LENGTH_SHORT).show();
				break;

			case Config.CODE_OUTMONEY_INIT:
				String result = msg.obj.toString();
				try {
					JSONArray jsonArray = new JSONArray(result);
					arrPojos = new ArrayList<PersonPojo>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject json = jsonArray.getJSONObject(i);
						PersonPojo pojo = new PersonPojo();
						String time[]= json.getString("create_date").split(":");
						
						pojo.setCreate_date(time[0]+":"+time[1]);

						pojo.setCardusername(json.getString("cardusername")
								.substring(0, 1) + "**");
						pojo.setJk_money(json.getInt("jk_money"));
						arrPojos.add(pojo);
					}
					time.schedule(new TimeTaskScroll(ac, record_list, arrPojos), 20,
							20);
//					adapter = new HomeAdapter(ac, arrPojos);
//					record_list.setAdapter(adapter);
//					adapter.notifyDataSetChanged();
//					record_list.setSelection(Integer.MAX_VALUE / 2 + 1);
				} catch (JSONException e) {
					Toast.makeText(ac, "数据解析错误", Toast.LENGTH_SHORT).show();
					System.out.println("数据解析错误"+e+msg.what);
					System.out.println("数据解析状态"+msg.what);
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		};
	};
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		time.cancel();
		super.onDestroy();
	}	
	private TextView txt;
	private ImageView news_warm;
	private Timer time = new Timer();

	private void setDialog(final String type, String msg) {
		new AlertDialog(ac).builder().setMsg(msg)
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (type=="camera") {
							startActivity(new Intent(ac, CameraActivity.class));
						}
						if (rzstatus != 1) {// 请绑定银行卡
							Intent intent = new Intent(ac,
									BindCard2Activity.class);
							startActivity(intent);
						} else if (isshenfen != 1) {// 请填写个人信息
							Intent intent = new Intent(ac, MyInfoActivity.class);
							startActivity(intent);
						} else if (isjob != 1) {// 请填写工作信息
							Intent intent = new Intent(ac,
									MyInfoActivity.class);
							startActivity(intent);
						} else if (islianxi != 1) {// 请填写联系人信息
							Intent intent = new Intent(ac,
									MyInfoActivity.class);
							startActivity(intent);
						} else if (ismobile != 1) {
							Intent intent = new Intent(ac, MyInfoActivity.class);
							startActivity(intent);
						} else if (isjd != 1 || istaobao != 1) {
							Intent intent = new Intent(ac, MyInfoActivity.class);
							startActivity(intent);
						}else if (sfzmrz!=1) {
							Intent intent = new Intent(ac, MyInfoActivity.class);
							startActivity(intent);
						}

					}
				}).show();
	}

}
