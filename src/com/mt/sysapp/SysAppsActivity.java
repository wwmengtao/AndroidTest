package com.mt.sysapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.GridView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;
public class SysAppsActivity extends Activity {
	GridView mGridView = null;
	ListViewAdapter mListViewAdapter = null;
	private ArrayList<HashMap<String, Object>> mSysAppList = new ArrayList<HashMap<String, Object>>();
	ProgressDialog mProgressDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sys_apps);
		mGridView=(GridView)findViewById(R.id.gridview_sysapp);
		mListViewAdapter = new ListViewAdapter(this);
		mProgressDialog = new ProgressDialog(this);
	}

	@Override
	protected void onResume(){	
		super.onResume();
		initData();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}

	private void initData(){
		loadSystemApp();
	}
	
    private void loadSystemApp(){
   	 new Thread(){
  	           public void run() {
	        	      Message msg_dialog = mAnimationHandler.obtainMessage(1);
                      mAnimationHandler.sendMessage(msg_dialog);
                      mSysAppList.clear();
  	 		         Intent it = new Intent(Intent.ACTION_MAIN);
  	                 it.addCategory(Intent.CATEGORY_LAUNCHER);
  	 		         List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(it,PackageManager.GET_ACTIVITIES);
  	 		         Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(getPackageManager()));
  	 		         for(ResolveInfo info : resolveInfos) {
  	 			              if(!"com.lenovo.lesnapshot".equals(info.activityInfo.packageName)){
  	 				                  HashMap<String,Object> map = new HashMap<String,Object>();
  	 				                 map.put("itemImage",info.loadIcon(getPackageManager()));
  	 				                 map.put("label",info.loadLabel(getPackageManager()));
  	 				                 map.put("packname", info.activityInfo.packageName);
  	 				                 map.put("classname", info.activityInfo.name);
  	 				                 mSysAppList.add(map);
  	 				                 ALog.Log("packname:"+info.activityInfo.packageName+" classname:"+info.activityInfo.name);
  	 			              }
  	 		         }
  	 			    Message msg = mAnimationHandler.obtainMessage(2);
  	 			    mAnimationHandler.sendMessage(msg);
  	            }
         }.start();
         
    }
 	Handler mAnimationHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				  mProgressDialog.setMessage(getString(R.string.msg_loading));
				  mProgressDialog.setCancelable(false);
				  mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				  mProgressDialog.show();
				  break;			
				case 2:
					mProgressDialog.dismiss();
					mListViewAdapter.setupList(mSysAppList);
					mGridView.setNumColumns(4);
					mGridView.setAdapter(mListViewAdapter);
			  	break;			
			}
		}
 	};
}
