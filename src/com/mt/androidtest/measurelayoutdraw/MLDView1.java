package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.util.AttributeSet;

public class MLDView1 extends MLDViewModel{
    private String text = "MLDView1";
	public MLDView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		setText(text);
		setColor(context.getResources().getColor(android.R.color.holo_green_dark));
	}
}
