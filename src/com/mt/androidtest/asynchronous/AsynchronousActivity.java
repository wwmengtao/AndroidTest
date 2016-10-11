package com.mt.androidtest.asynchronous;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.tool.ExecutorHelper;

public class AsynchronousActivity extends BaseActivity{
    private String [] mMethodNameFT={"startAsyncTaskDemo","cancelAsyncTaskDemo","startAsyncTaskProgressBar","cancelAsyncTaskProgressBar"};
    public ProgressBar mProgressBar=null;    
    public TextView mProgressTV=null;    
    private Handler mHander=null;
    private static AsyncTaskProgressBar mAsyncTaskProgressBar=null;
    private static AsyncTaskDemo mAsyncTaskDemo=null;
	private ExecutorHelper mExecutorHelper =null;
	private ExecutorService mExecutorService=null;
	private static ArrayList<AsyncTaskProgressBar> mAsyncTaskProgressBarList=null;
	private static final int MSG_CANCEL_ASYNC_1 = 0x001;
	private static final int MSG_CANCEL_ASYNC_2 = 0x002;	
	private int delayMillis = 100;
	private Message mMessage=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asynchronous);
		super.initListFTData(mMethodNameFT);
		super.initListActivityData(null);
		mHander=getHandler();
		ALog.Log("AsynchronousActivity_ThreadId:"+Thread.currentThread().getId());
		mExecutorHelper = new ExecutorHelper();
		mExecutorService = mExecutorHelper.getExecutorService(2,2);//设置线程池种类以及核心线程数量
	}
	
	@Override
	public void onResume(){	
		super.onResume();
	}
	
	@Override
	public void onPause(){	
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
		cancelAsyncTaskProgressBar();//取消进度条的更新
		if(null!=mExecutorService){
			mExecutorService.shutdownNow();//关闭线程池
			mExecutorService = null;
		}
		super.onDestroy();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		switch(mMethodNameFT[position]){
			case "startAsyncTaskDemo":
				startAsyncTaskDemo();
				break;		
			case "cancelAsyncTaskDemo":
				mMessage = mHander.obtainMessage(MSG_CANCEL_ASYNC_1);
				mHander.sendMessageDelayed(mMessage, delayMillis);
		        break;			
			case "startAsyncTaskProgressBar":
	        	startAsyncTaskProgressBar();//开启进度条的更新
				break;
			case "cancelAsyncTaskProgressBar":
				mMessage = mHander.obtainMessage(MSG_CANCEL_ASYNC_2);
				mHander.sendMessageDelayed(mMessage, delayMillis);
		        break;	
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		mHander.removeMessages(msg.what);
		switch(msg.what){
		case MSG_CANCEL_ASYNC_1:
			cancelAsyncTaskDemo();
			break;
		case MSG_CANCEL_ASYNC_2:
			cancelAsyncTaskProgressBar();//取消进度条的更新
			break;			
		}
		return true;
	}
	
	public void startAsyncTaskDemo(){
    	mAsyncTaskDemo = new AsyncTaskDemo();
    	//mAsyncTaskDemo.execute();//队列方式
    	mAsyncTaskDemo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//并发方式
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
			mProgressBar=(ProgressBar) findViewById(R.id.AsyncTaskProgressBar);
			mProgressTV=(TextView) findViewById(R.id.AsyncTaskTextView); 
			mAsyncTaskProgressBarList=new ArrayList<AsyncTaskProgressBar>();
		}
		mAsyncTaskProgressBar=new AsyncTaskProgressBar(this);  
		//多次点击"AsyncDownload"查看下列几种线程池实现的区别
		//mAsyncTaskProgressBar.execute();//方式1：按照队列模式更新
		//mAsyncTaskProgressBar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//方式2：按照并发方式更新
		mAsyncTaskProgressBar.executeOnExecutor(mExecutorService);//方式3：自选线程池
		mAsyncTaskProgressBarList.add(mAsyncTaskProgressBar);
	}
	
	public void cancelAsyncTaskProgressBar(){
		if(null!=mAsyncTaskProgressBarList && mAsyncTaskProgressBarList.size()>0){
			for(AsyncTaskProgressBar mAsyncTask:mAsyncTaskProgressBarList){
				if (mAsyncTask != null && mAsyncTask.getStatus()==AsyncTask.Status.RUNNING) {  
					ALog.Log("cancelAsyncTaskProgressBar");
					mAsyncTask.cancel(true);  
				}
			}
		}
	}
}
