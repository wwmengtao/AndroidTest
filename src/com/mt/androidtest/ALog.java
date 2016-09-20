package com.mt.androidtest;
import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.util.Log;

public class ALog {
	public  static String TAG_M = "M_T_AT";
	private static Activity mActivity=null;
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
	
    private static String formatStr="%-24s";
    private static String regPrefix = "([a-zA-Z0-9]+\\.)+";//ƥ�俪ͷ��С������������ʽ�������Ǳ��һ���ӱ��ʽ�Ŀ�ʼ�ͽ���λ��
    private static String regSuffix = "@[a-zA-Z0-9]+";//ƥ���β
    /**
     * Activity��toString���ݿ���������"com.mt.androidtest.showview.1s.sdf2.s4rt.ShowViewActivity@7d129f7"��
     * ���к���������ȡShowViewActivity֮�������
     * @param info
     * @param activity
     */
	public static void Log(String info, Activity activity){
		mActivityReference=new WeakReference<Activity >(activity);
		mActivity=mActivityReference.get();
		String str = getActivityName(mActivity);
		if(null != str)Log(String.format(formatStr,info)+":"+str);
	}

	public static void fillInStackTrace(String info, Activity activity){
		mActivityReference=new WeakReference<Activity >(activity);
		mActivity=mActivityReference.get();
		String str = getActivityName(mActivity);
		if(null != str)fillInStackTrace(info+":"+str);
	}
	
	public static String getActivityName(Activity mActivity){
		String str = null;
		if(null!=mActivity){
			str = mActivity.toString();
			if(null==str)return null;
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
		}
		return str;
	}
}
