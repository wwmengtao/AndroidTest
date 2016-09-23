package com.mt.androidtest.myselfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.mt.androidtest.ALog;

public class SelfDrawnView extends View implements OnClickListener {  
	  
    private Paint mPaint;  
    private Rect mBounds;  
    private int mCount;  
    private String text = "S/D click!";
    long mLastTime=0;
    long mCurTime=0;
    public SelfDrawnView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mBounds = new Rect();  
        setOnClickListener(this);  
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
    public void onClick(View v) {  
    	mLastTime=mCurTime;
        mCurTime= System.currentTimeMillis();
        if(mCurTime-mLastTime<300){//˫���¼�
        	mCurTime =0;
            mLastTime = 0;
            removeCallbacks(mSingleClickRunnable);
            post(mDoubleClickRunnable);
        }else{//�����¼�
        	postDelayed(mSingleClickRunnable,310);
        }
        invalidate();	
    }  
  
    /**
     * �Զ���View�о�����Ҫʹ��Handler��ʹ��post����
     */
    private Runnable mSingleClickRunnable = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
        	ALog.Log("���ǵ����¼�");
            mCount++;  
            text = String.valueOf(mCount);
		}
    };
    
    private Runnable mDoubleClickRunnable = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
        	ALog.Log("����˫���¼�");
        	text = "doubleClick";
		}
    };
}  
