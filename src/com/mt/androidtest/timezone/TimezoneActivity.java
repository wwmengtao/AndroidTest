package com.mt.androidtest.timezone;

import android.app.Fragment;

import com.mt.androidtest.BaseFragmentActivity;

public class TimezoneActivity extends BaseFragmentActivity {

    @Override
    public Fragment getFragment(){
    	return new TimeZoneFragment();
    }

}
