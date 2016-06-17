package com.mt.androidtest;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private static ExecutorService mExecutorService =null;
	private static ArrayList<AsyncTaskProgressBar> mAsyncTaskProgressBarList=null;
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
			initProgressView();
			setExecutorService(2);//设置线程池种类
			mAsyncTaskProgressBarList=new ArrayList<AsyncTaskProgressBar>();
		}
		mAsyncTaskProgressBar=new AsyncTaskProgressBar(this);  
		//多次点击"AsyncDownload"查看下列几种线程池实现的区别
		//mAsyncTaskProgressBar.execute();//方式1：按照队列模式更新
		mAsyncTaskProgressBar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//方式2：按照并发方式更新
		//mAsyncTaskProgressBar.executeOnExecutor(mExecutorService);//方式3：自选线程池
		mAsyncTaskProgressBarList.add(mAsyncTaskProgressBar);
	}
	
	boolean isCancelAsyncTaskProgressBarEnd=true;
	public void cancelAsyncTaskProgressBar(){
		if(isCancelAsyncTaskProgressBarEnd){
			if(null!=mAsyncTaskProgressBarList && mAsyncTaskProgressBarList.size()>0){
				isCancelAsyncTaskProgressBarEnd=false;
				for(AsyncTaskProgressBar mAsyncTask:mAsyncTaskProgressBarList){
					if (mAsyncTask != null && mAsyncTask.getStatus()==AsyncTask.Status.RUNNING) {  
						mAsyncTask.cancel(true);  
					}
				}
				isCancelAsyncTaskProgressBarEnd=true;
			}
		}
		/*
		if (mAsyncTaskProgressBar != null && mAsyncTaskProgressBar.getStatus()==AsyncTask.Status.RUNNING) {  
			mAsyncTaskProgressBar.cancel(true);  
		}*/
	}

	/**getExecutorService：获取不同类型的线程池
	 * 1、newFixedThreadPool() ： 
	作用：该方法返回一个固定线程数量的线程池，该线程池中的线程数量始终不变，即不会再创建新的线程，也不会销毁已经创建好的线程，
	自始自终都是那几个固定的线程在工作，所以该线程池可以控制线程的最大并发数。
	2、newCachedThreadPool() ： 
	作用：该方法返回一个可以根据实际情况调整线程池中线程的数量的线程池。即该线程池中的线程数量不确定，是根据实际情况动态调整的。 
	例子：假如该线程池中的所有线程都正在工作，而此时有新任务提交，那么将会创建新的线程去处理该任务，而此时假如之前有一些线程
	完成了任务，现在又有新任务提交，那么将不会创建新线程去处理，而是复用空闲的线程去处理新任务。那么此时有人有疑问了，
	那这样来说该线程池的线程岂不是会越集越多？其实并不会，因为线程池中的线程都有一个“保持活动时间”的参数，通过配置它，
	如果线程池中的空闲线程的空闲时间超过该“保存活动时间”则立刻停止该线程，而该线程池默认的“保持活动时间”为60s。
	3、newSingleThreadExecutor() ： 
	作用：该方法返回一个只有一个线程的线程池，即每次只能执行一个线程任务，多余的任务会保存到一个任务队列中，等待这一个线程空闲，当这个线程空闲了再按FIFO方式顺序执行任务队列中的任务。
	4、newScheduledThreadPool() ： 
	作用：该方法返回一个可以控制线程池内线程定时或周期性执行某任务的线程池。
	5、newSingleThreadScheduledExecutor() ： 
	作用：该方法返回一个可以控制线程池内线程定时或周期性执行某任务的线程池。只不过和上面的区别是该线程池大小为1，而上面的可以指定线程池的大小。
	 * @param type
	 * @return
	 */
	public static void setExecutorService(int type){
		switch(type){
			case 1:
				mExecutorService = Executors.newSingleThreadExecutor();
				break;
			case 2:
				mExecutorService = Executors.newFixedThreadPool(2);
				break;
			case 3:
				mExecutorService = Executors.newCachedThreadPool();
				break;
			case 4:
				mExecutorService = Executors.newSingleThreadScheduledExecutor();
				break;
			case 5:
				mExecutorService = Executors.newScheduledThreadPool(1);
				break;				
		}
	}
}
