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
    	//permission.SYSTEM_ALERT_WINDOW��������Ӵ�Ȩ���жϣ���ô��ͨӦ��(û��ǩ��������system��)���޷���ȡ��Ȩ��
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
     * API level 23�������豸��SYSTEM_ALERT_WINDOWȨ��˵��(�������Android developer)��
     * ����1)ϵͳǩ����2)system/xxxĿ¼������������������checkPermissionGranted����ֵΪtrue����Settings.canDrawOverlaysΪfalse
     * �����Settings.canDrawOverlaysΪtrue����������������android:sharedUserId="android.uid.system"�������û���ϵͳ����-Ӧ���йرա�������Ӧ������ʾ��
     * ���ٴ���ʾ�������Ļ��ᱨ���������еĴ���
     * java.lang.RuntimeException: Unable to create service xxx: java.lang.SecurityException: xxxx from uid xxxx not allowed to perform SYSTEM_ALERT_WINDOW
     * @param activity
     * @return
     */
    public static boolean startPermissionActivity(Activity activity) {
    	/**
    	 * ����checkSelfPermission��Settings.canDrawOverlays��ֵ���£�
    	 * 1)��ͨӦ�ã�false,false
    	 * 2)ǩ��Ӧ��+system/priv-app��true,false
    	 * 3)ǩ��Ӧ��+system/priv-app+android:sharedUserId="android.uid.system"��true,true
    	 */
		if(isLogRun)ALog.Log("RequestPermissionsActivity_checkSelfPermission:"+checkSelfPermission(activity,permission.SYSTEM_ALERT_WINDOW));
		if(isLogRun)ALog.Log("RequestPermissionsActivity_Settings.canDrawOverlays:"+Settings.canDrawOverlays(activity));
        return startPermissionActivity(activity, REQUIRED_PERMISSIONS, RequestPermissionsActivity.class);
    }
 
}
