package com.mt.androidtest.storage;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.permission.RequestPermissionsActivity;

public class ExtStorageActivity extends BaseActivity {
    private int AndroidVersion=-1;
	private byte[] bytesToSave=new byte[]{'m','t'};
	private byte[] bytesLoaded=null;
	private String type=null;
	private String fileName="mt.txt";
	private File mFile=null;
	private String [] mMethodNameFT={
			"isExternalStorageWritable","isExternalStorageReadable",
			"getExtStorageSize",
			"getExternalStorageDirectory","getExternalStoragePublicDirectory","getExternalCacheDir","getExternalFilesDir",
			"saveFileToExtStoragePublicDir","saveFileToExtStorageCustomDir","saveFileToExtStoragePrivateFilesDir","saveFileToExtStoragePrivateCacheDir",
			"loadFileFromExtStorage"
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
	}
	
	public void requestPermissions(){
        if (RequestPermissionsActivity.startPermissionActivity(this)) {
        	return;
        }
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "isExternalStorageWritable":
				ALog.Log("isExtStorageMounted:"+mExtStorageHelper.isExternalStorageWritable());
				break;
			case "isExternalStorageReadable":
				ALog.Log("isExternalStorageReadable:"+mExtStorageHelper.isExternalStorageReadable());
				break;				
			case "getExtStorageSize":
				ALog.Log("getExtStorageSize:"+mExtStorageHelper.getExtStorageSize());
				ALog.Log("getExtStorageFreeSize:"+mExtStorageHelper.getExtStorageFreeSize());
				ALog.Log("getExtStorageAvailableSize:"+mExtStorageHelper.getExtStorageAvailableSize());
				break;
			case "getExternalStorageDirectory":
				ALog.Log("getExternalStorageDirectory:"+mExtStorageHelper.getExternalStorageDirectory());
				break;			
			case "getExternalStoragePublicDirectory":
				type=Environment.DIRECTORY_PICTURES;
				ALog.Log("getExternalStoragePublicDirectory:"+mExtStorageHelper.getExternalStoragePublicDirectory(type));
				break;		
			case "getExternalCacheDir":
				ALog.Log("getExternalCacheDir:"+mExtStorageHelper.getExternalCacheDir());
				break;		
			case "getExternalFilesDir":
				type="M_T";
				ALog.Log("getExternalFilesDir:"+mExtStorageHelper.getExternalFilesDir(type));
				break;						
			case "saveFileToExtStoragePublicDir":
	    		if(AndroidVersion>22)requestPermissions();
				type=Environment.DIRECTORY_DOWNLOADS;
				mFile = mExtStorageHelper.saveFileToExtStoragePublicDir(bytesToSave,type,fileName);
				break;
			case "saveFileToExtStorageCustomDir":
	    		if(AndroidVersion>22)requestPermissions();
				type="M_T";
				mFile = mExtStorageHelper.saveFileToExtStorageCustomDir(bytesToSave,type,fileName);
				break;
			case "saveFileToExtStoragePrivateFilesDir":
				type=Environment.DIRECTORY_DOCUMENTS;
				mFile = mExtStorageHelper.saveFileToExtStoragePrivateFilesDir(bytesToSave,type,fileName);
				break;
			case "saveFileToExtStoragePrivateCacheDir":
				type=Environment.DIRECTORY_DOCUMENTS;
				mFile = mExtStorageHelper.saveFileToExtStoragePrivateCacheDir(bytesToSave,fileName);
				break;
			case "loadFileFromExtStorage":
				if(null!=mFile){
					bytesLoaded = mExtStorageHelper.loadFileFromExtStorage(mFile.getAbsolutePath());
					if(null!=bytesLoaded){
						ALog.Log("File loaded:"+mFile.getAbsolutePath());
						ALog.Log("Loaded contents:"+new String(bytesLoaded));
					}
				}
				break;
				
		}
	}
	
}