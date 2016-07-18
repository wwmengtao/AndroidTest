package com.mt.androidtest;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.service.dreams.DreamService;
import android.view.View;
import android.widget.AdapterView;

public class PackageManagerActivity extends BaseActivity{
	private ActivityManager mActivityManager=null;
	private ArrayList<RunningServiceInfo> runningServices = null;
	private String runningServiceName = null;
	private String [] mMethodNameFT={
			"showRunningServices",
			"showCertainService","startCertainService","stopCertainService",
			"getAppInfos"
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
			case "getAppInfos":
				getAppInfos();
				break;					
		}
	}
    
    public void getAppInfos(){
    	PackageManager pm = getPackageManager();
    	String packageName = "com.google.android.apps.photos";
    	String className = "com.google.android.apps.photos.home.HomeActivity";
    	ComponentName mCN = new ComponentName(packageName,className);
    	String label = null;
    	//由ActivityInfo.loadLabel确定标签名称
    	ActivityInfo mAI = null;
        try {
        	mAI = pm.getActivityInfo(mCN, 0);
        	label = mAI.loadLabel(pm).toString();
        	ALog.Log("ActivityInfo_loadLabel:"+label);
        } catch (PackageManager.NameNotFoundException e) {
        	ALog.Log("NameNotFoundException");
        }
        //由PM以及包名确定标签名称
    	ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
            label = (String)pm.getApplicationLabel(ai);
            ALog.Log("ApplicationInfo_loadLabel:"+label);
        } catch (PackageManager.NameNotFoundException e) {
        	ALog.Log("NameNotFoundException");
        }
        //由ResolveInfo.loadLabel来确定标签名称
        Intent dreamIntent = new Intent(DreamService.SERVICE_INTERFACE);
        List<ResolveInfo> resolveInfos = pm.queryIntentServices(dreamIntent, PackageManager.GET_META_DATA);
        for (ResolveInfo resolveInfo : resolveInfos) {
            if (resolveInfo.serviceInfo == null)
                continue;
            String serviceName="com.google.android.apps.photos.daydream.PhotosDreamService";
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            String str_serviceInfo = serviceInfo.toString();
            label = resolveInfo.loadLabel(pm).toString();
            if(str_serviceInfo.contains(serviceName)){
            	ALog.Log("serviceInfo:"+str_serviceInfo);
            	ALog.Log("resolveInfo_loadLabel:"+label);
            }
        }
    }
}
