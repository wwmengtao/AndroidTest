package com.mt.androidtest.language;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

/**
 * 
 * @author Mengtao1
 *
 */
public class LanguageForNActivity extends Activity {

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
    	return new LocaleListEditor();
    }

    @LayoutRes
    protected Integer getResourceID(){//���ڹ涨BaseActivity�Ĳ����ļ�
        return R.layout.activity_base_fragment;
    }
}
