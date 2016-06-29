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
		File mFile = getExistedFile(uri.getPath());
		if(null!=mFile){
			if(isLogRun)ALog.Log("CPDemo_openFile:"+mFile.getPath());
			return ParcelFileDescriptor.open(mFile, ParcelFileDescriptor.MODE_READ_ONLY);
		}
		throw new FileNotFoundException(uri.getPath());
	}
	//���½������ݿ��CRUD����
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(isLogRun)ALog.Log("CPDemo_insert");
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// ����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��
		// ��һ������:������
		// �ڶ���������Ϊ��ǿ�в������
		// ������������ContentValues����
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
		// ����update����
		// ��һ������String������
		// �ڶ�������ContentValues��ContentValues����
		// ����������String��where�־䣬�൱��sql���where�������䣬������ռλ��
		// ���ĸ�����String[]��ռλ����ֵ
		 db.update(tableName, values, selection, selectionArgs);
		return 0;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		if(isLogRun)ALog.Log("CPDemo_query");
		Cursor cursor = null;
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// ����SQLiteDatabase�����query�������в�ѯ������һ��Cursor���������ݿ��ѯ���صĽ��������
		// ��һ������String������
		// �ڶ�������String[]:Ҫ��ѯ������
		// ����������String����ѯ����
		// ���ĸ�����String[]����ѯ�����Ĳ���
		// ���������String:�Բ�ѯ�Ľ�����з���
		// ����������String���Է���Ľ����������
		// ���߸�����String���Բ�ѯ�Ľ����������
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
