package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.TouchEventActivity.formatStr;
import static com.mt.androidtest.touchevent.TouchEventActivity.formatStr2;
import static com.mt.androidtest.touchevent.TouchEventActivity.strDispatch;
import static com.mt.androidtest.touchevent.TouchEventActivity.strLogIntercept;
import static com.mt.androidtest.touchevent.TouchEventActivity.strLogTouch;
import static com.mt.androidtest.touchevent.TouchEventActivity.strLogTouchEvent;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.mt.androidtest.ALog;

public class MyRelativeLayout extends RelativeLayout implements View.OnClickListener, View.OnTouchListener{
	private String strLayout = "2.MyRelativeLayout";
	
    public MyRelativeLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
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
        return super.dispatchTouchEvent(event);
        //return true;
    }
    
    /**
     * onInterceptTouchEvent：如果返回true，相应的事件就不会往子View传递了，当然ViewGroup的默认处理是返回false，即交给子View处理。
     */
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent event) {  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strLogIntercept)+"ACTION_DOWN");  
            //return true;
            break;  
        case MotionEvent.ACTION_MOVE:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strLogIntercept)+"ACTION_MOVE");  
            break;              
        case MotionEvent.ACTION_UP:  
        	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strLogIntercept)+"ACTION_UP");  
            break;  
        }          
        return super.onInterceptTouchEvent(event);  
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
    
   /**
    * onTouchEvent：代表执行/消费事件，返回true则事件被消费、处理了，从而不再往下传了。
    */
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ALog.Log(String.format(formatStr, strLayout)+String.format(formatStr2,"onClick")); 
	}  
}
