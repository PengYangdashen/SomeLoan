package Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestNet {
	public static void main(String[] args) {
		String url = "http://news.baidu.com/ns?cl=2&rn=20&tn=news&word=%E6%B0%B4%E7%94%B5%E8%B4%B9";

		PersonForHome home = new PersonForHome("1", "2", 2);
		PersonForHome home2 = new PersonForHome("3", "2", 2);
		PersonForHome[] a = { home, home2 };
		System.out.println(Arrays.asList(a));
		List list = Arrays.asList(a);
		System.out.println(list.toString());
		JsonArray arr = new JsonArray();
		JsonObject o1 = new JsonObject();
		o1.addProperty("name", "name");
		o1.addProperty("name2", "name2");
		arr.add(o1);
		System.out.println(arr.toString());

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		// 换成自己的ip就行
		OkHttpClient client = new OkHttpClient();// 创建okhttp实例
		RequestBody body = RequestBody.create(JSON, arr.toString());
		Request request = new Request.Builder().url(url).post(body).build();
		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			// 请求失败时调用
			@Override
			public void onFailure(Call call, IOException e) {
				System.out.println("onFailure: " + e);
			}

			// 请求成功时调用

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				System.out.println("Response: " + arg1);
			}

		});
		String response = "";
		JsonObject obj = new JsonObject();
		JsonObject obj2 = new JsonObject();
		JsonArray arr2 = new JsonArray();
		obj.addProperty("1", "1");
		obj.addProperty("12", "12");
		obj.addProperty("13", "3");
		obj2.addProperty("213", "213");
		obj2.addProperty("213", "23");
		obj2.addProperty("213", "23");
		obj2.add("obj", obj);
		arr2.add(obj);
		obj2.add("arr2", arr2);
		System.out.println(obj2.toString());

	}
}
