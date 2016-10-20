package com.mt.androidtest.showview.fragmentdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest.BaseFragment;
import com.mt.androidtest.R;

public class ContactsFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contactsLayout = inflater.inflate(R.layout.contacts_fragment,
				container, false);
		return contactsLayout;
	}

}
