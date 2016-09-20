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
            childView.setDefaultWidthAndHeight(true);//��ԡ�Android View��������˵������ͼ4���һ�е������ÿ������ͼ�����Զ�����
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }    	
    }
    
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		ALog.Log("MLDSelfRootViewGroup_onLayout");
		//����ؼ�MLDSelfRootViewGroup�Ĵ�С��λ��û�з����仯���򲻻�ִ�� if (changed)�ڲ����룬�Ӷ�����ִ���ӿؼ���layout������
		//Ҳ�Ͳ���ִ��mPrivateFlags &= ~PFLAG_FORCE_LAYOUT���PFLAG_FORCE_LAYOUT��ǡ������ʱĳ�ӿؼ�ִ����requestLayout��
		//��ô�п������������ӿؼ�����onMeasure��������Ϊ�����ӿؼ�֮ǰ�п�����Ϊĳ��ԭ����ù�requestLayout��
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // Ϊÿһ���ӿؼ��ڴ�ֱ�����϶��벼��
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
