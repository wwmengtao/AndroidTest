package com.mt.androidtest.touchevent;

import android.view.MotionEvent;

public class EventHandleInfo {
    //下列二维数组标识了相应方法中事件的处理结果，0代表返回false，1代表返回true，其他数值采用默认值。
    public static class EventHandleInfoArrays_TouchEventActivity{
       
    	public static int [][] dispatchTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};
    	public static int [][] onTouchArrays = {
			{MotionEvent.ACTION_DOWN,   -9},
			{MotionEvent.ACTION_MOVE,    -9},
			{MotionEvent.ACTION_UP,          -9},
			{MotionEvent.ACTION_CANCEL, -9},
    	};	    	
    	public static int [][] onTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};	
    }
    
    public static class EventHandleInfoArrays_MyLinearLayout{

    	public static int [][] dispatchTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};
    	public static int [][] onInterceptTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};
    	public static int [][] onTouchArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};		
    	public static int [][] onTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};	
    }
    
    public static class EventHandleInfoArrays_MyRelativeLayout{

    	public static int [][] dispatchTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};
    	public static int [][] onInterceptTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};	
    	public static int [][] onTouchArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};		
    	public static int [][] onTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};	
    }    
    
    public static class EventHandleInfoArrays_MyTextView{
    	
    	public static int [][] dispatchTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};
    	public static int [][] onTouchArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};		
    	public static int [][] onTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};	
    }        

    public static class EventHandleInfoArrays_MyButton{
    	
    	public static int [][] dispatchTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};
    	public static int [][] onTouchArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};		
    	public static int [][] onTouchEventArrays = {
    			{MotionEvent.ACTION_DOWN,   -9},
    			{MotionEvent.ACTION_MOVE,    -9},
    			{MotionEvent.ACTION_UP,          -9},
    			{MotionEvent.ACTION_CANCEL, -9},
    	};	
    }     
}
