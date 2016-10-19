package com.mt.androidtest.showview.fragmentdemo;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class FragmentTestActivity extends BaseActivity  implements SummaryListFragment.Callback{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_test);
	}

	
	@Override
	public void onItemSelected(Integer id){
		Bundle arguments = new Bundle();
		arguments.putInt(DetailInfoFragment.ITEM_ID, id);
		DetailInfoFragment fragment = new DetailInfoFragment();
		fragment.setArguments(arguments);
		//
	    FragmentTransaction mFragmentTransaction=getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.info_detail_container, fragment);//mFragmentTransaction.add(R.id.info_detail_container, fragment);
        mFragmentTransaction.addToBackStack(null);//��ʵ�������ڰ��ֻ����ؼ�������һҳ��Ч������Ϊ�������add/replace����ѹ��ջ��
		mFragmentTransaction.commit();
	}
}
