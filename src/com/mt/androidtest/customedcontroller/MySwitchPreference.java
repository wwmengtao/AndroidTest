package com.mt.androidtest.customedcontroller;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.ViewGroup.MarginLayoutParams; 

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;
public class MySwitchPreference extends Preference{
	private Context mContext=null;
	private int mDensityDpi=0;
	public MySwitchPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
	}
	
	public MySwitchPreference(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		mContext=context;
	}
	
	@Override
	protected View onCreateView(ViewGroup parent) {
        mDensityDpi = mContext.getResources().getDisplayMetrics().densityDpi;
		return LayoutInflater.from(getContext()).inflate(
                R.layout.preference_switchview, parent, false);
	}
	
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		TextView mTVStatus = (TextView)view.findViewById(R.id.status_tv);
		mTVStatus.setText("on");
		ALog.Log("mDensityDpi:"+mDensityDpi);
		MarginLayoutParams params =  (MarginLayoutParams) mTVStatus.getLayoutParams();
		params.setMarginStart(16*(mDensityDpi/160));//px=dp*(dpi/160)
		mTVStatus.setLayoutParams(params);
	}
}
