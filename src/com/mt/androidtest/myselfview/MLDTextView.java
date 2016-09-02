package com.mt.androidtest.myselfview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import android.view.*;

import com.mt.androidtest.ALog;

public class MLDTextView  extends TextView implements View.OnClickListener{
	private boolean isClickChanged = true;
	private String desStr = "MLDTextView";
	public MLDTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setText(desStr);
		setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
		setOnClickListener(this);
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ALog.Log("MLDTextView_onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	super.onLayout(changed, l, t, r, b);
    	ALog.Log("MLDTextView_onLayout");
    }	
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        ALog.Log("MLDTextView_onDraw");
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(isClickChanged){
			setText(desStr+"\nchangeHeight");
			isClickChanged=false;
		}else{
			setText(desStr);
			isClickChanged=true;
		}
	}      
}
