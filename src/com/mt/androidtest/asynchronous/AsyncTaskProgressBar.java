package com.mt.androidtest.asynchronous;  
  
import java.lang.ref.WeakReference;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mt.androidtest.ALog;

public class AsyncTaskProgressBar extends AsyncTask<Void, Integer, Integer>{  
    private TextView mTextView=null;  
    private ProgressBar mProgressBar=null;  
    private int currentProcess=0;
    private WeakReference<AsynchronousActivity>mWeakReference=null;
    AsynchronousActivity mAsynchronousActivity=null;
    public AsyncTaskProgressBar(AsynchronousActivity activity){//�����ҾͲ��ù�������TextView,ProgressBarֱ�Ӵ���,Ȼ���ڸ�����ֱ�Ӹ���UI  
    	mWeakReference=new WeakReference<AsynchronousActivity>(activity);
    	if((mAsynchronousActivity=mWeakReference.get())!=null){
    		mTextView=mAsynchronousActivity.mProgressTV;
    		mProgressBar=mAsynchronousActivity.mProgressBar;
    	}
    }  
    @Override  
    protected Integer doInBackground(Void... params) {  
    	ALog.Log("AsyncTaskProgressBar_doInBackground");    	
        for(int i=1;i<101;i++){  
        	currentProcess=i;
            try {
                if (isCancelled()) {//�ж����Ϊtrue��ô˵���Ѿ�������ȡ����ǰ������ź��ˣ���Ȼ�޷���ֹ�̵߳����У����ǿ�����ֹ�������߳���һϵ�в���  
                	break;
                }
                Thread.sleep(15);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }
            publishProgress(currentProcess);  
        }
        return currentProcess;
    }  
    
    @Override  
    protected void onProgressUpdate(Integer... values) {  
        mProgressBar.setProgress(values[0]);  
        mTextView.setText("���ؽ���:"+values[0]+"%");  
        super.onProgressUpdate(values);  
    }  
    
    @Override  
    protected void onPostExecute(Integer result) { 
    	ALog.Log("AsyncTaskProgressBar_onPostExecute");
        mTextView.setText(result+"%");
        super.onPostExecute(result);  
    }  
    
    @Override
    protected void onCancelled(){
    	ALog.Log("AsyncTaskProgressBar_onCancelled");
    	mTextView.setText("����ȡ������ǰ����Ϊ��"+currentProcess+"%");
    	super.onCancelled();
    }    
}  
