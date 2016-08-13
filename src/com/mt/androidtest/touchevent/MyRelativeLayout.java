package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.EventInfo.*;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.mt.androidtest.ALog;

public class MyRelativeLayout extends RelativeLayout implements View.OnClickListener, View.OnTouchListener{

	private String strLayout = "2.MyRelativeLayout";
    private int [][]dispatchTouchEventArrays = null;
    private int [][]onInterceptTouchEventArrays = null;
    private int [][]onTouchEventArrays = null;
    private int [][]onTouchArrays = null;
    
    public MyRelativeLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
		ALog.Log("1_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
		//setOnTouchListener(this);//注册OnTouchListener可以响应onTouch函数
		//setOnClickListener(this);//注册OnClickListener可以响应onClick函数	
		ALog.Log("2_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
		//
        dispatchTouchEventArrays =      MyRelativeLayout_handleTouchEventArrays.dispatchTouchEventArrays;
        onInterceptTouchEventArrays = MyRelativeLayout_handleTouchEventArrays.onInterceptTouchEventArrays;
        onTouchArrays =                         MyRelativeLayout_handleTouchEventArrays.onTouchArrays;
        onTouchEventArrays =                MyRelativeLayout_handleTouchEventArrays.onTouchEventArrays;
    }  
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	if(!setReturnResult(event, strLayout, dispatchTouchEvent, dispatchTouchEventArrays)){
    		setDefaultReturnResult(super.dispatchTouchEvent(event));
    	}
        return getReturnResult(strLayout, dispatchTouchEvent);
    }

    @Override  
    public boolean onInterceptTouchEvent(MotionEvent event) {  
    	if(!setReturnResult(event, strLayout, onInterceptTouchEvent, onInterceptTouchEventArrays)){
    		setDefaultReturnResult(super.onInterceptTouchEvent(event));
    	}
        return getReturnResult(strLayout, onInterceptTouchEvent);
    }  

    @Override  
    public boolean onTouch(View v, MotionEvent event) {
    	if(!setReturnResult(event, strLayout, onTouch, onTouchArrays)){
    		setDefaultReturnResult(false);
    	}
        return getReturnResult(strLayout, onTouch);
    }

    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
    	if(!setReturnResult(event, strLayout, onTouchEvent, onTouchEventArrays)){
    		setDefaultReturnResult(super.onTouchEvent(event));
    	}
        return getReturnResult(strLayout, onTouchEvent);
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ACTION_LOG(strLayout, onClick, DES_ACTION_ONCLICK);
	}
}
