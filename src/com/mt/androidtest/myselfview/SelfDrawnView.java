package com.mt.androidtest.myselfview;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

public class SelfDrawnView extends View implements OnClickListener,Handler.Callback {  
	  
    private Paint mPaint;  
    private Rect mBounds;  
    private int mCount;  
    private String text = "S/D click!";
    long mLastTime=0;
    long mCurTime=0;
    private Handler handler = null;
    public SelfDrawnView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mBounds = new Rect();  
        setOnClickListener(this);  
        handler = new Handler(this);
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        mPaint.setColor(Color.BLUE);  
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);  
        mPaint.setColor(Color.YELLOW);  
        mPaint.setTextSize(50);
        mPaint.getTextBounds(text, 0, text.length(), mBounds);  
        float textWidth = mBounds.width();  
        float textHeight = mBounds.height();  
        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2  
                + textHeight / 2, mPaint);  
    }  
  
	@Override
	public void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		handler.removeCallbacksAndMessages(null);
	}
    
    @Override  
    public void onClick(View v) {  
    	mLastTime=mCurTime;
        mCurTime= System.currentTimeMillis();
        if(mCurTime-mLastTime<300){//双击事件
        	mCurTime =0;
            mLastTime = 0;
            handler.removeMessages(1);
            handler.sendEmptyMessage(2);
        }else{//单击事件
        	handler.sendEmptyMessageDelayed(1, 310);
        }
    }  
  
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
        switch (msg.what) {
            case 1:
            	ALog.Log("这是单击事件");
                mCount++;  
                text = String.valueOf(mCount);
                break;
            case 2:
            	ALog.Log("这是双击事件");
            	text = "doubleClick";
                break;
        }
        invalidate();		
		return false;
	}
}  
