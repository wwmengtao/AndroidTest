package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.TouchEventActivity.formatStr;
import static com.mt.androidtest.touchevent.TouchEventActivity.formatStr2;
import static com.mt.androidtest.touchevent.TouchEventActivity.strDispatch;
import static com.mt.androidtest.touchevent.TouchEventActivity.strLogTouch;
import static com.mt.androidtest.touchevent.TouchEventActivity.strLogTouchEvent;
import com.mt.androidtest.ALog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MyButton extends Button implements View.OnClickListener, View.OnTouchListener{
	private String strLayout = "3.MyButton";
	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		ALog.Log("1_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
		//setOnClickListener(this);//注册OnClickListener可以响应onClick函数
		//setOnTouchListener(this);//注册OnTouchListener可以响应onTouch函数
		ALog.Log("2_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
	}

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strDispatch)+"ACTION_DOWN"); 
        	//return true;
            break;  
        case MotionEvent.ACTION_MOVE:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strDispatch)+"ACTION_MOVE"); 
            break;              
        case MotionEvent.ACTION_UP:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strDispatch)+"ACTION_UP"); 
            break;  
        }
        super.dispatchTouchEvent(event);
        return false;
    }
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strLogTouch)+"ACTION_DOWN"); 
        	//return true;
            break;  
        case MotionEvent.ACTION_MOVE:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strLogTouch)+"ACTION_MOVE"); 
            break;              
        case MotionEvent.ACTION_UP:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strLogTouch)+"ACTION_UP"); 
            break;  
        }
		return false;
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
        //return super.onTouchEvent(event);
        return true;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	ALog.Log(String.format(formatStr, strLayout)+String.format(formatStr2,"onClick")); 
	}  
}
