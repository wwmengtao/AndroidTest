package com.mt.androidtest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class BaseActivity extends Activity {
	private boolean isLogRun = true;
    private static final int SWITCH_MARGIN_RIGHT =26;
    private int mDensityDpi = 0;
    private DisplayMetrics metric=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		metric  = getResources().getDisplayMetrics();
		mDensityDpi = metric.densityDpi;
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
    public void onStart() {
        super.onStart();
		if(isLogRun)ALog.Log("onStart",this);		
    }
	
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    int IntTest = savedInstanceState.getInt("IntTest_BA");
	    String StrTest = savedInstanceState.getString("StrTest_BA");
	    if(isLogRun){
	    	ALog.Log("onRestoreInstanceState",this);	
	    	ALog.Log("IntTest_BA:"+IntTest+" StrTest_BA:"+StrTest);
	    }
	    
	}
    
	@Override
	public void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("onResume",this);
	}	
	
	@Override
	public void onPause(){
		super.onPause();
		if(isLogRun)ALog.Log("onPause",this);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt("IntTest_BA", 18);
		savedInstanceState.putString("StrTest_BA", "onSaveInstanceState_StrTest_BA");
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
		super.onDestroy();
		if(isLogRun)ALog.Log("onDestroy",this);
	}	
	
	public void setLayoutParams(View mView,double paraWidth,double paraHeight){
		if(null==mView)return;
		ViewGroup.LayoutParams lp = mView.getLayoutParams();
    	lp.width= (int)(mDensityDpi*paraWidth);
    	lp.height = (int)(mDensityDpi*paraHeight);
    	mView.setLayoutParams(lp);
	}
	/**
	 * 有关View.toString()结果分类：
	 * 1)contentParent：android.widget.FrameLayout{31be20d V.E...... ......ID 0,240-1080,1776 #1020002 android:id/content}
	 * 2)inflate出来的View：android.widget.LinearLayout{31be20d V.E...... ......ID 0,0-0,0}
	 * 3)采用@+id方式自定义的View：android.widget.LinearLayout{a08e3cd V.E...C.. ......ID 0,0-0,0 #7f070042 app:id/switch_bar}
	 */
	private static String mReg = "[a-zA-Z]+[:]id\\/[a-zA-Z]+.+\\}";//仅仅获取控件id，其他内容不要
	private static Pattern mPattern = Pattern.compile(mReg);
	private static Matcher mMatcher = null;
	//
	private static String mReg2 = "([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+";
	private static Pattern mPattern2 = Pattern.compile(mReg2);
	private static Matcher mMatcher2 = null;
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
        mMatcher = mPattern.matcher(str);
        while(mMatcher.find()){
        	strViewDes1 = mMatcher.group().replace("}", "");
            break;
        }
    	String strViewDes2=null;
        mMatcher2 = mPattern2.matcher(str);    	
        while(mMatcher2.find()){
        	strViewDes2 = mMatcher2.group();
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
