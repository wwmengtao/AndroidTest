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
	public static final String authority="com.mt.androidtest.cpdemo";//ָ����ContentProvider����AndroidManifest.xml��ָ��android:authorities="com.mt.androidtest.cpdemo"
	//������Ӧ��ͨ��CRUD����ContentProviderʱUriMatcher�����ж���Щ�����Ƿ����ContentProviderƥ��
	private static UriMatcher mUriMatcher  = new UriMatcher(UriMatcher.NO_MATCH);
	public static final String GrantURI_grant="/grant";
	private static final int GrantURI_code=0x01;	
	public static final String SqliteURI_sqlite="/sqlite";
	private static final int SqliteURI_code=0x02;
	//
	private static final String intent_authority="com.mt.androidtest.cpdemo/intent_test";
	static{
		// ΪUriMatcherע�����ݿ����Uri
		mUriMatcher.addURI(authority, GrantURI_grant, GrantURI_code);
		mUriMatcher.addURI(authority, SqliteURI_sqlite, SqliteURI_code);	
	}
	@Override
	//ContentProviderֻ��һ����������onCreate��������Ӧ��ͨ��contentResolver��һ�η��ʸ�ContentProviderʱ��ᴥ��onCreate�����ĵ���
	public boolean onCreate() {
		if(isLogRun)ALog.Log("CPDemo_onCreate");
		mContext=getContext();
		initBaseDir();
		initFiles();
		initSqlite();
		return true;
	}

	//���ڲ����ⲿ�洢��·��ȫ�����������Է�������ļ�
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
	 * ������ContentResolver.openInputStream(Uri)����ʱ��������ᱻ���ã��Ӷ���������ṩUri��Ӧ�ı����ļ�����
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
	/**ʵ�����£�
		Uri mUri=Uri.parse("content://com.mt.androidtest.cpdemo/sqlite");
		mIntent=new Intent("com.mt.androidtest.ContentResolver",mUri);
		mIntent.addCategory(Intent.CATEGORY_DEFAULT);
		startActivity(mIntent);
		����ʵ���У�ϵͳ����mIntentʱȡ��mUriֵ��������mUri��authorityΪ"com.mt.androidtest.cpdemo"��pathΪ"sqlite"���Ӷ�
		�ҵ�ContentProviderDemo������getType�õ�intent_authority��Ȼ�����activity��<intent-filter>�µ�<data android:mimeType="xxx"/>��
		������ִ�ʱ��xxx���ݺ�intent_authority������ͬ���Ϳ���activity(ǰ����action��category�Ѿ���������)��
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
