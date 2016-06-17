package com.mt.androidtest.asynchronous;

import android.os.AsyncTask;

import com.mt.androidtest.ALog;

public class AsyncTaskDemo extends AsyncTask<Void, Void, Void> {

	public AsyncTaskDemo(){
		ALog.Log("AsyncTaskDemo_AsyncTaskDemo_ThreadId:"+Thread.currentThread().getId());
	}
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ALog.Log("AsyncTaskDemo_onPreExecute");
    }

    @Override
    protected Void doInBackground(Void... params) {
    	ALog.Log("AsyncTaskDemo_doInBackground");
    	//一、下列代码说明：mAsyncTask.cancel(true)可以打断sleep，直接跳到if(isLogRunAll)ALog.Log("AsyncTaskDemo_doInBackground:sleep end");
    	try {
    		ALog.Log("AsyncTaskDemo_Sleep begins");
			Thread.sleep(1000*5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ALog.Log("AsyncTaskDemo_Sleep ends");
		}
    	//二、下列代码说明：mAsyncTask.cancel(true)不可以打断for循环，除非添加标记位判断
    	ALog.Log("AsyncTaskDemo_For loop begins");
    	for(int i=0;i<0x1F00000;i++){
     		if(isCancelled())break;//只能通过标记位判断跳出for循环
    	}
    	ALog.Log("AsyncTaskDemo_For loop ends");
    	return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        ALog.Log("AsyncTaskDemo_onPostExecute");
        super.onPostExecute(result);
    }
    
    @Override
	/**onCancelled：
	 * 1)在java的线程中，没有办法停止一个正在运行中的线程。在Android的AsyncTask中也是一样的。
	 * 2)下列cancel(false)和cancel(true)区别：true可以中断可中断操作，比如sleep、wait、join方法而休眠的线程，
	 * 使他们不再休眠，但是二者都不能终止doInBackground的调用完成。二者的调用都会使得onPostExecute不被调用而调用onCancelled.
	 * 3)cancel()方法不一定能成功，所以 onCancel() 回调方法不一定被调用。
	 */    
    protected void onCancelled(Void result){
    	ALog.Log("AsyncTaskDemo_onCancelled");
    }
}  	
