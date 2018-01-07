package com.sd.pallentloan.utils;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 倒计时工具类
 * 
 * @author jy qiu
 * 
 */
public class TimeCount extends CountDownTimer {
	private Button mBtn;
	private String title;

	public TimeCount(long millisInFuture, long countDownInterval, Button btn,
			String title) {
		super(millisInFuture, countDownInterval);
		this.mBtn = btn;
		this.title = title;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		mBtn.setText(millisUntilFinished / 1000 + "秒后重发");
		mBtn.setClickable(false);

	}

	@Override
	public void onFinish() {
		
		mBtn.setText(title);
		mBtn.setClickable(true);
	}

}
