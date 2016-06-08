package com.mt.androidtest.customedcontroller;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public class SwitchPreference extends Preference{
	TextView mTVStatus;
	Switch mSwitch;

	public SwitchPreference(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		ALog.Log("SwitchPreference3==");
	}

	public SwitchPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		ALog.Log("SwitchPreference2==");
	}

	public SwitchPreference(Context context) {
		super(context);
	}

	@Override
	protected void onBindView(View view) {
		ALog.Log("SwitchPreference_onBindView");
		super.onBindView(view);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		ALog.Log("SwitchPreference_onCreateView");
		LinearLayout mLinearLayout = (LinearLayout)LayoutInflater.from(getContext()).inflate(
                R.layout.preference_switchview, parent, false);
		return mLinearLayout;
	}
}
