package com.mt.androidtest.showview.fragmentdemo;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

/**
 *AndroidManifest.xml�У�FragmentTest2Activity��Ҫ��android:configChanges="orientation|keyboardHidden|screenSize"��
 *�Դ���ģ���ڴ�����(��ʱΪת�������ؽ�)�����
 * @author Mengtao1
 *
 */
public class FragmentTest2Activity extends BaseActivity implements OnClickListener{
    private static final String STATE_SAVE_IS_SHOWN = "FRAGMENT_SHOWN_ID";
	private FragmentManager mFragmentManager=null;
	private ArrayList<InfoHolder>mInfoHolderAL=null;
	private int currentFragmentID = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_test2);
		doInit();
		mFragmentManager = getFragmentManager();
		if(null != savedInstanceState){//�����ڴ���������ת��ʱ���ָ�֮ǰ��ʾ��fragment
			currentFragmentID = savedInstanceState.getInt(STATE_SAVE_IS_SHOWN);
		}
		selectFragment(currentFragmentID);
	}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
    	savedInstanceState.putInt(STATE_SAVE_IS_SHOWN, currentFragmentID);
    }
    
	private void doInit() {
		mInfoHolderAL=new ArrayList<InfoHolder>();
		for(int i=0;i<InfoHolder.BaseViewIDs.length;i++){
			mInfoHolderAL.add(new InfoHolder(this,
					InfoHolder.BaseViewIDs[i],
					InfoHolder.ImageViewIDs[i],
					InfoHolder.TextViewIDs[i]));
		}
	}
	
	@Override
	public void onClick(View v) {
		int layoutID = v.getId();
		for(int i=0;i<InfoHolder.BaseViewIDs.length;i++){
			if(layoutID==InfoHolder.BaseViewIDs[i]){
				currentFragmentID = i;
				selectFragment(i);
				break;
			}
		}
	}

	/**
	 * selectFragment����ָ��λ�õ�fragment��Ҫ��ʾ��ʱ����Ҫ��������λ��fragment�Լ����ڿؼ�����Դ��ʾ������
	 * @param index
	 */
	private void selectFragment(int index) {
		reSetSelection(index);
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		Fragment mFragment = null;
		for(int i=0;i<InfoHolder.BaseViewIDs.length;i++){
			mFragment = mFragmentManager.findFragmentByTag(InfoHolder.FragmentsTAG[i]);
			if(null == mFragment){//Ϊ�գ�����mFragmentManagerû�б���mFragment����Ҫ����
				mFragment = InfoHolder.getFragment(i);
				transaction.add(R.id.content, mFragment, InfoHolder.FragmentsTAG[i]);
			}
			if(index != i){
				transaction.hide(mFragment);
			}else{
				transaction.show(mFragment);
			}
		}
		transaction.commit();
	}
	
	/**
	 * reSetSelection����������fragments��Ӧ�Ŀؼ���ʾ��Ϣ
	 * @param index ��λ�õ�fragment�����Ӧ�ؼ���Ϣ����Ϊ��ѡ�С�
	 */
	private void reSetSelection(int index) {
		InfoHolder mInfoHolder = null;
		for(int i=0;i<mInfoHolderAL.size();i++){
			mInfoHolder = mInfoHolderAL.get(i);
			if(index != i){
				mInfoHolder.setUnSelected(InfoHolder.DrawableUnSelectedIDs[i]);
			}else{
				mInfoHolder.setSelected(InfoHolder.DrawableSelectedIDs[i]);
			}
		}
	}

}
