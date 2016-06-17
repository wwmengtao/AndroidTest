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
    	mAsyncTaskDemo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//������ʽ
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
			setExecutorService(2);//�����̳߳�����
			mAsyncTaskProgressBarList=new ArrayList<AsyncTaskProgressBar>();
		}
		mAsyncTaskProgressBar=new AsyncTaskProgressBar(this);  
		//��ε��"AsyncDownload"�鿴���м����̳߳�ʵ�ֵ�����
		//mAsyncTaskProgressBar.execute();//��ʽ1�����ն���ģʽ����
		mAsyncTaskProgressBar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//��ʽ2�����ղ�����ʽ����
		//mAsyncTaskProgressBar.executeOnExecutor(mExecutorService);//��ʽ3����ѡ�̳߳�
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

	/**getExecutorService����ȡ��ͬ���͵��̳߳�
	 * 1��newFixedThreadPool() �� 
	���ã��÷�������һ���̶��߳��������̳߳أ����̳߳��е��߳�����ʼ�ղ��䣬�������ٴ����µ��̣߳�Ҳ���������Ѿ������õ��̣߳�
	��ʼ���ն����Ǽ����̶����߳��ڹ��������Ը��̳߳ؿ��Կ����̵߳���󲢷�����
	2��newCachedThreadPool() �� 
	���ã��÷�������һ�����Ը���ʵ����������̳߳����̵߳��������̳߳ء������̳߳��е��߳�������ȷ�����Ǹ���ʵ�������̬�����ġ� 
	���ӣ�������̳߳��е������̶߳����ڹ���������ʱ���������ύ����ô���ᴴ���µ��߳�ȥ��������񣬶���ʱ����֮ǰ��һЩ�߳�
	������������������������ύ����ô�����ᴴ�����߳�ȥ�������Ǹ��ÿ��е��߳�ȥ������������ô��ʱ�����������ˣ�
	��������˵���̳߳ص��߳����ǻ�Խ��Խ�ࣿ��ʵ�����ᣬ��Ϊ�̳߳��е��̶߳���һ�������ֻʱ�䡱�Ĳ�����ͨ����������
	����̳߳��еĿ����̵߳Ŀ���ʱ�䳬���á�����ʱ�䡱������ֹͣ���̣߳������̳߳�Ĭ�ϵġ����ֻʱ�䡱Ϊ60s��
	3��newSingleThreadExecutor() �� 
	���ã��÷�������һ��ֻ��һ���̵߳��̳߳أ���ÿ��ֻ��ִ��һ���߳����񣬶��������ᱣ�浽һ����������У��ȴ���һ���߳̿��У�������߳̿������ٰ�FIFO��ʽ˳��ִ����������е�����
	4��newScheduledThreadPool() �� 
	���ã��÷�������һ�����Կ����̳߳����̶߳�ʱ��������ִ��ĳ������̳߳ء�
	5��newSingleThreadScheduledExecutor() �� 
	���ã��÷�������һ�����Կ����̳߳����̶߳�ʱ��������ִ��ĳ������̳߳ء�ֻ����������������Ǹ��̳߳ش�СΪ1��������Ŀ���ָ���̳߳صĴ�С��
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
