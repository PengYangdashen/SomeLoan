package com.example.alarmclock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: AlarmReceiver
 * @Description: 闹铃时间到了会进入这个广播，这个时�?可以做一些该做的业务�? * @author HuHood
 * @date 2013-11-25 下午4:44:30
 * 
 */
public class AlarmReceiver extends BroadcastReceiver {

	private String dwlat;
	private String dwlng;
	private String returnStr;
	private LocationManager locationManager;
	private String locationProvider;
	
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		Toast.makeText(context, "闹铃响了, 可以做点事情了~~", Toast.LENGTH_LONG).show();
        //获取地理位置管理器  
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
        
        if (!isOPen(context)) {
			openGPS(context);
			Log.w(getClass().getSimpleName(), "openGPS");
			locationProvider = LocationManager.GPS_PROVIDER;  
		}
        //获取所有可用的位置提供器  
        List<String> providers = locationManager.getProviders(true);  
        if(providers.contains(LocationManager.GPS_PROVIDER)){  
            //如果是GPS  
            locationProvider = LocationManager.GPS_PROVIDER;  
        }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){  
            //如果是Network  
            locationProvider = LocationManager.NETWORK_PROVIDER;  
        }else if(providers.contains(LocationManager.PASSIVE_PROVIDER)){  
        	//如果是Network  
        	locationProvider = LocationManager.PASSIVE_PROVIDER;  
        }else{  
            Toast.makeText(context, "没有可用的位置提供器:GPS->" + LocationManager.GPS_PROVIDER, Toast.LENGTH_SHORT).show();  
        }  
        Log.w(getClass().getSimpleName(), "locationProvider:" + locationProvider);

        //获取Location  
        Location location = locationManager.getLastKnownLocation(locationProvider);  
          
        if(location!=null){  
            //不为空,显示地理位置经纬度  
              
            showLocation(location);  
        }else{  
            Toast.makeText(context, "location为空", Toast.LENGTH_SHORT).show();  
            
        }  
        //监视地理位置变化  
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);  
          
	}

	/**
	 * 显示地理位置经度和纬度信息
	 * 
	 * @param location
	 */
	private void showLocation(final Location location) {
		/*
		 * String locationStr = "维度：" + location.getLatitude() +"\n" + "经度：" +
		 * location.getLongitude(); postionView.setText(locationStr);
		 */
		String locationStr = "维度：" + location.getLatitude() + "\n" + "经度："
				+ location.getLongitude();
		final java.text.DecimalFormat df = new java.text.DecimalFormat("#.000000");  
		
		Toast.makeText(mContext, locationStr, Toast.LENGTH_LONG).show();
		new Thread(new Runnable() {

			public void run() {
				String Url = "http://www.shandkj.com/servlet/current/JBDUserAction?function=GetIP"
						+ "&userid=118845"
						+ "&dwlat="
						+ df.format(location.getLatitude())
						+ "&dwlng="
						+ df.format(location.getLongitude());
				URL url = null;
				HttpURLConnection conn = null;
				InputStream is = null;
				ByteArrayOutputStream baos = null;
				try {
					System.out.println("url--" + Url);
					url = new URL(Url);
					conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					conn.setRequestProperty("accept", "*/*");
					conn.setRequestProperty("connection", "Keep-Alive");
					conn.connect();
					if (conn.getResponseCode() == 200) {
						is = conn.getInputStream();
						baos = new ByteArrayOutputStream();
						int len = -1;
						byte[] buf = new byte[128];
						System.out.println("--------");
						while ((len = is.read(buf)) != -1) {
							baos.write(buf, 0, len);
						}
						baos.flush();
						System.out.println("doGet:" + baos.toString());
						returnStr = baos.toString();
					}

				} catch (MalformedURLException e) {
					// url错误的异常
					e.printStackTrace();
				} catch (IOException e) {
					// 网络错误异常
					e.printStackTrace();
				} finally {
					try {
						if (is != null)
							is.close();
					} catch (IOException e) {
					}
					try {
						if (baos != null)
							baos.close();
					} catch (IOException e) {
					}
					conn.disconnect();
				}
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 组装反向地理编码的接口位置
					StringBuilder url = new StringBuilder();
					url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
					url.append(location.getLatitude()).append(",");
					url.append(location.getLongitude());
					url.append("&sensor=false");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url.toString());
					httpGet.addHeader("Accept-Language", "zh-CN");
					HttpResponse response = client.execute(httpGet);
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						String res = EntityUtils.toString(entity);
						// 解析
						JSONObject jsonObject = new JSONObject(res);
						// 获取results节点下的位置信息
						JSONArray resultArray = jsonObject
								.getJSONArray("results");
						if (resultArray.length() > 0) {
							JSONObject obj = resultArray.getJSONObject(0);
							// 取出格式化后的位置数据
							String address = obj.getString("formatted_address");

							// Message msg = new Message();
							// msg.what = SHOW_LOCATION;
							// msg.obj = address;
							// handler.sendMessage(msg);

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/** 
     * LocationListern监听器 
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器 
     */  
      
    LocationListener locationListener =  new LocationListener() {  
          
        @Override  
        public void onStatusChanged(String provider, int status, Bundle arg2) {  
              
        }  
          
        @Override  
        public void onProviderEnabled(String provider) {  
              
        }  
          
        @Override  
        public void onProviderDisabled(String provider) {  
              
        }  
          
        @Override  
        public void onLocationChanged(Location location) {  
            //如果位置发生变化,重新显示  
            showLocation(location);  
              
        }  
    };  

	 /** 
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的 
     * @param context 
     * @return true 表示开启 
     */  
    public static final boolean isOPen(final Context context) {  
        LocationManager locationManager   
                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）  
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
        if (gps || network) {  
            return true;  
        }  
  
        return false;  
    }  
    
    /** 
     * 强制帮用户打开GPS 
     * @param context 
     */  
    public static final void openGPS(Context context) {  
        Intent GPSIntent = new Intent();  
        GPSIntent.setClassName("com.android.settings",  
                "com.android.settings.widget.SettingsAppWidgetProvider");  
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");  
        GPSIntent.setData(Uri.parse("custom:3"));  
        try {  
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();  
        } catch (CanceledException e) {  
            e.printStackTrace();  
        }  
    }  
	
}
