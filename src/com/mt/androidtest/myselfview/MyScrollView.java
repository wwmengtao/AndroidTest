package com.mt.androidtest.myselfview;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView{

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ALog.Log("MyScrollView");
	}

}
