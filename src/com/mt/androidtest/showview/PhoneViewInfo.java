package com.mt.androidtest.showview;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseListActivity;

public class PhoneViewInfo {
	private BaseListActivity mBaseListActivity=null;
	public PhoneViewInfo(BaseListActivity mBaseListActivity){
		this.mBaseListActivity = mBaseListActivity;
	}
	public void showPhoneViewInfo(){
		//2��ȥ��״̬��/֪ͨ��
		mBaseListActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//3����ȡ�������߶�
		getNavigationBarHeight(mBaseListActivity);
		//4����ȡ״̬��/֪ͨ���߶�
		getStatusBarHeight(mBaseListActivity);
		//5����ȡ�������߶ȣ���onWindowFocusChanged����
		//6����ȡ�ֻ���Ļ�Ŀ�ȡ��߶�
		getPhoneWidthHeight();
	}
	
	/**
	 * getPhoneWidthHeight:��ȡ�ֻ���Ļ�Ŀ�ȡ��߶�
	 */
	public void getPhoneWidthHeight(){
		DisplayMetrics dm =mBaseListActivity.getResources().getDisplayMetrics();  
        int width = dm.widthPixels;  
        int height = dm.heightPixels; 
        //���ں��е��������ֻ���˵��height�ǿ۳��������߶Ⱥ����Ļʣ��߶�
        ALog.Log("getPhoneWidthHeight width:"+width+" height:"+height);
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
		int resourceId = mBaseListActivity.getResources().getIdentifier("status_bar_height", "dimen", "android"); 
		if (resourceId > 0) {  
		    //������ԴID��ȡ��Ӧ�ĳߴ�ֵ  
			StatusBarHeight = mBaseListActivity.getResources().getDimensionPixelSize(resourceId);  
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
