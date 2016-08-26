package com.mt.androidtest.showview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class InflateActivity extends BaseActivity{
	View viewActivityInflate=null;
	View viewViewInflate=null;
	LinearLayout mLinearLayoutInflate=null;
	LayoutInflater mLayoutInflater=null;
	boolean isInflated = true;
	int int_groupId_Submenu=0x00;
	final int ROOT_NULL=0x11;
	final int ROOT_NOT_NULL=0x12;
	final int ROOT_ONLY=0x21;
	final int ROOT_ADDVIEW=0x22;	
	MenuItem mMenuItem=null;
	/**
	 * 任何一个Activity中显示的界面其实主要都由两部分组成，标题栏和内容布局。标题栏就是在很多界面顶部显示的那部分内容，
	 * 可以在代码中控制让它是否显示。而内容布局就是一个FrameLayout，这个布局的id叫作content，我们调用setContentView()
	 * 方法时所传入的布局其实就是放到这个FrameLayout中的，这也是为什么这个方法名叫作setContentView()，而不是叫setView()。
	 */
	private FrameLayout mContentView = null;//内容布局就是一个FrameLayout
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mLayoutInflater=getLayoutInflater();
	    mContentView = (FrameLayout)findViewById(android.R.id.content);//
	    ALog.Log("mContentView:"+(mContentView!=null));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		SubMenu setContentViewMenu = menu.addSubMenu(int_groupId_Submenu,1,0,"setContentView");
		setContentViewMenu.setHeaderTitle("setContentView的方式");
		setContentViewMenu.add(0, ROOT_ONLY, 0, "root only");
		setContentViewMenu.add(0, ROOT_ADDVIEW, 0, "root.addView");
		SubMenu inflateMethodMenu = menu.addSubMenu(int_groupId_Submenu,2,0,"inflate");
		mMenuItem=menu.findItem(2);
		setContentViewMenu.setHeaderTitle("inflate的方式");
		inflateMethodMenu.add(1, ROOT_NULL, 0, "root=null");
		inflateMethodMenu.add(1, ROOT_NOT_NULL, 0, "root!=null");
		mMenuItem.setEnabled(false);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)
	{
		switch (mi.getItemId()){
		case ROOT_ONLY:
			mContentView.removeAllViews();
			//仅仅将activity_inflate填充后的试图作为activity的试图，可以发现，xml中的layout_height和layout_width属性失效了
			mMenuItem.setEnabled(false);
			viewActivityInflate = mLayoutInflater.inflate(R.layout.activity_inflate, null);
			setContentView(viewActivityInflate);
			break;
		case ROOT_ADDVIEW:
			mContentView.removeAllViews();
			mMenuItem.setEnabled(true);
			setInflateView();
			break;
		case ROOT_NULL:
			//方法1：此时view_inflate_relativelayout中定义的属性将不起作用
			viewViewInflate = mLayoutInflater.inflate(R.layout.view_inflate_linearlayout, null);
			mLinearLayoutInflate.addView(viewViewInflate);
			break;
		case ROOT_NOT_NULL:
			viewViewInflate = mLayoutInflater.inflate(R.layout.view_inflate_linearlayout, mLinearLayoutInflate);
			break;
		}
		
		return super.onOptionsItemSelected(mi);
	}	

	/**
	 * setInflateView：为activity_inflate填充后的试图添加其他试图
	 */
	public void setInflateView(){
		viewActivityInflate=mLayoutInflater.inflate(R.layout.activity_inflate, null);
	    viewViewInflate = mLayoutInflater.inflate(R.layout.view_inflate,null);
	    ((ViewGroup)viewActivityInflate).addView(viewViewInflate);
	    setContentView(viewActivityInflate);
		mLinearLayoutInflate = (LinearLayout)findViewById(R.id.linearlayout_inflater);
	}
}
