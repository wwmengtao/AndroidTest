package com.mt.androidtest.showview;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.mt.androidtest.ALog;

public class PhoneViewInfo {
	private Activity mActivity=null;
	public PhoneViewInfo(Activity mActivity){
		this.mActivity = mActivity;
	}
	public void showPhoneViewInfo(){
		//1、去除状态栏/通知栏
		mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//2、获取导航栏高度
		getNavigationBarHeight(mActivity);
		//3、获取状态栏/通知栏高度
		getStatusBarHeight(mActivity);
		//4、获取标题栏高度，见onWindowFocusChanged函数
		//5、获取手机屏幕的宽度、高度
		getPhoneWidth(mActivity);
		getPhoneHeight(mActivity);
	}
	
	/**
	 * getPhoneWidthHeight:获取手机屏幕的宽度、高度
	 */
	public static int getPhoneWidth(Context mContext){
		DisplayMetrics dm =mContext.getResources().getDisplayMetrics();  
        int width = dm.widthPixels;  
        ALog.Log("getPhoneWidth_width:"+width);
        return width;
	}
	
	public static int getPhoneHeight(Context mContext){
		DisplayMetrics dm =mContext.getResources().getDisplayMetrics();  
        int height = dm.heightPixels; 
        //对于含有导航栏的手机来说，height是扣除导航栏高度后的屏幕剩余高度
        ALog.Log("getPhoneWidth_height:"+height);
        return height;
	}

	
	/**
	 * getNavigationBarHeight:对于没有物理按键的手机来说，此函数可以获取导航栏的高度
	 * @param mContext
	 * @return
	 */
	private int NavigationBarHeight = 0;
	public void getNavigationBarHeight(Context mContext) {
		boolean hasMenuKey = ViewConfiguration.get(mContext).hasPermanentMenuKey();  
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK); 
        //以下说明手机有导航栏
        if (!hasMenuKey && !hasBackKey) {  
	        Resources resources = mContext.getResources();
	        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
	        //获取NavigationBar的高度
	        NavigationBarHeight = resources.getDimensionPixelSize(resourceId);
	        ALog.Log("NavigationBarHeight:"+NavigationBarHeight);
        }
	}
	
	/**
	 * getStatusBarHeight:获取状态栏/通知栏的高度
	 * @param context
	 * @return
	 */
	public int StatusBarHeight = 0;
	public void getStatusBarHeight(Context context){
		//1、直接调用
		int resourceId = mActivity.getResources().getIdentifier("status_bar_height", "dimen", "android"); 
		if (resourceId > 0) {  
		    //根据资源ID获取响应的尺寸值  
			StatusBarHeight = mActivity.getResources().getDimensionPixelSize(resourceId);  
			ALog.Log("StatusBarHeight1:"+StatusBarHeight);
		}  
		//2、反射调用
		Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            StatusBarHeight = context.getResources().getDimensionPixelSize(x);
            ALog.Log("StatusBarHeight2:"+StatusBarHeight);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
	}

}
