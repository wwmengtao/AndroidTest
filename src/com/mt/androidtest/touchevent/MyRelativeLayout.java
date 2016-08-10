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
     * onInterceptTouchEvent���������true����Ӧ���¼��Ͳ�������View�����ˣ���ȻViewGroup��Ĭ�ϴ����Ƿ���false����������View����
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
    * onTouchEvent������ִ��/�����¼�������true���¼������ѡ������ˣ��Ӷ��������´��ˡ�
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
