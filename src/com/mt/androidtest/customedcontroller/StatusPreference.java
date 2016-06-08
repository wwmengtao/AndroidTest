package com.mt.androidtest.customedcontroller;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mt.androidtest.R;

public class StatusPreference extends Preference{
		private static final boolean sShowIcon = false;
		private boolean hasSubPreference = true;
		private CharSequence mLenovoStatusSummary = null;
		public StatusPreference(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CharacterSettingsK7);
			final int N = a.getIndexCount();
			for(int i=0;i<N;i++){
				int attr = a.getIndex(i);
				switch(attr){
				case R.styleable.CharacterSettingsK7_hasSubPreference:
					hasSubPreference = a.getBoolean(i, false);
					break;
				default:
				}
			}
			a.recycle();
		}
		
		public StatusPreference(Context context, AttributeSet attrs) {
			this(context, attrs,0);
		}
		
		@Override
		protected void onBindView(View view) {
			super.onBindView(view);
			View widget = view.findViewById(android.R.id.widget_frame);
			TextView statusSummary = (TextView) view.findViewById(R.id.lenovo_status_summary);
			if(statusSummary != null){
				if(mLenovoStatusSummary == null){
					mLenovoStatusSummary = getContext().getString(R.string.preference_status_on);
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
			
			View nextIcon = view.findViewById(R.id.lenovo_has_next);
			if(nextIcon != null){
				nextIcon.setVisibility(hasSubPreference?View.VISIBLE:View.GONE);
			}
		}
	}
