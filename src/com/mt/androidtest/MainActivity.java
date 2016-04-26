package com.mt.androidtest;

import java.lang.reflect.Method;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
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
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mt.androidtest.R;
public class MainActivity extends Activity implements View.OnClickListener{
	boolean isLogRun=true;
	boolean isPermissionGranted = false;
	TelephonyManager telephonyManager=null;
	PackageManager mPackageManager=null;
	IntentFilter mUrgentFilter=null;
	String mText=null;
	private TextView mTextView ;  
    private EditText mEditText;  
	Button btn=null;
	int [] buttonID = {R.id.btn_showsysapp,
								  R.id.btn_start_activity,
								  R.id.btn_showswitcher};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTextView = (TextView) findViewById(R.id.textview);  
		mText = mTextView.getText().toString();
		mEditText = (EditText) findViewById(R.id.editText);  
		mEditText.setText(mText);  
		mEditText.setSelection(mText.length()); //���һֱλ�����ݺ��棬��������
		for(int i=0;i<buttonID.length;i++){
			btn = (Button)findViewById(buttonID[i]);
			btn.setOnClickListener(this);
		}

		if(isLogRun)ALog.Log("====onCreate");
		telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		mPackageManager = getPackageManager();
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
		//1���������
		//reflectCall();
		//reflectCallListAll();
		//2���������Ƿ����
		//checkComponentExist();
		//3��������Ӧ�û�ȡ��Դ
		getCertainResources();
	}
	
	public void testFunctionsRegister(){
		//1����������״̬
		setListenCall();
		setListenCallAnother();
	}
	
	public void testFunctionsUnRegister(){
		//1��ȡ����������״̬
		unSetListenCall();
		unSetListenCallAnother();
	}
    @Override
    //onConfigurationChanged����������AndroidManifest������android:configChanges="orientation|keyboardHidden|screenSize"��Ż����
    //���û������android:configChanges���ԣ���ô�ֻ�������ʱ��Activity���ػ�Ӷ�����onCreate��onResume�Ⱥ���
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

	private void getResourceID0(Context context,String name,String type,String packageName) {
		int resID = 0;
		Context mResources = null;
		String str=null;
		try {
			mResources = this.createPackageContext(packageName,
					Context.CONTEXT_IGNORE_SECURITY);
			if (mResources != null) {
				resID = mResources.getResources().getIdentifier(
						packageName+":"+type+"/"+name, null, null);
			}
		} catch (NameNotFoundException e) {
			ALog.Log(packageName + " not found��-->" + e.getMessage());
		}
     	if(0!=resID){
    		str = getResources().getString(resID);
    	}
    	ALog.Log(packageName+"_getResourceID0_Resource:"+str);
	}
    
    public void getResourceID1(Context context,String name,String type,String packageName){
    	int resID = 0;
    	String str = null;
        Resources mResources=null;
        PackageManager pm=context.getPackageManager();
        try {
        	mResources=pm.getResourcesForApplication(packageName);
        	if (mResources != null) {
        		resID = mResources.getIdentifier(name, type, packageName);
        	}
        } catch(NameNotFoundException e) {
        	 e.printStackTrace();
         }
     	if(0!=resID){
    		str = getResources().getString(resID);
    	}
    	ALog.Log(packageName+"_getResourceID1_Resource:"+str);
	}
    
    public void getResourceID2(String name,String type,String packageName){
    	int resID = 0;
    	String str = null;
    	resID = getResources().getIdentifier(name, type ,packageName);
     	if(0!=resID){
    		str = getResources().getString(resID);
    	}
    	ALog.Log(packageName+"_getResourceID2_Resource:"+str);
    }
    
    public void getResourceID3(String name,String type,String packageName){
    	int resID = 0;
    	String str = null;
    	resID =  getResources().getIdentifier(packageName+":"+type+"/"+name,null,null);
     	if(0!=resID){
    		str = getResources().getString(resID);
    	}
    	ALog.Log(packageName+"_getResourceID3_Resource:"+str);
    }
    
    public void getSystemResource(){
    	//�����κ�Ȩ�޿���ֱ�ӻ�ȡ
        boolean isCellBroadcastAppLinkEnabled = false;
        int resId = getResources().getIdentifier("config_cellBroadcastAppLinks", "bool", "android");
        if (resId > 0) {
            isCellBroadcastAppLinkEnabled = this.getResources().getBoolean(resId);
        }
        ALog.Log("android_isCellBroadcastAppLinkEnabled:"+isCellBroadcastAppLinkEnabled);
    }
    
    public void getCertainResources(){
    	boolean thisApplication=false;
    	String name = "app_name";
    	String type = "string";
    	String packageNameOther = "com.example.androidtest2";
    	if(thisApplication){
	    	//һ����ȡ��Ӧ�õ���Դ
	    	getResourceID0(this, name, type ,getPackageName()); 	
	    	getResourceID1(this, name, type ,getPackageName());
	    	getResourceID2(name, type ,getPackageName());
	    	getResourceID3(name, type ,getPackageName());
    	}else{
    	//������ȡ����Ӧ�õ���Դ
	    	getResourceID0(this, name, type ,packageNameOther);
	    	getResourceID1(this, name, type ,packageNameOther);
	    	getResourceID2(name, type ,packageNameOther);
	    	getResourceID3(name, type ,packageNameOther);
    	}
    	//������ȡϵͳ��Դ
    	//getSystemResource();
    }

    
    /**
     * checkComponentExist������������Ƿ���ڵļ��ַ���
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
     * isComponentExist������Ƿ��Ӧ���������
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
     * hasComponent������Ƿ��Ӧ���������
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
	 * getComponentName����ȡ��Ӧ�����Ӧ������
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
     *setListenCall������ҪȨ��<uses-permission android:name="android.permission.READ_PHONE_STATE" />
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
     * setListenCallAnother����ҪȨ��<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * ע�⣺����1)���Ի���android6.0���ϰ汾;2)���뻷���� targetSdkVersion����22����������������READ_PHONE_STATEȨ�����벻������Ҫ����
     * �û������������û��ֶ���Ȩ
     */
    public void setListenCallAnother(){
    	String permissionDes = Manifest.permission.READ_PHONE_STATE;
    	isPermissionGranted = checkPermissionGranted(permissionDes);
    	if(!isPermissionGranted)return;
		mUrgentFilter = new IntentFilter();
		mUrgentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		this.registerReceiver(mIncallReceiver, mUrgentFilter);
    }
    
    public void unSetListenCallAnother(){
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
	 * reflectCall������setDataEnabledUsingSubId��setDataEnabled�ĸ���������
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
	 * reflectCallListAll�����г����еĴ����÷�������������ʡȥreflectCall�е�try-catch�ṹ�Լ������йط����������鷳
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
				m.invoke(telephonyManager, subId, true);//�򿪿���subId����������
			}catch(Exception ex){
				
			}
		}

	}
	
	/**
	 * isMethodExist���ж�ָ�����Ƶķ����Ƿ����
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

}

