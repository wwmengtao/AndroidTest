package com.mt.androidtest.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
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
    private static StorageHelper mStorageHelper=null;
	private ArrayList<File>mBaseDir=null;
	private Context mContext=null;
	//
	private SQLiteOpenHelper mSqlOpenHelper;
	private String tableName=null;
	@Override
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
		if(isLogRun)ALog.Log("CPDemo_insert");
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// 调用insert方法，就可以将数据插入到数据库当中
		// 第一个参数:表名称
		// 第二个参数：为了强行插入空行
		// 第三个参数：ContentValues对象
		db.insert(tableName, null, values);
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if(isLogRun)ALog.Log("CPDemo_delete");
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		 db.delete(tableName, selection, selectionArgs);
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,	String[] selectionArgs) {
		if(isLogRun)ALog.Log("CPDemo_update");
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// 调用update方法
		// 第一个参数String：表名
		// 第二个参数ContentValues：ContentValues对象
		// 第三个参数String：where字句，相当于sql语句where后面的语句，？号是占位符
		// 第四个参数String[]：占位符的值
		 db.update(tableName, values, selection, selectionArgs);
		return 0;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		if(isLogRun)ALog.Log("CPDemo_query");
		Cursor cursor = null;
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象
		// 第一个参数String：表名
		// 第二个参数String[]:要查询的列名
		// 第三个参数String：查询条件
		// 第四个参数String[]：查询条件的参数
		// 第五个参数String:对查询的结果进行分组
		// 第六个参数String：对分组的结果进行限制
		// 第七个参数String：对查询的结果进行排序
		cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
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
