package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.util.AttributeSet;

public class MLDView3 extends MLDViewModel{
    private String text = "MLDView3_";
	public MLDView3(Context context, AttributeSet attrs) {
		super(context, attrs);
		setText(text);
		setColor(context.getResources().getColor(android.R.color.holo_blue_dark));
	}
}
