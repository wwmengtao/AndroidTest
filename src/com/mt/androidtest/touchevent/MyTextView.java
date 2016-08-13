package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.EventInfo.*;
import static com.mt.androidtest.touchevent.EventHandleInfo.*;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mt.androidtest.ALog;

/**
 * TextView.isClickable()Ϊfalse������setOnClickListener�����setOnTouchListener�������ӦonTouch����
 * @author Mengtao1
 *
 */
public class MyTextView extends TextView implements View.OnClickListener, View.OnTouchListener{

	private String strLayout = "3.MyTextView";
    private int [][]dispatchTouchEventArrays = null;
    private int [][]onTouchEventArrays = null;
    private int [][]onTouchArrays = null;
    
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		ALog.Log("1_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
		//setOnTouchListener(this);//ע��OnTouchListener������ӦonTouch����
		//setOnClickListener(this);//ע��OnClickListener������ӦonClick����	
		ALog.Log("2_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
		//
        dispatchTouchEventArrays =      EventHandleInfoArrays_MyTextView.dispatchTouchEventArrays;
        onTouchArrays =                         EventHandleInfoArrays_MyTextView.onTouchArrays;
        onTouchEventArrays =                EventHandleInfoArrays_MyTextView.onTouchEventArrays;		
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
