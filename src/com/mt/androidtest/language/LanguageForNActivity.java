package com.mt.androidtest.language;

import android.app.Fragment;

import com.mt.androidtest.BaseFragmentActivity;

/**
 * 
 * @author Mengtao1
 *
 */
public class LanguageForNActivity extends BaseFragmentActivity {

    @Override
    public Fragment getFragment(){
    	return (android.os.Build.VERSION.SDK_INT < 24)?null : new LocaleListEditor();// If not android 7.x, return
    }
}
