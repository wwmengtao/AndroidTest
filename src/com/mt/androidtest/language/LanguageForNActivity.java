package com.mt.androidtest.language;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class LanguageForNActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceID());
        initActionBar();
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
    	return new LocaleListEditor();
    }

    @LayoutRes
    protected Integer getResourceID(){//用于规定BaseActivity的布局文件
        return R.layout.activity_base_fragment;
    }
}
