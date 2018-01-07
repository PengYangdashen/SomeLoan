package com.sd.pallentloan.menu;

import com.sd.pallentloan.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ResideMenuItem extends LinearLayout {

	private ImageView residemenu_item_img;
	private TextView residemenu_item_text;
	private int direction = 0;

	public ResideMenuItem(Context context, int direction) {
		super(context);
		initViews(context, direction);
	}

	public ResideMenuItem(Context context, int direction, int icon, int title) {
		super(context);
		initViews(context, direction);
		residemenu_item_img.setImageResource(icon);
		residemenu_item_text.setText(title);
	}

	public ResideMenuItem(Context context, int direction, int icon, String title) {
		super(context);
		initViews(context, direction);
		residemenu_item_img.setImageResource(icon);
		residemenu_item_text.setText(title);
	}

	private void initViews(Context context, int direction) {
		this.direction = direction;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (direction == ResideMenu.DIRECTION_LEFT) {
			inflater.inflate(R.layout.residemenu_item_left, this);
		} else {
			inflater.inflate(R.layout.residemenu_item_right, this);
		}
		residemenu_item_img = (ImageView) findViewById(R.id.residemenu_item_img);
		residemenu_item_text = (TextView) findViewById(R.id.residemenu_item_text);
	}

	public void setIcon(int icon) {
		residemenu_item_img.setImageResource(icon);
	}

	public void setTitle(int title) {
		residemenu_item_text.setText(title);
	}

	public void setTitle(String title) {
		residemenu_item_text.setText(title);
	}

	public int getDirection() {
		return direction;
	}

}
