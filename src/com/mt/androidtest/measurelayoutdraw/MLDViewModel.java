package com.mt.androidtest.measurelayoutdraw;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class MLDViewModel extends View{
    private Paint mPaint;  
    private Rect mBounds;  	
    private String text = "MLDViewModel";
    private int color = -1;
    private float textWidth;  
    private float textHeight;     
	public MLDViewModel(Context context, AttributeSet attrs) {
		super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mBounds = new Rect();		
	}
	
	protected void setText(String str){
		text = str;
	}
	
	protected void setColor(int cl){
		color = cl;
	}
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        mPaint.setColor(color);  
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);  
        mPaint.setTextSize(50);
        mPaint.getTextBounds(text, 0, text.length(), mBounds);  
        textWidth = mBounds.width();  
        textHeight = mBounds.height();  
        mPaint.setColor(Color.BLACK);  
        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2  
                + textHeight / 2, mPaint);  
    }
}
