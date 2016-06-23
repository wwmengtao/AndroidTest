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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidVersion=getAndroidVersion();
		setContentView(R.layout.activity_base);
		initListFTData(mMethodNameFT);
		initListActivityData(null);

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
				ALog.Log("isExtStorageMounted:"+ExtStorageHelper.isExternalStorageWritable());
				break;
			case "isExternalStorageReadable":
				ALog.Log("isExternalStorageReadable:"+ExtStorageHelper.isExternalStorageReadable());
				break;				
			case "getExtStorageSize":
				ALog.Log("getExtStorageSize:"+ExtStorageHelper.getExtStorageSize());
				ALog.Log("getExtStorageFreeSize:"+ExtStorageHelper.getExtStorageFreeSize());
				ALog.Log("getExtStorageAvailableSize:"+ExtStorageHelper.getExtStorageAvailableSize());
				break;
			case "getExternalStorageDirectory":
				ALog.Log("getExternalStorageDirectory:"+ExtStorageHelper.getExternalStorageDirectory());
				break;			
			case "getExternalStoragePublicDirectory":
				type=Environment.DIRECTORY_PICTURES;
				ALog.Log("getExternalStoragePublicDirectory:"+ExtStorageHelper.getExternalStoragePublicDirectory(type));
				break;		
			case "getExternalCacheDir":
				ALog.Log("getExternalCacheDir:"+ExtStorageHelper.getExternalCacheDir(this));
				break;		
			case "getExternalFilesDir":
				type="M_T";
				ALog.Log("getExternalFilesDir:"+ExtStorageHelper.getExternalFilesDir(this,type));
				break;						
			case "saveFileToExtStoragePublicDir":
	    		if(AndroidVersion>22)requestPermissions();
				type=Environment.DIRECTORY_DOWNLOADS;
				mFile = ExtStorageHelper.saveFileToExtStoragePublicDir(bytesToSave,type,fileName);
				break;
			case "saveFileToExtStorageCustomDir":
	    		if(AndroidVersion>22)requestPermissions();
				type="M_T";
				mFile = ExtStorageHelper.saveFileToExtStorageCustomDir(bytesToSave,type,fileName);
				break;
			case "saveFileToExtStoragePrivateFilesDir":
				type=Environment.DIRECTORY_DOCUMENTS;
				mFile = ExtStorageHelper.saveFileToExtStoragePrivateFilesDir(bytesToSave,type,fileName,this);
				break;
			case "saveFileToExtStoragePrivateCacheDir":
				type=Environment.DIRECTORY_DOCUMENTS;
				mFile = ExtStorageHelper.saveFileToExtStoragePrivateCacheDir(bytesToSave,fileName,this);
				break;
			case "loadFileFromExtStorage":
				if(null!=mFile){
					bytesLoaded = ExtStorageHelper.loadFileFromExtStorage(mFile.getAbsolutePath());
					if(null!=bytesLoaded){
						ALog.Log("File loaded:"+mFile.getAbsolutePath());
						ALog.Log("Loaded contents:"+new String(bytesLoaded));
					}
				}
				break;
				
		}
	}
	
}
