package com.mt.androidtest.myselfview;

import com.mt.androidtest.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InheritedView extends LinearLayout implements View.OnClickListener{
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
        initDeleteButton();
    }
    
    public void initDeleteButton(){
    	setOnClickListener(this);
        deleteButton = LayoutInflater.from(mContext).inflate(R.layout.button_delete, null);  
        deleteButton.setOnClickListener(this);  
        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        params.gravity = Gravity.CENTER_HORIZONTAL;
        addView(deleteButton, params);
    }
    @Override  
    public void onClick(View v) {  
    	if(isButtonDel && v instanceof InheritedView){//如果点击的是CustomView
            addView(deleteButton, params);
    		isButtonDel=false;
    	}else if(!(v instanceof InheritedView)){//如果点击的是deleteButton
    		removeView(deleteButton);  
    		isButtonDel=true;
    	}
    }  
}