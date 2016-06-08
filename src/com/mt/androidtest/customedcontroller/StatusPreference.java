package com.mt.androidtest.customedcontroller;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.mt.androidtest.R;

public class StatusPreference extends Preference{
		
		private static final boolean sShowIcon = false;
		
		private boolean hasSubPreference = true;
		
		private int mLenovoStatusSummaryDefault = R.string.preference_status_on;
		private int mLenovoStatusSummaryRes = -1;
		private CharSequence mLenovoStatusSummary = null;
		
		public StatusPreference(Context context, AttributeSet attrs,
				int defStyle) {
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
            final TextView tvSummary = (TextView) view.findViewById(android.R.id.summary);
			if(statusSummary != null){
				
				if(mLenovoStatusSummary == null){
					setLenovoStatusSummaryRes(mLenovoStatusSummaryDefault);
				}
				
				if(!TextUtils.isEmpty(mLenovoStatusSummary)){
					if(statusSummary.getVisibility() != View.VISIBLE){
						statusSummary.setVisibility(View.VISIBLE);
					}
					statusSummary.setText(getLenovoStatusSummary());
				}
			}
            /*Begin Lenovo-sw dingys2 20150917 for ellipsize text more than 4 lines [KOLEOSROW-2234]*/
            if (null != tvSummary) {
                String summary = (String)tvSummary.getText();
                if (!TextUtils.isEmpty(summary)) {
                    ViewTreeObserver ovserver = tvSummary.getViewTreeObserver();
                    ovserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            ViewTreeObserver obs = tvSummary.getViewTreeObserver();
                            obs.removeGlobalOnLayoutListener(this);

                            if (tvSummary.getLineCount() > 4) {
                                int endIndex = tvSummary.getLayout().getLineEnd(3);
                                String text = ((String) tvSummary.getText()).substring(0, endIndex - 3) + "...";
                                tvSummary.setText(text);
                            }
                        }
                    });
                }
            }
            /*End Lenovo-sw dingys2 20150917 [KOLEOSROW-2234] */
			
			View nextIcon = view.findViewById(R.id.lenovo_has_next);
			if(nextIcon != null){
				nextIcon.setVisibility(hasSubPreference?View.VISIBLE:View.GONE);
			}
			
			View icon = view.findViewById(android.R.id.icon);
			if(icon != null){
				icon.setVisibility(sShowIcon?View.VISIBLE:View.GONE);
			}
		}
		
		public int getmLenovoStatusSummaryRes() {
			return mLenovoStatusSummaryRes;
		}
		
		public void setLenovoStatusSummaryRes(int mLenovoStatusSummaryResId) {
			try{
				setLenovoStatusSummary(getContext().getString(mLenovoStatusSummaryResId));
				mLenovoStatusSummaryRes = mLenovoStatusSummaryResId;
			}catch(Exception e){
				e.printStackTrace();
				setLenovoStatusSummaryRes(mLenovoStatusSummaryDefault);
			}
			notifyChanged();
		}
		
		public CharSequence getLenovoStatusSummary() {
			return mLenovoStatusSummary;
		}
		
		public void setLenovoStatusSummary(CharSequence mLenovoStatusSummaryId) {
			mLenovoStatusSummary = mLenovoStatusSummaryId;
			notifyChanged();
		}
		
	}
