package com.mt.androidtest.touchevent;

import android.app.Activity;
import android.os.Bundle;

import com.mt.androidtest.R;

/**
 * �����¼��ķַ��ᾭ����ô����˳��dispatchTouchEvent --> onInterceptTouchEvent --> onTouchEvent��
 * �¼����ؾ���onInterceptTouchEvent�����н��У��ڸ÷����з���true���������ش����¼���
 * �����¼��ķַ���һ�����͵�����¼��������ϵ��µĹ��̡�
 * @author Mengtao1
 *
 */
public class TouchEventActivity extends Activity {  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_on_click);  
    }  
}  