package com.mt.androidtest;

import java.lang.reflect.Method;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;

public class PermissionActivity extends BaseActivity{
    private PowerManager mPowerManager =null;
	private TelephonyManager telephonyManager=null;
	private PackageManager mPackageManager=null;
	private IntentFilter mUrgentFilter=null;
	boolean isPermissionGranted = false;
	private String [] mMethodNameFT={"setListenCall","unSetListenCall","setListenCallAnother","unSetListenCallAnother"
			,"shutdown","gotosleep"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ALog.Log("onCreate",this);
		mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		mPackageManager = getPackageManager();
		initListFTData(mMethodNameFT);
		initListActivityData(null);
	}

	@Override
	public void onResume(){	
		super.onResume();
		ALog.Log("onResume",this);
	}

	@Override
	public void onPause(){
		super.onPause();
		ALog.Log("onPause",this);
	}	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ALog.Log("onDestroy",this);
	}	
	
	@Override
	public void initListFTData(String [] mMethodNameFT){
		super.initListFTData(mMethodNameFT);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		switch(mMethodNameFT[position]){
		case "setListenCall":
			setListenCall();
			break;
		case "unSetListenCall":
			unSetListenCall();
			break;
		case "setListenCallAnother"://检测组件是否存在
			setListenCallAnother();
			break;
		case "unSetListenCallAnother":
			unSetListenCallAnother();
			break;					
		case "shutdown":
			powerOperate("shutdown");
			//powerOperate2("shutdown");
			break;		
		case "gotosleep":
			//powerOperate("goToSleep");
			powerOperate2("goToSleep");
			break;					
		}
	
	}
    
    /**
     *setListenCall：不需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
    public void setListenCall(){//监听来电状态
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    
    public void unSetListenCall(){//取消监听来电状态
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
     * TelephonyManager.ACTION_PHONE_STATE_CHANGED：需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * 注意：满足1)测试机是android6.0以上版本;2)编译环境的 targetSdkVersion大于22这两个条件，上述READ_PHONE_STATE权限申请不到，需要设置
     * 用户交互界面让用户手动授权
     */
    public void setListenCallAnother(){//监听来电状态
    	String permissionDes = Manifest.permission.READ_PHONE_STATE;
    	isPermissionGranted = checkPermissionGranted(permissionDes);
    	if(!isPermissionGranted)return;
		mUrgentFilter = new IntentFilter();
		mUrgentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		this.registerReceiver(mIncallReceiver, mUrgentFilter);
    }
    
    public void unSetListenCallAnother(){//取消监听来电状态
    	if(isPermissionGranted){
    		this.unregisterReceiver(mIncallReceiver);
    	}
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
		if (mPackageManager.checkPermission(permissionDes,
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
	 *  一、PowerManager.goToSleep(long time, int reason, int flags)
	 *  A.生效的条件：
	 *  1)需要<uses-permission android:name="android.permission.DEVICE_POWER" />权限;
	 *  2)平台签名
	 *  3)位于system/priv-app或者(system/app)下
	 *  B.PowerManager.goToSleep中，reason可取的数值如下：
	 *  public static final int GO_TO_SLEEP_REASON_APPLICATION = 0;
	 *  public static final int GO_TO_SLEEP_REASON_DEVICE_ADMIN = 1;
	 *  public static final int GO_TO_SLEEP_REASON_TIMEOUT = 2;
	 *  public static final int GO_TO_SLEEP_REASON_LID_SWITCH = 3;
	 *  public static final int GO_TO_SLEEP_REASON_POWER_BUTTON = 4;
	 *  二、PowerManager.shutdown(boolean confirm, boolean wait)生效的条件：
	 *  1)需要<uses-permission android:name="android.permission.REBOOT" />权限;
	 *  2)平台签名
	 *  3)位于system/priv-app或者(system/app)下
	 */
	public void powerOperate(String methodName){
		if (mPowerManager != null) {
			Class<?> mClass = mPowerManager.getClass();
			Method mMethod = null;
			try {
				if(methodName.equals("shutdown")){
					mMethod = mClass.getMethod(methodName, boolean.class, boolean.class);
					mMethod.invoke(mPowerManager, true, false);
				}else if(methodName.equals("goToSleep")){
					mMethod= mClass.getMethod("goToSleep",new Class[]{long.class,int.class,int.class});
					mMethod.invoke(mPowerManager, SystemClock.uptimeMillis(),4,0);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void powerOperate2(String methodName){
	try {
			Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
			Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
			Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);
			Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
			Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
			Object oIPowerManager = asInterface.invoke(null, oRemoteService);
			Method mMethod = null;
			if(methodName.equals("shutdown")){
				mMethod = oIPowerManager.getClass().getMethod(methodName,	boolean.class, boolean.class);
				mMethod.invoke(oIPowerManager, true, false);
			}else if(methodName.equals("goToSleep")){
				mMethod= oIPowerManager.getClass().getMethod(methodName,new Class[]{long.class,int.class,int.class});
				mMethod.invoke(oIPowerManager, SystemClock.uptimeMillis(),4,0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
