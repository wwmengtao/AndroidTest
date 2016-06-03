package com.mt.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class InflateActivity extends Activity {
	View rootView=null;
	View mView=null;
	LinearLayout mLinearLayout=null;
	LayoutInflater mLayoutInflater=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mLayoutInflater=getLayoutInflater();
	    rootView = mLayoutInflater.inflate(R.layout.activity_inflate, null);
	    mView = mLayoutInflater.inflate(R.layout.view_inflate, (ViewGroup)rootView,false);
	    ((ViewGroup)rootView).addView(mView);
	    setContentView(rootView);
	    setInflateView();
	}
	
	public void setInflateView(){
		mLinearLayout = (LinearLayout)findViewById(R.id.linearlayout_inflater);
		//方法1：此时view_inflate_relativelayout中定义的属性将不起作用
		mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, null);
		mLinearLayout.addView(mView);
		//方法2：此时view_inflate_relativelayout中定义的属性将生效
		//mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout);
		//或者使用下列方式，效果相同。传入mLinearLayout的目的是为了view_inflate_relativelayout中layout_属性能生效
		//mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout,false);
		//mLinearLayout.addView(mView);
	}
}
