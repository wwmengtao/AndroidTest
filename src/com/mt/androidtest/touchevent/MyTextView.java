package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.TouchEventActivity.formatStr;
import static com.mt.androidtest.touchevent.TouchEventActivity.formatStr2;
import static com.mt.androidtest.touchevent.TouchEventActivity.strLogTouchEvent;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.mt.androidtest.ALog;

public class MyTextView extends TextView{
	private String strLayout = "MyTextView";
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strLogTouchEvent)+"ACTION_DOWN"); 
        	//return true;
            break;  
        case MotionEvent.ACTION_MOVE:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strLogTouchEvent)+"ACTION_MOVE"); 
            break;              
        case MotionEvent.ACTION_UP:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strLogTouchEvent)+"ACTION_UP"); 
            break;  
        }  
        return super.onTouchEvent(event);
    }  
}
