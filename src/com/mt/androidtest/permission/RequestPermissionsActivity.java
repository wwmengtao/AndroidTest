package com.mt.androidtest.permission;

import android.Manifest.permission;
import android.app.Activity;

/**
 * Activity that requests permissions needed for activities exported from Contacts.
 */
public class RequestPermissionsActivity extends RequestPermissionsActivityBase {

    private static final String[] REQUIRED_PERMISSIONS = new String[]{
    	permission.READ_PHONE_STATE,
    	permission.BLUETOOTH,
    	permission.ACCESS_WIFI_STATE,
    	permission.REBOOT,
    	permission.READ_EXTERNAL_STORAGE,
    	permission.WRITE_EXTERNAL_STORAGE,
    	permission.SYSTEM_ALERT_WINDOW,   
    };
	//SYSTEM_ALERT_WINDOW�����Ȩ�������������²ſ����Զ���ȡ��
    //1)ϵͳǩ����2)system/xxxĿ¼�£�

    @Override
    protected String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    @Override
    protected String[] getDesiredPermissions() {
        return new String[]{
            	permission.READ_PHONE_STATE,
            	permission.BLUETOOTH,
            	permission.ACCESS_WIFI_STATE,
            	permission.REBOOT,
            	permission.READ_EXTERNAL_STORAGE,
            	permission.WRITE_EXTERNAL_STORAGE,
            	permission.SYSTEM_ALERT_WINDOW,  
        };
    }
    public static boolean startPermissionActivity(Activity activity) {
        return startPermissionActivity(activity, REQUIRED_PERMISSIONS,
                RequestPermissionsActivity.class);
    }
}
