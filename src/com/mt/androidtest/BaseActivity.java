package com.mt.androidtest;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class BaseActivity extends ListActivity implements AdapterView.OnItemClickListener, Handler.Callback, View.OnClickListener{
	boolean isLogRun=true;
	private LinearLayout mLinearlayout_listview_android=null;
	private LinearLayout mLinearlayout_listview_functions=null;
	private ListView mListViewFT=null;
	private ListViewAdapter mListViewAdapterFT = null;	
    private Handler mHandler=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ALog.Log("BaseActivity_onCreate");
	}
	
	@Override
	public void onResume(){
		super.onResume();
        if (mHandler == null) {
        	mHandler = new Handler(this);
        }
	}	
	
	@Override
	public void onPause(){
		if(null!=mHandler){
			mHandler.removeCallbacksAndMessages(0);
		}		
		super.onPause();
	}
	
	public Handler getHandler(){
		return mHandler;
	}
	
	public ListViewAdapter getListViewAdapterFT(){
		return mListViewAdapterFT;
	}
	
	public void initListFTData(String [] mMethodNameFT){
		if(null==mMethodNameFT){
			mLinearlayout_listview_functions=(LinearLayout)findViewById(R.id.linearlayout_listview_functions);
			mLinearlayout_listview_functions.setVisibility(View.GONE);
			return;
		}
		mListViewAdapterFT = new ListViewAdapter(this);
		mListViewAdapterFT.setMode(2);
		mListViewAdapterFT.setupList(mMethodNameFT);
		mListViewFT=(ListView)findViewById(R.id.listview_functions);	
		mListViewFT.setOnItemClickListener(this);		
		mListViewFT.setAdapter(mListViewAdapterFT);
		ListViewAdapter.setListViewHeightBasedOnChildren(mListViewFT);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
	}	
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
	}
	
	public void initListActivityData(String [] mActivitiesName){
		if(null==mActivitiesName){
			mLinearlayout_listview_android=(LinearLayout)findViewById(R.id.linearlayout_listview_android);
			mLinearlayout_listview_android.setVisibility(View.GONE);
			return;
		}
		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.item_getview_android, R.id.listText, mActivitiesName);
        setListAdapter(myAdapter);
	}	

	@Override
	public boolean handleMessage(Message msg) {
		return true;
	}
	
	@Override
	public void onClick(View v) {
		
	}
}