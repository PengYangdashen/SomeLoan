package com.sd.pallentloan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;



import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.sd.pallentloan.R;
import com.sd.pallentloan.utils.AlertDialog;
import com.sd.pallentloan.utils.BaseActivity;
import com.sd.pallentloan.utils.HttpUtils;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InviteActivity extends BaseActivity implements OnClickListener {

	Bitmap bmp=null;
	private ImageView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);
		initView();
		ShareSDK.initSDK(this);
	}

	private void initView() {
		TextView title_txt_center = (TextView) findViewById(R.id.title_txt_center);
		title_txt_center.setText("我要赚钱");
		RelativeLayout backpress = (RelativeLayout) findViewById(R.id.backpress);
		backpress.setOnClickListener(this);
//		Button adward_btn = (Button) findViewById(R.id.adward_btn);
//		adward_btn.setOnClickListener(this);
		Button getmoney_btn = (Button) findViewById(R.id.getmoney_btn);
		getmoney_btn.setOnClickListener(this);
		
		SharedPreferences sp = getSharedPreferences("config", 0x0000);
		userId = sp.getString("userid", "");
		
		 result=(ImageView)findViewById(R.id.img);
		
		 
		 HttpUtils.doGetAsyn(com.sd.pallentloan.utils.Config.HOME_INIT + "&userid=" + userId, mHandler, com.sd.pallentloan.utils.Config.CODE_HOME_INIT);
		
		
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case com.sd.pallentloan.utils.Config.CODE_HOME_INIT:
				String reString = msg.obj.toString();
				try {
					// {"mobilephone":"17620110768","username":"GSD22","phone":"17620***68","creditlimit":"5000",
					// "isyhbd":"0","isshenfen":"0","islianxi":"0",
					// "err":0,"isjob":"0","rzstatus":"0","usablecreditlimit":"500","lastdate":"2017-02-22 17:39:09.0"}
					JSONObject jsonObject = new JSONObject(reString);

					// "dataJK":{"id":8,"cl03_status":0,"yuq_ts":null,"cl_status":0,"sjds_money":420,"sjsh_money":600,"cl02_status":0,"jk_money":800}
					
//					int rzstatus = Integer.parseInt(jsonObject
//							.getString("rzstatus"));

					String mobilephone = jsonObject.getString("mobilephone");
					String rzname = jsonObject.getString("rzname");
					if (rzname.equals("")) {
						url = com.sd.pallentloan.utils.Config.SHARE_CORD+"tgid="+userId+"&yqren="+mobilephone;
					}else {
						try {
							url = com.sd.pallentloan.utils.Config.SHARE_CORD+"tgid="+userId+"&yqren="+URLEncoder.encode(rzname, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
					
					 try {
							bmp=createBitmap(Create2DCode(url));
						} catch (WriterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
					 result.setImageBitmap(bmp);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;
			case com.sd.pallentloan.utils.Config.CODE_URL_ERROR:
				Toast.makeText(InviteActivity.this, "url错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case com.sd.pallentloan.utils.Config.CODE_NET_ERROR:
				Toast.makeText(InviteActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case com.sd.pallentloan.utils.Config.CODE_SHARE:
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
				} catch (JSONException e) {
					Toast.makeText(InviteActivity.this, "数据解析错误",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;
			default:
				break;
			}
		};
	};
	private String url;
	private String userId;
	
	
	 private Bitmap createBitmap( Bitmap src)
	    {
	    if( src == null )
	    {
	    return null;
	    }
	    Paint paint=new Paint();
	    paint.setColor(Color.WHITE);
	    paint.setAntiAlias(true);
	   
	    int w = 300;
	    int h = 300;
	    Bitmap newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );
	    Canvas cv = new Canvas( newb );

	    cv.drawColor(Color.WHITE);
	 
	    cv.drawBitmap(src, 0, 0, null );
	    cv.save( Canvas.ALL_SAVE_FLAG );
	    cv.restore();//存储
	    return newb;

	    }
	
	 public Bitmap Create2DCode(String str) throws WriterException, UnsupportedEncodingException {
			//生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
	    	
			BitMatrix matrix = new MultiFormatWriter().encode(new String(str.getBytes("GBK"),"ISO-8859-1"),BarcodeFormat.QR_CODE, 300, 300);
			
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			//二维矩阵转为一维像素数组,也就是一直横着排了
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if(matrix.get(x, y)){
						pixels[y * width + x] = 0xff000000;
					}
					
				}
			}	
			int[] colors={R.color.white};
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			//通过像素数组生成bitmap,具体参考api
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		}

	@Override
	public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backpress:
				finish();
				break;
//			case R.id.adward_btn:
//				startActivity(new Intent(this, MoneyRecordActivity.class));
//				break;
			case R.id.getmoney_btn://分享
				showShare();
				break;
			default:
				break;
			}
	}
	
	
	private void showShare() {
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		 // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
		 oks.setTitle("送你个借款神器，粒粒贷");
		 // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
		 oks.setTitleUrl(url);
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("有身份证就能轻松借5000元，纯信用，秒到账......");
		 //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		 oks.setImageUrl(com.sd.pallentloan.utils.Config.SHARELOGO_CODE);
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl(url);
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("没钱就找粒粒贷");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(url);
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl(url);

		// 启动分享GUI
		 oks.show(this);
		 }

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
	
}
