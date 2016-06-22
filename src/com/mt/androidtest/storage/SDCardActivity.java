package com.mt.androidtest.storage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class SDCardActivity extends BaseActivity {
	private String [] mMethodNameFT={"isSDCardMounted","getSDCardBaseDir"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
		case "isSDCardMounted":
			ALog.Log("isSDCardMounted:"+SDCardHelper.isSDCardMounted());
			break;
		case "getSDCardBaseDir":
			ALog.Log("getSDCardBaseDir:"+SDCardHelper.getSDCardBaseDir());
			break;			
		}
	}
	
}
