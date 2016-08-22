package com.mt.androidtest;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	private boolean isLogRun = true;
    private static final int SWITCH_MARGIN_RIGHT =26;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	public void onDestroy() {
		super.onDestroy();
		if(isLogRun)ALog.Log("onDestroy",this);
	}	
}
