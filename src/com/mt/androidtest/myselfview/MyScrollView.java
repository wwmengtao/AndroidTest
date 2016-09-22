package com.mt.androidtest.myselfview;

import android.content.Context;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.mt.androidtest.R;

public class MyScrollView extends ScrollView{
	private ViewPager mViewPager =null;
	private RectF rectViewPager = null;
	boolean isInViewRect = false;
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    @Override  
    protected void onAttachedToWindow(){
		mViewPager = (ViewPager) findViewById(R.id.myviewpager);
    }
	
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent event) {  
    	int curEventType = event.getAction();
    	//��ʶ�¼�����ʼ��Ϣ
        switch (curEventType) {
	        case MotionEvent.ACTION_DOWN:  
	        	//�����жϵ���¼��Ƿ�λ��ViewPager��������
	    		rectViewPager = calcViewRectangle(mViewPager);
	        	isInViewRect = rectViewPager.contains(event.getRawX(), event.getRawY());
	            return isInViewRect? false : true;
        }
        return super.onInterceptTouchEvent(event);
    }  
    
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
    	int curEventType = event.getAction();
    	//��ʶ�¼�����ʼ��Ϣ
        switch (curEventType) {
	        case MotionEvent.ACTION_DOWN:
	        	break;
        }
        return super.onTouchEvent(event);
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
