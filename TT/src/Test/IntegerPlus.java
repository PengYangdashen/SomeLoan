package Test;

import java.text.DecimalFormat;

public class IntegerPlus {
	public static void main(String[] args) {
		String a = "500";
		String b = "5";
		int c = Integer.parseInt(a) + Integer.parseInt(b);
		System.out.println(Integer.parseInt(a));
		System.out.println(Integer.parseInt(b));
		System.out.println(Integer.parseInt(a) + Integer.parseInt(b));
		System.out.println(c);
		
		double z = 20.3285441;
		DecimalFormat df = new DecimalFormat("#.00");  
		System.out.println(df.format(z));  
		double x = 32.011;
		double v = z+x;
		
		System.out.println(Double.toString(v));
	}
}
