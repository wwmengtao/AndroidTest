package com.mt.androidtest.timezone;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.mt.androidtest.R;

public class TimezoneActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	if(android.os.Build.VERSION.SDK_INT < 24)return;// If not android 7.x, return
        super.onCreate(savedInstanceState);
        setContentView(getResourceID());
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
    public Fragment getFragment(){
    	return new ZonePicker();
    }

    @LayoutRes
    protected Integer getResourceID(){//用于规定BaseActivity的布局文件
        return R.layout.activity_base_fragment;
    }
}
