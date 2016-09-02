package com.mt.androidtest.myselfview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mt.androidtest.ALog;

public class MLDTextView2  extends TextView{

	public MLDTextView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		setText("MLDTextView2");
		setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ALog.Log("MLDTextView2_onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	super.onLayout(changed, l, t, r, b);
    	ALog.Log("MLDTextView2_onLayout");
    }	
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        ALog.Log("MLDTextView2_onDraw");
    }
}
