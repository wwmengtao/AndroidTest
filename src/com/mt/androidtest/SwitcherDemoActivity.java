package com.mt.androidtest;

import java.util.ArrayList;
import java.util.HashMap;

import com.mt.androidtest.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;

public class SwitcherDemoActivity extends Activity {
	GridView mGridView = null;
	ListViewAdapter mListViewAdapter = null;
	ProgressDialog mProgressDialog = null;
	private ArrayList<HashMap<String, Object>> mSwitchersList = new ArrayList<HashMap<String, Object>>();
	private static int[] switchIMAGE = { R.drawable.switch_brightness,
		R.drawable.switch_wifi, R.drawable.switch_rotation,
		R.drawable.switch_bluetooth, R.drawable.back,
		R.drawable.switch_profile, R.drawable.switch_torchlight,
		R.drawable.switch_mobile, R.drawable.switch_gps };

private static int[] switchTXT = { R.string.switch_brightness,
		R.string.switch_wifi, R.string.switch_rotation,
		R.string.switch_bluetooth, R.string.switch_empty,
		R.string.switch_profile, R.string.widetouch_torchlight,
		R.string.switch_mobile, R.string.switch_gps };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_switcher_demo);
		mProgressDialog = new ProgressDialog(this);
		mGridView=(GridView)findViewById(R.id.gridview_sysapp);
		mListViewAdapter = new ListViewAdapter(this);
	}
	@Override
	protected void onResume(){	
		super.onResume();
		initData();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	public void loadSwitchersResource(){
		
	}
	
    private void initData(){
      	 new Thread(){
      		public void run() {
	      		Message msg = mAnimationHandler.obtainMessage(1);
				mAnimationHandler.sendMessage(msg);
				loadSwitchersResource();
				msg = mAnimationHandler.obtainMessage(2);
				mAnimationHandler.sendMessage(msg);
      		}
         }.start();
	}
	
 	Handler mAnimationHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mProgressDialog.setMessage(getString(R.string.msg_loading));
				mProgressDialog.setCancelable(false);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.show();
			break;	
			case 2:
				mProgressDialog.dismiss();
				mListViewAdapter.setMode(1);
				mListViewAdapter.setupList(mSwitchersList);
				mGridView.setNumColumns(4);
				mGridView.setAdapter(mListViewAdapter);
			break;
			}
		}
 	};
}
