package com.mt.androidtest;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.AdapterView;

public class PackageManagerActivity extends BaseActivity{
	private ActivityManager mActivityManager=null;
	private ArrayList<RunningServiceInfo> runningServices = null;
	private String runningServiceName = null;
	private String [] mMethodNameFT={
			"showRunningServices",
			"showCertainService",
			"startCertainService",
			"stopCertainService"
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		mActivityManager = (ActivityManager) PackageManagerActivity.this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		super.initListActivityData(null);
		super.initListFTData(mMethodNameFT);
	}
	
	private void showRunningServices() {
		runningServices = (ArrayList<RunningServiceInfo>) mActivityManager.getRunningServices(50);
		if(null ==runningServices || 0==runningServices.size()){
			return;
		}
        for (RunningServiceInfo  RSI : runningServices) {  
        	runningServiceName = RSI.service.getClassName().toString();
            if (runningServiceName!=null) {  
                ALog.Log("RS: "+runningServiceName); 
            }  
        }
    }
	
	private void showCertainService(String certainService){
		runningServices = (ArrayList<RunningServiceInfo>) mActivityManager.getRunningServices(50);
		if(null ==runningServices || 0==runningServices.size()){
			return;
		}
        for (RunningServiceInfo  RSI : runningServices) {  
        	runningServiceName = RSI.service.getClassName().toString();
            if (runningServiceName!=null&&runningServiceName.equals(certainService)) {  
                ALog.Log("CertainService: "+certainService);
                return;
            }  
        }
        ALog.Log("CertainService not found!");
	}
	
	public void startCertainService(){
		Intent mIntent = new Intent();
		ComponentName mCn = new ComponentName("com.lenovo.widetouch","com.lenovo.widetouch.TouchService");
		mIntent.setComponent(mCn);
		startService(mIntent);
	}
	
	public void stopCertainService(){
		Intent mIntent = new Intent();
		ComponentName mCn = new ComponentName("com.lenovo.widetouch","com.lenovo.widetouch.TouchService");
		mIntent.setComponent(mCn);
		stopService(mIntent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "showRunningServices":
				showRunningServices();
				break;
			case "showCertainService":
				String certainService = "com.lenovo.widetouch.TouchService";
				showCertainService(certainService);
				break;
			case "startCertainService":
				startCertainService();
				break;
			case "stopCertainService":
				stopCertainService();
				break;
		}
	}
	
}
