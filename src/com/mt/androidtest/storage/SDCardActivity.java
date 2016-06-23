package com.mt.androidtest.storage;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class SDCardActivity extends BaseActivity {
	private byte[] bytesToSave=new byte[]{'m','t'};
	private byte[] bytesLoaded=null;
	private String type=null;
	private String fileName="mt.txt";
	private File mFile=null;
	private String [] mMethodNameFT={
			"isExternalStorageWritable","isExternalStorageReadable",
			"getSDCardSize",
			"getSDCardBaseDir","getSDCardPublicDir","getSDCardPrivateCacheDir","getSDCardPrivateFilesDir",
			"saveFileToSDCardPublicDir","saveFileToSDCardCustomDir","saveFileToSDCardPrivateFilesDir","saveFileToSDCardPrivateCacheDir",
			"loadFileFromSDCard"
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "isExternalStorageWritable":
				ALog.Log("isSDCardMounted:"+SDCardHelper.isExternalStorageWritable());
				break;
			case "isExternalStorageReadable":
				ALog.Log("isExternalStorageReadable:"+SDCardHelper.isExternalStorageReadable());
				break;				
			case "getSDCardSize":
				ALog.Log("getSDCardSize:"+SDCardHelper.getSDCardSize());
				ALog.Log("getSDCardFreeSize:"+SDCardHelper.getSDCardFreeSize());
				ALog.Log("getSDCardAvailableSize:"+SDCardHelper.getSDCardAvailableSize());
				break;
			case "getSDCardBaseDir":
				ALog.Log("getSDCardBaseDir:"+SDCardHelper.getSDCardBaseDir());
				break;			
			case "getSDCardPublicDir":
				type=Environment.DIRECTORY_PICTURES;
				ALog.Log("getSDCardPublicDir:"+SDCardHelper.getSDCardPublicDir(type));
				break;		
			case "getSDCardPrivateCacheDir":
				ALog.Log("getSDCardPrivateCacheDir:"+SDCardHelper.getSDCardPrivateCacheDir(this));
				break;		
			case "getSDCardPrivateFilesDir":
				type="M_T";
				ALog.Log("getSDCardPrivateFilesDir:"+SDCardHelper.getSDCardPrivateFilesDir(this,type));
				break;						
			case "saveFileToSDCardPublicDir":
				type=Environment.DIRECTORY_DOWNLOADS;
				mFile = SDCardHelper.saveFileToSDCardPublicDir(bytesToSave,type,fileName);
				break;
			case "saveFileToSDCardCustomDir":
				type="M_T";
				mFile = SDCardHelper.saveFileToSDCardCustomDir(bytesToSave,type,fileName);
				break;
			case "saveFileToSDCardPrivateFilesDir":
				type=Environment.DIRECTORY_DOCUMENTS;
				mFile = SDCardHelper.saveFileToSDCardPrivateFilesDir(bytesToSave,type,fileName,this);
				break;
			case "saveFileToSDCardPrivateCacheDir":
				type=Environment.DIRECTORY_DOCUMENTS;
				mFile = SDCardHelper.saveFileToSDCardPrivateCacheDir(bytesToSave,fileName,this);
				break;
			case "loadFileFromSDCard":
				if(null!=mFile){
					bytesLoaded = SDCardHelper.loadFileFromSDCard(mFile.getAbsolutePath());
					ALog.Log("bytesLoaded:"+bytesLoaded);
				}
				break;				
				
		}
	}
	
}
