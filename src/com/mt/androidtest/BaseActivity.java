package com.mt.androidtest;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.mt.androidtest.listview.ListViewAdapter;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.widget.ListAdapter;
import android.widget.ListView;

public class BaseActivity extends ListActivity implements AdapterView.OnItemClickListener, Handler.Callback, View.OnClickListener{
	private boolean isLogRun=false;
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
    private ArrayList<String>mActivitiesName=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isLogRun)ALog.Log("BaseActivity_onCreate");
		packageName = this.getPackageName();
		mLayoutInflater=LayoutInflater.from(this);
		metric  = getResources().getDisplayMetrics();
		mDensityDpi = metric.densityDpi;
		mBaseActivityWR=new WeakReference<BaseActivity>(this);
		getActivities(this);
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
	
	public boolean getLogRun(){
		return isLogRun;
	}
	
	/**
	 * getActivities：获取当前应用AndroidManifest.xml文件中所有<activity>节点信息
	 * @param mContext
	 */
	public void getActivities(Context mContext) {
		ActivityInfo[] activities=null;
		try {
			activities = mContext.getPackageManager().getPackageInfo(this.getPackageName(),PackageManager.GET_ACTIVITIES).activities;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}
		if(null==activities)finish();
		mActivitiesName = new ArrayList<String>();
		ActivityInfo mActivityInfo=null;
	      for (int i=0;i<activities.length;i++) {
	    	  mActivityInfo=activities[i];
	    	  if(null!=mActivityInfo){
	    		  //ALog.Log(""+mActivityInfo.name);
	    		  mActivitiesName.add(mActivityInfo.name);
	    	  }
	      }
	  }
	
	public String getActivityName(String str){
		if(null==str||null==mActivitiesName)return null;
		for(String mStr : mActivitiesName){
			if(mStr.endsWith(str)){
				return mStr;
			}
		}
		return null;
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
		setListViewHeightBasedOnChildren(mListViewFT);
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
		if(null==(className = getActivityName(selectedItem)))return;
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
		setListViewHeightBasedOnChildren(getListView());
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
	
	/**
	 * 在有ScrollView存在的时候，ListView显示全部内容而不是收缩。
	 * 说明：默认情况下Android是禁止在ScrollView中放入另外的ScrollView的，它的高度是无法计算的。
	 * 解决思路：在设置完ListView的Adapter后，根据ListView的子项目重新计算ListView的高度，然后把高度再作为LayoutParams设置给ListView，这样它的高度就正确了
	 * 注意事项：
	 * 1)子ListView的每个Item必须是LinearLayout，不能是其他的，因为其他的Layout(如RelativeLayout)没有重写onMeasure()，所以会在onMeasure()时抛出异常。
	 * 2)在ScrollView中嵌套ListView(或者ScrollView)的另外一个问题就是，子ScrollView中无法滑动的(如果它没有显示完全的话)，因为滑动事件会被父ScrollView吃掉，如果想要让子ScrollView也可以滑动，只能强行截取滑动事件
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
