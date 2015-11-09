package com.duang.easyecard.Activity;

import com.duang.easyecard.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FourthFragment extends Fragment {

	private View viewFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewFragment=inflater.inflate(R.layout.fourth, null);
		return viewFragment;
	}
}
