package com.mt.androidtest.storage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class IntStorageActivity extends BaseActivity {
	private String [] mMethodNameFT={"listDirs","writeToFile","readFromFile"};
	private IntStorageHelper mIntStorageHelper=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
		mIntStorageHelper=new IntStorageHelper(this.getApplicationContext());
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "listDirs":
				mIntStorageHelper.listDirs();
				break;		
			case "writeToFile":
				//writeToFile("test.txt","hello\nxixi\nhaha",0);
				mIntStorageHelper.writeToFile("test.txt","hello\nxixi\nhaha",10);
				break;		
			case "readFromFile":
				mIntStorageHelper.readFromFile("test.txt",10);
				break;	
		}
	}
}
