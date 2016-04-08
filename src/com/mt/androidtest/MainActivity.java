package com.mt.androidtest;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemProperties;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;


public class MainActivity extends Activity {
	boolean isLogRun=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(isLogRun)ALog.Log("====onCreate");
		setListenCall();
	}
	
	@Override
	protected void onResume(){	
		super.onResume();
		if(isLogRun)ALog.Log("====onResume:"+getLocale());
		writeToXml(this);//或者调用ALog.howToWriteToXml(this);
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		if(isLogRun)ALog.Log("====onPause");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(isLogRun)ALog.Log("====onDestroy");
	}	
	
	
    @Override
    //onConfigurationChanged函数必须在AndroidManifest中增加android:configChanges="orientation|keyboardHidden|screenSize"后才会调用
    //如果没有上述android:configChanges属性，那么手机横竖屏时，Activity将重绘从而调用onCreate、onResume等函数
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int mOrientation = this.getResources().getConfiguration().orientation;
        if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
        	if(isLogRun)ALog.Log("====ORIENTATION_LANDSCAPE");
        }
        if(mOrientation == Configuration.ORIENTATION_PORTRAIT){  
        	if(isLogRun)ALog.Log("====ORIENTATION_PORTRAIT");
        }
    }
    /*
	[persist.sys.first_time_boot]: [false]
	[persist.sys.sd.defaultpath]: [/storage/emulated/0]
	[persist.sys.timezone]: [Europe/Moscow]
	[persist.sys.usb.config]: [mtp,adb]
	
	[ro.lenovo.wificert]: [pass]
	[ro.lenovo.platform]: [mtk]
	[ro.lenovo.region]: [row]
	[ro.lenovo.series]: [Lenovo S1]
	[ro.lenovo.device]: [phone]
	[ro.lenovo.easyimage.code]: [ru]
    */
    public String getLocale(){
			String locale = SystemProperties.get("persist.sys.locale");
			return locale;
    }
    /**
     * checkComponentExist：试验检测组件是否存在的几种方法
     */
    public void checkComponentExist(){
        String packageName = "com.mt.androidprocessservice";
        String className = "com.mt.androidprocessservice.MainActivity";
        if(isLogRun)ALog.Log("====checkComponentExist:"+isComponentExist(packageName, className));
		if(isLogRun)ALog.Log("====hasComponent:"+hasComponent(packageName, className));
		if(isLogRun)ALog.Log("====getComponentName:"+getComponentName(packageName));
		
    }
    /**
     * isComponentExist：检测是否对应的组件存在
     * @param packageName
     * @param className
     * @return boolean
     */
	public boolean isComponentExist(String packageName, String className){
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClassName(packageName, className);
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		//startActivity(intent);
		boolean isIntentSafe = activities.size() > 0;
		return isIntentSafe;
	}
    /**
     * hasComponent：检测是否对应的组件存在
     * @param packageName
     * @param className
     * @return boolean
     */
    public boolean hasComponent(String packageName, String className) {
        ComponentName mComponentName = new ComponentName(packageName, className);
        PackageManager pm = getPackageManager();
        try {
            ActivityInfo info = pm.getActivityInfo(mComponentName, 0);
            if (info != null) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }	
	/**
	 * getComponentName：获取对应组件的应用名称
	 * @param packageName
	 * @return
	 */
    private String getComponentName(String packageName) {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);

            if (applicationInfo != null) {
                return pm.getApplicationLabel(applicationInfo).toString();
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    public void setListenCall(){
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state) {
                case TelephonyManager.CALL_STATE_RINGING ://电话响铃
                	ALog.Log("CALL_STATE_RINGING");
                	break;
                case TelephonyManager.CALL_STATE_OFFHOOK ://电话接听
                	ALog.Log("CALL_STATE_OFFHOOK");
                    break;
                default:
                    break;
            }
        };
    };
    
    /**
     * writeToXml：Android环境下调用ALog中的方法写xml
     */
    public void writeToXml(Context mContext){
		String fileToSave = "1.xml";
		String docTag = "Document";
		ALog.startSaving(mContext,fileToSave,docTag);
		for(int i=0;i<5;i++){
			ALog.stag("name"+i);
			ALog.attr("attr1", "123");
			ALog.etag("name"+i);
		}
		ALog.endSaving();
    }
}

