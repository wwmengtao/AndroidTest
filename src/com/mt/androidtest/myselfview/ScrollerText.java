package com.mt.androidtest.myselfview;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.view.MotionEvent;

public class ScrollerText extends FrameLayout{
    private int xEventPre = 0;
    private int xEventAfter = 0;
    private int dxEvent = 0;
    
    public ScrollerText(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	
    	switch(event.getAction()){
    	case MotionEvent.ACTION_DOWN:
    		ALog.Log("MotionEvent.ACTION_DOWN");
    		xEventPre = (int)event.getRawX();
            return true;
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
    	ALog.Log("onTouchEvent");
    	switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
        	ALog.Log("onTouchEvent.ACTION_DOWN");
            break;
    	case MotionEvent.ACTION_MOVE:
    		ALog.Log("onTouchEvent.ACTION_MOVE");
    		xEventAfter = (int)event.getRawX();
    		dxEvent = xEventAfter-xEventPre;
    		ALog.Log("dxEvent:"+dxEvent);
    		scrollBy(-dxEvent, 0);
    		xEventPre = xEventAfter;
    		break;
    	case MotionEvent.ACTION_UP:
    		break;
    	}
    	return super.onTouchEvent(event);
    }
    
}
