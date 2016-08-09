package com.mt.androidtest.touchevent;

import android.app.Activity;
import android.os.Bundle;

import com.mt.androidtest.R;

/**
 * 触摸事件的分发会经过这么几个顺序，dispatchTouchEvent --> onInterceptTouchEvent --> onTouchEvent，
 * 事件拦截就在onInterceptTouchEvent方法中进行，在该方法中返回true即代表拦截触摸事件。
 * 触摸事件的分发是一个典型的隧道事件，即从上到下的过程。
 * @author Mengtao1
 *
 */
public class TouchEventActivity extends Activity {  
    public static final String formatStr="%-15s";
    public static final String formatStr2="%-23s";
    public static final String strLogIntercept = "onInterceptTouchEvent: ";
    public static final String strLogTouchEvent = "onTouchEvent: ";
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_on_click);  
    }  
}  