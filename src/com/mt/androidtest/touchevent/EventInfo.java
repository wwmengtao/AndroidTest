package com.mt.androidtest.touchevent;

import android.view.MotionEvent;
import com.mt.androidtest.ALog;

public class EventInfo {
	private static boolean showActionLog = true;
	private static boolean showActionReturnLog = true;	
	private static boolean returnResult = false; 
	private static int curEventType = -99;
	private static int curEventTypeReturnResult = -99;	
	private static boolean noNeedToSetDefaultReturnResult = false;
    /*------------------------------------------------String info---------------------------------------------------*/
    public static final String formatStr="%-25s";
    public static final String formatStr2="%-29s";
    public static final String formatStr3="%-13s";    
    public static final String dispatchTouchEvent = "dispatchTouchEvent";    
    public static final String onInterceptTouchEvent = "onInterceptTouchEvent";
    public static final String onTouch = "onTouch";
    public static final String onTouchEvent = "onTouchEvent";
    public static final String onClick = "onClick";
    private static final String DES_ACTION_DOWN = "ACTION_DOWN";
    private static final String DES_ACTION_MOVE = "ACTION_MOVE";    
    private static final String DES_ACTION_UP = "ACTION_UP";    
    private static final String DES_ACTION_CANCEL = "ACTION_CANCEL";    
    public static final String DES_ACTION_ONCLICK = "ON_CLICK";    
    
    /*------------------------------------------------Log info---------------------------------------------------*/
    public static void ACTION_LOG(String strLayout, String strHandleEvent, String acDescription){
    	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strHandleEvent)+acDescription); 
    }
    
    public static void ACTION_LOG(String strLayout, String strHandleEvent, String acDescription, boolean result){
    	ALog.Log(String.format(formatStr,strLayout)+String.format(formatStr2,strHandleEvent)
    			+String.format(formatStr3,acDescription)+result); 
    }    
    
    public static void ACTION_DOWN_LOG(String strLayout, String strHandleEvent){
    	if(showActionLog)ACTION_LOG(strLayout, strHandleEvent, DES_ACTION_DOWN);
    }
    
    public static void ACTION_DOWN_LOG(String strLayout, String strHandleEvent, boolean result){
    	if(showActionReturnLog)ACTION_LOG(strLayout, strHandleEvent, DES_ACTION_DOWN+": ", result);
    }    
    
    public static void ACTION_MOVE_LOG(String strLayout, String strHandleEvent){
    	if(showActionLog)ACTION_LOG(strLayout, strHandleEvent, DES_ACTION_MOVE);
    }
    
    public static void ACTION_MOVE_LOG(String strLayout, String strHandleEvent, boolean result){
    	if(showActionReturnLog)ACTION_LOG(strLayout, strHandleEvent, DES_ACTION_MOVE+": ", result);
    }    
    
    public static void ACTION_UP_LOG(String strLayout, String strHandleEvent){
    	if(showActionLog)ACTION_LOG(strLayout, strHandleEvent, DES_ACTION_UP);
    }
    
    public static void ACTION_UP_LOG(String strLayout, String strHandleEvent, boolean result){
    	if(showActionReturnLog)ACTION_LOG(strLayout, strHandleEvent, DES_ACTION_UP+": ", result);
    }
    
    public static void ACTION_CANCEL_LOG(String strLayout, String strHandleEvent){
    	if(showActionLog)ACTION_LOG(strLayout, strHandleEvent, DES_ACTION_CANCEL);
    }
    
    public static void ACTION_CANCEL_LOG(String strLayout, String strHandleEvent, boolean result){
    	if(showActionReturnLog)ACTION_LOG(strLayout, strHandleEvent, DES_ACTION_CANCEL+": ",result);
    }
    /*------------------------------------------------Log info---------------------------------------------------*/
    
    //返回事件处理结果
    public static boolean setReturnResult(MotionEvent event, String strLayout, String strHandleEvent, int [][] handleEvents){
    	String currentView = strLayout;
    	String currentHandleMehod = strHandleEvent;
    	curEventType = event.getAction();
    	//标识事件处理开始信息
        switch (curEventType) {
	        case MotionEvent.ACTION_DOWN:  
	        	ACTION_DOWN_LOG(currentView, currentHandleMehod);
	            break;  
	        case MotionEvent.ACTION_MOVE:  
	        	ACTION_MOVE_LOG(currentView, currentHandleMehod); 
	            break;              
	        case MotionEvent.ACTION_UP:  
	        	ACTION_UP_LOG(currentView, currentHandleMehod); 
	            break;  
	        case MotionEvent.ACTION_CANCEL:
	        	ACTION_CANCEL_LOG(currentView, currentHandleMehod); 
	            break;
	        default:
	        	;
        }
    	curEventTypeReturnResult = handleEvents[curEventType][1];
    	noNeedToSetDefaultReturnResult = (0==curEventTypeReturnResult || 1==curEventTypeReturnResult);
    	return noNeedToSetDefaultReturnResult;
    }
    
    public static void setDefaultReturnResult(boolean result){
    	returnResult = result;
    }
    
    /**
     * 输出事件处理结果的详细信息并返回事件处理值
     * @param defaultReturnResult
     * 事件处理结果的默认返回值
     * @return
     */
    public static boolean getReturnResult(String strLayout, String strHandleEvent){
    	String currentView = strLayout;
    	String currentHandleMehod = strHandleEvent;
    	//确认事件处理的返回值类型
    	switch(curEventTypeReturnResult){
	    	case 0:
	    		returnResult = false;
	    	case 1:
	    		returnResult = true;
			default:
				;
    	}
    	//输出事件处理结果的详细信息
        switch (curEventType) {
	        case MotionEvent.ACTION_DOWN:  
	        	ACTION_DOWN_LOG(currentView, currentHandleMehod, returnResult);
	            break;  
	        case MotionEvent.ACTION_MOVE:  
	        	ACTION_MOVE_LOG(currentView, currentHandleMehod, returnResult); 
	            break;              
	        case MotionEvent.ACTION_UP:  
	        	ACTION_UP_LOG(currentView, currentHandleMehod, returnResult); 
	            break;  
	        case MotionEvent.ACTION_CANCEL:
	        	ACTION_CANCEL_LOG(currentView, currentHandleMehod, returnResult); 
	            break;
	        default:
	        	;
        }     
        return returnResult;
    }

}
