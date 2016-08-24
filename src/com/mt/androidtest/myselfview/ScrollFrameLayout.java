package com.mt.androidtest.myselfview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public class ScrollFrameLayout extends FrameLayout{
    private int xEventPre = 0;
    private int xEventAfter = 0;
    private int dxEvent = 0;
    private int widthOfScreen = 0;
    private int widthOfTV = 0;
    private TextView mTextView = null;
    private Scroller mScroller;
    public ScrollFrameLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
		DisplayMetrics dm =context.getResources().getDisplayMetrics();  
		widthOfScreen = dm.widthPixels; //获取手机屏幕的宽度
		mScroller = new Scroller(context);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	mTextView = (TextView)findViewById(R.id.scrolltv);
    	widthOfTV = mTextView.getWidth();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
        	xEventPre = (int)event.getRawX();
            return true;
    	case MotionEvent.ACTION_MOVE:
    		ALog.Log("getScrollX()："+getScrollX());
    		int tvScrollX = getScrollX();
    		xEventAfter = (int)event.getRawX();
    		dxEvent = xEventAfter-xEventPre;
    		if(0>=(tvScrollX-dxEvent) && (tvScrollX-dxEvent)>=(widthOfTV-widthOfScreen)){
        		ALog.Log("dxEvent:"+dxEvent);
        		scrollBy(-dxEvent, 0);
    		}
    		xEventPre = xEventAfter;
    		break;
    	case MotionEvent.ACTION_UP:
    		int dx = 0;
    		if(Math.abs(getScrollX())>=(int)widthOfScreen/2){
    			dx = Math.abs(getScrollX())+widthOfTV-widthOfScreen;
    			mScroller.startScroll(getScrollX(), 0, dx, 0, Math.abs(dx) * 3);
    		}else{
    			dx = getScrollX();
    			mScroller.startScroll(getScrollX(), 0, -dx, 0, Math.abs(dx) * 3);
    		}
            postInvalidate();//会调用draw方法
    		break;
    	}
    	return super.onTouchEvent(event);
    }
    
    /**invalidate会调用draw方法
     * View.draw函数有如下片段：
     * if (!drawingWithRenderNode) {
            computeScroll();
            ...
        }
     */
    
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {//computeScrollOffset：计算Scroller中的数据变化
        	scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        	postInvalidate();
        }
    }
}
