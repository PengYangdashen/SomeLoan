package Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestList {
	public static void main(String[] args) {
		List<PersonForHome> list = new ArrayList<PersonForHome>();
		for (int i = 1; i < 15; i++) {
			list.add(new PersonForHome());
		}
		Collections.sort(list);
		
		List<String> list2 = new ArrayList<String>();
		list2.add("aaa");
		list2.add("222");
		list2.add("aa33a");
		list2.add("aaffa");
		System.out.println(list2.toString());
	}
}
