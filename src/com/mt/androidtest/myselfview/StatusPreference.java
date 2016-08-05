package com.mt.androidtest.myselfview;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mt.androidtest.R;

public class StatusPreference extends Preference{		
		public StatusPreference(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}
		
		public StatusPreference(Context context, AttributeSet attrs) {
			this(context, attrs,0);
		}
		
		@Override
		protected void onBindView(View view) {
			super.onBindView(view);
			TextView tv_summary = (TextView) view.findViewById(R.id.tv_status_summary);
			if(tv_summary != null){
				tv_summary.setText(getContext().getString(R.string.preference_on));
			}
			
			View icon = view.findViewById(android.R.id.icon);
			if(icon != null){
				icon.setVisibility(View.VISIBLE);
				icon.setBackgroundResource(R.drawable.right_arrow_select);
			}
		}
	}
