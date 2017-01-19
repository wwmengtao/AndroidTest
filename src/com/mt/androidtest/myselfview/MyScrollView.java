package com.mt.androidtest.myselfview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.mt.androidtest.R;

public class MyScrollView extends ScrollView{
	private List<View> mViewsNotIntercept=null;//��Ӵ����¼����ػ��������
	private HorizontalScrollView mHorizontalScrollView =null;
	private RectF mRectF = null;
	boolean isInViewRect = false;
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    @Override  
    protected void onAttachedToWindow(){
    	mViewsNotIntercept = new ArrayList<View>();
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.myhorizontalscrollview);
		mViewsNotIntercept.add(mHorizontalScrollView);
    }
	
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent event) {  
    	int curEventType = event.getAction();
    	//��ʶ�¼�����ʼ��Ϣ
        switch (curEventType) {
	        case MotionEvent.ACTION_DOWN:  
	            return shouldInterceptTouchEvent(event);
        }
        return super.onInterceptTouchEvent(event);
    }  
    
	/**
	 * �жϵ���¼��Ƿ�λ��mViewsNotIntercept��������View������
	 */
    public boolean shouldInterceptTouchEvent(MotionEvent event){
    	for(View mView : mViewsNotIntercept){
    		mRectF = calcViewRectangle(mView);
    		if(mRectF.contains(event.getRawX(), event.getRawY()))return false;
    	}
    	return true;
    }
    
    /**
     * ����view�ľ�������
     * @param view
     * @return
     */
    public RectF calcViewRectangle (View view) {
        int[] location = new int[2];
        // ��ȡ�ؼ�����Ļ�е�λ�ã����ص�����ֱ�Ϊ�ؼ��󶥵�� x��y ��ֵ
        view.getLocationOnScreen(location); 
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }
}
