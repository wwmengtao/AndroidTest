package com.mt.androidtest;
import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.util.Log;

public class ALog {
	public  static String TAG_M = "M_T_AT";
	static Activity mActivity=null;
	private static WeakReference<Activity> mActivityReference=null;
	public static void Log(String info){
		Log.e(TAG_M,info);
	}

	public static void fillInStackTrace(String info){
		RuntimeException RTE = new RuntimeException(info);
		RTE.fillInStackTrace();
		Log.e(TAG_M,"Called:", RTE);
	}

	public static String toHexString(int mInt){
		return Integer.toHexString(mInt);
	}
	
	//eg:parseHexString("11"), result is 17
	public static int parseHexString(String mData){
		return Integer.parseInt(mData,16);
	}	
	
	public static void Log(String info, Activity activity){
		mActivityReference=new WeakReference<Activity >(activity);
		mActivity=mActivityReference.get();
		if(null!=mActivity){
			String str = mActivity.toString();
			String packageName = mActivity.getPackageName();
			if(null==str||null==packageName)return;
			String str_return=null;
			String regShowWidthAndHeight = "@[a-zA-Z0-9]+";//仅仅获取控件id，其他内容不要
		    Pattern mPatternShowWidthAndHeight = Pattern.compile(regShowWidthAndHeight);
		    Matcher mMatcher = null;
	        mMatcher = mPatternShowWidthAndHeight.matcher(str);
	        if(mMatcher.find()){
	        	str_return = str.replace(mMatcher.group(),"").replace(packageName+".", "");
	        }
			Log(info+"===="+str_return);
		}
	}
}
