package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.EventInfo.*;
import static com.mt.androidtest.touchevent.EventReturn.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;


/**
 * 触摸事件的分发会经过这么几个顺序，dispatchTouchEvent --> onInterceptTouchEvent --> onTouchEvent，
 * 事件拦截就在onInterceptTouchEvent方法中进行，在该方法中返回true即代表拦截触摸事件。
 * 触摸事件的分发是一个典型的隧道事件，即从上到下的过程。
 * @author Mengtao1
 *
 */
public class TouchEventActivity extends Activity implements View.OnClickListener, View.OnTouchListener{  

    private static final String strLayout = "0.TouchEventActivity";
    private SparseArray<Integer> dispatchTouchEventArrays = null;
    private SparseArray<Integer> onTouchEventArrays = null;
    private SparseArray<Integer> onTouchArrays = null;
    private TouchEventActivityERA mET = null;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_touch_event);
        //
        getWindow().getDecorView().setOnTouchListener(this);
        //getWindow().getDecorView().setOnClickListener(this);
        //为各个View的事件处理结果赋新值
        mET =  new TouchEventActivityERA();
        //为TouchEventActivity的各个数组赋值
        dispatchTouchEventArrays = mET.getDispatchTouchEventArrays();
        onTouchArrays                    = mET.getOnTouchArrays();
        onTouchEventArrays           = mET.getOnTouchEventArrays();
    }
    
    /**
     * dispatchTouchEvent：此函数为TouchEventActivity的对应处理函数
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	if(!setReturnResult(event, strLayout, dispatchTouchEvent, dispatchTouchEventArrays)){
    		setDefaultReturnResult(super.dispatchTouchEvent(event));
    	}
        return getReturnResult(strLayout, dispatchTouchEvent);
    }
    
    /**
     * onTouch：此函数为DecorView的对应处理函数
     */
    @Override  
    public boolean onTouch(View v, MotionEvent event) {
    	//ALog.fillInStackTrace("DecorView_onTouch");
    	String mStrLayout = "0.DecorView";
    	if(!setReturnResult(event, mStrLayout, onTouch, onTouchArrays)){
    		setDefaultReturnResult(false);
    	}
        return getReturnResult(mStrLayout, onTouch);
    }    
    
    /**
     * onTouchEvent：此函数为TouchEventActivity的对应处理函数
     */
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
    	//ALog.fillInStackTrace("TouchEventActivity_onTouchEvent");
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