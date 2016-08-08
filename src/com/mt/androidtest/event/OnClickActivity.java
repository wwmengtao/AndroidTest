package com.mt.androidtest.event;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

/**
 * 触摸事件的分发会经过这么几个顺序，dispatchTouchEvent --> onInterceptTouchEvent --> onTouchEvent，
 * 事件拦截就在onInterceptTouchEvent方法中进行，在该方法中返回true即代表拦截触摸事件。
 * 触摸事件的分发是一个典型的隧道事件，即从上到下的过程。
 * @author Mengtao1
 *
 */
public class OnClickActivity extends Activity {  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_on_click);  
          
        TextView mTextView = (TextView) findViewById(R.id.text);  
        mTextView.setOnTouchListener(new OnTouchListener() {  
              
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                switch (event.getAction()) {  
                case MotionEvent.ACTION_DOWN:  
                    ALog.Log("TextView----DOWN");  
                    break;  
                case MotionEvent.ACTION_MOVE:  
                	ALog.Log("TextView----MOVE");  
                    break;                         
                case MotionEvent.ACTION_UP:  
                	ALog.Log("TextView----UP");  
                    break;  
                }  
                return true;  
            }  
        });  
    }  
  
  
}  