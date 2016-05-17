package com.mt.androidtest;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mt.androidtest.R;

public class MainActivity extends Activity implements View.OnClickListener,DialogInterface.OnClickListener{
	boolean isLogRun=true;
	PackageManager mPackageManager=null;
	int [] buttonID = {
								  R.id.btn_permission,					
								  R.id.btn_showsysapp,
								  R.id.btn_start_activity,
								  R.id.btn_start_documentsactivity,
								  R.id.btn_start_downloadproviderui,
								  R.id.btn_showswitcher,
								  R.id.btn_getresource,
								  R.id.btn_showdialog,
								  R.id.btn_shownotification,
								  R.id.btn_showview_test};

    private NotificationManager mNotificationManager = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isLogRun)ALog.Log("onCreate",this);
		setContentView(R.layout.activity_main);
		Button btn=null;
		for(int i=0;i<buttonID.length;i++){
			btn = (Button)findViewById(buttonID[i]);
			btn.setOnClickListener(this);
		}
		mPackageManager = getPackageManager();
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		//addOnGlobalLayoutListener();//视图树监听器
	}
	
	@Override
	protected void onResume(){	
		super.onResume();
		if(isLogRun)ALog.Log("onResume",this);
		testFunctions();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		if(isLogRun)ALog.Log("onPause",this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(isLogRun)ALog.Log("onDestroy",this);
	}	
	
	public void testFunctions(){
		//1、反射调用
		//reflectCall();
		//reflectCallListAll();
		//2、检测组件是否存在
		checkComponentExist();
		//3、从其他应用获取资源，参照onClick函数中case R.id.btn_getresource
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

    /**
     * checkComponentExist：试验检测组件是否存在的几种方法
     */
    public void checkComponentExist(){
        String packageName = "com.mt.androidprocessservice";
        String className = "com.mt.androidprocessservice.MainActivity";
        if(isLogRun)ALog.Log("====isComponentExist:"+isComponentExist(packageName, className));
		if(isLogRun)ALog.Log("====hasComponent:"+hasComponent(packageName, className));
		if(isLogRun)ALog.Log("====getComponentName:"+getComponentName(packageName));
		if(isLogRun)ALog.Log("====isLaunchIntentForPackageNull:"+isLaunchIntentForPackageNull(this.getPackageName()));
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
		List<ResolveInfo> activities = mPackageManager.queryIntentActivities(intent, 0);
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
        try {
            ActivityInfo info = mPackageManager.getActivityInfo(mComponentName, 0);
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
            ApplicationInfo applicationInfo = mPackageManager.getApplicationInfo(packageName, 0);

            if (applicationInfo != null) {
                return mPackageManager.getApplicationLabel(applicationInfo).toString();
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    private boolean isLaunchIntentForPackageNull(String packageName){
    	Intent intent = mPackageManager.getLaunchIntentForPackage(packageName);
    	return null==intent;
    }
	
	/**
	 * reflectCall：测试setDataEnabledUsingSubId和setDataEnabled哪个方法存在
	 */
	public void reflectCall(){
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		Class<?> mClass = telephonyManager.getClass();
		Method mMethod = null;
		try {
			mMethod = mClass.getMethod("setDataEnabledUsingSubId",new Class[]{int.class,boolean.class});
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			ALog.Log("setDataEnabledUsingSubId not found!");
			try {
				mMethod= mClass.getMethod("setDataEnabled",new Class[]{int.class,boolean.class});
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				ALog.Log("setDataEnabled not found!");
			}
		}
		ALog.Log("mMethod.getName():"+mMethod.getName());
	}
	
	/**
	 * reflectCallListAll：罗列出所有的待调用方法，这样可以省去reflectCall中的try-catch结构以及罗列有关方法参数的麻烦
	 */
	public void reflectCallListAll(){
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		Class<?> mClass = telephonyManager.getClass();
		Method [] mMethods = mClass.getDeclaredMethods();
		Method m = getExistedMethod(mMethods,"setDataEnabled");
		int subId = 1;
		if(null!=m){
			m.setAccessible(true);
			try{
				m.invoke(telephonyManager, subId, true);//打开卡槽subId的数据连接
			}catch(Exception ex){
				
			}
		}

	}
	
	/**
	 * isMethodExist：判断指定名称的方法是否存在
	 * @param mMethods
	 * @param methodName
	 * @return
	 */
	public Method getExistedMethod(Method [] mMethods,String methodName){
		if(null==mMethods||null==methodName)return null;
		for (Method m : mMethods) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		switch(v.getId()){
			case	R.id.btn_showsysapp:
				intent=new Intent();
				intent.setClass(MainActivity.this, SysAppsActivity.class);
				startActivity(intent);
			break;
			case	R.id.btn_showswitcher:
				intent=new Intent();
				intent.setClass(MainActivity.this, SwitcherDemoActivity.class);
				startActivity(intent);
			break;
			case	R.id.btn_start_activity:
				startActivityByFlags();
			break;
			case	R.id.btn_start_documentsactivity:
				startDocumentsActivity();
			break;			
			case	R.id.btn_start_downloadproviderui:
				startDownloadProviderUI();
			break;					
			case R.id.btn_getresource:
				intent=new Intent();
				intent.setClass(MainActivity.this, ResourceActivity.class);
				startActivity(intent);
			break;
			case	R.id.btn_showdialog:
				showDialog();
			break;
			case R.id.btn_shownotification:
				if(!isNotificationShown){
					showNotification(this,1,null);
				}
				else{
					cancelNotification(this, 1);
				}
			break;
			case R.id.btn_showview_test:		
				intent=new Intent();
				intent.setClass(MainActivity.this, ShowViewActivity.class);
				startActivity(intent);
			break;			
			case R.id.btn_permission:
				intent=new Intent();
				intent.setClass(MainActivity.this, PermissionActivity.class);
				startActivity(intent);
			break;
		}
	}
	
	String NOTIFICATION_ID="AndroidTest.Notification";
	boolean isNotificationShown=false;
	/**
	 * 显示通知
	 * @param mContext
	 * @param id
	 * @param intent
	 */
	private void showNotification(Context mContext,int id,PendingIntent intent) {
        	CharSequence title0 = "AndroidTest";
	        CharSequence title = "title";
	        CharSequence details = "details";
	        int icon = R.drawable.toolbar_brightness_off;
	        Notification notification = new Notification.Builder(mContext)
	                .setWhen(0)
	                .setSmallIcon(icon)
	                .setAutoCancel(true)
	                .setTicker(title0)
	                .setColor(mContext.getColor(R.color.wheat))
	                .setContentTitle(title)
	                .setContentText(details)
	                .setContentIntent(intent)
	                .setLocalOnly(true)
	                .setPriority(Notification.PRIORITY_DEFAULT)
	                .setDefaults(Notification.DEFAULT_ALL)
	                .setOnlyAlertOnce(true)
	                .build();
	        try {
	        	mNotificationManager.notify(NOTIFICATION_ID, id, notification);
	        	isNotificationShown=true;
	        } catch (NullPointerException npe) {
	            npe.printStackTrace();
	        }
	    }
	
	/**
	 * cancelNotification：取消状态栏内容的显示
	 * @param mContext
	 * @param id
	 */
	public void cancelNotification(Context mContext,int id){

	    try {
	    	mNotificationManager.cancel(NOTIFICATION_ID, id);
	    	isNotificationShown=false;
	    } catch (NullPointerException npe) {
	        ALog.Log("setNotificationVisible: cancel notificationManager npe=" + npe);
	        npe.printStackTrace();
	    }
	}

    private DialogInterface mShowDialog;
    private void showDialog() {
        // TODO: DialogFragment?
    	mShowDialog = new AlertDialog.Builder(this).setTitle(
                getResources().getString(R.string.app_name))
                .setIcon(R.drawable.ic_notfound)
                .setMessage(getResources().getString(R.string.title_activity_switcher_demo))
                .setPositiveButton(android.R.string.yes, this)
                .setNegativeButton(android.R.string.no, this)
                .show();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
    	String str="Nothing to show!";
        if (dialog == mShowDialog) {
            if(which == DialogInterface.BUTTON_POSITIVE){
            	str = "DialogInterface.BUTTON_POSITIVE";
            }else if(which == DialogInterface.BUTTON_NEGATIVE){
            	str = "DialogInterface.BUTTON_NEGATIVE";
            }else if(which == DialogInterface.BUTTON_NEUTRAL){
            	str = "DialogInterface.BUTTON_NEUTRAL";
            }
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        }
    }
	public void startActivityByFlags(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		//String packname = "com.lenovo.serviceit";
		//String classname = "com.lenovo.serviceit.activity.MainActivity";
		String packname = "com.mt.androidstorage";
		String classname = "com.mt.androidstorage.AndroidStorage";
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| ActivityManager.MOVE_TASK_WITH_HOME
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_TASK_ON_HOME
				| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
				);
		intent.setComponent(new ComponentName(packname, classname));
		try{
			startActivity(intent);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void startDocumentsActivity(){
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| ActivityManager.MOVE_TASK_WITH_HOME
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_TASK_ON_HOME
				| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
				);
		try{
			startActivity(intent);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void startDownloadProviderUI(){
		Intent intent = new Intent("android.intent.action.VIEW_DOWNLOADS");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| ActivityManager.MOVE_TASK_WITH_HOME
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_TASK_ON_HOME
				| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
				);
		try{
			startActivity(intent);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
		}
	}

}

