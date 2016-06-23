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
			mHandler.removeCallbacksAndMessages(null);//�����ڴ�й¶
		}
		super.onPause();
	}
	
	public boolean getLogRun(){
		return isLogRun;
	}
	
	/**
	 * getActivities����ȡ��ǰӦ��AndroidManifest.xml�ļ�������<activity>�ڵ���Ϣ
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
		//���²���Ϊ�򿪱�Ӧ���ڲ���Activity
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
	 * ����ScrollView���ڵ�ʱ��ListView��ʾȫ�����ݶ�����������
	 * ˵����Ĭ�������Android�ǽ�ֹ��ScrollView�з��������ScrollView�ģ����ĸ߶����޷�����ġ�
	 * ���˼·����������ListView��Adapter�󣬸���ListView������Ŀ���¼���ListView�ĸ߶ȣ�Ȼ��Ѹ߶�����ΪLayoutParams���ø�ListView���������ĸ߶Ⱦ���ȷ��
	 * ע�����
	 * 1)��ListView��ÿ��Item������LinearLayout�������������ģ���Ϊ������Layout(��RelativeLayout)û����дonMeasure()�����Ի���onMeasure()ʱ�׳��쳣��
	 * 2)��ScrollView��Ƕ��ListView(����ScrollView)������һ��������ǣ���ScrollView���޷�������(�����û����ʾ��ȫ�Ļ�)����Ϊ�����¼��ᱻ��ScrollView�Ե��������Ҫ����ScrollViewҲ���Ի�����ֻ��ǿ�н�ȡ�����¼�
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
