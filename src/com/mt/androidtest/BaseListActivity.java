package com.mt.androidtest;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.mt.androidtest.listview.ListViewAdapter;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
/**
 * BaseListActivity����������ListView��ListView��Adapter�����������������super.initListFTData�Լ�super.initListActivityData����
 * ����initListFTData��Ӧ���Ǻ������ƣ�initListActivityData��Ӧ����Activity����
 * @author Mengtao1
 *
 */
public class BaseListActivity extends ListActivity implements AdapterView.OnItemClickListener, Handler.Callback, View.OnClickListener{
	private boolean isLogRun = true;
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
    private static WeakReference<BaseListActivity>mBaseListActivityWR=null;
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
     * �й��������ڣ�
     * ���ִ��adb shell am start -a "com.android.phone.EmergencyDialer.DIAL"���������̽��浲ס��ǰActivity
     * ���һ��������绰�������ò����̽����ס��ǰActivity
     * ��ôActivityִ��onPause->onSaveInstanceState->onStop
     * ��ʱ���µ�Դ���ر���Ļ��Activity����onRestart->onStart->onResume->onPause->onSaveInstanceState->onStop
     * �ٴΰ��µ�Դ��������Ļ��Activity����onRestart->onStart->onResume
     * �������ͨ�������̲����������(���磺112)����ͨ�����棬��ͨ������ִ�����в���
     * ���µ�Դ���ر���Ļ��Activity�������κ���������
     * �ٴΰ��µ�Դ��������Ļ��Activity����onRestart->onStart
     * �ظ������������裬�ȴ�ͨ������������ʾActivity���棬��ʱActivity����onResume��
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		AndroidVersion =Build.VERSION.SDK_INT;
		requestPermissions(permissionsRequiredBase);
		super.onCreate(savedInstanceState);
		preferenceFileName += ALog.getActivityName(this);
		if(isLogRun)ALog.Log("onCreate",this);
		packageName = this.getPackageName();
		mLayoutInflater=LayoutInflater.from(this);
		metric  = getResources().getDisplayMetrics();
		mDensityDpi = metric.densityDpi;
		mBaseListActivityWR=new WeakReference<BaseListActivity>(this);
		getActivities(this);
		mSharedPreferences	= this.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
		xScrollView = mSharedPreferences.getInt(preferenceXScrollView, 0);
		yScrollView = mSharedPreferences.getInt(preferenceYScrollView, 0);
    	editor = mSharedPreferences.edit();
    	//����ActionBar
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
		//���»�ȡRootScrollView�ĵ�ǰ����λ��
		if(null==mRootScrollView){
			mRootScrollView = (ScrollView)findViewById(R.id.rootScrollView);
		}
		if(null!=mRootScrollView){
			mRootScrollView.post(runnableScrollView);
		}
	}	
	
	private Runnable runnableScrollView = new Runnable(){
		public void run() {
			mRootScrollView.smoothScrollTo(xScrollView, yScrollView);//ֱ����onResume��ִ�л᲻�ɹ�
		}
	};
	
	@Override
	public void onPause(){
		if(null!=mHandler){
			mHandler.removeCallbacksAndMessages(null);//�����ڴ�й¶
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
	
	public int getAndroidVersion(){
		return AndroidVersion;
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
        	BaseListActivity mBaseListActivity=mBaseListActivityWR.get();
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
	
	boolean logListViewHeight = false;
	/**
	 * setListViewHeightBasedOnChildren�������ܣ�����ScrollView���ڵ�ʱ��ListView��ʾȫ�����ݶ�����������
	 * ˵����Ĭ�������Android�ǽ�ֹ��ScrollView�з��������ScrollView�ģ����ĸ߶����޷�����ġ�
	 * ���˼·����������ListView��Adapter�󣬸���ListView������Ŀ���¼���ListView�ĸ߶ȣ�Ȼ��Ѹ߶�����ΪLayoutParams���ø�ListView���������ĸ߶Ⱦ���ȷ��
	 * ʵ����Ϣ��ͨ����frameworks��View.java�м�log��֪��
	 * 1)measureִ��layoutδִ�У�View.java�е�mLeft��mRight��mTop�Լ�mBottom��Ϊ0����getHeight��getWidth��Ϊ0��
	 * getMeasuredHeight�Լ�getMeasuredWidth����(��������δ�أ���������������)
	 * ���ò��裺measure->onMeasure->setMeasuredDimension->setMeasuredDimensionRaw����setMeasuredDimensionRaw�������£�
	 * mMeasuredWidth = measuredWidth;
	 * mMeasuredHeight = measuredHeight;
	 * �������ǵ��ú���getMeasuredxx���ص������ǣ�
	 * mMeasuredWidth & MEASURED_SIZE_MASK;
	 * mMeasuredHeight & MEASURED_SIZE_MASK;
	 * 2)measureִ�к�layoutִ�У���ʱmRight��mBottom��Ϊ0��
	 * layout->setFrame������setFrame������Ƭ�Σ�
     * mLeft = left;
     * mTop = top;
     * mRight = right;
     * mBottom = bottom;
     * �������ǵ��ú�����
     * getWidth������mRight - mLeft;
     * getHeight������mBottom - mTop;
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
			 * �������ڱ�������getMeasuredxxҪ��measure�����ֵ��getxxҪ��layout�����ֵ��
			 */
			/**
			 * 1��measureִ����Ϻ�frameworks��View.java�и��������£�
			 * mLeft:0 mRight:0 mTop:0 mBottom:0
			 * getHeight:0 getMeasuredHeight:133
			 */
			listItem.measure(0, 0);
			if(0==i && logListViewHeight)ALog.Log("@@@@measure_getHeight:"+listItem.getHeight());
			if(0==i && logListViewHeight)ALog.Log("@@@@measure_getMeasuredHeight:"+listItem.getMeasuredHeight());
			/**
			 * 2��layoutִ����Ϻ�frameworks��View.java�и��������£�
			 * mLeft:0 mRight:343 mTop:0 mBottom:133
			 * getHeight:133 getMeasuredHeight:133
			 */
			listItem.layout(0, 0, listItem.getMeasuredWidth(), listItem.getMeasuredHeight());
			if(0==i && logListViewHeight)ALog.Log("@@@@layout_getHeight:"+listItem.getHeight());
			if(0==i && logListViewHeight)ALog.Log("@@@@layout_getMeasuredHeight:"+listItem.getMeasuredHeight());
			//���½�����ListView��ÿ��item view�߶��ۼ�����
			totalHeight += listItem.getMeasuredHeight();
		}
		if(logListViewHeight)ALog.Log("@@@@setListViewHeightBasedOnChildren2");
		logListViewHeight = false;
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	//��������Ȩ��
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
}
