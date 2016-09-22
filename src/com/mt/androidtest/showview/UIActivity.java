package com.mt.androidtest.showview;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.R.layout;

import android.os.Bundle;

public class UIActivity extends BaseActivity {
	private String [] mActivitiesName={
			"ListViewTestActivity","MySelfViewActivity","MyPreferenceActivity","MeasureLayoutDrawActivity",
			"ResourceActivity","InflateActivity","ShowViewActivity","SwitcherDemoActivity","SysAppsActivity"};		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui);
		super.initListActivityData(mActivitiesName);
	}
	
	
}
