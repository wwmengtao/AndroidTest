package com.mt.androidtest.showview;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class MeasureLayoutDrawActivity extends BaseActivity {
	
	private static final int Menu_Linear = 0;
	private static final int Menu_Relative = 1;
	private static final int Menu_Frame = 2;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// -------------��menu����������С���Ӳ˵�-------------
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu_Linear, 0, "LinearLayout");
		menu.add(0, Menu_Relative, 0, "RelativeLayout");
		menu.add(0, Menu_Frame, 0, "FrameLayout");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)	{
		switch (mi.getItemId()){
		case Menu_Linear:
			setContentView(R.layout.activity_measure_layout_draw_linear);//������ΪLinearLayout
			break;
		case Menu_Relative:
			setContentView(R.layout.activity_measure_layout_draw_relat);//������ΪRelativeLayout
			break;		
		case Menu_Frame:
			setContentView(R.layout.activity_measure_layout_draw_frame);//������ΪFrameLayout
			break;					
		}
		return super.onOptionsItemSelected(mi);
	}	
}
