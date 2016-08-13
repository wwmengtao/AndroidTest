package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.EventInfo.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.mt.androidtest.R;

/**
 * �����¼��ķַ��ᾭ����ô����˳��dispatchTouchEvent --> onInterceptTouchEvent --> onTouchEvent��
 * �¼����ؾ���onInterceptTouchEvent�����н��У��ڸ÷����з���true���������ش����¼���
 * �����¼��ķַ���һ�����͵�����¼��������ϵ��µĹ��̡�
 * @author Mengtao1
 *
 */
public class TouchEventActivity extends Activity{  

    private static final String strLayout = "0.TouchEventActivity";
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_on_click);  
    }  
    //���ж�ά�����ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
	int [][] dispatchTouchEventArrays = {
			{MotionEvent.ACTION_DOWN,   -1},
			{MotionEvent.ACTION_MOVE,    -1},
			{MotionEvent.ACTION_UP,          -1},
			{MotionEvent.ACTION_CANCEL, -1},
	};
	int [][] onTouchEventArrays = {
			{MotionEvent.ACTION_DOWN,   -1},
			{MotionEvent.ACTION_MOVE,    -1},
			{MotionEvent.ACTION_UP,          -1},
			{MotionEvent.ACTION_CANCEL, -1},
	};	
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	if(!setReturnResult(event, strLayout, dispatchTouchEvent, dispatchTouchEventArrays)){
    		setDefaultReturnResult(super.dispatchTouchEvent(event));
    	}
        return getReturnResult(strLayout, dispatchTouchEvent);
    }
    
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
    	if(!setReturnResult(event, strLayout, onTouchEvent, onTouchEventArrays)){
    		setDefaultReturnResult(super.onTouchEvent(event));
    	}
        return getReturnResult(strLayout, onTouchEvent);
    }
}  