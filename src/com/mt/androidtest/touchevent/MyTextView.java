package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.EventInfo.*;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mt.androidtest.ALog;

/**
 * TextView.isClickable()为false，除非setOnClickListener。如果setOnTouchListener则可以响应onTouch函数
 * @author Mengtao1
 *
 */
public class MyTextView extends TextView implements View.OnClickListener, View.OnTouchListener{

	private String strLayout = "3.MyTextView";
    private SparseArray<Integer>dispatchTouchEventArrays = null;
    private SparseArray<Integer>onTouchEventArrays = null;
    private SparseArray<Integer>onTouchArrays = null;
    private MyTextViewERA mEM = null;
    
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		ALog.Log("1_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
		setOnTouchListener(this);//注册OnTouchListener可以响应onTouch函数
		//setOnClickListener(this);//注册OnClickListener可以响应onClick函数	
		ALog.Log("2_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
		//
		mEM = new MyTextViewERA();
        dispatchTouchEventArrays = mEM.getDispatchTouchEventArrays();
        onTouchArrays                    = mEM.getOnTouchArrays();
        onTouchEventArrays           = mEM.getOnTouchEventArrays();	
	}

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	if(!setReturnResult(event, strLayout, dispatchTouchEvent, dispatchTouchEventArrays)){
    		setDefaultReturnResult(super.dispatchTouchEvent(event));
    	}
        return getReturnResult(strLayout, dispatchTouchEvent);
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
