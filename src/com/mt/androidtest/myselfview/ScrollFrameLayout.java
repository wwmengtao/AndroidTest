package com.mt.androidtest.myselfview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.mt.androidtest.ALog;

public class ScrollFrameLayout extends FrameLayout{
    private int xEventPre = 0;
    private int xEventAfter = 0;
    private int dxEvent = 0;
    
    public ScrollFrameLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	
    	switch(event.getAction()){
    	case MotionEvent.ACTION_DOWN:
    		ALog.Log("MotionEvent.ACTION_DOWN");
    		xEventPre = (int)event.getRawX();
            //return true;
    		break;
    	case MotionEvent.ACTION_MOVE:
    		ALog.Log("MotionEvent.ACTION_MOVE");
    		xEventAfter = (int)event.getRawX();
    		dxEvent = xEventAfter-xEventPre;
    		if(Math.abs(dxEvent)>0){
    			return true;
    		}
    		break;
    	}
    	return super.onInterceptTouchEvent(event);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
        	ALog.Log("onTouchEvent.ACTION_DOWN");
            return true;
    	case MotionEvent.ACTION_MOVE:
    		ALog.Log("onTouchEvent.ACTION_MOVE");
    		xEventAfter = (int)event.getRawX();
    		dxEvent = xEventAfter-xEventPre;
    		scrollBy(-dxEvent, 0);
    		xEventPre = xEventAfter;
    		break;
    	case MotionEvent.ACTION_UP:
    		ALog.Log("onTouchEvent.ACTION_UP");
    		break;
    	}
    	return super.onTouchEvent(event);
    }
    
}
