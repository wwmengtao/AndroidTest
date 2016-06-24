package com.mt.androidtest.data;

import java.io.IOException;
import java.io.InputStream;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
public class ContentResolverDemoActivity extends BaseActivity {
	private static final String CONTENT_URI = "content://com.mt.androidtest.cpdemo/";
	private static Uri mUri=null;
	private String [] mMethodNameFT={
			"fileExist"};
	ContentResolver mContentResolver=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resolver);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
		mContentResolver = getContentResolver();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "fileExist":
				ALog.Log("fileExist:"+fileExist());
				break;
		}
	}
	
	private boolean fileExist() {

		InputStream inputStream = null;
		/**
		 * "content://com.mt.androidtest.cpdemo/myAssets_FilesDir/test/test.txt"说明文件存储路径为：
		 /data/data/包名/files/myAssets_FilesDir/test/test.txt
		 */
		mUri = Uri.parse(CONTENT_URI+"myAssets_FilesDir/test/test.txt");
		try {
			inputStream = mContentResolver.openInputStream(mUri);
			if (inputStream != null) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
