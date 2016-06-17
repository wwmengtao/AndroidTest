package com.mt.androidtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mt.androidtest.asynchronous.AsyncTaskDemo;
import com.mt.androidtest.asynchronous.AsyncTaskProgressBar;

public class AsynchronousActivity extends BaseActivity{
    private String [] mMethodNameFT={"startAsyncTaskDemo","cancelAsyncTaskDemo","startAsyncTaskProgressBar","cancelAsyncTaskProgressBar"};
    public ProgressBar mProgressBar=null;    
    public TextView mProgressTV=null;    
    private static AsyncTaskProgressBar mAsyncTaskProgressBar=null;
    private static AsyncTaskDemo mAsyncTaskDemo=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asynchronous);
		super.initListFTData(mMethodNameFT);
		super.initListActivityData(null);
		ALog.Log("AsynchronousActivity_ThreadId:"+Thread.currentThread().getId());
	}
	
	@Override
	public void onResume(){	
		super.onResume();
		startAsyncTaskDemo();
	}
	
	@Override
	public void onPause(){	
		super.onPause();
		cancelAsyncTaskProgressBar();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		switch(mMethodNameFT[position]){
			case "startAsyncTaskDemo":
				startAsyncTaskDemo();
				break;		
			case "cancelAsyncTaskDemo":
				cancelAsyncTaskDemo();
		        break;			
			case "startAsyncTaskProgressBar":
	        	startAsyncTaskProgressBar();//开启进度条的更新
				break;
			case "cancelAsyncTaskProgressBar":
				cancelAsyncTaskProgressBar();//取消进度条的更新
		        break;	
		}
	}
	
	public void initProgressView(){
		mProgressBar=(ProgressBar) findViewById(R.id.AsyncTaskProgressBar);
		mProgressTV=(TextView) findViewById(R.id.AsyncTaskTextView); 
	}
	
	public void startAsyncTaskDemo(){
    	mAsyncTaskDemo = new AsyncTaskDemo();
    	//mAsyncTaskDemo.execute();//队列方式
    	mAsyncTaskDemo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);	//并发方式
	}
	
	public void cancelAsyncTaskDemo(){
        if (mAsyncTaskDemo != null) {
        	//mAsyncTaskDemo.cancel(false);
        	mAsyncTaskDemo.cancel(true);
        	ALog.Log("mAsyncTaskDemo.isCancelled():"+mAsyncTaskDemo.isCancelled());
    	}
	}
	
	public void startAsyncTaskProgressBar(){
		if(null==mProgressBar){
			initProgressView();
		}
		mAsyncTaskProgressBar=new AsyncTaskProgressBar(this);  
		/**多次点击"AsyncDownload"会发现，下列两种异步任务更新进度条方式不同：
		 * execute()：按照队列模式更新
		 * executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)：按照并发方式更新
		*/
		mAsyncTaskProgressBar.execute();
		//mAsyncTaskProgressBar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);	
	}
	
	public void cancelAsyncTaskProgressBar(){
		if (mAsyncTaskProgressBar != null && mAsyncTaskProgressBar.getStatus()==AsyncTask.Status.RUNNING) {  
			mAsyncTaskProgressBar.cancel(true);  
		}
	}
}
