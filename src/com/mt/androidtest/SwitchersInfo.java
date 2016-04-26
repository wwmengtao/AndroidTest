package com.mt.androidtest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.IPowerManager;
import android.provider.Settings;

public class SwitchersInfo {
	Context mContext;
	WifiManager mWifiManager=null;
	public SwitchersInfo(Context mContex){
		this.mContext = mContex;
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

	}
	
	public HashMap<String, Object> getBluetoothSwitch() {
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (null != adapter && adapter.isEnabled()) {
			map.put("itemImage", R.drawable.lenovo_widget_btn_bluetooth_on);
		} else {
			map.put("itemImage", R.drawable.lenovo_widget_btn_bluetooth_off);
		}
		return map;
	}
	public HashMap<String, Object> getTorchLightSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean isEnabled = Settings.Global.getInt(mContext.getContentResolver(),"torch_on", 0) != 0;
		if (isEnabled) {
			map.put("itemImage", R.drawable.lenovo_widget_btn_flashlight_on);
		} else {
			map.put("itemImage", R.drawable.lenovo_widget_btn_flashlight_off);
		}
		return map;
	}
	

	public HashMap<String, Object> getGpsSwitcher() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int gpsmode = Settings.Secure.getInt(mContext.getContentResolver(),
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
	
	public HashMap<String, Object> getMobileSwitcher() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean simStateReady = true;
		boolean airMode = Settings.Global.getInt(mContext.getContentResolver(),
				Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
		if (!airMode  && simStateReady) {
			map.put("itemImage", R.drawable.toolbar_mobile_enable);
		} else {
			map.put("itemImage", R.drawable.toolbar_mobile_off);
		}
		return map;
	}


	public HashMap<String, Object> getProfileSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		AudioManager mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
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
    	AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
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

	
	public HashMap<String, Object> getRotationSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int smart_rotation = Settings.System.getInt(mContext.getContentResolver(),
				"screen_smart_rotation", 3);
		int flag = Settings.System.getInt(mContext.getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 0);
		if (flag == 1) {
			map.put("itemImage", R.drawable.toolbar_auto_rotation_enable);
			map.put("itemText", mContext.getString(R.string.switch_rotation));
		} else {
			map.put("itemImage", R.drawable.toolbar_auto_rotation_off);
			map.put("itemText", mContext.getString(R.string.switch_rotation));
		}
		return map;
	}


	public HashMap<String, Object> getWifiSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int state = mWifiManager.getWifiState();
		if (!(state == WifiManager.WIFI_STATE_ENABLED)) {
			map.put("itemImage", R.drawable.toolbar_wifi_off);
		} else {
			map.put("itemImage", R.drawable.toolbar_wifi_enable);
		}
		return map;
	}

	
	public HashMap<String, Object> getBrightnessSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int brightnessMode = Settings.System.getInt(mContext.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
			map.put("itemImage", R.drawable.toolbar_brightness_auto);
		} else {
			int brightness = getBrightness(mContext);
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
