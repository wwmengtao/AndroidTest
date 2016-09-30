package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.util.AttributeSet;

public class MLDView2 extends MLDViewModel{
    private String text = "MLDView2_";
	public MLDView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		setText(text);
		setColor(context.getResources().getColor(android.R.color.holo_purple));
	}
}
