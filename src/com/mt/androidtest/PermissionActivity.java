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
		case "setListenCallAnother"://�������Ƿ����
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
     *setListenCall������ҪȨ��<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
    public void setListenCall(){//��������״̬
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    
    public void unSetListenCall(){//ȡ����������״̬
    	 telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state) {
                case TelephonyManager.CALL_STATE_RINGING ://�绰����
                	ALog.Log("CALL_STATE_RINGING");
                	break;
                case TelephonyManager.CALL_STATE_OFFHOOK ://�绰����
                	ALog.Log("CALL_STATE_OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_IDLE ://�绰�Ҷϣ�ע��֮��ͻ������
                	ALog.Log("CALL_STATE_IDLE");
                    break;                    
                default:
                    break;
            }
        }
    };
    
    /**
     * TelephonyManager.ACTION_PHONE_STATE_CHANGED����ҪȨ��<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * ע�⣺����1)���Ի���android6.0���ϰ汾;2)���뻷���� targetSdkVersion����22����������������READ_PHONE_STATEȨ�����벻������Ҫ����
     * �û������������û��ֶ���Ȩ
     */
    public void setListenCallAnother(){//��������״̬
    	String permissionDes = Manifest.permission.READ_PHONE_STATE;
    	isPermissionGranted = checkPermissionGranted(permissionDes);
    	if(!isPermissionGranted)return;
		mUrgentFilter = new IntentFilter();
		mUrgentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		this.registerReceiver(mIncallReceiver, mUrgentFilter);
    }
    
    public void unSetListenCallAnother(){//ȡ����������״̬
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
	 * checkPermissionGranted���ж��Ƿ�֧�ֶ�ӦȨ��
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
	 *  һ��PowerManager.goToSleep(long time, int reason, int flags)
	 *  A.��Ч��������
	 *  1)��Ҫ<uses-permission android:name="android.permission.DEVICE_POWER" />Ȩ��;
	 *  2)ƽ̨ǩ��
	 *  3)λ��system/priv-app����(system/app)��
	 *  B.PowerManager.goToSleep�У�reason��ȡ����ֵ���£�
	 *  public static final int GO_TO_SLEEP_REASON_APPLICATION = 0;
	 *  public static final int GO_TO_SLEEP_REASON_DEVICE_ADMIN = 1;
	 *  public static final int GO_TO_SLEEP_REASON_TIMEOUT = 2;
	 *  public static final int GO_TO_SLEEP_REASON_LID_SWITCH = 3;
	 *  public static final int GO_TO_SLEEP_REASON_POWER_BUTTON = 4;
	 *  ����PowerManager.shutdown(boolean confirm, boolean wait)��Ч��������
	 *  1)��Ҫ<uses-permission android:name="android.permission.REBOOT" />Ȩ��;
	 *  2)ƽ̨ǩ��
	 *  3)λ��system/priv-app����(system/app)��
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
