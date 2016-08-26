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
	 * �κ�һ��Activity����ʾ�Ľ�����ʵ��Ҫ������������ɣ������������ݲ��֡������������ںܶ���涥����ʾ���ǲ������ݣ�
	 * �����ڴ����п��������Ƿ���ʾ�������ݲ��־���һ��FrameLayout��������ֵ�id����content�����ǵ���setContentView()
	 * ����ʱ������Ĳ�����ʵ���Ƿŵ����FrameLayout�еģ���Ҳ��Ϊʲô�������������setContentView()�������ǽ�setView()��
	 */
	private FrameLayout mContentView = null;//���ݲ��־���һ��FrameLayout
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
		setContentViewMenu.setHeaderTitle("setContentView�ķ�ʽ");
		setContentViewMenu.add(0, ROOT_ONLY, 0, "root only");
		setContentViewMenu.add(0, ROOT_ADDVIEW, 0, "root.addView");
		SubMenu inflateMethodMenu = menu.addSubMenu(int_groupId_Submenu,2,0,"inflate");
		mMenuItem=menu.findItem(2);
		setContentViewMenu.setHeaderTitle("inflate�ķ�ʽ");
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
			//������activity_inflate�������ͼ��Ϊactivity����ͼ�����Է��֣�xml�е�layout_height��layout_width����ʧЧ��
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
			//����1����ʱview_inflate_relativelayout�ж�������Խ���������
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
	 * setInflateView��Ϊactivity_inflate�������ͼ���������ͼ
	 */
	public void setInflateView(){
		viewActivityInflate=mLayoutInflater.inflate(R.layout.activity_inflate, null);
	    viewViewInflate = mLayoutInflater.inflate(R.layout.view_inflate,null);
	    ((ViewGroup)viewActivityInflate).addView(viewViewInflate);
	    setContentView(viewActivityInflate);
		mLinearLayoutInflate = (LinearLayout)findViewById(R.id.linearlayout_inflater);
	}
}
