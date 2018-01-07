package com.example.bookmarket;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MarketFragment extends Fragment {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_market, container, false);
			init();
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	private void init() {
		
	}

}
