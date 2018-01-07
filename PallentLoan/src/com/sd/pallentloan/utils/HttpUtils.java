package com.sd.pallentloan.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.StringBody;

import android.os.Message;

import android.os.Handler;

public class HttpUtils {
	private static final int TIMEOUT_IN_MILLIONS = 10000;
	private static String retrunStr,retrunStr2;

	public interface CallBack {
		void onRequestComplete(String result);
	}

	/**
	 * 异步的Get请求
	 * 
	 * @param urlStr
	 * @param callBack
	 */
	static String result = "";

	public static String doGetAsyn(final String urlStr, final Handler handler,
			final int type) {

		new Thread() {
			public void run() {
				try {
					result = doGet(urlStr, handler, type);

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		return result;
	}

	public static String doGet(final String urlStr, Handler handler, int type) {

		Message msg = Message.obtain();
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			System.out.println("url--" + urlStr);
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
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
				retrunStr = baos.toString();
				msg.what = type;
			} else {
				msg.what = Config.CODE_URL_ERROR;
			}

		} catch (MalformedURLException e) {
			// url错误的异常
			msg.what = Config.CODE_URL_ERROR;
			e.printStackTrace();
		} catch (IOException e) {
			// 网络错误异常
			msg.what = Config.CODE_NET_ERROR;
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
		msg.obj = retrunStr;
		handler.sendMessage(msg);
		return retrunStr;
	}

	public static String doPostAsyn(final String urlStr, final String params,
			final Handler handler, final int type) {
		new Thread() {
			public void run() {
				try {
					result = doPost(urlStr, params, handler, type);

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		return result;
	}

	/**
	 * 另一种Post请求，获得返回数据
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public static String MyPost(String url, final Handler handler,
			Map<String, String> param, int type) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		MultipartEntity entity = new MultipartEntity();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() > 0) {
					entity.addPart(entry.getKey(),
							new StringBody(entry.getValue()));
				}
			}
		}
		post.setEntity(entity);
		HttpResponse response = httpClient.execute(post);
		int stateCode = response.getStatusLine().getStatusCode();
		if (stateCode == HttpStatus.SC_OK) {
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "utf-8"));
				String data = br.readLine();
				return data;
			}
		} else {
			System.out.println("获取失败");
		}
		post.abort();
		return null;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws Exception
	 */
	public static String doPost(String url, String param, Handler handler,
			int type) {
		System.out.println(url+"参数"+param);
		Message msg = Message.obtain();
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
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
			conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

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
			msg.what = type;
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
		handler.sendMessage(msg);
		return retrunStr2;
	}

}
