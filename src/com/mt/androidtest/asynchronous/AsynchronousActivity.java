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

public class AsynchronousActivity extends BaseActivity{
    private String [] mMethodNameFT={"startAsyncTaskDemo","cancelAsyncTaskDemo","startAsyncTaskProgressBar","cancelAsyncTaskProgressBar"};
    public ProgressBar mProgressBar=null;    
    public TextView mProgressTV=null;    
    private Handler mHander=null;
    private static AsyncTaskProgressBar mAsyncTaskProgressBar=null;
    private static AsyncTaskDemo mAsyncTaskDemo=null;
	private static ExecutorService mExecutorService =null;
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
		cancelAsyncTaskProgressBarInThread();
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
	        	startAsyncTaskProgressBar();//�����������ĸ���
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

			break;			
		}
		return true;
	}
	
	public void cancelAsyncTaskProgressBarInThread(){
		new Thread() {
			public void run() {
				cancelAsyncTaskProgressBar();//ȡ���������ĸ���
			}
		}.start();
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
		//mAsyncTaskProgressBar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//��ʽ2�����ղ�����ʽ����
		mAsyncTaskProgressBar.executeOnExecutor(mExecutorService);//��ʽ3����ѡ�̳߳�
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
	public void setExecutorService(int type){
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
