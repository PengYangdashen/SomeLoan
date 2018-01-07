package com.fruit.dayloan.adapter;

import java.util.ArrayList;
import java.util.List;

import com.fruit.dayloan.R;
import com.fruit.dayloan.pojo.PersonPojo;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WorkAdapter extends BaseAdapter {

	private ArrayList<PersonPojo> pojo;
	private LayoutInflater mInflater;

	public WorkAdapter(Context context, ArrayList<PersonPojo> pojo) {
		this.pojo = pojo;
		this.mInflater = LayoutInflater.from(context);
	}

	/**
	 */
	@Override
	public int getCount() {
		return pojo.size();
	}

	@Override
	public Object getItem(int arg0) {

		return pojo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int postition, View convertView, ViewGroup arg2) {
		ViewHoler viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHoler();
			convertView = mInflater.inflate(R.layout.adapter_work, null);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.txt_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHoler) convertView.getTag();
		}
		PersonPojo data = pojo.get(postition);
		viewHolder.content.setText(data.getCardusername());

		return convertView;
	}

	static class ViewHoler {
		public TextView content;
	}

}
