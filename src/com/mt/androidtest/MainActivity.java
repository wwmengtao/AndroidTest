package com.mt.androidtest;

import java.lang.reflect.Method;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;


public class MainActivity extends Activity {
	boolean isLogRun=true;
	TelephonyManager telephonyManager;
	IntentFilter mUrgentFilter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(isLogRun)ALog.Log("====onCreate");
		telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		testFunctionsRegister();
	}
	
	@Override
	protected void onResume(){	
		super.onResume();
		if(isLogRun)ALog.Log("====onResume");
		testFunctions();
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
		testFunctionsUnRegister();
	}	
	
	public void testFunctions(){
		//1、反射调用
		//reflectCall();
		//reflectCallListAll();
		//2、检测组件是否存在
		//checkComponentExist();
	}
	
	public void testFunctionsRegister(){
		//1、监听来电状态
		setListenCall();
		setListenCallAnother();
	}
	
	public void testFunctionsUnRegister(){
		//1、取消监听来电状态
		unSetListenCall();
		unSetListenCallAnother();
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
    
    private boolean isLaunchIntentForPackageNull(String packageName){
    	Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
    	return null==intent;
    }
    
    /**
     *setListenCall：不需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
    public void setListenCall(){
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    
    public void unSetListenCall(){
    	 telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
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
                case TelephonyManager.CALL_STATE_IDLE ://电话挂断，注册之后就会监听到
                	ALog.Log("CALL_STATE_IDLE");
                    break;                    
                default:
                    break;
            }
        }
    };
    
    /**
     * setListenCallAnother：需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * 注意：满足1)测试机是android6.0以上版本;2)编译环境的 targetSdkVersion大于22这两个条件，上述READ_PHONE_STATE权限申请不到，需要设置
     * 用户交互界面让用户手动授权
     */
    public void setListenCallAnother(){
    	String permissionDes = Manifest.permission.READ_PHONE_STATE;
    	if(!checkPermissionGranted(permissionDes))return;
		mUrgentFilter = new IntentFilter();
		mUrgentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		this.registerReceiver(mIncallReceiver, mUrgentFilter);
    }
    
    public void unSetListenCallAnother(){
    	this.unregisterReceiver(mIncallReceiver);
    }
	private BroadcastReceiver mIncallReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ALog.Log("mIncallReceiver");
		}
	};
    
	/**
	 * checkPermissionGranted：判断是否支持对应权限
	 * @param permissionDes
	 * @return
	 */
	public boolean checkPermissionGranted(String permissionDes){
		boolean isGranted = false;
		Context context = getApplicationContext();  
		if (context.getPackageManager().checkPermission(permissionDes,
		        context.getPackageName()) == PackageManager.PERMISSION_GRANTED){  
			isGranted = true;
		    ALog.Log(getApplicationContext().getString(R.string.permission_granted,permissionDes));  
		}  
		else{  
			isGranted = false;
		    ALog.Log(getApplicationContext().getString(R.string.permission_not_granted,permissionDes));  
		}  
		return isGranted;
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
}

