package com.mt.androidtest.showview.fragmentdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt.androidtest.BaseFragment;
import com.mt.androidtest.R;

public class DetailInfoFragment extends BaseFragment{
	public static final String ITEM_ID = "item_id";
	InfoContent.Info info;
	
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ITEM_ID)){
			info = InfoContent.ITEM_MAP.get(getArguments()	.getInt(ITEM_ID)); //¢Ù
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_info_detail,	container, false);
		if (info != null){
			((TextView) rootView.findViewById(R.id.book_title)).setText(info.title);
			((TextView) rootView.findViewById(R.id.book_desc)).setText(info.desc);	
		}
		return rootView;
	}
}
