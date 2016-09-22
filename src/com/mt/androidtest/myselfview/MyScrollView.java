package com.mt.androidtest.myselfview;

import android.content.Context;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.mt.androidtest.R;

public class MyScrollView extends ScrollView{
	private ViewPager mViewPager =null;
	private RectF rectViewPager = null;
	boolean isInViewRect = false;
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    @Override  
    protected void onAttachedToWindow(){
		mViewPager = (ViewPager) findViewById(R.id.myviewpager);
    }
	
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent event) {  
    	int curEventType = event.getAction();
    	//标识事件处理开始信息
        switch (curEventType) {
	        case MotionEvent.ACTION_DOWN:  
	        	//以下判断点击事件是否位于ViewPager所在区域
	    		rectViewPager = calcViewRectangle(mViewPager);
	        	isInViewRect = rectViewPager.contains(event.getRawX(), event.getRawY());
	            return isInViewRect? false : true;
        }
        return super.onInterceptTouchEvent(event);
    }  
    
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
    	int curEventType = event.getAction();
    	//标识事件处理开始信息
        switch (curEventType) {
	        case MotionEvent.ACTION_DOWN:
	        	break;
        }
        return super.onTouchEvent(event);
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
