package com.mt.androidtest.data;

import java.io.File;
import java.io.FileNotFoundException;

import com.mt.androidtest.ALog;
import com.mt.androidtest.storage.StorageHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class ContentProviderDemo extends ContentProvider {
    private static StorageHelper mStorageHelper=null;
	@Override
	public boolean onCreate() {
		ALog.Log("ContentProviderDemo_onCreate");
		mStorageHelper=new StorageHelper(getContext());
		createSharedData();
		return true;
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

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {
		File file = new File(getContext().getFilesDir(), uri.getPath());
		if (file.exists()) {
			return ParcelFileDescriptor.open(file,
					ParcelFileDescriptor.MODE_READ_ONLY);
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
	
	public void createSharedData(){
		File mFile= new File(getContext().getFilesDir(),"myAssets_FilesDir");
		mStorageHelper.copyFilesInAssets("",mFile.getAbsolutePath());
		ALog.Log("ContentProvider create files::"+mFile.getAbsolutePath());
	}
}
