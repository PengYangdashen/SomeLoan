package com.example.alarmclock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
public class AlarmReceiver2 extends BroadcastReceiver {

	private String returnStr;

	private LocationManager lm = null;
	final java.text.DecimalFormat df = new java.text.DecimalFormat("#.000000");
	private Location myLocation = null;

	private Context mContext;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10010:
				String returnStr = msg.obj.toString();
				try {
				JSONObject jsonObject = new JSONObject(returnStr);
					if (0 == jsonObject.getInt("error")) {
						MyApplication.setCount(true);
						lm.removeUpdates(listener);
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

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss.SSSZ");

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		Toast.makeText(context, "闹铃响了, 可以做点事情了~~", Toast.LENGTH_LONG).show();

		lm = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
				listener);
	}

	LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(final Location location) {
			// 实际上报时间
			// String time = sdf.format(new Date(location.getTime()));
			// timeText.setText("实际上报时间：" + time);
			if (isBetterLocation(location, myLocation)) {
				
				MyApplication.setLocation(location);
				String Address = "";
				String city = "";
				String state = "";
				String zipCode = "";
				String country = "";

				String locationTime = sdf.format(new Date(location.getTime()));
				String currentTime = null;

				if (myLocation != null) {
					currentTime = sdf.format(new Date(myLocation.getTime()));
					myLocation = location;

				} else {
					myLocation = location;
				}

				// 获取当前详细地址
				StringBuffer sb = new StringBuffer();
				if (myLocation != null) {
					Geocoder gc = new Geocoder(mContext);
					List<Address> addresses = null;
					try {
						addresses = gc.getFromLocation(
								myLocation.getLatitude(),
								myLocation.getLongitude(), 1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (addresses != null && addresses.size() > 0) {
						Address address = addresses.get(0);
						sb.append(address.getCountryName()
								+ address.getLocality());
						sb.append(address.getSubThoroughfare());

						Address = addresses.get(0).getAddressLine(0);
					
						MyApplication.setAddress(address);
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
									conn = (HttpURLConnection) url
											.openConnection();
									conn.setReadTimeout(5000);
									conn.setConnectTimeout(5000);
									conn.setRequestMethod("GET");
									conn.setRequestProperty("accept", "*/*");
									conn.setRequestProperty("connection",
											"Keep-Alive");
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
										System.out.println("doGet:"
												+ baos.toString());
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
					}
				}

				// tv.setText("经度：" + lon + "\n纬度：" + lat + "\n服务商：" + provider
				// + "\n准确性：" + accuracy + "\n高度：" + altitude + "\n方向角："
				// + bearing + "\n速度：" + speed + "\n上次上报时间：" + currentTime
				// + "\n最新上报时间：" + locationTime + "\n您所在的城市："
				// + sb.toString());
				//
				// tv.append("\n " + Address + "," + city + "," + state + ","
				// + zipCode + "," + country);
			}

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i("tag", "onStatusChanged: " + provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i("tag", "onProviderEnabled: " + provider);
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.i("tag", "onProviderDisabled: " + provider);
		}

	};

	private static final int TWO_MINUTES = 1000 * 2 * 60;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

}