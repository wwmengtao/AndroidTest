package com.mt.androidtest.touchevent;

import android.util.SparseArray;
import android.view.MotionEvent;

import com.mt.androidtest.ALog;

public class EventReturn {
	   
    /**
     * 所有继承EventReturnArrays的类，在其setData函数中可以设置触摸事件处理函数的返回值。
     * 下列SparseArray中数值标识了相应方法中事件的处理结果，0代表返回false，1代表返回true，其他数值采用默认值。
     * @author Mengtao1
     *
     */
    public static class EventReturnArrays{
    	protected SparseArray<Integer>dispatchTouchEventArrays      = null;
    	protected SparseArray<Integer>onInterceptTouchEventArrays = null;
    	protected SparseArray<Integer>onTouchArrays                         = null;
    	protected SparseArray<Integer>onTouchEventArrays                = null;    	
    	private int INIT_RETURN_RESULT = -9;//-9：表明事件处理函数的返回值采用默认处理方式，而不是直接返回true或者false
    	//初始化各个SparseArray
    	public void initData(){
    		dispatchTouchEventArrays      = getInitialSparseArray();
    		onInterceptTouchEventArrays = getInitialSparseArray();
    		onTouchArrays                         = getInitialSparseArray();
    		onTouchEventArrays                = getInitialSparseArray();
    	}
    	
    	public SparseArray<Integer> getInitialSparseArray(){
    		//给TouchEventArrays赋初值
    		SparseArray<Integer>TouchEventArrays = new SparseArray<Integer>();
    		TouchEventArrays.put(MotionEvent.ACTION_DOWN,    INIT_RETURN_RESULT);
    		TouchEventArrays.put(MotionEvent.ACTION_UP,           INIT_RETURN_RESULT);
    		TouchEventArrays.put(MotionEvent.ACTION_MOVE,     INIT_RETURN_RESULT);
    		TouchEventArrays.put(MotionEvent.ACTION_CANCEL,  INIT_RETURN_RESULT);
    		return TouchEventArrays;
    	}    	
    	
    	/**
    	 * 检查SparseArray中的数值内容
    	 * @param strLayout 当前检查所发生的View名称
    	 * @param showAll 是否显示所有元素
    	 */
    	public void checkData(String strLayout, boolean showAll){
    		ALog.Log("/----------------"+strLayout+".checkData"+"----------------/");
    		visitSparseArray("dispatchTouchEventArrays", dispatchTouchEventArrays, showAll);
    		visitSparseArray("onInterceptTouchEventArrays", onInterceptTouchEventArrays, showAll);
    		visitSparseArray("onTouchArrays", onTouchArrays, showAll);
    		visitSparseArray("onTouchEventArrays", onTouchEventArrays, showAll);
    		ALog.Log("/----------------"+strLayout+".checkData"+"----------------/");    		
    	}
    	
    	/**
    	 * 遍历SparseArray中所有元素
    	 * @param name mSA对应的View名称
    	 * @param mSA
    	 * @param showAll 如果为false，那么只显示出和INIT_RETURN_RESULT不同的元素
    	 */
    	public void visitSparseArray(String name, SparseArray<Integer> mSA, boolean showAll){
    		if(null==mSA)return;
    		int returnResult;
    		for(int i=0; i<mSA.size(); i++){
    			returnResult = mSA.get(i);
    			if(showAll || (!showAll&&INIT_RETURN_RESULT != returnResult)){
    				ALog.Log(String.format("%-40s",name+".item"+i+": ")+returnResult);
    			}
    		}
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
    		//数组初始化
    		initData();
    		//特定数组数值设定
    		setData();
    		//检查数组中数据
    		//checkData("TouchEventActivityERA", false);
    	}

    	/**
    	 * setData()中修改各处理事件的返回值，如dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult标识了相应方法中事件的处理结果，0代表返回false，1代表返回true，其他数值采用默认值。
    	 */
    	public void setData(){

    	}
    }
    
    public static class MyLinearLayoutERA extends EventReturnArrays{
    	public MyLinearLayoutERA(){
    		//数组初始化
    		initData();
    		//特定数组数值设定
    		setData();
    		//检查数组中数据
    		//checkData("MyLinearLayoutERA", false);
    	}
    	/**
    	 * setData()中修改各处理事件的返回值，如dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult标识了相应方法中事件的处理结果，0代表返回false，1代表返回true，其他数值采用默认值。
    	 */
    	public void setData(){

    	}
    }
    
    public static class MyRelativeLayoutERA extends EventReturnArrays{
    	public MyRelativeLayoutERA(){
    		//数组初始化
    		initData();
    		//特定数组数值设定
    		setData();
    		//检查数组中数据
    		//checkData("MyRelativeLayoutERA", false);
    	}    	
    	/**
    	 * setData()中修改各处理事件的返回值，如dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult标识了相应方法中事件的处理结果，0代表返回false，1代表返回true，其他数值采用默认值。
    	 */
    	public void setData(){

    	}
    }
    
    public static class MyTextViewERA extends EventReturnArrays{
    	public MyTextViewERA(){
    		//数组初始化
    		initData();
    		//特定数组数值设定
    		setData();
    		//检查数组中数据
    		//checkData("MyTextViewERA", false);
    	}    	    	
    	/**
    	 * setData()中修改各处理事件的返回值，如dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult标识了相应方法中事件的处理结果，0代表返回false，1代表返回true，其他数值采用默认值。
    	 */
    	public void setData(){

    	}
    }        

    public static class MyButtonERA extends EventReturnArrays{
    	public MyButtonERA(){
    		//数组初始化
    		initData();
    		//特定数组数值设定
    		setData();
    		//检查数组中数据
    		//checkData("MyButtonERA", false);
    	}    	    	
    	/**
    	 * setData()中修改各处理事件的返回值，如dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult标识了相应方法中事件的处理结果，0代表返回false，1代表返回true，其他数值采用默认值。
    	 */
    	public void setData(){

    	}
    }     
}
