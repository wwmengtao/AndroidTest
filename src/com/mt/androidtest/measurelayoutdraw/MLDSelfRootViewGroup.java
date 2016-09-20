package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest.ALog;

public class MLDSelfRootViewGroup extends ViewGroup{
	
    public MLDSelfRootViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec); 
    	ALog.Log("MLDSelfRootViewGroup_onMeasure");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
        	MLDTextViewModel childView = (MLDTextViewModel)getChildAt(i);
            childView.setDefaultWidthAndHeight(true);//针对“Android View绘制流程说明”中图4最后一行的问题给每个子视图设置自定义宽高
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }    	
    }
    
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		ALog.Log("MLDSelfRootViewGroup_onLayout");
		//如果控件MLDSelfRootViewGroup的大小、位置没有发生变化，则不会执行 if (changed)内部代码，从而不会执行子控件的layout函数，
		//也就不会执行mPrivateFlags &= ~PFLAG_FORCE_LAYOUT清除PFLAG_FORCE_LAYOUT标记。如果此时某子控件执行了requestLayout，
		//那么有可能引发其他子控件调用onMeasure函数，因为其他子控件之前有可能因为某种原因调用过requestLayout。
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // 为每一个子控件在垂直方向上对齐布局
                childView.layout(0, i*childView.getMeasuredHeight(), childView.getMeasuredWidth(), (i+1)*childView.getMeasuredHeight());
            }
        }
	}
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
		ALog.Log("MLDSelfRootViewGroup_onDraw");
    }
}
