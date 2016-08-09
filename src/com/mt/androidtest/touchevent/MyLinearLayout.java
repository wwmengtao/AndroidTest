package com.mt.androidtest.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.mt.androidtest.ALog;
import static com.mt.androidtest.touchevent.TouchEventActivity.formatStr;
import static com.mt.androidtest.touchevent.TouchEventActivity.formatStr2;
import static com.mt.androidtest.touchevent.TouchEventActivity.strLogIntercept;
import static com.mt.androidtest.touchevent.TouchEventActivity.strLogTouchEvent;

public class MyLinearLayout extends LinearLayout {  
	private String strLayout = "MyLinearLayout";
    public MyLinearLayout(Context context, AttributeSet attrs) {  
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