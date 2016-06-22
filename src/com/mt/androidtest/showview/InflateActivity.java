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
	 * setInflateView��Ϊactivity_inflate�������ͼ���������ͼ
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
		setContentViewMenu.setHeaderTitle("setContentView�ķ�ʽ");
		setContentViewMenu.add(0, SetContentView_1, 0, "root only");
		setContentViewMenu.add(0, SetContentView_2, 0, "root.addView");
		SubMenu inflateMethodMenu = menu.addSubMenu(int_groupId_Submenu,2,0,"inflate");
		mMenuItem=menu.findItem(2);
		setContentViewMenu.setHeaderTitle("inflate�ķ�ʽ");
		inflateMethodMenu.add(1, Inflate_method1, 0, "root=null");
		inflateMethodMenu.add(1, Inflate_method2, 0, "root!=null");
		mMenuItem.setEnabled(false);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)
	{
		switch (mi.getItemId()){
		case SetContentView_1:
			//������activity_inflate�������ͼ��Ϊactivity����ͼ�����Է��֣�xml�е�layout_height��layout_width����ʧЧ��
			mMenuItem.setEnabled(false);
			rootView = mLayoutInflater.inflate(R.layout.activity_inflate, null);
			setContentView(rootView);
			break;
		case SetContentView_2:
			mMenuItem.setEnabled(true);
			setInflateView();
			break;
		case Inflate_method1:
			//����1����ʱview_inflate_relativelayout�ж�������Խ���������
			mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, null);
			mLinearLayout.addView(mView);
			break;
		case Inflate_method2:
			boolean method21=true;
			//�������ַ�������mLinearLayout��Ŀ����Ϊ��view_inflate_relativelayout��layout_��������Ч
			if(method21){
				//����2.1���˷����ȼ���mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout,true);
				mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout);
			}else{
				//����2.2��Ч���ͷ���2.1��ͬ��
				mView = mLayoutInflater.inflate(R.layout.view_inflate_relativelayout, mLinearLayout,false);
				mLinearLayout.addView(mView);
			}
			break;			
		}
		
		return true;
	}	
}
