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
		//����1����ʱview_inflate_relativelayout�ж�������Խ���������
		mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, null);
		mLinearLayout.addView(mView);
		//����2����ʱview_inflate_relativelayout�ж�������Խ���Ч
		//mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout);
		//����ʹ�����з�ʽ��Ч����ͬ������mLinearLayout��Ŀ����Ϊ��view_inflate_relativelayout��layout_��������Ч
		//mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout,false);
		//mLinearLayout.addView(mView);
	}
}
