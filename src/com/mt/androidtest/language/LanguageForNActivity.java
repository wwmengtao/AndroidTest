package com.mt.androidtest.language;

import android.app.Fragment;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseFragmentActivity;
import com.mt.androidtest.R;

import android.app.FragmentManager;
/**
 * 
 * @author Mengtao1
 *
 */
public class LanguageForNActivity extends BaseFragmentActivity implements FragmentManager.OnBackStackChangedListener{

    @Override
    public Fragment getFragment(){
    	getFragmentManager().addOnBackStackChangedListener(this);
    	return (android.os.Build.VERSION.SDK_INT < 24)?null : new LocaleListEditor();// If not android 7.x, return
    }

	@Override
	public void onBackStackChanged() {
		// TODO Auto-generated method stub
		/**
		 * getBackStackEntryCount：可以判断FragmentManager的Fragment回退层级，0表示第一级，此处为LocaleListEditor
		 */
		final int count = getFragmentManager().getBackStackEntryCount();
		
		String str = LocaleListEditor.class.getName();
		Fragment fm = getFragmentManager().findFragmentById(R.id.fragment_container);//R.id.main_content
		ALog.Log("onBackStackChanged_BackStackEntryCount: "+count+" CurrentFragment: "+fm.toString()+" str: "+str);
	}
}
