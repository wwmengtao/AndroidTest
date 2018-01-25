package com.mt.androidtest.showview;

import android.os.Bundle;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class UIActivity extends BaseActivity {
	private String [] mActivitiesName={
			"BitmapFactoryActivity","BitmapUILActivity","FragmentTestActivity","FragmentTest2Activity",
			"ListViewTestActivity","MySelfViewActivity",
			"MyPreferenceActivity","MeasureLayoutDrawActivity",
			"ResourceActivity","InflateActivity","ShowViewActivity"};		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui);
		super.initListActivityData(mActivitiesName);
	}
	
	
}
