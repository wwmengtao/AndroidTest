package com.mt.androidtest.myselfview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.mt.androidtest.R;

public class MyScrollView extends ScrollView{
	private List<View> mViewsNotIntercept=null;//添加触摸事件拦截豁免白名单
	private HorizontalScrollView mHorizontalScrollView =null;
	private RectF mRectF = null;
	boolean isInViewRect = false;
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    @Override  
    protected void onAttachedToWindow(){
    	mViewsNotIntercept = new ArrayList<View>();
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.myhorizontalscrollview);
		mViewsNotIntercept.add(mHorizontalScrollView);
    }
	
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent event) {  
    	int curEventType = event.getAction();
    	//标识事件处理开始信息
        switch (curEventType) {
	        case MotionEvent.ACTION_DOWN:  
	            return shouldInterceptTouchEvent(event);
        }
        return super.onInterceptTouchEvent(event);
    }  
    
	/**
	 * 判断点击事件是否位于mViewsNotIntercept包含的子View区域内
	 */
    public boolean shouldInterceptTouchEvent(MotionEvent event){
    	for(View mView : mViewsNotIntercept){
    		mRectF = calcViewRectangle(mView);
    		if(mRectF.contains(event.getRawX(), event.getRawY()))return false;
    	}
    	return true;
    }
    
    /**
     * 计算view的矩形区域
     * @param view
     * @return
     */
    public RectF calcViewRectangle (View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location); 
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }
}
