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
	        	startAsyncTaskProgressBar();//�����������ĸ���
				break;
			case "cancelAsyncTaskProgressBar":
				cancelAsyncTaskProgressBar();//ȡ���������ĸ���
		        break;	
		}
	}
	
	public void initProgressView(){
		mProgressBar=(ProgressBar) findViewById(R.id.AsyncTaskProgressBar);
		mProgressTV=(TextView) findViewById(R.id.AsyncTaskTextView); 
	}
	
	public void startAsyncTaskDemo(){
    	mAsyncTaskDemo = new AsyncTaskDemo();
    	//mAsyncTaskDemo.execute();//���з�ʽ
    	mAsyncTaskDemo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);	//������ʽ
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
		/**��ε��"AsyncDownload"�ᷢ�֣����������첽������½�������ʽ��ͬ��
		 * execute()�����ն���ģʽ����
		 * executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)�����ղ�����ʽ����
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
