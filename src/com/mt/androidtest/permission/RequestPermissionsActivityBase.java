package com.mt.androidtest.permission;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Trace;
import android.widget.Toast;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public abstract class RequestPermissionsActivityBase extends Activity {
    public static final String PREVIOUS_ACTIVITY_INTENT = "previous_intent";
    private static final int PERMISSIONS_REQUEST_ALL_PERMISSIONS = 1;
    protected abstract String[] getRequiredPermissions();
    protected abstract String[] getDesiredPermissions();
    private Intent mPreviousActivityIntent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreviousActivityIntent = (Intent) getIntent().getExtras().get(PREVIOUS_ACTIVITY_INTENT);
        if (savedInstanceState == null) {
            requestPermissions();
        }
    }

    private void requestPermissions() {
    	Trace.beginSection("requestPermissions");
        try {
			// Construct a list of missing permissions
			final ArrayList<String> unsatisfiedPermissions = new ArrayList<>();
			for (String permission : getDesiredPermissions()) {
			    if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
			        unsatisfiedPermissions.add(permission);
			        ALog.Log("Requested permission:"+permission);
			    }
			}
			if (unsatisfiedPermissions.size() == 0) {
			    throw new RuntimeException("Request permission activity was called even though all permissions are satisfied.");
			}
			requestPermissions(unsatisfiedPermissions.toArray(new String[unsatisfiedPermissions.size()]), PERMISSIONS_REQUEST_ALL_PERMISSIONS);
		} finally {
		    Trace.endSection();
		}
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (permissions != null && permissions.length > 0 && isAllGranted(permissions, grantResults)) {
            mPreviousActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//FLAG_ACTIVITY_NO_ANIMATION：禁止掉系统切换动画
            startActivity(mPreviousActivityIntent);
            finish();
            overridePendingTransition(0, 0);
        } else {
            Toast.makeText(this, R.string.missing_required_permission, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean isAllGranted(String permissions[], int[] grantResult) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResult[i] != PackageManager.PERMISSION_GRANTED && isPermissionRequired(permissions[i])) {
            	return false;
            }
        }
        return true;
    }

    private boolean isPermissionRequired(String p) {
        return Arrays.asList(getRequiredPermissions()).contains(p);
    }
    
	protected static boolean startPermissionActivity(Activity activity, String[] requiredPermissions, Class<?> newActivityClass) {
		if (!hasAllPermissions(activity, requiredPermissions)) {//如果所要求的权限没有全部允许，那么需要申请权限
			final Intent intent = new Intent(activity,  newActivityClass);
			Intent mIntentGet=activity.getIntent();
		    intent.putExtra(PREVIOUS_ACTIVITY_INTENT, mIntentGet);
		    activity.startActivity(intent);
		    activity.finish();
		    return true;
		}
        return false;
    }    
	
    protected static boolean hasAllPermissions(Context context, String[] permissions) {
        Trace.beginSection("hasPermission");
        try {
            for (String permission : permissions) {
                if (!checkSelfPermission(context,permission)) {
                    return false;
                }
            }
            return true;
        } finally {
            Trace.endSection();
        }
    }	
    
    public static boolean checkSelfPermission(Activity activity, String permission){
    	return activity.getApplicationContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
    
    public static boolean checkSelfPermission(Context context, String permission){
    	return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
