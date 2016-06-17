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
    	//һ�����д���˵����mAsyncTask.cancel(true)���Դ��sleep��ֱ������if(isLogRunAll)ALog.Log("AsyncTaskDemo_doInBackground:sleep end");
    	try {
    		ALog.Log("AsyncTaskDemo_Sleep begins");
			Thread.sleep(1000*5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ALog.Log("AsyncTaskDemo_Sleep ends");
		}
    	//�������д���˵����mAsyncTask.cancel(true)�����Դ��forѭ����������ӱ��λ�ж�
    	ALog.Log("AsyncTaskDemo_For loop begins");
    	for(int i=0;i<0x1F00000;i++){
     		if(isCancelled())break;//ֻ��ͨ�����λ�ж�����forѭ��
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
	/**onCancelled��
	 * 1)��java���߳��У�û�а취ֹͣһ�����������е��̡߳���Android��AsyncTask��Ҳ��һ���ġ�
	 * 2)����cancel(false)��cancel(true)����true�����жϿ��жϲ���������sleep��wait��join���������ߵ��̣߳�
	 * ʹ���ǲ������ߣ����Ƕ��߶�������ֹdoInBackground�ĵ�����ɡ����ߵĵ��ö���ʹ��onPostExecute�������ö�����onCancelled.
	 * 3)cancel()������һ���ܳɹ������� onCancel() �ص�������һ�������á�
	 */    
    protected void onCancelled(Void result){
    	ALog.Log("AsyncTaskDemo_onCancelled");
    }
}  	
