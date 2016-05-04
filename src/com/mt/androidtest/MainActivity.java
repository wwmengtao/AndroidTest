package com.mt.androidtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mt.androidtest.R;
public class MainActivity extends Activity implements View.OnClickListener,DialogInterface.OnClickListener{
	boolean isLogRun=true;
	boolean isPermissionGranted = false;
	TelephonyManager telephonyManager=null;
	PackageManager mPackageManager=null;
	IntentFilter mUrgentFilter=null;
	String mText=null;
	private TextView mTextView=null;
    private EditText mEditText=null;
    private ImageView mImageView=null;
    private RelativeLayout mRelativeLayout=null;
	Button btn=null;
	int [] buttonID = {R.id.btn_showsysapp,
								  R.id.btn_start_activity,
								  R.id.btn_showswitcher,
								  R.id.btn_getresource,
								  R.id.btn_showdialog,
								  R.id.btn_shutdown,
								  R.id.btn_gotosleep};
    private int mDensityDpi = 0;
    private DisplayMetrics metric=null;
    private PowerManager mPowerManager =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isLogRun)ALog.Log("====onCreate");
		setContentView(R.layout.activity_main);
		mTextView = (TextView) findViewById(R.id.textview);  
		mText = mTextView.getText().toString();
		mEditText = (EditText) findViewById(R.id.editText);  
		mEditText.setText(mText);  
		mEditText.setSelection(mText.length()); //光标一直位于内容后面，方便输入
		mRelativeLayout=(RelativeLayout) findViewById(R.id.layout_relative);  
		mRelativeLayout.setVisibility(View.GONE);
		for(int i=0;i<buttonID.length;i++){
			btn = (Button)findViewById(buttonID[i]);
			btn.setOnClickListener(this);
		}
		telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		mPackageManager = getPackageManager();
		mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        metric = getResources().getDisplayMetrics();
        mDensityDpi = metric.densityDpi;
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
		//3、从其他应用获取资源，参照onClick函数中case R.id.btn_getresource:

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
     * getDrawbleFromSrc：从src文件夹中获取图片
     * @return
     */
    public Drawable getDrawbleFromSrc(){
    	String path = "com/drawable/resource/test.png"; 
    	InputStream is = getClassLoader().getResourceAsStream(path); 
    	return Drawable.createFromStream(is, "src"); 
    }
    /**
     * getDrawbleFromAsset：从assets目录中获取png类型图片
     * @return
     */
    public Drawable getDrawbleFromAsset(){
    	InputStream is=null;
    	AssetManager asm=this.getAssets();
    	Drawable mDrawable=null;
    	try {
			is=asm.open("pic_switcher/ic_notfound.png");
			mDrawable=Drawable.createFromStream(is, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return mDrawable;
    }
    
    public Drawable getDrawableFromResourcesXml(){
    	Resources mResources = this.getResources();
    	XmlPullParser parser = mResources.getXml(R.drawable.ic_qs_bluetooth_on);
        Drawable mDrawable = null;
		try {
			mDrawable = Drawable.createFromXml(mResources, parser);
		} catch (XmlPullParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return mDrawable;
    }
    
	private Object getResourceType0(Context context,String name,String type,String packageName) {
		Object obj=null;
		int resID = 0;
		Context mContext = null;
		try {
			mContext = this.createPackageContext(packageName,
					Context.CONTEXT_IGNORE_SECURITY);
			if (mContext != null) {
				resID = getResourceID1(mContext.getResources(), name, type ,packageName);
			}
		} catch (NameNotFoundException e) {
			ALog.Log(packageName + " not found！-->" + e.getMessage());
			return null;
		}
		if(0==resID)return null;
    	switch(type){
	    	case "drawable":
	    		obj = mContext.getResources().getDrawable(resID);
	    	break;
	    	case "string":
	    		obj = mContext.getResources().getString(resID);
    	break;    		
    	}
		return obj;
    	
	}
    
	
	
    public Object getResourceType1(Context context,String name,String type,String packageName){
		Object obj=null;
    	int resID = 0;
        Resources mResources=null;
        PackageManager pm=context.getPackageManager();
        try {
        	mResources=pm.getResourcesForApplication(packageName);
        	if (mResources != null) {
        		resID = getResourceID0(mResources, name, type ,packageName);
        	}
        } catch(NameNotFoundException e) {
        	 e.printStackTrace();
        	 return null;
         }
		if(0==resID)return null;
    	switch(type){
	    	case "drawable":
	    		obj = mResources.getDrawable(resID);
	    	break;
	    	case "string":
	    		obj = mResources.getString(resID);
    	break;    		
    	}
		return obj;
	}
    
    public int getResourceID0(Resources mResources, String name, String type, String packageName){
    	return mResources.getIdentifier(name, type ,packageName);
    }
    
    public int getResourceID1(Resources mResources, String name, String type, String packageName){
    	return mResources.getIdentifier(packageName+":"+type+"/"+name,null,null);

    }
    
    public void getSystemResource(){
    	//无需任何权限可以直接获取
        boolean isCellBroadcastAppLinkEnabled = false;
        int resId = getResources().getIdentifier("config_cellBroadcastAppLinks", "bool", "android");
        if (resId > 0) {
            isCellBroadcastAppLinkEnabled = this.getResources().getBoolean(resId);
        }
        ALog.Log("android_isCellBroadcastAppLinkEnabled:"+isCellBroadcastAppLinkEnabled);
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
				//startActivityByFlags();
				startDocumentsActivity();
			break;
			case R.id.btn_getresource:
				getResourceBtn();
			break;
			case	R.id.btn_showdialog:
				showDialog();
			break;
			case	R.id.btn_shutdown:
				powerOperate("shutdown");
				//powerOperate2("shutdown");
			break;
			case R.id.btn_gotosleep:
				//powerOperate("goToSleep");
				powerOperate2("goToSleep");
			break;
		}
	}
	
	public void getResourceBtn(){
		if(!mRelativeLayout.isShown()){
			mRelativeLayout.setVisibility(View.VISIBLE);
    		mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.wheat));
			mTextView = (TextView) findViewById(R.id.tv_relative);
			mImageView = (ImageView) findViewById(R.id.img_relative);
    		setLayoutParams(mImageView);
    		setTextView();
    		setImageView();
		}else{
			mRelativeLayout.setVisibility(View.GONE);
		}
	}
	
	public void setTextView(){
		String packageName = "com.lenovo.powersetting";//其他应用的包名
    	String name="app_name";
    	String type = "string";
    	Object obj1 = getResourceType0(this, name, type ,packageName);
    	if(null!=obj1){
    		mTextView.setText((String)obj1);
    	}
	}
	
	public void setImageView(){
		Drawable mDrawable=null;
		//
		String packageName = "com.lenovo.powersetting";//其他应用的包名
    	//packageName = getPackageName();//本应用的包名
    	String name = "ic_launcher";
    	String type = "drawable";
    	Object obj0 = getResourceType1(this, name, type ,packageName);
    	if(null!=obj0){
    		mDrawable = (Drawable)obj0;//getIdentifier获取图片资源
    	}
		//mDrawable = getDrawbleFromSrc();//从src中获取图片资源
		//mDrawable = getDrawbleFromAsset();//从assets中获取图片资源
    	//mDrawable = getDrawbleFromAssetXml();
		//mDrawable = getDrawableFromResourcesXml();//从系统xml资源获取图片
		mImageView.setBackground(mDrawable);
	}
	
    public void setLayoutParams(View mView){
    	ViewGroup.LayoutParams lp = mView.getLayoutParams();
    	lp.width= (int)(mDensityDpi*0.3);//144;
    	lp.height = (int)(mDensityDpi*0.3);//144;
    	mView.setLayoutParams(lp);
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
		try{
			startActivity(intent);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
		}
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

