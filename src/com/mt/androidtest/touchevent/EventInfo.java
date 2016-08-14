package com.mt.androidtest.touchevent;

import android.util.SparseArray;
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
    //
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
    
    /**
     * ��ʶ�Ƿ���Ҫ����Ĭ�Ϸ���ֵ(super.xxx)
     * @param event
     * @param strLayout
     * @param strHandleEvent
     * @param handleEvents
     * @return
     */
    public static boolean setReturnResult(MotionEvent event, String strLayout, String strHandleEvent, SparseArray<Integer> handleEvents){
    	String currentView = strLayout;
    	String currentHandleMehod = strHandleEvent;
    	curEventType = event.getAction();
    	//��ʶ�¼�����ʼ��Ϣ
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
    	curEventTypeReturnResult = handleEvents.get(curEventType);
    	noNeedToSetDefaultReturnResult = (0==curEventTypeReturnResult || 1==curEventTypeReturnResult);
    	return noNeedToSetDefaultReturnResult;
    }
    
    public static void setDefaultReturnResult(boolean result){
    	returnResult = result;
    }
    
    /**
     * ����¼�����������ϸ��Ϣ�������¼�����ֵ
     * @param defaultReturnResult
     * �¼���������Ĭ�Ϸ���ֵ
     * @return
     */
    public static boolean getReturnResult(String strLayout, String strHandleEvent){
    	String currentView = strLayout;
    	String currentHandleMehod = strHandleEvent;
    	//ȷ���¼�����ķ���ֵ����
    	switch(curEventTypeReturnResult){
	    	case 0:
	    		returnResult = false;
	    		break;
	    	case 1:
	    		returnResult = true;
	    		break;
			default:
				;
    	}
    	//����¼�����������ϸ��Ϣ
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
   
    /**
     * ���м̳�EventReturnArrays���࣬����setData�����п������ô����¼��������ķ���ֵ��
     * ����SparseArray����ֵ��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
     * @author Mengtao1
     *
     */
    public static class EventReturnArrays{
    	private      SparseArray<Integer>TouchEventArrays = null;
    	protected SparseArray<Integer>dispatchTouchEventArrays      = null;
    	protected SparseArray<Integer>onInterceptTouchEventArrays = null;
    	protected SparseArray<Integer>onTouchArrays                         = null;
    	protected SparseArray<Integer>onTouchEventArrays                = null;    	
    	protected EventReturnArrays(){
    		//��TouchEventArrays����ֵ
    		TouchEventArrays = new SparseArray<Integer>();
    		TouchEventArrays.put(MotionEvent.ACTION_DOWN,    -9);
    		TouchEventArrays.put(MotionEvent.ACTION_UP,           -9);
    		TouchEventArrays.put(MotionEvent.ACTION_MOVE,     -9);
    		TouchEventArrays.put(MotionEvent.ACTION_CANCEL,  -9);
    		//
        	dispatchTouchEventArrays      = TouchEventArrays.clone();
        	onInterceptTouchEventArrays = TouchEventArrays.clone();
        	onTouchArrays                         = TouchEventArrays.clone();
        	onTouchEventArrays                = TouchEventArrays.clone();
    	}

    	public SparseArray<Integer> getDispatchTouchEventArrays(){
    		return dispatchTouchEventArrays;
    	}
    	public SparseArray<Integer> getOnInterceptTouchEventArrays(){
    		return onInterceptTouchEventArrays;
    	}
    	public SparseArray<Integer> getOnTouchArrays(){
    		return onTouchArrays;
    	}
    	public SparseArray<Integer> getOnTouchEventArrays(){
    		return onTouchEventArrays;
    	}    	
    }

    public static class TouchEventActivityERA extends EventReturnArrays{
    	
    	public TouchEventActivityERA(){
    		super();
    		setData();
    	}
        //����setData()���޸ĸ������¼��ķ���ֵ
    	public void setData(){
        	//��ֵ��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
    		//dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    	}
    }
    
    public static class MyLinearLayoutERA extends EventReturnArrays{
    	public MyLinearLayoutERA(){
    		super();
    		setData();
    	}
        //����setData()���޸ĸ������¼��ķ���ֵ
    	public void setData(){
        	//��ֵ��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��    		
    		//dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onInterceptTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    	}
    }
    
    public static class MyRelativeLayoutERA extends EventReturnArrays{
    	public MyRelativeLayoutERA(){
    		super();
    		setData();
    	}    	
        //����setData()���޸ĸ������¼��ķ���ֵ
    	public void setData(){
        	//��ֵ��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��    		
    		//dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onInterceptTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    	}
    }
    
    public static class MyTextViewERA extends EventReturnArrays{
    	public MyTextViewERA(){
    		super();
    		setData();
    	}    	    	
        //����setData()���޸ĸ������¼��ķ���ֵ
    	public void setData(){
        	//��ֵ��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
    		//dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    	}
    }        

    public static class MyButtonERA extends EventReturnArrays{
    	public MyButtonERA(){
    		super();
    		setData();
    	}    	    	
        //����setData()���޸ĸ������¼��ķ���ֵ
    	public void setData(){
        	//��ֵ��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��    		
       		//dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchArrays.put(MotionEvent.ACTION_DOWN, -9);
    		//onTouchEventArrays.put(MotionEvent.ACTION_DOWN, -9);
    	}
    }     
}
