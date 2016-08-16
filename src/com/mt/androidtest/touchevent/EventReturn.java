package com.mt.androidtest.touchevent;

import android.util.SparseArray;
import android.view.MotionEvent;

import com.mt.androidtest.ALog;

public class EventReturn {
	   
    /**
     * ���м̳�EventReturnArrays���࣬����setData�����п������ô����¼��������ķ���ֵ��
     * ����SparseArray����ֵ��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
     * @author Mengtao1
     *
     */
    public static class EventReturnArrays{
    	protected SparseArray<Integer>dispatchTouchEventArrays      = null;
    	protected SparseArray<Integer>onInterceptTouchEventArrays = null;
    	protected SparseArray<Integer>onTouchArrays                         = null;
    	protected SparseArray<Integer>onTouchEventArrays                = null;    	
    	private int INIT_RETURN_RESULT = -9;//-9�������¼��������ķ���ֵ����Ĭ�ϴ���ʽ��������ֱ�ӷ���true����false
    	//��ʼ������SparseArray
    	public void initData(){
    		dispatchTouchEventArrays      = getInitialSparseArray();
    		onInterceptTouchEventArrays = getInitialSparseArray();
    		onTouchArrays                         = getInitialSparseArray();
    		onTouchEventArrays                = getInitialSparseArray();
    	}
    	
    	public SparseArray<Integer> getInitialSparseArray(){
    		//��TouchEventArrays����ֵ
    		SparseArray<Integer>TouchEventArrays = new SparseArray<Integer>();
    		TouchEventArrays.put(MotionEvent.ACTION_DOWN,    INIT_RETURN_RESULT);
    		TouchEventArrays.put(MotionEvent.ACTION_UP,           INIT_RETURN_RESULT);
    		TouchEventArrays.put(MotionEvent.ACTION_MOVE,     INIT_RETURN_RESULT);
    		TouchEventArrays.put(MotionEvent.ACTION_CANCEL,  INIT_RETURN_RESULT);
    		return TouchEventArrays;
    	}    	
    	
    	/**
    	 * ���SparseArray�е���ֵ����
    	 * @param strLayout ��ǰ�����������View����
    	 * @param showAll �Ƿ���ʾ����Ԫ��
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
    	 * ����SparseArray������Ԫ��
    	 * @param name mSA��Ӧ��View����
    	 * @param mSA
    	 * @param showAll ���Ϊfalse����ôֻ��ʾ����INIT_RETURN_RESULT��ͬ��Ԫ��
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
    		//�����ʼ��
    		initData();
    		//�ض�������ֵ�趨
    		setData();
    		//�������������
    		//checkData("TouchEventActivityERA", false);
    	}

    	/**
    	 * setData()���޸ĸ������¼��ķ���ֵ����dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
    	 */
    	public void setData(){

    	}
    }
    
    public static class MyLinearLayoutERA extends EventReturnArrays{
    	public MyLinearLayoutERA(){
    		//�����ʼ��
    		initData();
    		//�ض�������ֵ�趨
    		setData();
    		//�������������
    		//checkData("MyLinearLayoutERA", false);
    	}
    	/**
    	 * setData()���޸ĸ������¼��ķ���ֵ����dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
    	 */
    	public void setData(){

    	}
    }
    
    public static class MyRelativeLayoutERA extends EventReturnArrays{
    	public MyRelativeLayoutERA(){
    		//�����ʼ��
    		initData();
    		//�ض�������ֵ�趨
    		setData();
    		//�������������
    		//checkData("MyRelativeLayoutERA", false);
    	}    	
    	/**
    	 * setData()���޸ĸ������¼��ķ���ֵ����dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
    	 */
    	public void setData(){

    	}
    }
    
    public static class MyTextViewERA extends EventReturnArrays{
    	public MyTextViewERA(){
    		//�����ʼ��
    		initData();
    		//�ض�������ֵ�趨
    		setData();
    		//�������������
    		//checkData("MyTextViewERA", false);
    	}    	    	
    	/**
    	 * setData()���޸ĸ������¼��ķ���ֵ����dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
    	 */
    	public void setData(){

    	}
    }        

    public static class MyButtonERA extends EventReturnArrays{
    	public MyButtonERA(){
    		//�����ʼ��
    		initData();
    		//�ض�������ֵ�趨
    		setData();
    		//�������������
    		//checkData("MyButtonERA", false);
    	}    	    	
    	/**
    	 * setData()���޸ĸ������¼��ķ���ֵ����dispatchTouchEventArrays.put(MotionEvent.ACTION_DOWN, returnResult);
    	 * returnResult��ʶ����Ӧ�������¼��Ĵ�������0������false��1������true��������ֵ����Ĭ��ֵ��
    	 */
    	public void setData(){

    	}
    }     
}
