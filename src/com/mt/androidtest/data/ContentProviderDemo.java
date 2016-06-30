package com.mt.androidtest.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import com.mt.androidtest.ALog;
import com.mt.androidtest.storage.StorageHelper;

public class ContentProviderDemo extends ContentProvider {
	private boolean isLogRun = true; 
	private ArrayList<File>mBaseDir=null;
	private Context mContext=null;
	//
    private static StorageHelper mStorageHelper=null;
	private SQLiteOpenHelper mSqlOpenHelper;
	private String tableName=null;
	private static String authority="com.mt.androidtest.cpdemo";
	private static UriMatcher mUriMatcher  = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int SqliteURI_code=0x01;
	static
	{
		// 为UriMatcher注册数据库解析Uri
		mUriMatcher.addURI(authority, "sqlite", SqliteURI_code);
	}
	@Override
	//ContentProvider只有一个生命周期onCreate。当其他应用通过contentResolver第一次访问该ContentProvider时候会触发onCreate方法的调用
	public boolean onCreate() {
		if(isLogRun)ALog.Log("CPDemo_onCreate");
		mContext=getContext();
		initBaseDir();
		initFiles();
		initSqlite();
		return true;
	}

	//将内部、外部存储的路径全部保存起来以方便查找文件
	private void initBaseDir(){
		mBaseDir = new ArrayList<File>();
		mBaseDir.add(mContext.getFilesDir());///data/data/[PackageName]/files
		mBaseDir.add(mContext.getCacheDir());///data/data/[PackageName]/cache
		mBaseDir.add(Environment.getExternalStorageDirectory());//storage/emulated/0
		mBaseDir.add(mContext.getExternalCacheDir());///storage/emulated/0/Android/data/[PackageName]/cache
		mBaseDir.add(mContext.getExternalFilesDir(""));///storage/emulated/0/Android/data/[PackageName]/files
	}	
	
	public void initFiles(){
		File mFile= new File(mContext.getFilesDir(),"myAssets_FilesDir");
		mStorageHelper=new StorageHelper(mContext);
		mStorageHelper.copyFilesInAssets("",mFile.getAbsolutePath());
		if(isLogRun)ALog.Log("CPDemo create:"+mFile.getAbsolutePath());
	}
	
	public void initSqlite(){
		mSqlOpenHelper=DataBaseHelper.getInstance(mContext);
		tableName=DataBaseHelper.getTableName();
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
	/**
	 * 当调用ContentResolver.openInputStream(Uri)命令时，此命令会被调用，从而向调用者提供Uri对应的本地文件名称
	 */
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		File mFile = getExistedFile(uri.getPath());
		if(null!=mFile){
			if(isLogRun)ALog.Log("CPDemo_openFile:"+mFile.getPath());
			return ParcelFileDescriptor.open(mFile, ParcelFileDescriptor.MODE_READ_ONLY);
		}
		throw new FileNotFoundException(uri.getPath());
	}
	//以下进行数据库的CRUD操作
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (mUriMatcher.match(uri)){
	        case SqliteURI_code :
	    		if(isLogRun)ALog.Log("CPDemo_insert");
	    		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
				db.insert(tableName, null, values);
				break;
	        default:
	            throw new IllegalArgumentException("unknownUri:" + uri);
	}
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (mUriMatcher.match(uri)){
	        case SqliteURI_code :
				if(isLogRun)ALog.Log("CPDemo_delete");
				SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
				db.delete(tableName, selection, selectionArgs);
				break;
	        default:
	            throw new IllegalArgumentException("unknownUri:" + uri);
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,	String[] selectionArgs) {
		switch (mUriMatcher.match(uri)){
	        case SqliteURI_code :
				if(isLogRun)ALog.Log("CPDemo_update");
				SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
				db.update(tableName, values, selection, selectionArgs);
				break;
	        default:
	            throw new IllegalArgumentException("unknownUri:" + uri);
		}
		return 0;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		switch (mUriMatcher.match(uri)){
	        case SqliteURI_code :		
				if(isLogRun)ALog.Log("CPDemo_query");
				SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
				cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
				break;
	        default:
	            throw new IllegalArgumentException("unknownUri:" + uri);
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		if (uri.toString().endsWith(".png")) {
			return "image/png";
		}
		return null;
	}

}
