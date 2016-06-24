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
		File mDataDir = Environment.getDataDirectory();// /data
		File mCacheDir1 = Environment.getDownloadCacheDirectory();// /cache
		File mRootDir = Environment.getRootDirectory();// /system
		//����/data/user/0�����ӣ�ָ��/data/data
		String mPkgCodePath = mContext.getPackageCodePath();// /data/app/com.mt.androidtest-1/base.apk
		String mPkgResPath = mContext.getPackageResourcePath();// /data/app/com.mt.androidtest-1/base.apk
		File mCacheDir2 = mContext.getCacheDir();// /data/user/0/com.mt.androidtest/cache
		File mDBPath = mContext.getDatabasePath("test");// /data/user/0/com.mt.androidtest/databases/test
		File mDir = mContext.getDir("M_T", Context.MODE_PRIVATE);// /data/user/0/com.mt.androidtest/app_
		File mFilesDir = mContext.getFilesDir();// /data/user/0/com.mt.androidtest/files
		//
		ALog.Log("mRootDir:"+mRootDir);
		ALog.Log("mDataDir:"+mDataDir);
		ALog.Log("mCacheDir1:"+mCacheDir1);
		//
		ALog.Log("mPkgCodePath:"+mPkgCodePath);			
		ALog.Log("mPkgResPath:"+mPkgResPath);		

		ALog.Log("mDBPath:"+mDBPath);
		ALog.Log("mDir:"+mDir);			
		ALog.Log("mCacheDir2:"+mCacheDir2);		
		ALog.Log("mFilesDir:"+mFilesDir);	
	}
	
	public FileOutputStream getFileOutputStream(String fileName,int type) throws FileNotFoundException{
		File mDir = null;		
		File outFile = null;
	    FileOutputStream osw = null;
    	switch(type){
    	case 0:
    		mDir = mContext.getFilesDir();//�洢·��Ϊ�� /data/data/[PackageName]/files/
    		outFile = new File(mDir, fileName);
    		ALog.Log("File written:"+outFile.getAbsolutePath());
    		osw = new FileOutputStream(outFile);
    		break;
    	case 1://openFileOutput���� /data/data/[PackageName]/files/Ŀ¼�²���
	        osw = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);//openFileOutputЧ����case 0�ȼ�
	        break;
    	case 10:
    		mDir = mContext.getCacheDir();//�洢·��Ϊ�� /data/data/[PackageName]/cache/
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
    		mDir = mContext.getFilesDir();//�洢·��Ϊ�� /data/data/[PackageName]/files/
	        break;
    	case 10:
    		mDir = mContext.getCacheDir();//�洢·��Ϊ�� /data/data/[PackageName]/cache/
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
