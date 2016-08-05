package com.mt.androidtest.myselfview;
import java.util.Iterator;
import java.util.Set;

import com.mt.androidtest.R;
import com.mt.androidtest.R.xml;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

public class MyPreferenceActivity extends PreferenceActivity {
	 
    private final String CHECKBOXPREFERENCE_KEY1 = "checkbox_key1";
    private final String EDITTEXTPREFERENCE_KEY1 = "edit_key1";
    private final String LISTPREFERENCE_KEY = "listPreferenc_key";
    private final String MULTISELECTLISTPREFERENCE_KEY = "multiSelectListPreference_key";
    private final String SWITCHPREFERENCE_KEY = "switchPreference_key1";
 
    private SharedPreferences mSharedPreferences;
    private OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;
 
    private CheckBoxPreference mCheckBoxPreference;
    private EditTextPreference mEditTextPreference;
    private ListPreference mListPreference;
    private MultiSelectListPreference mMultiSelectListPreference;
    private SwitchPreference mSwitchPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加设置的选项
        addPreferencesFromResource(R.xml.mypreference);
        // 获取SharedPreferences对象
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mOnSharedPreferenceChangeListener = new MyOnSharedPreferenceChangeListener();
        //
        mCheckBoxPreference = (CheckBoxPreference) findPreference(CHECKBOXPREFERENCE_KEY1);
        mEditTextPreference = (EditTextPreference) findPreference(EDITTEXTPREFERENCE_KEY1);
        mListPreference = (ListPreference) findPreference(LISTPREFERENCE_KEY);
        mMultiSelectListPreference = (MultiSelectListPreference) findPreference(MULTISELECTLISTPREFERENCE_KEY);
        mSwitchPreference = (SwitchPreference) findPreference(SWITCHPREFERENCE_KEY);
        // 通用的读取设置的某个值的方法
        boolean b = mSharedPreferences.getBoolean(CHECKBOXPREFERENCE_KEY1, false);
        String s = mSharedPreferences.getString(EDITTEXTPREFERENCE_KEY1, "NULL");
    }
 
    // 数据发生变化时候的监听类设置
    private class MyOnSharedPreferenceChangeListener implements OnSharedPreferenceChangeListener {
 
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (CHECKBOXPREFERENCE_KEY1.equals(key)) {
                boolean b = sharedPreferences.getBoolean(CHECKBOXPREFERENCE_KEY1, false);
                mCheckBoxPreference.setSummary(b + "");
            } else if (EDITTEXTPREFERENCE_KEY1.equals(key)) {
                String s = sharedPreferences.getString(EDITTEXTPREFERENCE_KEY1, "null");
                mEditTextPreference.setSummary(s);
            } else if (LISTPREFERENCE_KEY.equals(key)) {
                String e = mListPreference.getEntry() + "";
                String v = mListPreference.getValue();
                mListPreference.setSummary(e + " : " + v);
            } else if (MULTISELECTLISTPREFERENCE_KEY.equals(key)) {
				String mEntries="Entries:";
				CharSequence [] entries=mMultiSelectListPreference.getEntries();
				for(CharSequence c:entries){
					mEntries+=c+" ";
				}
				Set<String> values=mMultiSelectListPreference.getValues();
				Iterator<String> it=values.iterator();
				String mValues="Values:";
				while(it.hasNext()){
					mValues+=it.next().toString()+" ";
				}
				mMultiSelectListPreference.setSummary(mEntries+"\n"+mValues);
            } else if (SWITCHPREFERENCE_KEY.equals(key)) {
                boolean b = sharedPreferences.getBoolean(SWITCHPREFERENCE_KEY, false);
                mSwitchPreference.setSummary(b + "");
            }
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        // 注册数据发生变化时候的监听
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        // 取消数据发生变化时候的监听
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }

}
