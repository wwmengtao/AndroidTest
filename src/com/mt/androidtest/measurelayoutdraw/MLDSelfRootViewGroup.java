package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
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
            childView.setDefaultWidthAndHeight(true);
            // ΪScrollerLayout�е�ÿһ���ӿؼ�������С
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }    	
    }
    
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // Ϊÿһ���ӿؼ��ڴ�ֱ�����϶��벼�ֲ���
                childView.layout(0, i*childView.getMeasuredHeight(), childView.getMeasuredWidth(), (i+1)*childView.getMeasuredHeight());
            }
        }
	}
}
