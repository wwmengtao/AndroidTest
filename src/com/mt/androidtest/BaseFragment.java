package com.mt.androidtest;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
onAttach方法：Fragment和Activity建立关联的时候调用。
onCreateView方法：为Fragment加载布局时调用。
onActivityCreated方法：当Activity中的onCreate方法执行完后调用。
onDestroyView方法：Fragment中的布局被移除时调用。
onDetach方法：Fragment和Activity解除关联的时候调用。
 * @author Mengtao1
 *
 */
public abstract class BaseFragment extends Fragment {
	private boolean isLogRun = true; 
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if(isLogRun)ALog.Log("onAttach",this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isLogRun)ALog.Log("onCreate",this);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if(isLogRun)ALog.Log("onCreateView",this);
        return null;
    }
	
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	if(isLogRun)ALog.Log("onActivityCreated",this);
    }
    
    @Override
	public void onStart() {
        super.onStart();
		if(isLogRun)ALog.Log("onStart",this);		
    }
	
    
	@Override
	public void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("onResume",this);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(isLogRun)ALog.Log("onPause",this);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if(isLogRun)ALog.Log("onSaveInstanceState",this);
	}
	
    @Override
	public void onStop() {
        super.onStop();
		if(isLogRun)ALog.Log("onStop",this);		
    }
	
    @Override
    public void onDestroyView() {
    	super.onDestroyView();
    	if(isLogRun)ALog.Log("onDestroyView",this);	
    }
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(isLogRun)ALog.Log("onDestroy",this);
	}	
	
	@Override
	public void onDetach() {
		super.onDetach();
		if(isLogRun)ALog.Log("onDetach",this);
	}
}
