package com.mt.androidtest.showview;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.mt.androidtest.ALog;
import com.mt.androidtest.R;
import com.mt.androidtest.listview.ListViewAdapter;

public class SysAppsActivity extends Activity implements DialogInterface.OnClickListener{
	GridView mGridView = null;
	ListViewAdapter mListViewAdapter = null;
	private ArrayList<HashMap<String, Object>> mSysAppList = new ArrayList<HashMap<String, Object>>();
	ProgressDialog mProgressDialog = null;
	ImageView mImageView = null;
	TextView mtvName = null;
	TextView mtvPackage = null;
	TextView mtvClass = null;
	TextView mtvSourceDir = null;	
	PackageManager mPackageManager=null;
	AnimationHandler mAnimationHandler=null;
	static final int MSG_SHOW_INIT_ANIMATION = 1;
	static final int MSG_SHOW_SYS_APPS = 2;
	static int mPosition=0;
	static SysAppsActivity mSysAppsActivity=null;
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
		mtvSourceDir  = (TextView)findViewById(R.id.sourceDir);
		mPackageManager = getPackageManager();
		mAnimationHandler=new AnimationHandler(this);
		mSysAppsActivity=this;
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
					String sourceDir=null;
					for(ResolveInfo info : resolveInfos) {
						if(!"com.lenovo.lesnapshot".equals(info.activityInfo.packageName)){
							HashMap<String,Object> map = new HashMap<String,Object>();
							Drawable mDrawable=null;
							boolean isAppIcon=true;
							map.put("itemText",info.loadLabel(mPackageManager));
		                    map.put("packname", info.activityInfo.packageName);
		                    map.put("classname", info.activityInfo.name);
		                    try {
								sourceDir=mPackageManager.getApplicationInfo(info.activityInfo.packageName,PackageManager.GET_ACTIVITIES).sourceDir;
								ALog.Log("sourceDir:"+sourceDir);
								map.put("sourceDir", sourceDir);
							} catch (NameNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
 	static class AnimationHandler extends Handler{
 		WeakReference<SysAppsActivity>mActivityWR=null;
 		SysAppsActivity mActivity=null;
 		ProgressDialog mProgressDialogH=null;
 		ListViewAdapter mListViewAdapterH=null;
 		ArrayList<HashMap<String, Object>> mSysAppListH=null;
 		GridView mGridViewH=null;
 		AnimationHandler(SysAppsActivity activity){
 			mActivityWR = new WeakReference<SysAppsActivity>(activity);
 		}
		@Override
		public void handleMessage(Message msg) {
			if(null==(mActivity=mActivityWR.get()))return;
			mProgressDialogH=mActivity.mProgressDialog;
			mListViewAdapterH=mActivity.mListViewAdapter;
			mSysAppListH=mActivity.mSysAppList;
			mGridViewH=mActivity.mGridView;
			switch (msg.what) {
				case MSG_SHOW_INIT_ANIMATION:
					mProgressDialogH.setMessage(mActivity.getApplicationContext().getString(R.string.msg_loading));
					mProgressDialogH.setCancelable(false);
					mProgressDialogH.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					mProgressDialogH.show();
					break;			
				case MSG_SHOW_SYS_APPS:
					mProgressDialogH.dismiss();
					mListViewAdapterH.setMode(1);
					mListViewAdapterH.setupList(mSysAppListH);
					mGridViewH.setNumColumns(4);
					mGridViewH.setAdapter(mListViewAdapterH);
					mGridViewH.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,
								int position, long id) {
							mActivity.mImageView.setImageDrawable((Drawable)mSysAppListH.get(position).get("itemImage"));
							mActivity.mtvName.setText((String) mSysAppListH.get(position).get("itemText"));
							mActivity.mtvPackage.setText((String) mSysAppListH.get(position).get("packname"));
							mActivity.mtvClass.setText((String) mSysAppListH.get(position).get("classname"));
							mActivity.mtvSourceDir.setText((String) mSysAppListH.get(position).get("sourceDir"));
							mPosition=position;
							showDialog();
						}
					});
					break;
			}
		}
 	};
 	
    private static DialogInterface mShowDialog;
    private static void showDialog() {
        // TODO: DialogFragment?
    	if(null==mSysAppsActivity)return;
    	mShowDialog = new AlertDialog.Builder(mSysAppsActivity).setTitle(
    			mSysAppsActivity.getResources().getString(R.string.app_name))
                .setMessage("Show what kind of info?")
                .setPositiveButton("AppInfo", mSysAppsActivity)
                .setNeutralButton("Overlay", mSysAppsActivity)
                .setNegativeButton(android.R.string.no, mSysAppsActivity)
                .show();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog == mShowDialog) {
        	Intent intent = null;
            if(which == DialogInterface.BUTTON_POSITIVE){
            	intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" +mSysAppList.get(mPosition).get("packname")));
            }else if(which == DialogInterface.BUTTON_NEGATIVE){
            }else if(which == DialogInterface.BUTTON_NEUTRAL){
            	intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +mSysAppList.get(mPosition).get("packname")));
            }
        	if(null!=intent)startActivity(intent);
        }
    }
}