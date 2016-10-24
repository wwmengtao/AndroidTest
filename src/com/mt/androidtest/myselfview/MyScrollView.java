package com.mt.androidtest.myselfview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mt.androidtest.R;

public class MyScrollView extends ScrollView{
	private List<View> mViews=null;
    private TextView mTextView;
	private ViewPager mViewPager =null;
	private RectF mRectF = null;
	boolean isInViewRect = false;
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    @Override  
    protected void onAttachedToWindow(){
    	mViews = new ArrayList<View>();
		mTextView = (TextView)findViewById(R.id.mytextview);
		mViewPager = (ViewPager) findViewById(R.id.myviewpager);
		mViews.add(mTextView);
		mViews.add(mViewPager);
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
	 * �жϵ���¼��Ƿ�λ��mViews��������View������
	 */
    public boolean shouldInterceptTouchEvent(MotionEvent event){
    	for(View mView:mViews){
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
