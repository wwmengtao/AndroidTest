package com.mt.androidtest;

import android.app.Application;

public class ATApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ALog.Log("ATApplication.onCreate");
	}
}