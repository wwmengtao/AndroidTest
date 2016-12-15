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
import com.mt.androidtest.tool.DataBaseHelper;

public class ContentProviderDemo extends ContentProvider {
	private boolean isLogRun = true; 
	private ArrayList<File>mBaseDir=null;
	private Context mContext=null;
	//
    private static StorageHelper mStorageHelper=null;
	private SQLiteOpenHelper mSqlOpenHelper;
	private String tableName=null;
	public static final String authority="com.mt.androidtest.cpdemo";//指定的ContentProvider会在AndroidManifest.xml中指定android:authorities="com.mt.androidtest.cpdemo"
	//当其他应用通过CRUD操作ContentProvider时UriMatcher用来判断这些操作是否与此ContentProvider匹配
	private static UriMatcher mUriMatcher  = new UriMatcher(UriMatcher.NO_MATCH);
	public static final String GrantURI_grant="/grant";
	private static final int GrantURI_code=0x01;	
	public static final String SqliteURI_sqlite="/sqlite";
	private static final int SqliteURI_code=0x02;
	//
	private static final String intent_authority="com.mt.androidtest.cpdemo/intent_test";
	static{
		// 为UriMatcher注册数据库解析Uri
		mUriMatcher.addURI(authority, GrantURI_grant, GrantURI_code);
		mUriMatcher.addURI(authority, SqliteURI_sqlite, SqliteURI_code);	
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
		ParcelFileDescriptor mParcelFileDescriptor=null;
		String fileName=uri.getPath();
		File mFile = getExistedFile(fileName);
		if(null!=mFile){
			if(isLogRun)ALog.Log("CPDemo_openFile:"+mFile.getPath());
			mParcelFileDescriptor = ParcelFileDescriptor.open(mFile, ParcelFileDescriptor.MODE_READ_ONLY);
		}else{
			throw new FileNotFoundException(uri.getPath());
		}
		return mParcelFileDescriptor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (mUriMatcher.match(uri)){
	        case SqliteURI_code :
	    		if(isLogRun)ALog.Log2("CPDemo_insert");
	    		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
				db.insert(tableName, null, values);
				break;
	        case GrantURI_code:
	        	if(isLogRun)ALog.Log2("CPDemo_insert_GrantURI_code");
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
				if(isLogRun)ALog.Log2("CPDemo_delete");
				SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
				db.delete(tableName, selection, selectionArgs);
				break;
	        case GrantURI_code:
	        	if(isLogRun)ALog.Log2("CPDemo_delete_GrantURI_code");
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
				if(isLogRun)ALog.Log2("CPDemo_update");
				SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
				db.update(tableName, values, selection, selectionArgs);
				break;
	        case GrantURI_code:
	        	if(isLogRun)ALog.Log2("CPDemo_update_GrantURI_code");
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
				if(isLogRun)ALog.Log2("CPDemo_query");
				SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
				cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
				break;
	        case GrantURI_code:
	        	if(isLogRun)ALog.Log2("CPDemo_query_GrantURI_code");
	        	break;				
	        default:
	            throw new IllegalArgumentException("unknownUri:" + uri);
		}
		return cursor;
	}

	@Override
	/**实例如下：
		Uri mUri=Uri.parse("content://com.mt.androidtest.cpdemo/sqlite");
		mIntent=new Intent("com.mt.androidtest.ContentResolver",mUri);
		mIntent.addCategory(Intent.CATEGORY_DEFAULT);
		startActivity(mIntent);
		上述实例中，系统解析mIntent时取出mUri值，解析出mUri的authority为"com.mt.androidtest.cpdemo"，path为"sqlite"，从而
		找到ContentProviderDemo并调用getType得到intent_authority。然后查找activity中<intent-filter>下的<data android:mimeType="xxx"/>。
		如果发现此时的xxx内容和intent_authority内容相同，就开启activity(前提是action和category已经满足条件)。
	 */
	public String getType(Uri uri) {
		switch (mUriMatcher.match(uri)){
	        case SqliteURI_code :
	        	return intent_authority;
	        default:
	            throw new IllegalArgumentException("unknownUri:" + uri);
		}
	}

}
