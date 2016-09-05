package com.mt.androidtest.myselfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public class InheritedView extends LinearLayout implements View.OnTouchListener{
	String TAG_M = "M_T";
	private View deleteButton=null;
	private Context mContext=null;
	private boolean isButtonDel=false;
	private LinearLayout.LayoutParams params=null;
    public InheritedView(Context context) {
        this(context,null);
    	//Log.e(TAG_M, "构造函数1");        
    }

    public InheritedView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.custom_style);
        //Log.e(TAG_M, "构造函数2");        

    }
    
    public InheritedView(Context context, AttributeSet set, int defStyleAttr) {
        super(context, set, defStyleAttr);
        mContext=context;
        setBackgroundColor(context.getResources().getColor(R.color.lawngreen));
        //Log.e(TAG_M, "构造函数3");
        final TypedArray a = context.obtainStyledAttributes(
        		set, R.styleable.custom_attrs, defStyleAttr, R.style.default_style);
        final int [] R_styleable_custom_attrs_custom_color = {
        		R.styleable.custom_attrs_custom_color1,
        		R.styleable.custom_attrs_custom_color2,
        		R.styleable.custom_attrs_custom_color3,
        		R.styleable.custom_attrs_custom_color4,
        		R.styleable.custom_attrs_custom_color5};
        TextView textView;
        int color;
        for(int i=0;i<5;i++){
        	textView = new TextView(context);
        	color = a.getColor(R_styleable_custom_attrs_custom_color[i],0xffff0000);
        	textView.setText("color"+(i+1)+":"+Integer.toHexString(color));
        	addView(textView);
        }
        a.recycle();
        setOnTouchListener(this);
        initDeleteButton();
    }
    
    public void initDeleteButton(){
        deleteButton = LayoutInflater.from(mContext).inflate(R.layout.button_delete, null);  
        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        params.gravity = Gravity.CENTER_HORIZONTAL;
        addView(deleteButton, params);
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean gestureHandled = gestureDetector.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN){//保证后续touch事件继续到来
        	return true;
        }
        return gestureHandled;
    }
    
    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
        	ALog.Log("这是单击事件");
        	if(isButtonDel){
	        	addView(deleteButton, params);
	    		isButtonDel=false;
        	}
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {//双击事件
        	ALog.Log("双击事件");
        	if(!isButtonDel){
        		removeView(deleteButton);  
        		isButtonDel=true;
        	}
        	return super.onDoubleTap(e);
        }

        /**
         * 双击手势过程中发生的事件，包括按下、移动和抬起事件
         * @param e
         * @return
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }
    });
}