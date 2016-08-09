package com.mt.androidtest.touchevent;

import com.mt.androidtest.ALog;
import static com.mt.androidtest.touchevent.TouchLogInfo.formatStr;
import static com.mt.androidtest.touchevent.TouchLogInfo.formatStr2;
import static com.mt.androidtest.touchevent.TouchLogInfo.strLogIntercept;
import static com.mt.androidtest.touchevent.TouchLogInfo.strLogTouchEvent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout{
	private String strLayout = "MyFrameLayout";
    public MyFrameLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
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
}
