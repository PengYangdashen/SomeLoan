package com.fruit.dayloan.utils;

import java.text.DecimalFormat;

public class Formatdou {

	
	public static  final String formatdou(Double str){
		DecimalFormat    df   = new DecimalFormat("######0.00"); 
		
		return df.format(str);
	}
}
