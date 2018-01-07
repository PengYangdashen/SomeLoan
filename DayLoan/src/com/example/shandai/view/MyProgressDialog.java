package com.example.shandai.view;

import com.fruit.dayloan.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class MyProgressDialog extends ProgressDialog {
	private ImageView lading;
	private TextView lading_text;
	private String string;
	private AnimationDrawable rocketAnimation;

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public MyProgressDialog(Context context, String string) {
		super(context);
		this.string = string;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_progress_dialog);
		lading = (ImageView) findViewById(R.id.lading);
		lading_text = (TextView) findViewById(R.id.lading_text);
		rocketAnimation = (AnimationDrawable) lading.getBackground();
		lading_text.setText(string);
		this.setCanceledOnTouchOutside(false);
	}

	@Override
	public void show() {
		super.show();
		rocketAnimation.start();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		rocketAnimation.stop();
	}
}
