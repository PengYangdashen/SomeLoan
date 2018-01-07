package com.fruit.dayloan;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.shandai.view.MyProgressDialog;
import com.fruit.dayloan.R;
import com.fruit.dayloan.pojo.MoneyPojo;
import com.fruit.dayloan.utils.AlertDialog;
import com.fruit.dayloan.utils.BaseActivity;
import com.fruit.dayloan.utils.Config;
import com.fruit.dayloan.utils.HttpUtils;
import com.fruit.dayloan.utils.TimeUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends BaseActivity {
	private String userId;
	private OSSClient oss;
	private OSSAsyncTask task;
	private String realname;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		userId = sp.getString("userid", "");

		String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
		// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
				"LTAIpA9T55i06zvn", "CmFJ27UzsKNcSVTeCn4nxxI1djyRRg");
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
		conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
		conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
		conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
		oss = new OSSClient(getApplicationContext(), endpoint,
				credentialProvider, conf);
		
		
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("上传视频");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (task!=null) {
		    		task.cancel(); // 可以取消任务
				}
				finish();
			}
		});
		dialog = new MyProgressDialog(CameraActivity.this, "");
		pf_money = (TextView) findViewById(R.id.pf_money);
		sj_money = (TextView) findViewById(R.id.sj_money);
		jk_date = (TextView) findViewById(R.id.jk_date);
		jk_referne = (TextView) findViewById(R.id.jk_referne);
		low_referne = (TextView) findViewById(R.id.low_referne);
		fx_money = (TextView) findViewById(R.id.fx_money);
		Button confirm = (Button) findViewById(R.id.confirm);
		check = (CheckBox) findViewById(R.id.check);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!check.isChecked()) {
					setDialog("请勾上借款说明");
					return;
				}
				new AlertDialog(CameraActivity.this).builder().setMsg("拍摄视频时请说出下列话：我叫"+realname+"通过人人闪贷成功借款"+pf_money.getText().toString())
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
//						finish();
						
						Intent intent = new Intent(CameraActivity.this,Camera2Activity.class);
						intent.putExtra("name",realname);
						intent.putExtra("money",pf_money.getText().toString());
						startActivity(intent);
						finish();
					}
				}).show();
				
			}
		});
		
		HttpUtils.doGetAsyn(Config.BACKMONEYINIT_CORD+"&jkid="+getIntent().getStringExtra("jkid"), mHandler,
				Config.CODE_BACKMONEYINIT);
	}
	
	/** 
	 * 捕捉back 
	 */  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
	    	if (task!=null) {
	    		task.cancel(); // 可以取消任务
			}
			finish();
	        return true;  
	        
	    }  
	      
	    return super.onKeyDown(keyCode, event);  
	}  
	
	
	private Handler mHandler = new Handler() {
	

		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case Config.CODE_URL_ERROR:
				Toast.makeText(CameraActivity.this, "url错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case Config.CODE_NET_ERROR:
				Toast.makeText(CameraActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 11:
				Toast.makeText(CameraActivity.this, "上传失败", Toast.LENGTH_SHORT)
				.show();
//				finish();
				dialog.dismiss();
				break;
			case Config.CODE_BACKMONEYINIT:
				String result = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(result);
					java.text.DecimalFormat df = new java.text.DecimalFormat("#.00"); 
					int all= jsonObject.getInt("sjsh_money");
					int real= jsonObject.getInt("sjds_money");
					pf_money.setText(df.format(all)+"元");
					sj_money.setText(df.format(real)+"元");
					int jk_dates = jsonObject.getInt("jk_date");
					int annualrate = jsonObject.getInt("annualrate");
					
					
					double low = 0.0,fw=0.0;
					if (jk_dates==1) {
						jk_date.setText("15天");
						low = (all*0.03*0.5);
						fw = (all*0.27*0.5);
						jk_referne.setText(df.format(all*0.3*0.5));  //总费用
					}else {
						jk_date.setText("30天");
						low = (all*0.03);
						fw = (all*0.27);
						jk_referne.setText(df.format(all*0.3));  //总费用
					}
					low_referne.setText(df.format(low));
					fx_money.setText(df.format(fw));
					realname = jsonObject.getString("realname");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Config.CODE_VIDEO:
				String result2 = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result2);
					if (json.getInt("err")==0) {
//						tv.setText("上传成功！");
//						pb.setVisibility(ProgressBar.GONE);
						startActivity(new Intent(CameraActivity.this, IndexActivity.class));
						finish();
						dialog.dismiss();
					}else {
//						setDialog(json.getString("msg"));
						new AlertDialog(CameraActivity.this).builder().setMsg(json.getString("msg"))
						.setNegativeButton("确定", new OnClickListener() {
							@Override
							public void onClick(View v) {
								finish();
							}
						}).show();
					}
					
				} catch (JSONException e) {
					Toast.makeText(CameraActivity.this, "数据解析错误",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;
			default:
				break;
			}
		};
	};
	private ProgressBar pb;
	private TextView tv;
	private CheckBox check;
	private TextView pf_money;
	private TextView sj_money;
	private TextView jk_date;
	private TextView jk_referne;
	private TextView low_referne;
	private TextView fx_money;
	private MyProgressDialog dialog;
	private void setDialog(String Message) {
		new AlertDialog(CameraActivity.this).builder().setMsg(Message)
				.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
//						finish();
					}
				}).show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("test", "onActivityResult() requestCode:" + requestCode
				+ ",resultCode:" + resultCode + ",data:" + data);
		dialog.show();
		if (null != data) {
			Uri uri = data.getData();
			if (uri == null) {
				return;
			} else {
				Cursor c = getContentResolver().query(uri,
						new String[] { MediaStore.MediaColumns.DATA }, null,
						null, null);
				if (c != null && c.moveToFirst()) {
					/* _data：文件的绝对路径 ，_display_name：文件名 */
					final String strVideoPath = c
							.getString(c.getColumnIndex("_data"));
//					Toast.makeText(this, strVideoPath, Toast.LENGTH_SHORT)
//							.show();
					final String fileName =userId + "=" + System.currentTimeMillis();
					ConnectionDetector cd = new ConnectionDetector(CameraActivity.this);
					if(cd.isConnectingToInternet()){
						
						// 构造上传请求
						PutObjectRequest put = new PutObjectRequest("sd-shipin",
								fileName,
								strVideoPath);

						// 异步上传时可以设置进度回调
						put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
							@Override
							public void onProgress(PutObjectRequest request,
									long currentSize, long totalSize) {
								Log.d("PutObject", "currentSize: " + currentSize
										+ " totalSize: " + totalSize);
							}
						});

						task = oss
								.asyncPutObject(
										put,
										new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
											@Override
											public void onSuccess(
													PutObjectRequest request,
													PutObjectResult result) {
												Log.d("PutObject", "UploadSuccess");
												
												HttpUtils.doGetAsyn(Config.VIDEO_CORD + "&userid=" + userId+"&videoUrl="+fileName, mHandler,
														Config.CODE_VIDEO);
//												handle.sendEmptyMessage(0);
											}

											@Override
											public void onFailure(
													PutObjectRequest request,
													ClientException clientExcepion,
													ServiceException serviceException) {

												// 请求异常
												if (clientExcepion != null) {
													// 本地异常如网络异常等
													clientExcepion
															.printStackTrace();
												}
												if (serviceException != null) {
													// 服务异常
													Log.e("ErrorCode",
															serviceException
																	.getErrorCode());
													Log.e("RequestId",
															serviceException
																	.getRequestId());
													Log.e("HostId",
															serviceException
																	.getHostId());
													Log.e("RawMessage",
															serviceException
																	.getRawMessage());
													mHandler.sendEmptyMessage(11);
												}
											}
										});
					}
				}
			}
					}else{
						mHandler.sendEmptyMessage(11);
					}
					
					
		}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

}