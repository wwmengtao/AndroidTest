package com.mt.androidtest.measurelayoutdraw;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

/**
 * 一、执行setContentView的时候：
 * 1、根布局为LineaLayout时
 * 1)onMeasure执行了两次，原因是ViewRootImpl.performTraversals内部两次measureHierarchy都被调用了
 * 2)onLayout和onDraw各执行了一次
 * 3)点击自定义TextView导致其Text内容发生变化，可能会导致其他TextView调用测量、布局、绘制流程。
 * 2、根布局为RelativeLayout时
 * 1)onMeasure执行了四次，原因是ViewRootImpl.performTraversals内部measureHierarchy两次调用，并且每次
 * 执行measureHierarchy时，onMeasure都执行两次
 * 2)onLayout和onDraw各执行了一次
 * 二、执行requestLayout的时候
 * 1、根布局为LineaLayout时，单击MLDTextView1，此时会引发MLDTextView2的onMeasure调用，原因是View.layout函数中满足条件：if ((mPrivateFlags3 & PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT) != 0)
 * 2、根布局为MLDRootViewGroup时，单击MLDTextView2，可能会引发其他TextView的onMeasure，原因见MLDRootViewGroup.onLayout中说明
 * @author Mengtao1
 */
public class MeasureLayoutDrawActivity extends BaseActivity {
	
	private static final int Menu_Linear = 0;
	private static final int Menu_Relative = 1;
	private static final int Menu_Frame = 2;	
	private static final int Menu_SelfVG = 3;		
	private static final int Menu_SelfVG2 = 4;		
	public static String layoutDes = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// -------------向menu中添加字体大小的子菜单-------------
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu_Linear, 0, "LinearLayout");
		menu.add(0, Menu_Relative, 0, "RelativeLayout");
		menu.add(0, Menu_Frame, 0, "FrameLayout");
		menu.add(0, Menu_SelfVG, 0, "MLDRootViewGroup");
		menu.add(0, Menu_SelfVG2, 0, "MLDRootViewGroup2");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)	{
		switch (mi.getItemId()){
		case Menu_Linear:
			layoutDes="LL";
			setContentView(R.layout.activity_measure_layout_draw_linear);//根布局为LinearLayout
			break;
		case Menu_Relative:
			layoutDes="RL";
			setContentView(R.layout.activity_measure_layout_draw_relat);//根布局为RelativeLayout
			break;		
		case Menu_Frame:
			layoutDes="FL";			
			setContentView(R.layout.activity_measure_layout_draw_frame);//根布局为FrameLayout
			break;	
		case Menu_SelfVG:
			layoutDes="SelfViewGroup";			
			setContentView(R.layout.activity_measure_layout_draw_viewgroup);//根布局为MLDRootViewGroup
			break;			
		case Menu_SelfVG2:
			setContentView(R.layout.activity_measure_layout_draw_viewgroup_2);//根布局为MLDRootViewGroup2
			break;	
		}
		return super.onOptionsItemSelected(mi);
	}	
}
