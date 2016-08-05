package com.mt.androidtest.event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.mt.androidtest.ALog;
  
public class MyLayout extends LinearLayout {  
	
    public MyLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
      
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent event) {  
        ALog.Log("MyLayout----onInterceptTouchEvent");  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            ALog.Log("MyLayout----DOWN");  
            break;  
        case MotionEvent.ACTION_MOVE:  
            ALog.Log("MyLayout----MOVE");  
            return true;
            //break;              
        case MotionEvent.ACTION_UP:  
            ALog.Log("MyLayout----UP");  
            break;  
        }          
        return super.onInterceptTouchEvent(event);  
    }  
      
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            ALog.Log("MyLayout----DOWN");  
            break;  
        case MotionEvent.ACTION_MOVE:  
            ALog.Log("MyLayout----MOVE");  
            break;              
        case MotionEvent.ACTION_UP:  
            ALog.Log("MyLayout----UP");  
            break;  
        }  
        return super.onTouchEvent(event);
    }  
}  