package com.mt.androidtest;

import java.lang.ref.WeakReference;

import com.mt.androidtest.listview.ListViewAdapter;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private static Handler mHandler=null;
	private Intent mIntent = null;
	private String packageName = null;
	private String className = null;		    
	private String selectedItem=null;
	private LayoutInflater mLayoutInflater = null;
    private DisplayMetrics metric=null;
    private int mDensityDpi = 0;
    private static WeakReference<BaseActivity>mBaseActivityWR=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ALog.Log("BaseActivity_onCreate");
		packageName = this.getPackageName();
		mLayoutInflater=LayoutInflater.from(this);
		metric  = getResources().getDisplayMetrics();
		mDensityDpi = metric.densityDpi;
		mBaseActivityWR=new WeakReference<BaseActivity>(this);
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}	
	
	@Override
	public void onPause(){
		if(null!=mHandler){
			mHandler.removeCallbacksAndMessages(null);//避免内存泄露
		}
		super.onPause();
	}
	
	public DisplayMetrics getDisplayMetrics(){
		return metric;
	}
	
	public int getDensityDpi(){
		return mDensityDpi;
	}
	
	public LayoutInflater getLayoutInflater(){
		return mLayoutInflater;
	}
	
	public static Handler getHandler(){
        if (mHandler == null) {
        	BaseActivity mBaseActivity=mBaseActivityWR.get();
        	if(null!=mBaseActivity){
        		mHandler = new Handler(mBaseActivity);
        	}
        }
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
		//以下操作为打开本应用内部的Activity
		mIntent = new Intent();
		selectedItem = (String) list.getItemAtPosition(position);
		className = packageName+"."+selectedItem;
		mIntent.setComponent(new ComponentName(packageName, className));
		try{
			startActivity(mIntent);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
		}
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
	
	public void setLayoutParams(View mView,double paraWidth,double paraHeight){
		if(null==mView)return;
		ViewGroup.LayoutParams lp = mView.getLayoutParams();
    	lp.width= (int)(mDensityDpi*paraWidth);
    	lp.height = (int)(mDensityDpi*paraHeight);
    	mView.setLayoutParams(lp);
	}
}
