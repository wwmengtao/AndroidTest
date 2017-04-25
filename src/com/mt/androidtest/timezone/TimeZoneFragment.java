package com.mt.androidtest.timezone;

import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mt.androidtest.BaseFragment;
import com.mt.androidtest.R;
import com.mt.androidtest.timezone.ZonePicker.ZoneSelectionListener;

public class TimeZoneFragment extends BaseFragment implements ZoneSelectionListener, View.OnClickListener{
	TextView tv1, tv2;
	Button btn;
	TimeZone mTimeZone = null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.timezone_update, container, false);
        tv1 = (TextView)view.findViewById(R.id.time_zone);
        tv2 = (TextView)view.findViewById(R.id.time_zone_detail);
        btn = (Button)view.findViewById(R.id.update_timezone);
        btn.setOnClickListener(this);
        return view;
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	 final Calendar now = Calendar.getInstance();
    	 tv1.setText(ZoneGetter.getTimeZoneOffsetAndName(now.getTimeZone(), now.getTime()));
    	 if(null != mTimeZone){
	         String str = mTimeZone.getDisplayName()+"\n"+mTimeZone.toString();
	         tv2.setText(str);
    	 }
     	//
    }

	@Override
	public void onZoneSelected(TimeZone tz) {
		// TODO Auto-generated method stub
    	getFragmentManager().popBackStack();
    	mTimeZone = tz;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ZonePicker zp = new ZonePicker();
		zp.setZoneSelectionListener(this);
		getFragmentManager()
			.beginTransaction()
			.replace(getId(), zp)
			.addToBackStack(null)
			.commit();
	}
}
