package com.mt.androidtest.showview.fragmentdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest.BaseFragment;
import com.mt.androidtest.R;

public class MessageFragment extends BaseFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.message_fragment,
				container, false);
		return messageLayout;
	}

}
