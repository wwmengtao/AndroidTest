package com.mt.androidtest.storage;

import java.io.File;

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
/**
 * mHandlerThread：处理非耗时操作
 * mHandlerCostTime：处理耗时操作，结合mHandlerThread使用
 * @author Mengtao1
 *
 */
public class StorageActivity extends BaseActivity {
	private boolean isLogRun=false;
	private Handler mHandler=null;
	private HandlerThread mHandlerThread=null;
	private HandlerCostTime mHandlerCostTime=null;
	private String [] mMethodNameFT={
			"readRawResources",
			"getResourcesDescription",
			"InstallApk",
			"listAssets",
			"getFromAssets",
			"copyFilesInAssets"};
	private String [] mActivitiesName={"ExtStorageActivity","IntStorageActivity"};
	private static final int MSG_readRawResources=0x000;
	private static final int MSG_getResourcesDescription=0x001;	
	private static final int MSG_InstallApk=0x002;
	private static final int MSG_listAssets=0x003;
	private static final int MSG_getFromAssets=0x004;
	private static final int MSG_copyFilesInAssets=0x005;
    private static final int TIME_INTERVAL_MS = 500;
    private Message mMessage=null;	
    private static StorageHelper mStorageHelper=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		super.initListFTData(mMethodNameFT);
		super.initListActivityData(mActivitiesName);
		initHandlerThread();
		mStorageHelper=new StorageHelper(this);
	}
	
	public void initHandlerThread(){
		mHandler = getHandler();//BaseActivity中已经对mHandler在onPause中做了处理
		//
		mHandlerThread = new HandlerThread("StorageActivity",
                android.os.Process.THREAD_PRIORITY_FOREGROUND);
		mHandlerThread.start();
		mHandlerCostTime=new HandlerCostTime(mHandlerThread.getLooper());
	}

	@Override
	public void onPause(){
		if(null!=mHandlerCostTime){
			mHandlerCostTime.removeCallbacksAndMessages(0);
		}
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
		/**HandlerThread的run方法是一个死循环，它不会自己结束，线程的生命周期超过了activity生命周期。
		当横竖屏切换，HandlerThread线程的数量会随着activity重建次数的增加而增加。应该在onDestroy时将线程停止掉。
		另外，对于不是HandlerThread的线程，也应该确保activity销毁前，线程已经终止，可以这样做：在onDestroy时调用
		mThread.join();*/
		mHandlerThread.getLooper().quit();
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
		// TODO Auto-generated method stub
		mMessage=Message.obtain(mHandler, position);
		if(position <= 2){//执行的是非耗时操作
			mHandler.sendMessageDelayed(mMessage, TIME_INTERVAL_MS);
		}else{//执行的是耗时操作
			mHandlerCostTime.sendMessageDelayed(mMessage, TIME_INTERVAL_MS);
		}
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
			case MSG_InstallApk:
				mStorageHelper.InstallApk();	
				break;					
		}
		return true;
	}
	
	/**
	 * HandlerCostTime：处理耗时操作的Handler
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
				mStorageHelper.listAssets("");//列举assets根目录下的文件
				//listAssets("test");//列举assets/test目录下的文件		
				break;
			case MSG_getFromAssets:
				mStorageHelper.getFromAssets("test/test.txt");		
				break;
			case MSG_copyFilesInAssets:
				//拷贝assets目录下的内容到指定位置
				/**
				 * getFilesDir：/data/data/com.mt.androidtest/files下创建子文件夹
				 * 向上述文件夹写入数据需要WRITE_EXTERNAL_STORAGE权限
				 */
				File mFile= new File(getFilesDir(),"myAssets_FilesDir");
				mStorageHelper.copyFilesInAssets("",mFile.getAbsolutePath());
				ALog.Log("copyFilesInAssets to new location:"+mFile.getAbsolutePath());
				/**
				 * getExternalFilesDir：storage/emulated/0/Android/data/com.mt.androidtest/files
				 */
				mFile= new File(getExternalFilesDir(null),File.separator+"myAssets_ExternalFilesDir");
				mStorageHelper.copyFilesInAssets("",mFile.getAbsolutePath());
				ALog.Log("copyFilesInAssets to new location:"+mFile.getAbsolutePath());
				break;
			}
		}
	}

}
