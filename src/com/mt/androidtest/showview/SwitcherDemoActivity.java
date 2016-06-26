package com.mt.androidtest.showview;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;
import android.widget.ListView;
import com.mt.androidtest.R;
import com.mt.androidtest.listview.ListViewAdapter;

public class SwitcherDemoActivity extends Activity implements Handler.Callback{
	GridView mGridView = null;
	ListView mListView = null;
	ListViewAdapter mListViewAdapter = null;
	ListViewAdapter mListViewAdapterLVP = null;
	ProgressDialog mProgressDialog = null;
	private ArrayList<HashMap<String, Object>> mSwitchersList = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> mLVPSwitchersList = new ArrayList<HashMap<String, Object>>();

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
    private Handler mAnimationHandler=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_switcher_demo);
		mProgressDialog = new ProgressDialog(this);
		mGridView=(GridView)findViewById(R.id.gridview_sysapp);
		mListView=(ListView)findViewById(R.id.list_lvp_switchers);
		mListViewAdapter = new ListViewAdapter(this);
		mListViewAdapterLVP = new ListViewAdapter(this);
		mSwitchersInfo = new SwitchersInfo(this);
	}
	
	@Override
	protected void onResume(){	
		super.onResume();
        if (mUpdater == null) {
            mUpdater = new Handler(this);
        }
        if (mAnimationHandler == null) {
        	mAnimationHandler = new Handler(this);
        }        
        initData(false);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
        if (mUpdater != null) {
            mUpdater.removeCallbacksAndMessages(null);
        }
        if (mAnimationHandler != null) {
        	mAnimationHandler.removeCallbacksAndMessages(null);
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
      				loadLVPSwitchersResource();
					Message msgNew = mAnimationHandler.obtainMessage(DIALOG_DISMISS_MESSAGE);
					mAnimationHandler.sendMessage(msgNew);
      			}
				if(isUpdateHandler){
					mUpdater.sendEmptyMessageDelayed(UPDATE_MESSAGE, RESCAN_INTERVAL_MS);//间隔一秒钟发送消息
				}
      		}
         }.start();
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case 1:
			mProgressDialog.setMessage(getString(R.string.msg_loading));
			mProgressDialog.setCancelable(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.show();
			break;	
		case 2:
			mProgressDialog.dismiss();
			//
			mListViewAdapter.setMode(1);
			mListViewAdapter.setupList(mSwitchersList);
			mGridView.setNumColumns(3);
			mGridView.setAdapter(mListViewAdapter);
			//
			mListViewAdapterLVP.setMode(1);
			mListViewAdapterLVP.setupList(mLVPSwitchersList);
			mListView.setAdapter(mListViewAdapterLVP);
			//
			mUpdater.sendEmptyMessage(UPDATE_MESSAGE);
			break;		
		case 3:
			mUpdater.removeMessages(UPDATE_MESSAGE);//
			mListViewAdapter.setupList(mSwitchersList);
			mListViewAdapter.notifyDataSetChanged();
			initData(true);
			break;
		}
        return true;
	}
 	
	public void loadSwitchersResource(){
		mSwitchersList.clear();
		for (int i = 0; i < switchTXT.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			switch (switchTXT[i]) {
			case R.string.switch_bluetooth:
				map = mSwitchersInfo.getBluetoothSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.string.widetouch_torchlight:
				map = mSwitchersInfo.getTorchLightSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.string.switch_gps:
				map = mSwitchersInfo.getGpsSwitcher();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.string.switch_mobile:
				map = mSwitchersInfo.getMobileSwitcher();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.string.switch_profile:
				map = mSwitchersInfo.getProfileSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.string.switch_rotation:
				map = mSwitchersInfo.getRotationSwitch();
				break;
			case R.string.switch_wifi:
				map = mSwitchersInfo.getWifiSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.string.switch_brightness:
				map = mSwitchersInfo.getBrightnessSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.string.switch_empty:
				map.put("itemImage", R.drawable.back);
				break;
			default:
				break;
			}
			mSwitchersList.add(map);
		}
	}
	
	public void loadLVPSwitchersResource(){
		mLVPSwitchersList.clear();
		for(int i=0;i<mSwitchersInfo.lvpDrawablesdes.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemText", mSwitchersInfo.lvpDrawablesdes[i]);
			map.put("itemImage", mSwitchersInfo.lvpDrawablesID[i]);
			mLVPSwitchersList.add(map);
		}
	}
	
}
