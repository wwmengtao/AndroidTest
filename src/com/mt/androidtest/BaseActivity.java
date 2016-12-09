package com.mt.androidtest;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.mt.androidtest.listview.ListViewAdapter;
/**
 * BaseListActivity：包含两个ListView，ListView的Adapter中数据须由子类调用super.initListFTData以及super.initListActivityData赋予
 * 其中initListFTData对应的是函数名称，initListActivityData对应的是Activity名称
 * @author Mengtao1
 *
 */
public abstract class BaseActivity extends Activity implements AdapterView.OnItemClickListener, Handler.Callback, View.OnClickListener{
	private boolean isLogRun = true;
	//
	private LinearLayout mLinearlayout_listview_activity=null;
	private LinearLayout mLinearlayout_listview_functions=null;
	private ListView mListViewAC=null;
	private ListViewAdapter mListViewAdapterAC = null;	
	public final String TAG_LIST_VIEW_AC = "listview_ac";
	private ListView mListViewFT=null;
	private ListViewAdapter mListViewAdapterFT = null;	
	//
    private static Handler mHandler=null;
	private Intent mIntent = null;
	private String className = null;		    
	private String activitySelectedItem=null;
	private LayoutInflater mLayoutInflater = null;
    private DisplayMetrics metric=null;
    private int mDensityDpi = -1;
    private AssetManager mAssetManager=null;
    private int maxMemory=-1;
    private static WeakReference<BaseActivity>mBaseListActivityWR=null;
    private ArrayList<String>mActivitiesName=null;
    private int AndroidVersion=-1;
    //
	private static final int REQUEST_PERMISSION_CODE = 0x001;
    protected String []permissionsRequiredBase = null;
    //
    private ScrollView mRootScrollView = null;
    private SharedPreferences mSharedPreferences = null;
    private SharedPreferences.Editor editor = null;
    private String preferenceFileName = "androidtest_";
    private String preferenceXScrollView="scrollviewposition_XScrollView";
    private String preferenceYScrollView="scrollviewposition_YScrollView";
    private int xScrollView = 0;
    private int yScrollView = 0;
    //
    private static final int SWITCH_MARGIN_RIGHT =26;
    /**
     * 有关生命周期：
     * 如果执行adb shell am start -a "com.android.phone.EmergencyDialer.DIAL"调出拨号盘界面挡住当前Activity
     * 情况一、不拨打电话，仅仅让拨号盘界面盖住当前Activity
     * 那么Activity执行onPause->onSaveInstanceState->onStop
     * 此时按下电源键关闭屏幕，Activity经历onRestart->onStart->onResume->onPause->onSaveInstanceState->onStop
     * 再次按下电源键开启屏幕，Activity经历onRestart->onStart->onResume
     * 情况二、通过拨号盘拨打紧急号码(比如：112)进入通话界面，在通话界面执行下列操作
     * 按下电源键关闭屏幕，Activity不经历任何生命周期
     * 再次按下电源键开启屏幕，Activity经历onRestart->onStart
     * 重复上述两个步骤，等待通话结束重新显示Activity界面，此时Activity经历onResume。
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		AndroidVersion =Build.VERSION.SDK_INT;
		requestPermissions(permissionsRequiredBase);
		super.onCreate(savedInstanceState);
		preferenceFileName += ALog.getActivityName(this);
		if(isLogRun)ALog.Log("onCreate",this);
		mBaseListActivityWR=new WeakReference<BaseActivity>(this);
		getActivities(this);
		mSharedPreferences	= this.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
		xScrollView = mSharedPreferences.getInt(preferenceXScrollView, 0);
		yScrollView = mSharedPreferences.getInt(preferenceYScrollView, 0);
    	editor = mSharedPreferences.edit();
    	//增加ActionBar
    	initActionBar();
	}
	
	public void initActionBar(){
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                    ActionBar.DISPLAY_SHOW_CUSTOM);
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_VERTICAL | Gravity.END);
            layout.setMarginEnd(SWITCH_MARGIN_RIGHT);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }	
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case android.R.id.home:
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onRestart(){
		super.onRestart();
		if(isLogRun)ALog.Log("onRestart",this);		
	}	
	
    @Override
    protected void onStart() {
        super.onStart();
		if(isLogRun)ALog.Log("onStart",this);		
    }
	
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    int IntTest = savedInstanceState.getInt("IntTest");
	    String StrTest = savedInstanceState.getString("StrTest");
	    if(isLogRun){
	    	ALog.Log("onRestoreInstanceState",this);	
	    	ALog.Log("IntTest:"+IntTest+" StrTest:"+StrTest);
	    }
	    
	}
    
	@Override
	public void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("onResume",this);
		//以下获取RootScrollView的当前滚动位置
		if(null==mRootScrollView){
			mRootScrollView = (ScrollView)findViewById(R.id.rootScrollView);
		}
		if(null!=mRootScrollView){
			mRootScrollView.post(runnableScrollView);
		}
	}	
	
	private Runnable runnableScrollView = new Runnable(){
		public void run() {
			mRootScrollView.smoothScrollTo(xScrollView, yScrollView);//直接在onResume中执行会不成功
		}
	};
	
    @Override
    public void onBackPressed(){
    	super.onBackPressed();
    	if(isLogRun)ALog.Log("onBackPressed",this);
    }
	
	@Override
	public void onPause(){
		if(null!=mHandler){
			mHandler.removeCallbacksAndMessages(null);//避免内存泄露
		}
		super.onPause();
		if(isLogRun)ALog.Log("onPause",this);
		if(null!=mRootScrollView){
			xScrollView = mRootScrollView.getScrollX();
			yScrollView = mRootScrollView.getScrollY();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt("IntTest", 18);
		savedInstanceState.putString("StrTest", "onSaveInstanceState_StrTest");
		super.onSaveInstanceState(savedInstanceState);
		if(isLogRun)ALog.Log("onSaveInstanceState",this);
	}
	
    @Override
    protected void onStop() {
        super.onStop();
		if(isLogRun)ALog.Log("onStop",this);		
    }
	
	@Override
	public void onDestroy() {
    	editor.putInt(preferenceXScrollView, xScrollView);
    	editor.putInt(preferenceYScrollView, yScrollView);
    	editor.commit();
		super.onDestroy();
		if(isLogRun)ALog.Log("onDestroy",this);
	}	
	
	
	@Override
    public void onContentChanged() {
		super.onContentChanged();
		if(isLogRun)ALog.Log("onContentChanged",this);
    }
	
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(isLogRun)ALog.Log("onWindowFocusChanged",this);
    }
    
    public void onAttachedToWindow() {
    	super.onAttachedToWindow();
    	if(isLogRun)ALog.Log("onAttachedToWindow",this);
    }

    public void onDetachedFromWindow() {
    	super.onDetachedFromWindow();
    	if(isLogRun)ALog.Log("onDetachedFromWindow",this);
    }
	
	public int getAndroidVersion(){
		return AndroidVersion;
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
		if(null==metric){
			metric = getResources().getDisplayMetrics();
		}
		return metric;
	}
	
	public int getDensityDpi(){
		if(-1==mDensityDpi){
			mDensityDpi = getResources().getDisplayMetrics().densityDpi;
		}
		return mDensityDpi;
	}
	
	public LayoutInflater getLayoutInflater(){
		if(null==mLayoutInflater){
			mLayoutInflater = LayoutInflater.from(this);
		}
		return mLayoutInflater;
	}
	
	public AssetManager getAssetManager(){
		if(null == mAssetManager){
			mAssetManager=getResources().getAssets();
		}
		return mAssetManager;
	}
	
	public int getMaxMemory(){
		if(-1==maxMemory){
			maxMemory = (int) (Runtime.getRuntime().maxMemory());//最大内存，单位bytes  
			ALog.Log("Max memory is " + maxMemory + "KB");  
		}
		return maxMemory;
	}
	
	public static Handler getHandler(){
        if (mHandler == null) {
        	BaseActivity mBaseListActivity=mBaseListActivityWR.get();
        	if(null!=mBaseListActivity){
        		mHandler = new Handler(mBaseListActivity);
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
		mListViewFT.setAdapter(mListViewAdapterFT);
		mListViewFT.setOnItemClickListener(this);	
		setListViewHeightBasedOnChildren(mListViewFT);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}
	
	public void initListActivityData(String [] mActivitiesName){
		if(null==mActivitiesName){
			mLinearlayout_listview_activity=(LinearLayout)findViewById(R.id.linearlayout_listview_activity);
			mLinearlayout_listview_activity.setVisibility(View.GONE);
			return;
		}
		mListViewAdapterAC = new ListViewAdapter(this);
		mListViewAdapterAC.setMode(2);
		mListViewAdapterAC.setupList(mActivitiesName);
		mListViewAC=(ListView)findViewById(R.id.listview_activity);	
		mListViewAC.setOnItemClickListener(mOnItemClickListenerAC);		
		mListViewAC.setAdapter(mListViewAdapterAC);
		mListViewAC.setTag(R.id.listview_activity, TAG_LIST_VIEW_AC);
		setListViewHeightBasedOnChildren(mListViewAC);
	}	
	
	private AdapterView.OnItemClickListener mOnItemClickListenerAC = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//以下操作为打开本应用内部的Activity
			mIntent = new Intent();
			HashMap<String, Object> mItem = mListViewAdapterAC.mList.get(position);
			if(null==mItem)return;
			activitySelectedItem = (String) mItem.get(ListViewAdapter.TAG_ITEM_TEXT);
			if(null==(className = getActivityName(activitySelectedItem)))return;
			mIntent.setComponent(new ComponentName(getPackageName(), className));
			try{
				startActivity(mIntent);
			}catch(ActivityNotFoundException e){
				e.printStackTrace();
			}
		}
	};

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
	
	boolean logListViewHeight = false;
	/**
	 * setListViewHeightBasedOnChildren函数功能：在有ScrollView存在的时候，ListView显示全部内容而不是收缩。
	 * 说明：默认情况下Android是禁止在ScrollView中放入另外的ScrollView的，它的高度是无法计算的。
	 * 解决思路：在设置完ListView的Adapter后，根据ListView的子项目重新计算ListView的高度，然后把高度再作为LayoutParams设置给ListView，这样它的高度就正确了
	 * 实验信息：通过往frameworks的View.java中加log可知：
	 * 1)measure执行layout未执行：View.java中的mLeft、mRight、mTop以及mBottom均为0，即getHeight和getWidth均为0。
	 * getMeasuredHeight以及getMeasuredWidth非零(其他场景未必，仅仅针对这个场景)
	 * 调用步骤：measure->onMeasure->setMeasuredDimension->setMeasuredDimensionRaw，而setMeasuredDimensionRaw内容如下：
	 * mMeasuredWidth = measuredWidth;
	 * mMeasuredHeight = measuredHeight;
	 * 最终我们调用函数getMeasuredxx返回的内容是：
	 * mMeasuredWidth & MEASURED_SIZE_MASK;
	 * mMeasuredHeight & MEASURED_SIZE_MASK;
	 * 2)measure执行后layout执行：此时mRight和mBottom不为0。
	 * layout->setFrame，其中setFrame有如下片段：
     * mLeft = left;
     * mTop = top;
     * mRight = right;
     * mBottom = bottom;
     * 最终我们调用函数：
     * getWidth：返回mRight - mLeft;
     * getHeight：返回mBottom - mTop;
	*/

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		if(logListViewHeight)ALog.Log("@@@@setListViewHeightBasedOnChildren1");
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if(0==i && logListViewHeight)ALog.Log("@@@@measure_getHeight:"+listItem.getHeight());
			if(0==i && logListViewHeight)ALog.Log("@@@@measure_getMeasuredHeight:"+listItem.getMeasuredHeight());
			/**
			 * 仅适用于本场景：getMeasuredxx要在measure后才有值，getxx要在layout后才有值。
			 */
			/**
			 * 1、measure执行完毕后，frameworks中View.java中各变量如下：
			 * mLeft:0 mRight:0 mTop:0 mBottom:0
			 * getHeight:0 getMeasuredHeight:133
			 */
			listItem.measure(0, 0);
			if(0==i && logListViewHeight)ALog.Log("@@@@measure_getHeight:"+listItem.getHeight());
			if(0==i && logListViewHeight)ALog.Log("@@@@measure_getMeasuredHeight:"+listItem.getMeasuredHeight());
			/**
			 * 2、layout执行完毕后，frameworks中View.java中各变量如下：
			 * mLeft:0 mRight:343 mTop:0 mBottom:133
			 * getHeight:133 getMeasuredHeight:133
			 */
			listItem.layout(0, 0, listItem.getMeasuredWidth(), listItem.getMeasuredHeight());
			if(0==i && logListViewHeight)ALog.Log("@@@@layout_getHeight:"+listItem.getHeight());
			if(0==i && logListViewHeight)ALog.Log("@@@@layout_getMeasuredHeight:"+listItem.getMeasuredHeight());
			//以下将所有ListView中每个item view高度累加起来
			totalHeight += listItem.getMeasuredHeight();
		}
		if(logListViewHeight)ALog.Log("@@@@setListViewHeightBasedOnChildren2");
		logListViewHeight = false;
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	//以下申请权限
	public void requestPermissions(String [] permissionsRequired){
		if(AndroidVersion<=22)return;
		if(null!=permissionsRequired && permissionsRequired.length>0){
			this.requestPermissions(permissionsRequired,REQUEST_PERMISSION_CODE);
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
		switch (requestCode){
			case REQUEST_PERMISSION_CODE:
				if (permissions.length != 0 && isAllGranted(grantResults)){
					Toast.makeText(this, "Get all Permissions!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, "Not get all Permissions!", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
	            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	            break;
			}
	  }
	
	public boolean isAllGranted(int[] grantResults){
		if(null==grantResults)return false;
		for(int i=0;i<grantResults.length;i++){
			if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 有关View.toString()结果分类：
	 * 1)contentParent：android.widget.FrameLayout{31be20d V.E...... ......ID 0,240-1080,1776 #1020002 android:id/content}
	 * 2)inflate出来的View：android.widget.LinearLayout{31be20d V.E...... ......ID 0,0-0,0}
	 * 3)采用@+id方式自定义的View：android.widget.LinearLayout{a08e3cd V.E...C.. ......ID 0,0-0,0 #7f070042 app:id/switch_bar}
	 */
	private static String mReg = "[a-zA-Z]+[:]id\\/[a-zA-Z]+.+\\}";//匹配类似于android:id/content}的内容
	private static String mReg2 = "([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+";//匹配类似于android.widget.LinearLayout的内容
	private static String mReg3="[{].+[}]";//匹配类似于{31be20d V.E...... ......ID 0,0-0,0}的内容
	private static Pattern mPattern = null;
	private static Matcher mMatcher = null;
	//
	//
	private boolean showWidthAndHeightShown = false;
    protected void showWidthAndHeight(View mView, String objName){
    	if(null==mView)return;
    	if(!showWidthAndHeightShown){
    		String betweenTitle=" ";
    		if(isLogRun)ALog.Log("getWidth"+betweenTitle+"getMeasuredWidth"+betweenTitle+"getHeight"+betweenTitle
    				+"getMeasuredHeight"+betweenTitle+"ViewDescription");
    		showWidthAndHeightShown = true;
    	}
        String str = mView.toString();    	
    	//
    	String strViewDes1=null;
    	mPattern = Pattern.compile(mReg);
        mMatcher = mPattern.matcher(str);
        while(mMatcher.find()){
        	strViewDes1 = mMatcher.group().replace("}", "");
            break;
        }
    	String strViewDes2=null;
    	mPattern = Pattern.compile(mReg2);
    	mMatcher = mPattern.matcher(str);    	
        while(mMatcher.find()){
        	strViewDes2 = mMatcher.group();
            break;
        }
    	String strViewDesTotal=null;
    	if(null==strViewDes1&&null==strViewDes2){
    		strViewDesTotal=objName;
    	}else if(null==strViewDes1){
    		strViewDesTotal = objName+"{"+strViewDes2+"}";
    	}else if(null==strViewDes2){
    		strViewDesTotal = objName+"{"+strViewDes1+"}";
    	}else{
    		strViewDesTotal = objName+"{"+strViewDes1+"$"+strViewDes2+"}";
    	}
        //
        String format="%-14d";
        String format2="%-12d";
        String strgetWidth = String.format(format,mView.getWidth());
        String strgetMeasuredWidth = String.format(format,mView.getMeasuredWidth());
        String strgetHeight = String.format(format,mView.getHeight());
        String strgetMeasuredHeight = String.format(format2,mView.getMeasuredHeight());
        if(isLogRun)ALog.Log(
        		strgetWidth+
        		strgetMeasuredWidth+
        		strgetHeight+
        		strgetMeasuredHeight+
        		strViewDesTotal);
    }

}
