package com.mt.androidtest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IPowerManager;
import android.os.Message;
import android.provider.Settings;
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
	
	SwitchersInfo mSwitchersInfo=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_switcher_demo);
		mProgressDialog = new ProgressDialog(this);
		mGridView=(GridView)findViewById(R.id.gridview_sysapp);
		mListViewAdapter = new ListViewAdapter(this);
		mSwitchersInfo = new SwitchersInfo(this);
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
		mSwitchersList.clear();
		for (int i = 0; i < switchIMAGE.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			switch (switchIMAGE[i]) {
			case R.drawable.switch_bluetooth:
				map = mSwitchersInfo.getBluetoothSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_torchlight:
				map = mSwitchersInfo.getTorchLightSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_gps:
				map = mSwitchersInfo.getGpsSwitcher();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_mobile:
				map = mSwitchersInfo.getMobileSwitcher();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_profile:
				map = mSwitchersInfo.getProfileSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_rotation:
				map = mSwitchersInfo.getRotationSwitch();
				break;
			case R.drawable.switch_wifi:
				map = mSwitchersInfo.getWifiSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_brightness:
				map = mSwitchersInfo.getBrightnessSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.back:
				map.put("itemImage", R.drawable.back);
				break;
			default:
				break;
			}
			mSwitchersList.add(map);
		}
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
				mGridView.setNumColumns(3);
				mGridView.setAdapter(mListViewAdapter);
				mGridView.setBackgroundColor(getResources().getColor(R.color.lawngreen));
			break;
			}
		}
 	};
 
}
