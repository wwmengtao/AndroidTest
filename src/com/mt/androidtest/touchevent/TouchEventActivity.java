package com.mt.androidtest.touchevent;

import static com.mt.androidtest.touchevent.EventInfo.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.mt.androidtest.R;


/**
 * �����¼��ķַ��ᾭ����ô����˳��dispatchTouchEvent --> onInterceptTouchEvent --> onTouchEvent��
 * �¼����ؾ���onInterceptTouchEvent�����н��У��ڸ÷����з���true���������ش����¼���
 * �����¼��ķַ���һ�����͵�����¼��������ϵ��µĹ��̡�
 * @author Mengtao1
 *
 */
public class TouchEventActivity extends Activity implements View.OnClickListener, View.OnTouchListener{  

    private static final String strLayout = "0.TouchEventActivity";
    private SparseArray<Integer> dispatchTouchEventArrays = null;
    private SparseArray<Integer> onTouchEventArrays = null;
    private SparseArray<Integer> onTouchArrays = null;
    private TouchEventActivityERA mET = null;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_on_click);
        //
        getWindow().getDecorView().setOnTouchListener(this);
        //getWindow().getDecorView().setOnClickListener(this);
        //Ϊ����View���¼�����������ֵ
        mET =  new TouchEventActivityERA();
        //ΪTouchEventActivity�ĸ������鸳ֵ
        dispatchTouchEventArrays = mET.getDispatchTouchEventArrays();
        onTouchArrays                    = mET.getOnTouchArrays();
        onTouchEventArrays           = mET.getOnTouchEventArrays();
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	if(!setReturnResult(event, strLayout, dispatchTouchEvent, dispatchTouchEventArrays)){
    		setDefaultReturnResult(super.dispatchTouchEvent(event));
    	}
        return getReturnResult(strLayout, dispatchTouchEvent);
    }
    
    @Override  
    public boolean onTouch(View v, MotionEvent event) {
    	if(!setReturnResult(event, strLayout, onTouch, onTouchArrays)){
    		setDefaultReturnResult(false);
    	}
        return getReturnResult(strLayout, onTouch);
    }    
    
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
    	if(!setReturnResult(event, strLayout, onTouchEvent, onTouchEventArrays)){
    		setDefaultReturnResult(super.onTouchEvent(event));
    	}
        return getReturnResult(strLayout, onTouchEvent);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ACTION_LOG(strLayout, onClick, DES_ACTION_ONCLICK);
	}
}  