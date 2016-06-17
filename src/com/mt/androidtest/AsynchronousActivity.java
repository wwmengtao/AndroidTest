package com.mt.androidtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class AsynchronousActivity extends BaseActivity{
	boolean mIsProcessTaskRuning = false;
    private ConsumptionRefreshTask mAsyncTask=null;
    private String [] mMethodNameFT={"AsynctaskCancel"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asynchronous);
		super.initListFTData(mMethodNameFT);
		super.initListActivityData(null);
	}
	
	@Override
	public void onResume(){	
		super.onResume();
        if (!mIsProcessTaskRuning) {//此时自动转屏，那么将会生成新的AsyncTask，并且之前的AsyncTask仍将继续执行
        	mAsyncTask = new ConsumptionRefreshTask();
        	mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);	
        }
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		switch(mMethodNameFT[position]){
			case "AsynctaskCancel":
		        if (mAsyncTask != null) {
		        	/**
		        	 * 1)在java的线程中，没有办法停止一个正在运行中的线程。在Android的AsyncTask中也是一样的。
		        	 * 2)下列cancel(false)和cancel(true)区别：true可以中断可中断操作，比如Sleep等，但是二者都不能终止
		        	 * doInBackground的调用完成。二者的调用都会使得onPostExecute不被调用而调用onCancelled.
		        	 * 3)cancel()方法不一定能成功，所以 onCancel() 回调方法不一定被调用。
		        	 */
		        	//
		        	mAsyncTask.cancel(false);
		        	//mAsyncTask.cancel(true);
		        	ALog.Log("mAsyncTask.isCancelled():"+mAsyncTask.isCancelled());
		        	mIsProcessTaskRuning = false;
		    	}
		        break;		
		}
	}
	
    private class ConsumptionRefreshTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ALog.Log("onPreExecute");
            mIsProcessTaskRuning = true ;
        }

        @Override
        protected Void doInBackground(Void... params) {
        	ALog.Log("doInBackground");
        	//一、下列代码说明：mAsyncTask.cancel(true)可以打断sleep，直接跳到if(isLogRunAll)ALog.Log("doInBackground:sleep end");
        	try {
				Thread.sleep(1000*5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	ALog.Log("doInBackground:sleep end");
        	//二、下列代码说明：mAsyncTask.cancel(true)不可以打断for循环，除非添加标记位判断
        	int j=0;
        	for(int i=0;i<0x1F00000;i++){
         		//if(!mIsProcessTaskRuning)break;//只能通过标记位判断跳出for循环
        	}
        	ALog.Log("doInBackground:for end");
        	return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mIsProcessTaskRuning = false;
            ALog.Log("onPostExecute");
            super.onPostExecute(result);
        }
        
        @Override
        protected void onCancelled(Void result){
        	ALog.Log("onCancelled");
        }
    }  	
}
