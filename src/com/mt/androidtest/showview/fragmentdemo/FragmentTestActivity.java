package com.mt.androidtest.showview.fragmentdemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class FragmentTestActivity extends BaseActivity implements SummaryListFragment.Callback{
	private FragmentManager mFragmentManager=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_test);
		mFragmentManager = getFragmentManager();
	}
	private static final int GroupID0 = 0x00;
	private static final int GroupID1 = 0x01;
	private static final int GroupID2 = 0x02;
	private static final int FRAG_OPE_ADD = 0x1011;
	private static final int FRAG_OPE_REPLACE = 0x1012;
	private static final int BACK_STACK_Y = 0x1021;
	private static final int BACK_STACK_N = 0x1022;
	private boolean fragmentAdd=false;
	private boolean hasBackStack = true;
	private MenuItem mMenuAdd, mMenuReplace, mMemuHas, mMenuNoHas;
	
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		SubMenu mSubMenu1 = menu.addSubMenu(GroupID0, 0, 0,"Fragment操作方式");
		mMenuAdd = mSubMenu1.add(GroupID1, FRAG_OPE_ADD, 0, "add");
		mMenuAdd.setCheckable(true);
		mMenuReplace = mSubMenu1.add(GroupID1, FRAG_OPE_REPLACE, 1, "replace");
		mMenuReplace.setCheckable(true);
		SubMenu mSubMenu2 = menu.addSubMenu(GroupID0, 1, 1,"是否有回退栈");
		mMemuHas = mSubMenu2.add(GroupID2, BACK_STACK_Y, 0, "有");
		mMemuHas.setCheckable(true);
		mMenuNoHas = mSubMenu2.add(GroupID2, BACK_STACK_N, 1, "没有");
		mMenuNoHas.setCheckable(true);
		return true;
	}
	
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        ALog.Log("onPrepareOptionsMenu_fragmentAdd:"+fragmentAdd+" hasBackStack:"+hasBackStack);
		mMenuAdd.setChecked(fragmentAdd);
		mMenuReplace.setChecked(!fragmentAdd);
		//
		mMemuHas.setChecked(hasBackStack);
		mMenuNoHas.setChecked(!hasBackStack);
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem mi)	{
		switch (mi.getItemId()){
			case FRAG_OPE_ADD:
				fragmentAdd = true;
				break;
			case FRAG_OPE_REPLACE:
				fragmentAdd = false;
				break;			
			case BACK_STACK_Y:
				hasBackStack = true;
				break;	
			case BACK_STACK_N:
				hasBackStack = false;
				break;						
		}
		return  super.onOptionsItemSelected(mi);
	}
	
	@Override
	public void onItemSelected(Integer id){
		Bundle arguments = new Bundle();
		arguments.putInt(DetailInfoFragment.ITEM_ID, id);
		Fragment fragment = new DetailInfoFragment();
		fragment.setArguments(arguments);
		//
	    FragmentTransaction mFragmentTransaction=mFragmentManager.beginTransaction();
        if(!fragmentAdd)
        	mFragmentTransaction.replace(R.id.info_detail_container, fragment);
        else
        	mFragmentTransaction.add(R.id.info_detail_container, fragment);
        if(hasBackStack)mFragmentTransaction.addToBackStack(null);//可实现类似于按手机返回键返回上一页的效果，因为将上面的add/replace操作压入栈内
		mFragmentTransaction.commit();
	}
}
