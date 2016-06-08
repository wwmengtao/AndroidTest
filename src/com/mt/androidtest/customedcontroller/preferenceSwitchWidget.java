package com.mt.androidtest.customedcontroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.mt.androidtest.R;

public class preferenceSwitchWidget  extends  LinearLayout{

	private LinearLayout mWidgetLayout;
	private LayoutInflater mLayoutInflater;
	Switch mStatusSwitch;
	Context mContext;

	
	public preferenceSwitchWidget(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public preferenceSwitchWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	public preferenceSwitchWidget(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		android.util.Log.d("bluewind","WideTouchPreferenceWidget   onFinishInflate");
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWidgetLayout = (LinearLayout)mLayoutInflater.inflate(R.layout.preference_switchview,null);
		mStatusSwitch = (Switch)mWidgetLayout.findViewById(R.id.switch_bt);
		addView(mWidgetLayout);
	}
}
