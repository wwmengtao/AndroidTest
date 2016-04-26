package com.mt.androidtest;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;
public class SwitcherDemoActivity extends Activity   implements Handler.Callback{
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
    private static final int DIALOG_MESSAGE = 1;
    private static final int DIALOG_DISMISS_MESSAGE = 2;
    private static final int UPDATE_MESSAGE = 3;
    private static final int RESCAN_INTERVAL_MS = 1000;
    private Handler mUpdater=null;
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
        if (mUpdater == null) {
            mUpdater = new Handler(this);
        }
        initData(false);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
        if (mUpdater != null) {
            mUpdater.removeCallbacksAndMessages(null);
        }
	}
	
	/**
	 * initData：如果initData比较耗时，那么调用者不会等到其执行完毕才往下执行。
	 */
    private void initData(final boolean isUpdateHandler){
      	 new Thread(){
      		public void run() {
      			if(!isUpdateHandler){
	      	  		Message msg = mAnimationHandler.obtainMessage(DIALOG_MESSAGE);
	      			mAnimationHandler.sendMessage(msg);
      			}
				loadSwitchersResource();
      			if(!isUpdateHandler){
					Message msgNew = mAnimationHandler.obtainMessage(DIALOG_DISMISS_MESSAGE);
					mAnimationHandler.sendMessage(msgNew);
      			}
				if(isUpdateHandler){
					mUpdater.sendEmptyMessageDelayed(UPDATE_MESSAGE, RESCAN_INTERVAL_MS);//每隔一秒钟发送消息
				}
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
				mUpdater.sendEmptyMessage(UPDATE_MESSAGE);
			break;		
			}
		}
 	};
 
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		mUpdater.removeMessages(UPDATE_MESSAGE);//如果更新操作耗时的话会导致有几次的信息没有处理，造成消息堆积
		mListViewAdapter.setupList(mSwitchersList);
		mListViewAdapter.notifyDataSetChanged();
		initData(true);
        return true;
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

}
