package com.mt.androidtest.measurelayoutdraw;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mt.androidtest.ALog;

import static com.mt.androidtest.measurelayoutdraw.MLDRootViewGroup2.*;
import static com.mt.androidtest.measurelayoutdraw.MeasureLayoutDrawActivity.layoutDes;

public class MLDViewModel extends View implements View.OnTouchListener{
	private Context mContext;
    private Paint mPaint;  
    private Rect mBounds;  	
    private String desStr = "MLDViewModel";
    private String desStrTemp=null;
    private int color = -1;
    private float textWidth;  
    private float textHeight;     
    //
    private boolean singleClickAgain = false;
    private boolean longPressAgain = false;
    long mLastTime=0;
    long mCurTime=0;
	public MLDViewModel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setOnTouchListener(this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mBounds = new Rect();		
	}
	
	protected void setText(String str){
		desStr = str;
	}
	
	protected void setColor(int cl){
		color = cl;
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
        mPaint.setColor(color);  
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);  
        mPaint.setTextSize(50);
        desStrTemp=desStr.replace("_", "");
        mPaint.getTextBounds(desStrTemp, 0, desStrTemp.length(), mBounds);  
        textWidth = mBounds.width();  
        textHeight = mBounds.height();  
        mPaint.setColor(Color.BLACK);  
        canvas.drawText(desStrTemp, getWidth() / 2 - textWidth / 2, getHeight() / 2  
                + textHeight / 2, mPaint);  
    }

    /**
     * 单击实现整体垂直平移，不调用子视图的onDraw
     */
	public void singleClick(){
        if(desStr.contains("MLDView1")){
            ALog.Log("MLDView1_singleClick");
            if(!singleClickAgain){
                setOffsetTop(20);
                singleClickAgain = true;
            }
            else{
                setOffsetTop(0);
                singleClickAgain = false;
            }
            requestLayout();
        }
	}
	
	/**
	 * 长按实现MLDView1的位置、大小重新布局，仅仅调用MLDView1的onDraw
	 */
	public void longPress(){
        if(desStr.contains("MLDView1")){
            ALog.Log("MLDView1_longPress");
            if(!longPressAgain){
                setOffsetRight(20);
                longPressAgain = true;
            }
            else{
                setOffsetRight(0);
                longPressAgain = false;
            }
            requestLayout();
        }
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
        	singleClick();
            return super.onSingleTapConfirmed(e);
        }
        
        @Override
        public void onLongPress(MotionEvent e) {//长按事件
        	longPress();
        }
    });
}
