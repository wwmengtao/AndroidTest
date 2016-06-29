package com.mt.androidtest.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import com.mt.androidtest.ALog;
import com.mt.androidtest.storage.StorageHelper;

public class ContentProviderDemo extends ContentProvider {
	private boolean isLogRun = true; 
    private static StorageHelper mStorageHelper=null;
	private ArrayList<File>mBaseDir=null;
	private Context mContext=null;
	private SQLiteOpenHelper mOpenHelper;
	@Override
	public boolean onCreate() {
		if(isLogRun)ALog.Log("CPDemo_onCreate");
		mContext=getContext();
		initBaseDir();
		mStorageHelper=new StorageHelper(mContext);
		mOpenHelper=DataBaseHelper.getInstance(mContext);
		createSharedData();
		return true;
	}

	public void createSharedData(){
		File mFile= new File(mContext.getFilesDir(),"myAssets_FilesDir");
		mStorageHelper.copyFilesInAssets("",mFile.getAbsolutePath());
		if(isLogRun)ALog.Log("CPDemo create:"+mFile.getAbsolutePath());
	}
	
	private void initBaseDir(){
		mBaseDir = new ArrayList<File>();
		mBaseDir.add(mContext.getFilesDir());///data/data/[PackageName]/files
		mBaseDir.add(mContext.getCacheDir());///data/data/[PackageName]/cache
		mBaseDir.add(Environment.getExternalStorageDirectory());//storage/emulated/0
		mBaseDir.add(mContext.getExternalCacheDir());///storage/emulated/0/Android/data/[PackageName]/cache
		mBaseDir.add(mContext.getExternalFilesDir(""));///storage/emulated/0/Android/data/[PackageName]/files
	}
	
	private File getExistedFile(String fileName){
		File mFile=null;
		for(File basedir : mBaseDir){
			mFile = new File(basedir, fileName);
			if(mFile.exists()){
				return mFile;
			}
		}
		return null;
	}
	
	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		File mFile = getExistedFile(uri.getPath());
		if(null!=mFile){
			if(isLogRun)ALog.Log("CPDemo_openFile:"+mFile.getPath());
			return ParcelFileDescriptor.open(mFile, ParcelFileDescriptor.MODE_READ_ONLY);
		}
		throw new FileNotFoundException(uri.getPath());
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		if (uri.toString().endsWith(".png")) {
			return "image/png";
		}
		return null;
	}

}
