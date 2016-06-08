package com.mt.androidtest.customedcontroller;
import android.content.Context;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mt.androidtest.R;

public class StatusPreference extends Preference{
		private static final boolean sShowIcon = false;
		private CharSequence mLenovoStatusSummary = null;
		
		public StatusPreference(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}
		
		public StatusPreference(Context context, AttributeSet attrs) {
			this(context, attrs,0);
		}
		
		@Override
		protected void onBindView(View view) {
			super.onBindView(view);
			View widget = view.findViewById(android.R.id.widget_frame);
			TextView statusSummary = (TextView) view.findViewById(R.id.tv_status_summary);
			if(statusSummary != null){
				if(mLenovoStatusSummary == null){
					mLenovoStatusSummary = getContext().getString(R.string.preference_on);
				}
				if(!TextUtils.isEmpty(mLenovoStatusSummary)){
					if(statusSummary.getVisibility() != View.VISIBLE){
						statusSummary.setVisibility(View.VISIBLE);
					}
					statusSummary.setText(mLenovoStatusSummary);
				}
			}
			
			View icon = view.findViewById(android.R.id.icon);
			if(icon != null){
				icon.setBackgroundResource(R.drawable.right_arrow_select);
				icon.setVisibility(sShowIcon?View.VISIBLE:View.GONE);
			}
		}
	}
