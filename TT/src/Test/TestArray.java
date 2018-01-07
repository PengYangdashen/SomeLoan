package Test;

import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TestArray {

	public static void main(String[] args) {
		PersonForHome home = new PersonForHome("1", "2", 2);
		PersonForHome home2 = new PersonForHome("3", "2", 2);
		PersonForHome [] a = {home,home2};
		System.out.println(Arrays.asList(a));
		List list = Arrays.asList(a);
		System.out.println(list.toString());
		JsonArray arr = new JsonArray();
		JsonObject o1 = new JsonObject();
		o1.addProperty("name", "name");
		o1.addProperty("name2", "name2");
		arr.add(o1);
		System.out.println(arr.toString());
	}
	
	
}
