package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.EventInfo.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.mt.androidtest.R;

/**
 * 触摸事件的分发会经过这么几个顺序，dispatchTouchEvent --> onInterceptTouchEvent --> onTouchEvent，
 * 事件拦截就在onInterceptTouchEvent方法中进行，在该方法中返回true即代表拦截触摸事件。
 * 触摸事件的分发是一个典型的隧道事件，即从上到下的过程。
 * @author Mengtao1
 *
 */
public class TouchEventActivity extends Activity{  

    private static final String strLayout = "0.TouchEventActivity";
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_on_click);  
    }  
    //下列二维数组标识了相应方法中事件的处理结果，0代表返回false，1代表返回true，其他数值采用默认值。
	int [][] dispatchTouchEventArrays = {
			{MotionEvent.ACTION_DOWN,   -1},
			{MotionEvent.ACTION_MOVE,    -1},
			{MotionEvent.ACTION_UP,          -1},
			{MotionEvent.ACTION_CANCEL, -1},
	};
	int [][] onTouchEventArrays = {
			{MotionEvent.ACTION_DOWN,   -1},
			{MotionEvent.ACTION_MOVE,    -1},
			{MotionEvent.ACTION_UP,          -1},
			{MotionEvent.ACTION_CANCEL, -1},
	};	
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	if(!setReturnResult(event, strLayout, dispatchTouchEvent, dispatchTouchEventArrays)){
    		setDefaultReturnResult(super.dispatchTouchEvent(event));
    	}
        return getReturnResult(strLayout, dispatchTouchEvent);
    }
    
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
    	if(!setReturnResult(event, strLayout, onTouchEvent, onTouchEventArrays)){
    		setDefaultReturnResult(super.onTouchEvent(event));
    	}
        return getReturnResult(strLayout, onTouchEvent);
    }
}  