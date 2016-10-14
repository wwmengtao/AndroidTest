package com.mt.androidtest.permission;

import android.Manifest.permission;
import android.app.Activity;
import android.provider.Settings;
import com.mt.androidtest.ALog;

/**
 * Activity that requests permissions needed for activities exported from Contacts.
 */
public class RequestPermissionsActivity extends RequestPermissionsActivityBase {
	static boolean isLogRun = false;
    public static final String[] REQUIRED_PERMISSIONS = new String[]{
    	permission.READ_EXTERNAL_STORAGE,
    	permission.WRITE_EXTERNAL_STORAGE,
    	//permission.SYSTEM_ALERT_WINDOW：如果增加此权限判断，那么普通应用(没有签名，不在system区)将无法获取该权限
    };

    @Override
    protected String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    @Override
    protected String[] getDesiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }
    
    /**
     * API level 23及以上设备的SYSTEM_ALERT_WINDOW权限说明(详情参照Android developer)：
     * 满足1)系统签名；2)system/xxx目录下这两个条件，下列checkPermissionGranted返回值为true，而Settings.canDrawOverlays为false
     * 如果让Settings.canDrawOverlays为true，必须增加条件：android:sharedUserId="android.uid.system"。否则用户在系统设置-应用中关闭“在其他应用上显示”
     * 后再次显示悬浮窗的话会报类似于下列的错误：
     * java.lang.RuntimeException: Unable to create service xxx: java.lang.SecurityException: xxxx from uid xxxx not allowed to perform SYSTEM_ALERT_WINDOW
     * @param activity
     * @return
     */
    public static boolean startPermissionActivity(Activity activity) {
    	/**
    	 * 下列checkSelfPermission和Settings.canDrawOverlays数值如下：
    	 * 1)普通应用：false,false
    	 * 2)签名应用+system/priv-app：true,false
    	 * 3)签名应用+system/priv-app+android:sharedUserId="android.uid.system"：true,true
    	 */
		if(isLogRun)ALog.Log("RequestPermissionsActivity_checkSelfPermission:"+checkSelfPermission(activity,permission.SYSTEM_ALERT_WINDOW));
		if(isLogRun)ALog.Log("RequestPermissionsActivity_Settings.canDrawOverlays:"+Settings.canDrawOverlays(activity));
        return startPermissionActivity(activity, REQUIRED_PERMISSIONS, RequestPermissionsActivity.class);
    }
 
}
