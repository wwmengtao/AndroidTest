package com.mt.androidtest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mt.androidtest.R;

public class MainActivity extends Activity implements View.OnClickListener,DialogInterface.OnClickListener{
	boolean isLogRun=false;
	boolean isPermissionGranted = false;
	TelephonyManager telephonyManager=null;
	PackageManager mPackageManager=null;
	IntentFilter mUrgentFilter=null;
	String mText=null;
	private TextView mTextView=null;
    private EditText mEditText=null;
    private ImageView mImageView=null;
    private RelativeLayout mRelativeLayout=null;
	private LinearLayout mLayout=null;
	private LinearLayout mLayout_linear_buttons=null;
	private LinearLayout mLayout_linear_switchbar=null;
	private TextView mTextView_Switchbar=null;
	int [] buttonID = {R.id.btn_showsysapp,
								  R.id.btn_start_activity,
								  R.id.btn_start_documentsactivity,
								  R.id.btn_start_downloadproviderui,
								  R.id.btn_showswitcher,
								  R.id.btn_getresource,
								  R.id.btn_showdialog,
								  R.id.btn_shownotification,
								  R.id.btn_shutdown,
								  R.id.btn_gotosleep,
								  R.id.btn_showview_test};
    private int mDensityDpi = 0;
    private DisplayMetrics metric=null;
    private PowerManager mPowerManager =null;
    private NotificationManager mNotificationManager = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isLogRun)ALog.Log("====onCreate");
		setContentView(R.layout.activity_main);
		mTextView = (TextView) findViewById(R.id.textview);  
		mText = mTextView.getText().toString();
		mEditText = (EditText) findViewById(R.id.editText);  
		mEditText.setText(mText);  
		mEditText.setSelection(mText.length()); //���һֱλ�����ݺ��棬��������
		mRelativeLayout=(RelativeLayout) findViewById(R.id.layout_relative);  
		mLayout=(LinearLayout) findViewById(R.id.layout_linear_test);
		mLayout_linear_buttons=(LinearLayout) findViewById(R.id.layout_linear_buttons21);
		mTextView_Switchbar=(TextView) findViewById(R.id.txStatus);
		mLayout_linear_switchbar=(LinearLayout) findViewById(R.id.switch_bar);
		Button btn=null;
		for(int i=0;i<buttonID.length;i++){
			btn = (Button)findViewById(buttonID[i]);
			btn.setOnClickListener(this);
		}
		telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		mPackageManager = getPackageManager();
		mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        metric = getResources().getDisplayMetrics();
        mDensityDpi = metric.densityDpi;
		//testFunctionsRegister();
		//addOnGlobalLayoutListener();//��ͼ��������
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
		//testFunctionsUnRegister();
	}	
	
	public void testFunctions(){
		//1���������
		//reflectCall();
		//reflectCallListAll();
		//2���������Ƿ����
		//checkComponentExist();
		//3��������Ӧ�û�ȡ��Դ������onClick������case R.id.btn_getresource��������ʵ��
		setSwitchBarBackground();
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
    
    /**
     * getDrawbleFromSrc����src�ļ����л�ȡͼƬ
     * @return
     */
    public Drawable getDrawbleFromSrc(){
    	String path = "com/drawable/resource/test.png"; 
    	InputStream is = getClassLoader().getResourceAsStream(path); 
    	return Drawable.createFromStream(is, "src"); 
    }
    /**
     * getDrawbleFromAsset����assetsĿ¼�л�ȡpng����ͼƬ
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
    
    public Drawable getDrawbleFromAssetXml(){
    	Drawable mDrawable = null;
    	XmlPullParser mXmlPullParser = null;
        InputStream mInputStream = null;
        Resources mResources = getResources();
        AssetManager mAM=null;
        try {
        	mAM = mResources.getAssets();
        	mInputStream = mAM.open("pic_switcher/vpn.xml"); 
        	mXmlPullParser = Xml.newPullParser();
        	mXmlPullParser.setInput(mInputStream, StandardCharsets.UTF_8.name());
            mDrawable = Drawable.createFromXml(mResources,  mXmlPullParser);
        } catch (XmlPullParserException e) {
        	ALog.fillInStackTrace("getDrawbleFromAssetXml.XmlPullParserException");
        } catch (IOException e) {
        	ALog.fillInStackTrace("getDrawbleFromAssetXml.Exception");
        }finally {
            try {
            	if(null!=mInputStream)mInputStream.close();
            } catch (IOException e) {
            	ALog.fillInStackTrace("getDrawbleFromAssetXml.IOException");
            }
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
    
    /**
     * ��ȡָ��packageName�е���Դ
     * @param context
     * @param name
     * @param type
     * @param packageName
     * @return
     */
	private Object getResourceType0(Context context,String name,String type,String packageName) {
		Object obj=null;
		int resID = 0;
		Context mContext = null;
		try {
			mContext = context.createPackageContext(packageName,
					Context.CONTEXT_IGNORE_SECURITY);
			if (mContext != null) {
				resID = getResourceID1(mContext.getResources(), name, type ,packageName);
			}
		} catch (NameNotFoundException e) {
			ALog.Log(packageName + " not found��-->" + e.getMessage());
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
	    	case "color":
	    		obj = mContext.getResources().getColor(resID);	    		
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
    	//�����κ�Ȩ�޿���ֱ�ӻ�ȡ
        boolean isCellBroadcastAppLinkEnabled = false;
        int resId = getResources().getIdentifier("config_cellBroadcastAppLinks", "bool", "android");
        if (resId > 0) {
            isCellBroadcastAppLinkEnabled = this.getResources().getBoolean(resId);
        }
        ALog.Log("android_isCellBroadcastAppLinkEnabled:"+isCellBroadcastAppLinkEnabled);
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
			case	R.id.btn_start_documentsactivity:
				startDocumentsActivity();
			break;			
			case	R.id.btn_start_downloadproviderui:
				startDownloadProviderUI();
			break;					
			case R.id.btn_getresource:
				getResourceBtn();
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
			case	R.id.btn_shutdown:
				powerOperate("shutdown");
				//powerOperate2("shutdown");
			break;
			case R.id.btn_gotosleep:
				//powerOperate("goToSleep");
				powerOperate2("goToSleep");
			break;
			case R.id.btn_showview_test:		
				intent=new Intent();
				intent.setClass(MainActivity.this, ShowViewActivity.class);
				startActivity(intent);
			break;					
		}
	}
	
	String NOTIFICATION_ID="AndroidTest.Notification";
	boolean isNotificationShown=false;
	/**
	 * ��ʾ֪ͨ
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
	 * cancelNotification��ȡ��״̬�����ݵ���ʾ
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
	
	ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener =new ViewTreeObserver.OnGlobalLayoutListener(){
		@Override
		public void onGlobalLayout() {
			ALog.Log("/------------------------onGlobalLayout------------------------/");
			showWidthAndHeight_onGlobalLayout(mEditText, "mEditText");	
			showWidthAndHeight_onGlobalLayout(mRelativeLayout, "mRelativeLayout");	
			showWidthAndHeight_onGlobalLayout(mLayout, "mLayout");
			showWidthAndHeight_onGlobalLayout(mLayout_linear_buttons, "mLayout_linear_buttons");
			ALog.Log("/************************onGlobalLayout************************/");
		}
	};
	
	public void showWidthAndHeight_onGlobalLayout(View mView, String objName){
		if(null!=mView){
			if(0!=mView.getWidth()||0!=mView.getHeight()){
				mView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener); 
				showWidthAndHeight(mView, objName);
			}	
		}
	}
	
	public void addOnGlobalLayoutListener(){
		mEditText.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
		mRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
		mLayout.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
		mLayout_linear_buttons.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
	}
	
	/**
	 * onWindowFocusChanged��
		1���������ʱִ��˳�����£�
		onStart--->onResume--->onAttachedToWindow--->onWindowVisibilityChanged--visibility=0--->onWindowFocusChanged(true)--->
		ִ�е�onWindowFocusChanged�����Ѿ���ȡ���㣬��ʱView�Ļ��ƹ����Ѿ���ɣ����Ի�ȡView�ؼ��Ŀ�ȡ��߶ȡ�
		2���뿪���ʱ
		2.1)onPause---->onStop---->onWindowFocusChanged(false)  --- (lockscreen)
		2.2)onPause--->onWindowFocusChanged(false)--->onWindowVisibilityChanged--visibility=8--->onStop(to another activity)
	 * @param hasFocus
	 */
    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		//ALog.Log("/------------------------onWindowFocusChanged------------------------/");
		//showWidthAndHeightLog();
		//ALog.Log("/************************onWindowFocusChanged************************/");
	}
    
    public void showWidthAndHeightLog(){
    	//showWidthAndHeight(mTextViewAdded, "mTextViewAdded");	
		showWidthAndHeight(mEditText, "mEditText");				
		showWidthAndHeight(mRelativeLayout, "mRelativeLayout");					
		showWidthAndHeight(mLayout, "mLayout");			
		showWidthAndHeight(mLayout_linear_buttons, "mLayout_linear_buttons");	
    }
    
	String regShowWidthAndHeight = "id+\\/[a-zA-Z]+.+\\}";//������ȡ�ؼ�id���������ݲ�Ҫ
    Pattern mPatternShowWidthAndHeight = Pattern.compile(regShowWidthAndHeight);
    Matcher mMatcher = null;
	boolean is_onWindowFocusChanged = false;
    public void showWidthAndHeight(View mView, String objName){
    	if(!is_onWindowFocusChanged){
    		String betweenTitle=" ";
    		ALog.Log("getWidth"+betweenTitle+"getMeasuredWidth"+betweenTitle+"getHeight"+betweenTitle+"getMeasuredHeight");
    		is_onWindowFocusChanged = true;
    	}
    	String str_ALog=null;
        String str = mView.toString();
        mMatcher = mPatternShowWidthAndHeight.matcher(str);
        while(mMatcher.find()){
        	str_ALog = mMatcher.group().replace("}", "");
            break;
        }
        String format="%-14d";
        String strgetWidth = String.format(format,mView.getWidth());
        String strgetMeasuredWidth = String.format(format,mView.getMeasuredWidth());
        String strgetHeight = String.format(format,mView.getHeight());
        String strgetMeasuredHeight = String.format(format,mView.getMeasuredHeight());
        ALog.Log(strgetWidth+
        				 strgetMeasuredWidth+
        				 strgetHeight+
        				 strgetMeasuredHeight+
        				 str_ALog+":"+objName);
        /*
		ALog.Log(String.format("%-5d",mView.getWidth())+":getWidth");
		ALog.Log(String.format("%-5d",mView.getMeasuredWidth())+":getMeasuredWidth");	
		ALog.Log(String.format("%-5d",mView.getHeight())+":getHeight");
		ALog.Log(String.format("%-5d",mView.getMeasuredHeight())+":getMeasuredHeight");	
		*/
    }
    
	public void getResourceBtn(){
		if(!mRelativeLayout.isShown()){
			mRelativeLayout.setVisibility(View.VISIBLE);
			mTextView = (TextView) findViewById(R.id.tv_relative);
			mImageView = (ImageView) findViewById(R.id.img_relative);
    		setLayoutParams(mImageView);
    		setTextView();
    		setImageView();
    		getViewColors();
		}else{
			mRelativeLayout.setVisibility(View.GONE);
		}
	}
	
	public void setTextView(){
		String packageName = "com.lenovo.powersetting";//����Ӧ�õİ���
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
		String packageName = "com.lenovo.powersetting";//����Ӧ�õİ���
    	//packageName = getPackageName();//��Ӧ�õİ���
    	String name = "ic_launcher";
    	String type = "drawable";
    	Object obj0 = getResourceType1(this, name, type ,packageName);
    	if(null!=obj0){
    		mDrawable = (Drawable)obj0;//getIdentifier��ȡͼƬ��Դ
    	}
		//mDrawable = getDrawbleFromSrc();//��src�л�ȡͼƬ��Դ
		//mDrawable = getDrawbleFromAsset();//��assets�л�ȡͼƬ��Դ
		//mDrawable = getDrawableFromResourcesXml();//��ϵͳxml��Դ��ȡͼƬ
    	//mDrawable = getDrawbleFromAssetXml();//������ʱFC����������
		mImageView.setBackground(mDrawable);
	}
	
	public void setSwitchBarBackground(){
		//����Ϊϵͳ����WiFi��SwitchBar�ı�������
    	//K5M��android:background="@color/switchbar_background_color"
    	//Sisley2M��android:background="@drawable/bg_switchbar"
		String packageName="com.android.settings";
		String [][]type_name = {	{"color","switchbar_background_color"},
												{"drawable","bg_switchbar"}};
		Object obj=null;
		for(int i=0;i<type_name.length;i++){
			if(null!=(obj=getResourceType0(this,type_name[i][1],type_name[i][0],packageName))){
				switch(type_name[i][0]){
				case "color":
					mLayout_linear_switchbar.setBackgroundColor((Integer)obj);
					break;
				case "drawable":
					mLayout_linear_switchbar.setBackground((Drawable)obj);
					break;
				}
				break;
			}
		}
	}	
	
	/**
	 * ��ȡָ���ؼ�������ɫ��������ֻ�����ڱ�Ӧ�����Ѿ�������ɵĿؼ����޷���Ӧ��
	 */
	public void getViewColors(){
		TextView mTvTemp =(TextView)this.findViewById(R.id.tv_relative_color);
		String packageName=null;
		packageName=this.getPackageName();
		int textViewId =0;
		textViewId=this.getResources().getIdentifier("tv_relative", "id", packageName);
		ALog.Log("textViewId:"+ALog.toHexString(textViewId));		
		mTextView= (TextView)findViewById(textViewId);
		mTvTemp.setTextColor(mTextView.getCurrentTextColor());
		mTvTemp.setBackground(mTextView.getBackground());
		//��ȡ����package�пؼ�������ֻ�ܻ�ȡID���޷����صõ��ؼ�����Ϊ����Ӧ�û�û��show Activity
		packageName="com.android.settings";
		Context mContext;
		try {
			mContext = this.createPackageContext(packageName,Context.CONTEXT_IGNORE_SECURITY);
			if (mContext != null) {
				textViewId = mContext.getResources().getIdentifier("switch_text", "id", packageName);
				ALog.Log("textViewId:"+ALog.toHexString(textViewId));		
				//mTvTemp =(TextView)mContext.findViewById(textViewId);//�﷨����findViewByIdֻ����Activityʹ�ã�������Context
				//mTvTemp= (TextView) LayoutInflater.from(mContext).inflate(textViewId, null);//����LayoutInflaterֻ�ܽ��������ļ�
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//ALog.Log("mTextView.getText:"+mTextView.getText());
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

