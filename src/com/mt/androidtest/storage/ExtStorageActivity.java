package com.mt.androidtest.storage;

import java.io.File;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.permission.RequestPermissionsActivity;

public class ExtStorageActivity extends BaseActivity {
    private int AndroidVersion=-1;

	private String [] mMethodNameFT={
			"getMountPointByMountProc",
			"getMountPointByStorageManager",
			"listDirs",
			"fileSaveAndLoadProStorage",
			"bitmapSaveAndLoadProStorage"
	};
	private ExtStorageHelper mExtStorageHelper=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidVersion=getAndroidVersion();
		setContentView(R.layout.activity_base);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
		mExtStorageHelper=new ExtStorageHelper(this.getApplicationContext());
		if(AndroidVersion>22){
			requestPermissions(RequestPermissionsActivity.REQUIRED_PERMISSIONS);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "getMountPointByMountProc":
				mExtStorageHelper.getMountPointByMountProc();
				break;
			case "getMountPointByStorageManager":
				mExtStorageHelper.getMountPointByStorageManager(getApplicationContext());
				break;
			case "listDirs":
				mExtStorageHelper.listDirs();
				break;						
			case "fileSaveAndLoadProStorage":
				mExtStorageHelper.fileSaveAndLoadProStorage();
				break;
			case "bitmapSaveAndLoadProStorage":
				mExtStorageHelper.bitmapSaveAndLoadProStorage();
				break;
		}
	}
	
}
