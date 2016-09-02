package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import android.view.*;

import static com.mt.androidtest.measurelayoutdraw.MeasureLayoutDrawActivity.*;
import com.mt.androidtest.ALog;

public class MLDTextViewModel  extends TextView implements View.OnClickListener{
	private boolean isClickChanged = true;
	private String desStr = null;
	public MLDTextViewModel(Context context, AttributeSet attrs) {
		super(context, attrs);
		setGravity(Gravity.CENTER);
		setOnClickListener(this);
	}
	
	protected void setLayoutDes(String newDesStr){
		desStr = newDesStr;
		setText(desStr+layoutDes);
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ALog.Log(desStr+"onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	super.onLayout(changed, l, t, r, b);
    	ALog.Log(desStr+"onLayout");
    }	
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        ALog.Log(desStr+"onDraw");
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(isClickChanged){
			setText(desStr+layoutDes+"\nchangeHeight");
			isClickChanged=false;
		}else{
			setText(desStr+layoutDes);
			isClickChanged=true;
		}
	}      
}
