package com.mt.androidtest;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class PackageManagerActivity extends BaseActivity{
	private String [] mMethodNameFT={
			"showRunningServices",
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		super.initListActivityData(null);
		super.initListFTData(mMethodNameFT);
	}
	
	private boolean showRunningServices() {  
        ActivityManager mActivityManager = (ActivityManager) PackageManagerActivity.this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);  
        ArrayList<RunningServiceInfo> runningServices = (ArrayList<RunningServiceInfo>) mActivityManager.getRunningServices(50);
        String str_RunningService = null;
        for (int i = 0; i < runningServices.size(); i++) {  
        	str_RunningService = runningServices.get(i).service.getClassName().toString();
            if (str_RunningService!=null) {  
                ALog.Log("RS: "+str_RunningService); 
            }  
        }
        return false;  
    }  
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "showRunningServices":
				showRunningServices();
				break;
		}
	}
	
}
