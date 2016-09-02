package com.mt.androidtest.myselfview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mt.androidtest.ALog;
/**
 * 一、根布局为LineaLayout时
 * 1)onMeasure执行了两次，原因是ViewRootImpl.performTraversals内部两次measureHierarchy都被调用了
 * 2)onLayout和onDraw各执行了一次
 * 二、根布局为RelativeLayout时
 * 1)onMeasure执行了四次，原因是ViewRootImpl.performTraversals内部measureHierarchy两次调用，并且每次
 * 执行measureHierarchy时，onMeasure都执行两次
 * 2)onLayout和onDraw各执行了一次
 * @author Mengtao1
 *
 */
public class MLDTextView  extends TextView{

	public MLDTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setText("MLDTextView");
		setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
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
}
