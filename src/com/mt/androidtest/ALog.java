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

	public static void Log2(String info){
		Log.e(TAG_M,info+" ThreadID:"+Thread.currentThread().getId());
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
	
    static String formatStr="%-10s";
	static String regPrefix = "([a-zA-Z0-9]+\\.)+";//匹配开头：小括号在正则表达式的作用是标记一个子表达式的开始和结束位置
    static String regSuffix = "@[a-zA-Z0-9]+";//匹配结尾
    /**
     * Activity的toString内容可能类似于"com.mt.androidtest.showview.1s.sdf2.s4rt.ShowViewActivity@7d129f7"，
     * 下列函数仅仅提取ShowViewActivity之类的内容
     * @param info
     * @param activity
     */
	public static void Log(String info, Activity activity){
		mActivityReference=new WeakReference<Activity >(activity);
		mActivity=mActivityReference.get();
		if(null!=mActivity){
			String str = mActivity.toString();
			if(null==str)return;
		    Pattern mPattern = null;
		    Matcher mMatcher = null;
		    mPattern = Pattern.compile(regPrefix);
	        mMatcher = mPattern.matcher(str);
	        if(mMatcher.find()){
	        	str=str.replace(mMatcher.group(), "");
	        }
	        mPattern = Pattern.compile(regSuffix);
	        mMatcher = mPattern.matcher(str);
	        if(mMatcher.find()){
	        	str=str.replace(mMatcher.group(),"");
	        }
			Log(String.format(formatStr,info)+":"+str);
		}
	}
}
