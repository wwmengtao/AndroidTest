package com.mt.sysapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mt.androidtest.R;
public class SysAppsActivity extends Activity {
	GridView mGridView = null;
	ListViewAdapter mListViewAdapter = null;
	private ArrayList<HashMap<String, Object>> mSysAppList = new ArrayList<HashMap<String, Object>>();
	ProgressDialog mProgressDialog = null;
	ImageView mImageView = null;
	TextView mtvName = null;
	TextView mtvPackage = null;
	TextView mtvClass = null;
	PackageManager mPackageManager=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sys_apps);
		mGridView=(GridView)findViewById(R.id.gridview_sysapp);
		mListViewAdapter = new ListViewAdapter(this);
		mProgressDialog = new ProgressDialog(this);
		mImageView = (ImageView)findViewById(R.id.icon);
		mtvName = (TextView)findViewById(R.id.name);
		mtvPackage = (TextView)findViewById(R.id.packageName);
		mtvClass = (TextView)findViewById(R.id.className);
		mPackageManager = getPackageManager();
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
		loadDeviceApp();
	}
	
    private void loadDeviceApp(){
   	 new Thread(){
  	           public void run() {
					Message msg_dialog = mAnimationHandler.obtainMessage(1);
					mAnimationHandler.sendMessage(msg_dialog);
					mSysAppList.clear();
					Intent it = new Intent(Intent.ACTION_MAIN);
					it.addCategory(Intent.CATEGORY_LAUNCHER);
					List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(it,PackageManager.GET_ACTIVITIES);
					Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(mPackageManager));
					for(ResolveInfo info : resolveInfos) {
						if(!"com.lenovo.lesnapshot".equals(info.activityInfo.packageName)){
							HashMap<String,Object> map = new HashMap<String,Object>();
							Drawable mDrawable=null;
							boolean isAppIcon=true;
							map.put("label",info.loadLabel(mPackageManager));
		                    map.put("packname", info.activityInfo.packageName);
		                    map.put("classname", info.activityInfo.name);		
		                    if(!isAppIcon){
		                    	mDrawable = info.loadIcon(mPackageManager);//Indicate the icon of each activity
		                    }else{
		                    	try {
		                    		//Indicate the icon of each application
									mDrawable = mPackageManager.getApplicationIcon(mPackageManager.getApplicationInfo(info.activityInfo.packageName,0));
								} catch (NameNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									mDrawable =  getResources().getDrawable(R.drawable.not_found);
								}
		                    }
							
							map.put("itemImage",mDrawable);
		                    mSysAppList.add(map);
		                    //ALog.Log("packname:"+info.activityInfo.packageName+" classname:"+info.activityInfo.name);
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
					mGridView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,
								int position, long id) {
							mImageView.setImageDrawable((Drawable)mSysAppList.get(position).get("itemImage"));
					        mtvName.setText((String) mSysAppList.get(position).get("label"));
					        mtvPackage.setText((String) mSysAppList.get(position).get("packname"));
					        mtvClass.setText((String) mSysAppList.get(position).get("classname"));
						}
					});
			  	break;			
			}
		}
 	};
}
