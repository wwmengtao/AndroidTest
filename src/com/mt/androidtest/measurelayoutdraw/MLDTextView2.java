package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.util.AttributeSet;

public class MLDTextView2  extends MLDTextViewModel{
	private String desStr = "MLDTextView2_";
	public MLDTextView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayoutDes(desStr);
		setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
	}
}
