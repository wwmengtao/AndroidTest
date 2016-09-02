package com.mt.androidtest.measurelayoutdraw;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

/**
 * 一、根布局为LineaLayout时
 * 1)onMeasure执行了两次，原因是ViewRootImpl.performTraversals内部两次measureHierarchy都被调用了
 * 2)onLayout和onDraw各执行了一次
 * 3)点击自定义TextView导致其Text内容发生变化，可能会导致其他TextView调用测量、布局、绘制流程。
 * 二、根布局为RelativeLayout时
 * 1)onMeasure执行了四次，原因是ViewRootImpl.performTraversals内部measureHierarchy两次调用，并且每次
 * 执行measureHierarchy时，onMeasure都执行两次
 * 2)onLayout和onDraw各执行了一次
 * @author Mengtao1
 *
 */
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
		// -------------向menu中添加字体大小的子菜单-------------
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu_Linear, 0, "LinearLayout");
		menu.add(0, Menu_Relative, 0, "RelativeLayout");
		menu.add(0, Menu_Frame, 0, "FrameLayout");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)	{
		switch (mi.getItemId()){
		case Menu_Linear:
			setContentView(R.layout.activity_measure_layout_draw_linear);//根布局为LinearLayout
			break;
		case Menu_Relative:
			setContentView(R.layout.activity_measure_layout_draw_relat);//根布局为RelativeLayout
			break;		
		case Menu_Frame:
			setContentView(R.layout.activity_measure_layout_draw_frame);//根布局为FrameLayout
			break;					
		}
		return super.onOptionsItemSelected(mi);
	}	
}
