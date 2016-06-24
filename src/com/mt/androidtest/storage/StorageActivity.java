package com.mt.androidtest.storage;

import java.io.File;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class StorageActivity extends BaseActivity {
	private boolean isLogRun=false;
	private Handler mHandler=null;
	private HandlerThread mHandlerThread=null;
	private HandlerCostTime mHandlerCostTime=null;
	private String [] mMethodNameFT={
			"readRawResources",
			"getResourcesDescription",
			"listAssets",
			"getFromAssets",
			"copyFilesInAssets"};
	private String [] mActivitiesName={"ExtStorageActivity","IntStorageActivity"};
	private static final int MSG_readRawResources=0x000;
	private static final int MSG_getResourcesDescription=0x001;	
	private static final int MSG_listAssets=0x002;
	private static final int MSG_getFromAssets=0x003;
	private static final int MSG_copyFilesInAssets=0x004;
    private static final int TIME_INTERVAL_MS = 500;
    private Message mMessage=null;	
    private static StorageHelper mStorageHelper=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		isLogRun=getLogRun();
		if(isLogRun)ALog.Log("StorageActivity_onCreate");
		initListFTData(mMethodNameFT);
		initListActivityData(mActivitiesName);
		initHandlerThread();
		mStorageHelper=new StorageHelper(this);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("StorageActivity_onResume");
		mHandler = getHandler();
	}

	@Override
	public void onPause(){
		if(null!=mHandlerCostTime){
			mHandlerCostTime.removeCallbacksAndMessages(0);
		}
		super.onPause();
		if(isLogRun)ALog.Log("StorageActivity_onPause");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		destroyHandlerThread();
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		super.handleMessage(msg);
		mHandler.removeMessages(msg.what);
		switch(msg.what){			
			case MSG_readRawResources:
				mStorageHelper.readRawResources();			
				break;
			case MSG_getResourcesDescription:
				mStorageHelper.getResourcesDescription();		
				break;	
		}
		return true;
	}
	
	/**
	 * HandlerCostTime�������ʱ������Handler
	 * @author Mengtao1
	 *
	 */
	class HandlerCostTime extends Handler{
	     public HandlerCostTime(Looper loop) {
	            super(loop);
	     }		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mHandler.removeMessages(msg.what);
			switch(msg.what){	
			case MSG_listAssets:
				mStorageHelper.listAssets("");//�о�assets��Ŀ¼�µ��ļ�
				//listAssets("test");//�о�assets/testĿ¼�µ��ļ�		
				break;
			case MSG_getFromAssets:
				mStorageHelper.getFromAssets("test/test.txt");		
				break;
			case MSG_copyFilesInAssets:
				//����assetsĿ¼�µ����ݵ�ָ��λ��
				/**
				 * getFilesDir��/data/data/com.mt.androidtest/files�´������ļ���
				 * �������ļ���д��������ҪWRITE_EXTERNAL_STORAGEȨ��
				 */
				File mFile= new File(getFilesDir(),"myAssets_FilesDir");
				mStorageHelper.copyFilesInAssets("",mFile.getAbsolutePath());
				ALog.Log("copyFilesInAssets to new location:"+mFile.getAbsolutePath());
				/**
				 * getExternalFilesDir��storage/emulated/0/Android/data/com.mt.androidtest/files
				 */
				mFile= new File(getExternalFilesDir(null),File.separator+"myAssets_ExternalFilesDir");
				mStorageHelper.copyFilesInAssets("",mFile.getAbsolutePath());
				ALog.Log("copyFilesInAssets to new location:"+mFile.getAbsolutePath());
				break;
			}
		}
	}
	
	public void initHandlerThread(){
		mHandlerThread = new HandlerThread("StorageActivity",
                android.os.Process.THREAD_PRIORITY_FOREGROUND);
		mHandlerThread.start();
		mHandlerCostTime=new HandlerCostTime(mHandlerThread.getLooper());
	}
	
	public void destroyHandlerThread(){
		/**HandlerThread��run������һ����ѭ�����������Լ��������̵߳��������ڳ�����activity�������ڡ�
		���������л���HandlerThread�̵߳�����������activity�ؽ����������Ӷ����ӡ�Ӧ����onDestroyʱ���߳�ֹͣ����
		���⣬���ڲ���HandlerThread���̣߳�ҲӦ��ȷ��activity����ǰ���߳��Ѿ���ֹ����������������onDestroyʱ����
		mThread.join();*/
		mHandlerThread.getLooper().quit();
	}
	
	@Override
	public void initListFTData(String [] mMethodNameFT){
		super.initListFTData(mMethodNameFT);
	}
	
	public void initListActivityData(String [] mActivitiesName){
		super.initListActivityData(mActivitiesName);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		//ALog.Log("position:"+position);
		mMessage=Message.obtain(mHandler, position);
		if(position<=1){//ִ�е��ǷǺ�ʱ����
			mHandler.sendMessageDelayed(mMessage, TIME_INTERVAL_MS);
		}else{//ִ�е��Ǻ�ʱ����
			mHandlerCostTime.sendMessageDelayed(mMessage, TIME_INTERVAL_MS);
		}
	}	
	
}
