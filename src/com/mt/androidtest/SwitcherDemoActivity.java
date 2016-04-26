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
	
	WifiManager mWifiManager=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_switcher_demo);
		mProgressDialog = new ProgressDialog(this);
		mGridView=(GridView)findViewById(R.id.gridview_sysapp);
		mListViewAdapter = new ListViewAdapter(this);
		
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
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
				map = getBluetoothSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_torchlight:
				map = getTorchLightSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_gps:
				map = getGpsSwitcher();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_mobile:
				map = getMobileSwitcher();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_profile:
				map = getProfileSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_rotation:
				map = getRotationSwitch();
				break;
			case R.drawable.switch_wifi:
				map = getWifiSwitch();
				map.put("itemText", getString(switchTXT[i]));
				break;
			case R.drawable.switch_brightness:
				map = getBrightnessSwitch();
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
			break;
			}
		}
 	};
 	

	private HashMap<String, Object> getBluetoothSwitch() {
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (null != adapter && adapter.isEnabled()) {
			map.put("itemImage", R.drawable.lenovo_widget_btn_bluetooth_on);
		} else {
			map.put("itemImage", R.drawable.lenovo_widget_btn_bluetooth_off);
		}
		return map;
	}
	private HashMap<String, Object> getTorchLightSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean isEnabled = Settings.Global.getInt(getContentResolver(),"torch_on", 0) != 0;
		if (isEnabled) {
			map.put("itemImage", R.drawable.lenovo_widget_btn_flashlight_on);
		} else {
			map.put("itemImage", R.drawable.lenovo_widget_btn_flashlight_off);
		}
		return map;
	}
	

	private HashMap<String, Object> getGpsSwitcher() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int gpsmode = Settings.Secure.getInt(getContentResolver(),
				Settings.Secure.LOCATION_MODE,
				Settings.Secure.LOCATION_MODE_OFF);
		boolean open = (gpsmode == 3) || (gpsmode == 1) ? true : false;
		if (!open) {
			map.put("itemImage", R.drawable.toolbar_gps_off);
		} else {
			map.put("itemImage", R.drawable.toolbar_gps_enable);
		}
		return map;
	}
	
	private HashMap<String, Object> getMobileSwitcher() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean simStateReady = true;
		boolean airMode = Settings.Global.getInt(getContentResolver(),
				Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
		if (!airMode  && simStateReady) {
			map.put("itemImage", R.drawable.toolbar_mobile_enable);
		} else {
			map.put("itemImage", R.drawable.toolbar_mobile_off);
		}
		return map;
	}


	private HashMap<String, Object> getProfileSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int ringMode = AudioManager.RINGER_MODE_NORMAL;
		if(android.os.Build.VERSION.SDK_INT>21){//android 5.1
			  ringMode = getRingerModeInternal();
		}else{
			  ringMode = mAudioManager.getRingerMode();
		}
		if (AudioManager.RINGER_MODE_NORMAL == ringMode) {
			map.put("itemImage", R.drawable.toolbar_normal_profile_enable);
		} else {
			map.put("itemImage", R.drawable.toolbar_normal_profile_off);
		}
		return map;
	}

    
    private int getRingerModeInternal(){
    	int result = AudioManager.RINGER_MODE_NORMAL;
    	AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    	Class<?> clazz;
		try {
			clazz = Class.forName("android.media.AudioManager");
			Method method = clazz.getMethod("getRingerModeInternal");
			result = (Integer)method.invoke(mAudioManager);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }

	
	private HashMap<String, Object> getRotationSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int smart_rotation = Settings.System.getInt(getContentResolver(),
				"screen_smart_rotation", 3);
		int flag = Settings.System.getInt(getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 0);
		if (flag == 1) {
			map.put("itemImage", R.drawable.toolbar_auto_rotation_enable);
			map.put("itemText", getString(R.string.switch_rotation));
		} else {
			map.put("itemImage", R.drawable.toolbar_auto_rotation_off);
			map.put("itemText", getString(R.string.switch_rotation));
		}
		return map;
	}


	private HashMap<String, Object> getWifiSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int state = mWifiManager.getWifiState();
		if (!(state == WifiManager.WIFI_STATE_ENABLED)) {
			map.put("itemImage", R.drawable.toolbar_wifi_off);
		} else {
			map.put("itemImage", R.drawable.toolbar_wifi_enable);
		}
		return map;
	}

	
	private HashMap<String, Object> getBrightnessSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int brightnessMode = Settings.System.getInt(getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
			map.put("itemImage", R.drawable.toolbar_brightness_auto);
		} else {
			int brightness = getBrightness(this);
			switch (brightness) {
			case MINIMUM_BACKLIGHT:
				map.put("itemImage", R.drawable.toolbar_brightness_auto1);
				break;
			case DEFAULT_BACKLIGHT:
				map.put("itemImage", R.drawable.toolbar_brightness_auto2);
				break;
			case MAXIMUM_BACKLIGHT:
				map.put("itemImage", R.drawable.toolbar_brightness_on);
				break;
			case MIDDLE_BACKLIGHT:
				map.put("itemImage", R.drawable.toolbar_brightness_middle);
				break;
			default:
				map.put("itemImage", R.drawable.toolbar_brightness_auto1);
				break;
			}
		}
		return map;
	}
	
	public static final int BRIGHTNESS_ON = 255;
	public static final int MINIMUM_BACKLIGHT = 30;// android.os.PowerManager.BRIGHTNESS_DIM
	public static final int MIDDLE_BACKLIGHT = 47;
	public static final int MAXIMUM_BACKLIGHT = BRIGHTNESS_ON;
	public static final int DEFAULT_BACKLIGHT = (int) (BRIGHTNESS_ON * 0.4f);
	public static int getBrightness(Context context) {
		try {
			Method method = Class.forName("android.os.ServiceManager")
					.getMethod("getService", String.class);
			IBinder binder;
			binder = (IBinder) method.invoke(null, new Object[] { "power" });
			IPowerManager power = IPowerManager.Stub.asInterface(binder);
			if (power != null) {
				int brightness = Settings.System.getInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS);
				android.util.Log.d("bluewind","get brightness:" + brightness);
				if (brightness <= MINIMUM_BACKLIGHT) {
					brightness = MINIMUM_BACKLIGHT;
				} else if (brightness <= MIDDLE_BACKLIGHT) {
					brightness = MIDDLE_BACKLIGHT;
				} else if (brightness <= DEFAULT_BACKLIGHT) {
					brightness = DEFAULT_BACKLIGHT;
				} else {
					brightness = MAXIMUM_BACKLIGHT;
				}
				return brightness;
			}
		} catch (Exception e) {
			ALog.Log("getBrightness: " + e);
		}
		return DEFAULT_BACKLIGHT;
	}

}
