package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.util.AttributeSet;

public class MLDTextView3  extends MLDTextViewModel{
	private String desStr = "MLDTextView3_";
	public MLDTextView3(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayoutDes(desStr);
		setBackgroundColor(context.getResources().getColor(android.R.color.holo_purple));
	}
}
