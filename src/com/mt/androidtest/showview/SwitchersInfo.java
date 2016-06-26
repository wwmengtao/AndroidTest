package com.mt.androidtest.showview;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.IPowerManager;
import android.provider.Settings;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public class SwitchersInfo {
	private Context mContext;
	private WifiManager mWifiManager=null;
    private AssetManager mAssetManager=null;	
	private boolean isVibeUI35=false;//表明当前是VIBEUI3.5资源环境
	private String dirAssets = "SwitcherDemo";
	public SwitchersInfo(Context contex){
		mContext = contex.getApplicationContext();
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		mAssetManager=mContext.getResources().getAssets();
		isVibeUI35 = isVibeUI35();
	}
	
    /**
     * getDrawbleFromAsset：从assets目录中获取png类型图片
     * @return
     */
    public Drawable getDrawbleFromAsset(String fileName){
    	InputStream is=null;
    	Drawable mDrawable=null;
    	try {
			is=mAssetManager.open(dirAssets+File.separator+fileName+".png");
			mDrawable=Drawable.createFromStream(is, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	return mDrawable;
    }
	
	public int [] lvpDrawablesID={
			R.drawable.ic_qs_airplane_off,
			R.drawable.ic_qs_airplane_on,
			R.drawable.ic_qs_back,
			R.drawable.ic_qs_bluetooth_connected,
			R.drawable.ic_qs_bluetooth_connecting,
			R.drawable.ic_qs_bluetooth_off,
			R.drawable.ic_qs_bluetooth_on,
			R.drawable.ic_qs_brightness_auto_off,
			R.drawable.ic_qs_brightness_auto_on,
			R.drawable.ic_qs_cancel,
			R.drawable.ic_qs_cast_detail_empty,
			R.drawable.ic_qs_cast_off,
			R.drawable.ic_qs_cast_on,
			R.drawable.ic_qs_dnd_off,
			R.drawable.ic_qs_dnd_on,
			R.drawable.ic_qs_dnd_on_total_silence,
			R.drawable.ic_qs_flashlight_off,
			R.drawable.ic_qs_flashlight_on,
			R.drawable.ic_qs_hotspot_off,
			R.drawable.ic_qs_hotspot_on,
			R.drawable.ic_qs_inversion_off,
			R.drawable.ic_qs_inversion_on,
			R.drawable.ic_qs_location_off,
			R.drawable.ic_qs_location_on,
			R.drawable.ic_qs_minus,
			R.drawable.ic_qs_no_sim,
			R.drawable.ic_qs_plus,
			R.drawable.ic_qs_ringer_audible,
			R.drawable.ic_qs_ringer_silent,
			R.drawable.ic_qs_ringer_vibrate,
			R.drawable.ic_qs_rotation_landscape,
			R.drawable.ic_qs_rotation_portrait,
			R.drawable.ic_qs_rotation_unlocked,
			R.drawable.ic_qs_signal_0,
			R.drawable.ic_qs_signal_1,
			R.drawable.ic_qs_signal_1x,
			R.drawable.ic_qs_signal_2,
			R.drawable.ic_qs_signal_3,
			R.drawable.ic_qs_signal_3g,
			R.drawable.ic_qs_signal_4,
			R.drawable.ic_qs_signal_4g,
			R.drawable.ic_qs_vpn,
			R.drawable.ic_qs_wifi_0,
			R.drawable.ic_qs_wifi_1,
			R.drawable.ic_qs_wifi_2,
			R.drawable.ic_qs_wifi_3,
			R.drawable.ic_qs_wifi_4,
			R.drawable.ic_qs_wifi_detail_empty,
			R.drawable.ic_qs_wifi_disabled,
			R.drawable.ic_qs_wifi_full_0,
			R.drawable.ic_qs_wifi_full_1,
			R.drawable.ic_qs_wifi_full_2,
			R.drawable.ic_qs_wifi_full_3,
			R.drawable.ic_qs_wifi_full_4,
			R.drawable.ic_qs_wifi_no_network};
	public String [] lvpDrawablesdes = {
			"ic_qs_airplane_off",
			"ic_qs_airplane_on",
			"ic_qs_back",
			"ic_qs_bluetooth_connected",
			"ic_qs_bluetooth_connecting",
			"ic_qs_bluetooth_off",
			"ic_qs_bluetooth_on",
			"ic_qs_brightness_auto_off",
			"ic_qs_brightness_auto_on",
			"ic_qs_cancel",
			"ic_qs_cast_detail_empty",
			"ic_qs_cast_off",
			"ic_qs_cast_on",
			"ic_qs_dnd_off",
			"ic_qs_dnd_on",
			"ic_qs_dnd_on_total_silence",
			"ic_qs_flashlight_off",
			"ic_qs_flashlight_on",
			"ic_qs_hotspot_off",
			"ic_qs_hotspot_on",
			"ic_qs_inversion_off",
			"ic_qs_inversion_on",
			"ic_qs_location_off",
			"ic_qs_location_on",
			"ic_qs_minus",
			"ic_qs_no_sim",
			"ic_qs_plus",
			"ic_qs_ringer_audible",
			"ic_qs_ringer_silent",
			"ic_qs_ringer_vibrate",
			"ic_qs_rotation_landscape",
			"ic_qs_rotation_portrait",
			"ic_qs_rotation_unlocked",
			"ic_qs_signal_0",
			"ic_qs_signal_1",
			"ic_qs_signal_1x",
			"ic_qs_signal_2",
			"ic_qs_signal_3",
			"ic_qs_signal_3g",
			"ic_qs_signal_4",
			"ic_qs_signal_4g",
			"ic_qs_vpn",
			"ic_qs_wifi_0",
			"ic_qs_wifi_1",
			"ic_qs_wifi_2",
			"ic_qs_wifi_3",
			"ic_qs_wifi_4",
			"ic_qs_wifi_detail_empty",
			"ic_qs_wifi_disabled",
			"ic_qs_wifi_full_0",
			"ic_qs_wifi_full_1",
			"ic_qs_wifi_full_2",
			"ic_qs_wifi_full_3",
			"ic_qs_wifi_full_4",
			"ic_qs_wifi_no_network"};
	
	public HashMap<String, Object> getBluetoothSwitch() {
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (null != adapter && adapter.isEnabled()) {
			map.put("itemImage", isVibeUI35?R.drawable.ic_qs_bluetooth_on : getDrawbleFromAsset("lenovo_widget_btn_bluetooth_on"));
		} else {
			map.put("itemImage", isVibeUI35?R.drawable.ic_qs_bluetooth_off : getDrawbleFromAsset("lenovo_widget_btn_bluetooth_off"));
		}
		return map;
	}
	public HashMap<String, Object> getTorchLightSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean isEnabled = Settings.Global.getInt(mContext.getContentResolver(),"torch_on", 0) != 0;
		if (isEnabled) {
			map.put("itemImage", isVibeUI35?R.drawable.ic_qs_flashlight_on : getDrawbleFromAsset("lenovo_widget_btn_flashlight_on"));
		} else {
			map.put("itemImage", isVibeUI35?R.drawable.ic_qs_flashlight_off : getDrawbleFromAsset("lenovo_widget_btn_flashlight_off"));
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
			map.put("itemImage", isVibeUI35?R.drawable.ic_qs_location_off : getDrawbleFromAsset("toolbar_gps_off"));
		} else {
			map.put("itemImage", isVibeUI35?R.drawable.ic_qs_location_on : getDrawbleFromAsset("toolbar_gps_enable"));
		}
		return map;
	}
	
	public HashMap<String, Object> getMobileSwitcher() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean simStateReady = false;
		boolean airMode = Settings.Global.getInt(mContext.getContentResolver(),
				Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
		if (!airMode  && simStateReady) {
			map.put("itemImage", isVibeUI35?R.drawable.ic_dataconnect_on : getDrawbleFromAsset("toolbar_mobile_enable"));
		} else {
			map.put("itemImage", isVibeUI35?R.drawable.ic_dataconnect_off : getDrawbleFromAsset("toolbar_mobile_off"));
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
			map.put("itemImage", getDrawbleFromAsset("toolbar_normal_profile_enable"));
		} else {
			map.put("itemImage", getDrawbleFromAsset("toolbar_normal_profile_off"));
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
		int flag = Settings.System.getInt(mContext.getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 0);
		if (flag == 1) {
			map.put("itemImage", isVibeUI35?R.drawable.ic_qs_rotation_unlocked : getDrawbleFromAsset("toolbar_auto_rotation_enable"));
			map.put("itemText", mContext.getString(R.string.switch_rotation));
		} else {
			map.put("itemImage", isVibeUI35?R.drawable.ic_qs_rotation_portrait : getDrawbleFromAsset("toolbar_auto_rotation_off"));
			map.put("itemText", mContext.getString(R.string.switch_rotation));
		}
		return map;
	}


	public HashMap<String, Object> getWifiSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int state = mWifiManager.getWifiState();
		if (!(state == WifiManager.WIFI_STATE_ENABLED)) {
			map.put("itemImage", isVibeUI35? R.drawable.ic_qs_wifi_disabled : getDrawbleFromAsset("toolbar_wifi_off"));
		} else {
			map.put("itemImage", isVibeUI35? R.drawable.ic_qs_wifi_full_4 : getDrawbleFromAsset("toolbar_wifi_enable"));
		}
		return map;
	}

	
	public HashMap<String, Object> getBrightnessSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int brightnessMode = Settings.System.getInt(mContext.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
			map.put("itemImage", getDrawbleFromAsset("toolbar_brightness_auto"));
		} else {
			int brightness = getBrightness(mContext);
			switch (brightness) {
			case MINIMUM_BACKLIGHT:
				map.put("itemImage", getDrawbleFromAsset("toolbar_brightness_auto1"));
				break;
			case DEFAULT_BACKLIGHT:
				map.put("itemImage", getDrawbleFromAsset("toolbar_brightness_auto2"));
				break;
			case MAXIMUM_BACKLIGHT:
				map.put("itemImage", getDrawbleFromAsset("toolbar_brightness_on"));
				break;
			case MIDDLE_BACKLIGHT:
				map.put("itemImage", getDrawbleFromAsset("toolbar_brightness_middle"));
				break;
			default:
				map.put("itemImage", getDrawbleFromAsset("toolbar_brightness_auto1"));
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
    public boolean isVibeUI35(){
    	String lvpVersion = SysProp.get("ro.lenovo.lvp.version",null);
    	boolean isVibeUI35 = (null!=lvpVersion&&lvpVersion.contains("V3.5"));
    	return isVibeUI35;
    }
}
