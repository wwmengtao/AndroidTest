package com.mt.androidtest.measurelayoutdraw;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

/**
 * һ��ִ��setContentView��ʱ��
 * 1��������ΪLineaLayoutʱ
 * 1)onMeasureִ�������Σ�ԭ����ViewRootImpl.performTraversals�ڲ�����measureHierarchy����������
 * 2)onLayout��onDraw��ִ����һ��
 * 3)����Զ���TextView������Text���ݷ����仯�����ܻᵼ������TextView���ò��������֡��������̡�
 * 2��������ΪRelativeLayoutʱ
 * 1)onMeasureִ�����ĴΣ�ԭ����ViewRootImpl.performTraversals�ڲ�measureHierarchy���ε��ã�����ÿ��
 * ִ��measureHierarchyʱ��onMeasure��ִ������
 * 2)onLayout��onDraw��ִ����һ��
 * ����ִ��requestLayout��ʱ��
 * 1��������ΪLineaLayoutʱ������MLDTextView1����ʱ������MLDTextView2��onMeasure���ã�ԭ����View.layout����������������if ((mPrivateFlags3 & PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT) != 0)
 * 2��������ΪMLDRootViewGroupʱ������MLDTextView2�����ܻ���������TextView��onMeasure��ԭ���MLDRootViewGroup.onLayout��˵��
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
		// -------------��menu����������С���Ӳ˵�-------------
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
			setContentView(R.layout.activity_measure_layout_draw_linear);//������ΪLinearLayout
			break;
		case Menu_Relative:
			layoutDes="RL";
			setContentView(R.layout.activity_measure_layout_draw_relat);//������ΪRelativeLayout
			break;		
		case Menu_Frame:
			layoutDes="FL";			
			setContentView(R.layout.activity_measure_layout_draw_frame);//������ΪFrameLayout
			break;	
		case Menu_SelfVG:
			layoutDes="SelfViewGroup";			
			setContentView(R.layout.activity_measure_layout_draw_viewgroup);//������ΪMLDRootViewGroup
			break;			
		case Menu_SelfVG2:
			setContentView(R.layout.activity_measure_layout_draw_viewgroup_2);//������ΪMLDRootViewGroup2
			break;	
		}
		return super.onOptionsItemSelected(mi);
	}	
}
