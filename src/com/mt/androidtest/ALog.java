package com.mt.androidtest;
import android.util.Log;

public class ALog {
	public  static String TAG_M = "M_T_AT";

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
	
}
