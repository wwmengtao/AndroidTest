package com.mt.androidtest.myselfview;

import com.mt.androidtest.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CombinedView extends FrameLayout {

	private Button leftButton;

	private TextView titleText;

	public CombinedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.customview_title, this);
		titleText = (TextView) findViewById(R.id.title_text);
		leftButton = (Button) findViewById(R.id.button_left);
		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) getContext()).finish();
			}
		});
	}

	public void setTitleText(String text) {
		titleText.setText(text);
	}

	public void setLeftButtonText(String text) {
		leftButton.setText(text);
	}

	public void setLeftButtonListener(OnClickListener l) {
		leftButton.setOnClickListener(l);
	}

}