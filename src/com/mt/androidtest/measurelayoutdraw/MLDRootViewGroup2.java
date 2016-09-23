package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest.ALog;

/**
 * 两个TextView由于布局的原因，导致有的子视图getMeasuredxxx()和getxxx()不同，有的相同。这说明下列内容：
 * getMeasuredxxx()的结果仅仅是控件期望值，供onLayout时候参考使用，控件实际占屏幕大小要看getxxx()结果。
 * @author Mengtao1
 *
 */
public class MLDRootViewGroup2 extends ViewGroup {  
	private View mView, mViewPre, mViewNext;
	private int preViewsHeight=0;
	private int defMeasuredWidthSize = 600;
	private int defMeasuredHeightSize = 600;
	private int defLayoutWidthSize = 300;
	private int defLayoutHeightSize = 300;	
	private int extraSize = 200;
    public MLDRootViewGroup2(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
        	mView = getChildAt(i);
            measureChild(mView, MeasureSpec.makeMeasureSpec(defMeasuredWidthSize, MeasureSpec.EXACTLY), 
            										MeasureSpec.makeMeasureSpec(defMeasuredHeightSize, MeasureSpec.EXACTLY));  
        }
    }  
  
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) {  
    	ALog.Log("MLDRootViewGroup2_onLayout");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
        	if(0==i){
        		mViewPre=getChildAt(i);
        		mViewPre.layout(0, 0, defLayoutWidthSize, defLayoutHeightSize); //第一个View采用默认宽高布局，不考虑getMeasuredxxx结果
        	}else{
        		preViewsHeight += mViewPre.getHeight();
        		mViewNext=getChildAt(i);
        		if(childCount-1==i)//最后一个子控件布局时候，宽度比getMeasuredWidth大extraSize
        			mViewNext.layout(0, preViewsHeight, mViewNext.getMeasuredWidth()+extraSize, preViewsHeight+mViewNext.getMeasuredHeight());
        		else
            		mViewNext.layout(0, preViewsHeight, mViewNext.getMeasuredWidth(), preViewsHeight+mViewNext.getMeasuredHeight());
        		mViewPre = mViewNext;
        	}
        }
    }
    
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
        super.onWindowFocusChanged(hasFocus);  
        ALog.Log("MLDRootViewGroup2_onWindowFocusChanged");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
        	mView = getChildAt(i);
            ALog.Log("mView"+i+".W:"+mView.getWidth()+" MW:"+mView.getMeasuredWidth()+" H:"+mView.getHeight()+" MH:"+mView.getMeasuredHeight());         	
        }
     }  

}  
