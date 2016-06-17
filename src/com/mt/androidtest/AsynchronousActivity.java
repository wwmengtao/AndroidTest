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
        if (!mIsProcessTaskRuning) {//��ʱ�Զ�ת������ô���������µ�AsyncTask������֮ǰ��AsyncTask�Խ�����ִ��
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
		        	 * 1)��java���߳��У�û�а취ֹͣһ�����������е��̡߳���Android��AsyncTask��Ҳ��һ���ġ�
		        	 * 2)����cancel(false)��cancel(true)����true�����жϿ��жϲ���������Sleep�ȣ����Ƕ��߶�������ֹ
		        	 * doInBackground�ĵ�����ɡ����ߵĵ��ö���ʹ��onPostExecute�������ö�����onCancelled.
		        	 * 3)cancel()������һ���ܳɹ������� onCancel() �ص�������һ�������á�
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
        	//һ�����д���˵����mAsyncTask.cancel(true)���Դ��sleep��ֱ������if(isLogRunAll)ALog.Log("doInBackground:sleep end");
        	try {
				Thread.sleep(1000*5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	ALog.Log("doInBackground:sleep end");
        	//�������д���˵����mAsyncTask.cancel(true)�����Դ��forѭ����������ӱ��λ�ж�
        	int j=0;
        	for(int i=0;i<0x1F00000;i++){
         		//if(!mIsProcessTaskRuning)break;//ֻ��ͨ�����λ�ж�����forѭ��
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
