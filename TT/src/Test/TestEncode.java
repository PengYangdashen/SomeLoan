package Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class TestEncode {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String name = "扣款";
		String nameEncode = URLEncoder.encode(name, "UTF-8");
		System.out.println(nameEncode);
		name = URLDecoder.decode(nameEncode, "UTF-8");
		System.out.println(name);
	}
	
}
