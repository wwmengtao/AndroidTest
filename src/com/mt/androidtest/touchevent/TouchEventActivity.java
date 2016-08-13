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
    private int [][]dispatchTouchEventArrays = null;
    private int [][]onTouchEventArrays = null;
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_on_click);  
        dispatchTouchEventArrays = TouchEventActivity_handleTouchEventArrays.dispatchTouchEventArrays;
        onTouchEventArrays = TouchEventActivity_handleTouchEventArrays.onTouchEventArrays;
    }  

    
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