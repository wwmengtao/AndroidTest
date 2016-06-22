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
    public AsyncTaskProgressBar(AsynchronousActivity activity){//这里我就采用构造器将TextView,ProgressBar直接传入,然后在该类中直接更新UI  
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
                if (isCancelled()) {//判断如果为true那么说明已经有请求取消当前任务的信号了，既然无法终止线程的运行，但是可以终止运行在线程中一系列操作  
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
        mTextView.setText("下载进度:"+values[0]+"%");  
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
    	mTextView.setText("下载取消，当前进度为："+currentProcess+"%");
    	super.onCancelled();
    }    
}  
