package com.mt.androidtest;

import android.app.ActivityManager;
import android.os.UserHandle;
import android.util.Log;

public class ALog {
	public  static String TAG_M = "M_T_AT";
	public static void Log(String info){
		Log.e(TAG_M,info);
	}
	public static void mLog(String info){
		Log.e(TAG_M,info+" Uid:"+getUid()+" Pid:"+getPid()+" ActivityManager.getCurrentUser:"+amGetCurrentUser()+
				" UserHandle.myUserId:"+uhMyUserId());
	}
	public static void printStackTrace(String info){
		RuntimeException RTE = new RuntimeException(info);
		RTE.fillInStackTrace();
		Log.e(TAG_M, "Pid:"+getPid()+" "+"Called:", RTE);
	}
	public static int getPid(){
		return android.os.Process.myPid();
	}
	public static int getUid(){
		return android.os.Process.myUid();
	}
	public static int amGetCurrentUser(){
		return ActivityManager.getCurrentUser();
	}
	public static int uhMyUserId(){
		return UserHandle.myUserId();
	}
}
