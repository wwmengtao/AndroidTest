package com.mt.androidtest.showview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.mt.androidtest.R;

public class InflateActivity extends Activity {
	View rootView=null;
	View mView=null;
	LinearLayout mLinearLayout=null;
	LayoutInflater mLayoutInflater=null;
	boolean isInflated = true;
	int int_groupId_Submenu=0x00;
	final int Inflate_method1=0x11;
	final int Inflate_method2=0x12;
	final int SetContentView_1=0x21;
	final int SetContentView_2=0x22;	
	MenuItem mMenuItem=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mLayoutInflater=getLayoutInflater();
	}
	
	/**
	 * setInflateView：为activity_inflate填充后的试图添加其他试图
	 */
	public void setInflateView(){
		rootView=mLayoutInflater.inflate(R.layout.activity_inflate, null);;
	    mView = mLayoutInflater.inflate(R.layout.view_inflate, (ViewGroup)rootView,false);
	    ((ViewGroup)rootView).addView(mView);
	    setContentView(rootView);
		mLinearLayout = (LinearLayout)findViewById(R.id.linearlayout_inflater);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		SubMenu setContentViewMenu = menu.addSubMenu(int_groupId_Submenu,1,0,"setContentView");
		setContentViewMenu.setHeaderTitle("setContentView的方式");
		setContentViewMenu.add(0, SetContentView_1, 0, "root only");
		setContentViewMenu.add(0, SetContentView_2, 0, "root.addView");
		SubMenu inflateMethodMenu = menu.addSubMenu(int_groupId_Submenu,2,0,"inflate");
		mMenuItem=menu.findItem(2);
		setContentViewMenu.setHeaderTitle("inflate的方式");
		inflateMethodMenu.add(1, Inflate_method1, 0, "root=null");
		inflateMethodMenu.add(1, Inflate_method2, 0, "root!=null");
		mMenuItem.setEnabled(false);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)
	{
		switch (mi.getItemId()){
		case SetContentView_1:
			//仅仅将activity_inflate填充后的试图作为activity的试图，可以发现，xml中的layout_height和layout_width属性失效了
			mMenuItem.setEnabled(false);
			rootView = mLayoutInflater.inflate(R.layout.activity_inflate, null);
			setContentView(rootView);
			break;
		case SetContentView_2:
			mMenuItem.setEnabled(true);
			setInflateView();
			break;
		case Inflate_method1:
			//方法1：此时view_inflate_relativelayout中定义的属性将不起作用
			mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, null);
			mLinearLayout.addView(mView);
			break;
		case Inflate_method2:
			boolean method21=true;
			//下面两种方法传入mLinearLayout的目的是为了view_inflate_relativelayout中layout_属性能生效
			if(method21){
				//方法2.1：此方法等价于mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout,true);
				mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout);
			}else{
				//方法2.2：效果和方法2.1相同。
				mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout,false);
				mLinearLayout.addView(mView);
			}
			break;			
		}
		
		return true;
	}	
}
