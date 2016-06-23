package com.mt.androidtest.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.os.Environment;
import com.mt.androidtest.ALog;

public class IntStorageHelper {
	
	Context mContext=null;
	public IntStorageHelper(Context Context){
		mContext = Context;
	}	
	
	public void listDirs(){
		File mDataDirectory = Environment.getDataDirectory();// /data
		File mDownloadCacheDirectory = Environment.getDownloadCacheDirectory();// /cache
		File mRootDirectory = Environment.getRootDirectory();// /system
		//下列/data/user/0是链接，指向/data/data
		String mPackageCodePath = mContext.getPackageCodePath();// /data/app/com.mt.androidtest-1/base.apk
		String mPackageResourcePath = mContext.getPackageResourcePath();// /data/app/com.mt.androidtest-1/base.apk
		File mCacheDir = mContext.getCacheDir();// /data/user/0/com.mt.androidtest/cache
		File mDatabasePath = mContext.getDatabasePath("test");// /data/user/0/com.mt.androidtest/databases/test
		File mDir = mContext.getDir("", Context.MODE_PRIVATE);// /data/user/0/com.mt.androidtest/app_
		File mFilesDir = mContext.getFilesDir();// /data/user/0/com.mt.androidtest/files
		//
		ALog.Log("mDataDirectory:"+mDataDirectory);
		ALog.Log("mDownloadCacheDirectory:"+mDownloadCacheDirectory);
		ALog.Log("mRootDirectory:"+mRootDirectory);
		ALog.Log("mPackageCodePath:"+mPackageCodePath);			
		ALog.Log("mPackageResourcePath:"+mPackageResourcePath);		
		ALog.Log("mCacheDir:"+mCacheDir);
		ALog.Log("mDatabasePath:"+mDatabasePath);
		ALog.Log("mDir:"+mDir);				
		ALog.Log("mFilesDir:"+mFilesDir);	
	}
	
	public FileOutputStream getFileOutputStream(String fileName,int type) throws FileNotFoundException{
		File mDir = null;		
		File outFile = null;
	    FileOutputStream osw = null;
    	switch(type){
    	case 0:
    		mDir = mContext.getFilesDir();//存储路径为： /data/data/[package.name]/files/
    		outFile = new File(mDir, fileName);
    		ALog.Log("File written:"+outFile.getAbsolutePath());
    		osw = new FileOutputStream(outFile);
    		break;
    	case 1://openFileOutput：在 /data/data/[package.name]/files/目录下操作
	        osw = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);//openFileOutput效果和case 0等价
	        break;
    	case 10:
    		mDir = mContext.getCacheDir();//存储路径为： /data/data/[package.name]/cache/
    		outFile = new File(mDir, fileName);
    		ALog.Log("File written:"+outFile.getAbsolutePath());
    		osw = new FileOutputStream(outFile);	    
    		break;
    	}
    	return osw;
	}
	
	public void writeToFile(String fileName, String data, int type) {
	    FileOutputStream osw = null;
	    try {
	    	osw = getFileOutputStream(fileName, type);
	        osw.write(data.getBytes());
	        osw.flush();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	        try {
	            osw.close();
	        } catch (Exception e) {
	           e.printStackTrace();
	        }
	    }
	}
	
	public void readFromFile(String fileName,int type) {
		File fin = null;
	    StringBuilder data = new StringBuilder();
	    BufferedReader reader = null;
		File mDir = null;
    	switch(type){
    	case 0:
    	case 1:
    		mDir = mContext.getFilesDir();//存储路径为： /data/data/[package.name]/files/
	        break;
    	case 10:
    		mDir = mContext.getCacheDir();//存储路径为： /data/data/[package.name]/cache/
    		break;
    	}
    	fin = new File(mDir, fileName);
    	ALog.Log("File read:"+fin.getAbsolutePath()+"\n/---Contents shown in following:---/\n");
	    try {
	        reader = new BufferedReader(new InputStreamReader(new FileInputStream(fin), "utf-8"));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            data.append(line);
	            ALog.Log(line.toString());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            reader.close();
	        } catch (Exception e) {
	            ;
	        }
	    }
	}	
}
