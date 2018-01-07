package com.fruit.dayloan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditUtils {
	
	/**
	 * 手机号码中间添加空格
	 * @param s
	 * @return
	 */
	public static String handlePhoneNumber(String s){
		if (s == null || s.length() == 0) return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
		return sb.toString();
	}
	
	/**
	 * 校验是否是正确的手机号码格式
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		
		if(TextUtils.isEmpty(mobiles)){
			return false;
		}
		
		if(mobiles.contains(" ")){
			mobiles = mobiles.replaceAll(" ", "");
		}
		
		String regex = "^(1[3|4|5|7|8][0-9])\\d{8}$";
		Pattern p = Pattern
				.compile(regex);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	
}
