package com.mt.androidtest.permission;

import java.lang.reflect.Method;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class PermissionActivity extends BaseActivity{
    private PowerManager mPowerManager =null;
	private TelephonyManager telephonyManager=null;
	private PackageManager mPackageManager=null;
	private IntentFilter mUrgentFilter=null;
	boolean isPermissionGranted = false;
	private String [] mMethodNameFT={"setListenCall","unSetListenCall","setListenCallAnother","unSetListenCallAnother"
			,"shutdown","gotosleep","requestPermissions","requestPermissionSAW"};
	private Context mContext = null; 
    private int AndroidVersion=-1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		//ALog.Log("onCreate",this);
		mContext = getApplicationContext();
		mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		mPackageManager = getPackageManager();
		initListFTData(mMethodNameFT);
		initListActivityData(null);
		AndroidVersion=getAndroidVersion();
	}

	@Override
	public void onResume(){	
		super.onResume();
		//ALog.Log("onResume",this);
	}

	@Override
	public void onPause(){
		super.onPause();
		//ALog.Log("onPause",this);
	}	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//ALog.Log("onDestroy",this);
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
		case "requestPermissions":
			requestPermissions();
			break;		
		case "requestPermissionSAW":
			requestPermissionSAW();
			break;					
		}
	}
    
	public void requestPermissions(){
        if (RequestPermissionsActivity.startPermissionActivity(this)) {
        	return;
        }
	}
	
	/**
	 * requestPermissionSAW������SYSTEM_ALERT_WINDOWȨ��
	 * SYSTEM_ALERT_WINDOW and WRITE_SETTINGS, ������Ȩ��ΪSpecial Permissions(����Ȩ��)������ͨ���������뷽ʽ��ȡ��
	 * ������û����������ҳ�ֶ��򿪣�������Ȩ��·���ǣ�Settings->Apps->App Setting->Draw over other apps��
	 * ����ʹ��Settings.canDrawOverlays�жϵ�ǰ�Ƿ�������ʾ������Manifest�����Ȩ������Ч�ġ�
	 */
	public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
	public void requestPermissionSAW(){
		if(AndroidVersion <= 22)return;
		if (!Settings.canDrawOverlays(this)) {
	        Toast.makeText(this, "Can not DrawOverlays", Toast.LENGTH_SHORT).show();
	        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
	        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
	    }else{
	    	Toast.makeText(this, "Permission already got!", Toast.LENGTH_SHORT).show();
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(Build.VERSION.SDK_INT <= 22)return;
	    if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
	    	ALog.Log("resultCode:"+resultCode);
	        if (!Settings.canDrawOverlays(this)) {
	            // SYSTEM_ALERT_WINDOW permission not granted...
	            Toast.makeText(this, "Permission Denieddd by user, Please Check it in Settings!", Toast.LENGTH_SHORT).show();
	        } else {
	            Toast.makeText(this, "Permission Allowed!", Toast.LENGTH_SHORT).show();
	            // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
	        }
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
		if (mPackageManager.checkPermission(permissionDes, mContext.getPackageName()) == PackageManager.PERMISSION_GRANTED){  
			isGranted = true;
		    ALog.Log(mContext.getString(R.string.permission_granted,permissionDes));  
		}else{  
			isGranted = false;
		    ALog.Log(mContext.getString(R.string.permission_not_granted,permissionDes));  
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
