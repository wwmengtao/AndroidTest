package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.util.AttributeSet;

public class MLDTextView1  extends MLDTextViewModel{
	private String desStr = "MLDTextView1_";
	public MLDTextView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayoutDes(desStr);
		setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
	}
}
