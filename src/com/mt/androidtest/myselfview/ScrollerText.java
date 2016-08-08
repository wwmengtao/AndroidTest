package com.mt.androidtest.myselfview;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.view.MotionEvent;

public class ScrollerText extends LinearLayout{
    private int xEventPre = 0;
    private int yEventPre = 0;
    private int xEventAfter = 0;
    private int yEventAfter = 0;    
    private int dxEvent = 0;
    private int dyEvent = 0;
    
    public ScrollerText(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	
    	switch(event.getAction()){
    	case MotionEvent.ACTION_DOWN:
    		ALog.Log("MotionEvent.ACTION_DOWN");
    		xEventPre = (int)event.getRawX();
    		yEventPre = (int)event.getRawY();
    		break;
    	case MotionEvent.ACTION_MOVE:
    		ALog.Log("MotionEvent.ACTION_MOVE");
    		xEventAfter = (int)event.getRawX();
    		dxEvent = xEventAfter-xEventPre;
    		if(dxEvent>0){
    			return true;
    		}
    		break;
    	}
    	return super.onInterceptTouchEvent(event);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	switch(event.getAction()){
    	case MotionEvent.ACTION_MOVE:
    		xEventAfter = (int)event.getRawX();
    		yEventAfter = (int)event.getRawY();    	
    		dxEvent = xEventAfter-xEventPre;
    		dyEvent = yEventAfter-yEventPre;
    		ALog.Log("dxEvent:"+dxEvent+" dyEvent:"+dyEvent);
    		scrollBy(dxEvent, dyEvent);
    		xEventPre = xEventAfter;
    		yEventPre = yEventAfter;
    		break;
    	case MotionEvent.ACTION_UP:
    		break;
    	}
    	return super.onTouchEvent(event);
    }
    
}
