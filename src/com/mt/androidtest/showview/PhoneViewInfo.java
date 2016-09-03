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
		//1��ȥ��״̬��/֪ͨ��
		mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//2����ȡ�������߶�
		getNavigationBarHeight(mActivity);
		//3����ȡ״̬��/֪ͨ���߶�
		getStatusBarHeight(mActivity);
		//4����ȡ�������߶ȣ���onWindowFocusChanged����
		//5����ȡ�ֻ���Ļ�Ŀ�ȡ��߶�
		getPhoneWidth(mActivity);
		getPhoneHeight(mActivity);
	}
	
	/**
	 * getPhoneWidthHeight:��ȡ�ֻ���Ļ�Ŀ�ȡ��߶�
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
        //���ں��е��������ֻ���˵��height�ǿ۳��������߶Ⱥ����Ļʣ��߶�
        ALog.Log("getPhoneWidth_height:"+height);
        return height;
	}

	
	/**
	 * getNavigationBarHeight:����û�����������ֻ���˵���˺������Ի�ȡ�������ĸ߶�
	 * @param mContext
	 * @return
	 */
	private int NavigationBarHeight = 0;
	public void getNavigationBarHeight(Context mContext) {
		boolean hasMenuKey = ViewConfiguration.get(mContext).hasPermanentMenuKey();  
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK); 
        //����˵���ֻ��е�����
        if (!hasMenuKey && !hasBackKey) {  
	        Resources resources = mContext.getResources();
	        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
	        //��ȡNavigationBar�ĸ߶�
	        NavigationBarHeight = resources.getDimensionPixelSize(resourceId);
	        ALog.Log("NavigationBarHeight:"+NavigationBarHeight);
        }
	}
	
	/**
	 * getStatusBarHeight:��ȡ״̬��/֪ͨ���ĸ߶�
	 * @param context
	 * @return
	 */
	public int StatusBarHeight = 0;
	public void getStatusBarHeight(Context context){
		//1��ֱ�ӵ���
		int resourceId = mActivity.getResources().getIdentifier("status_bar_height", "dimen", "android"); 
		if (resourceId > 0) {  
		    //������ԴID��ȡ��Ӧ�ĳߴ�ֵ  
			StatusBarHeight = mActivity.getResources().getDimensionPixelSize(resourceId);  
			ALog.Log("StatusBarHeight1:"+StatusBarHeight);
		}  
		//2���������
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
