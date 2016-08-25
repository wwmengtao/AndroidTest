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
	
	private String regShowWidthAndHeight = "id\\/[a-zA-Z]+.+\\}";//仅仅获取控件id，其他内容不要
	private Pattern mPatternShowWidthAndHeight = Pattern.compile(regShowWidthAndHeight);
	private Matcher mMatcher = null;
	private boolean is_onWindowFocusChanged = false;
    protected void showWidthAndHeight(View mView, String objName){
    	if(null==mView)return;
    	if(!is_onWindowFocusChanged){
    		String betweenTitle=" ";
    		if(isLogRun)ALog.Log("getWidth"+betweenTitle+"getMeasuredWidth"+betweenTitle+"getHeight"+betweenTitle+"getMeasuredHeight");
    		is_onWindowFocusChanged = true;
    	}
    	String str_ALog=null;
        String str = mView.toString();
        mMatcher = mPatternShowWidthAndHeight.matcher(str);
        while(mMatcher.find()){
        	str_ALog = mMatcher.group().replace("}", "");
            break;
        }
        String format="%-14d";
        String strgetWidth = String.format(format,mView.getWidth());
        String strgetMeasuredWidth = String.format(format,mView.getMeasuredWidth());
        String strgetHeight = String.format(format,mView.getHeight());
        String strgetMeasuredHeight = String.format(format,mView.getMeasuredHeight());
        if(isLogRun)ALog.Log(strgetWidth+
        				 strgetMeasuredWidth+
        				 strgetHeight+
        				 strgetMeasuredHeight+
        				 str_ALog+":"+objName);
    }		
}
