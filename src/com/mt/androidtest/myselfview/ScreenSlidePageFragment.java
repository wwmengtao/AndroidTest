package com.mt.androidtest.myselfview;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseFragment;
import com.mt.androidtest.R;
import static com.mt.androidtest.myselfview.MySelfViewActivity.NUM_PAGES;;


public class ScreenSlidePageFragment extends BaseFragment {
    private static final String ARG_PAGE = "page";
    private static SparseArray<BaseFragment> mFragments = null;
    
    public static BaseFragment get(int pageNumber) {
    	if(null == mFragments)mFragments = new SparseArray<>();
    	BaseFragment fragment = mFragments.get(pageNumber);
    	if(null == fragment){
    		fragment = new ScreenSlidePageFragment();
    		mFragments.put(pageNumber, fragment);
	        Bundle args = new Bundle();
	        args.putInt(ARG_PAGE, pageNumber);
	        fragment.setArguments(args);
    	}
        return fragment;
    }
    
    public static void clearFragments(){
    	if(null != mFragments)mFragments.clear();
    }
    
    public static void scanFragments(){
    	if(null != mFragments){
    		ALog.Log1("/---------------ScreenSlidePageFragment.scanFragments begin---------------/");
    		for(int i=0;i<NUM_PAGES;i++){
    			BaseFragment fragment = mFragments.get(i);
    			if(null != fragment)ALog.Log1("index"+i+": "+fragment);
    		}
    		ALog.Log1("/---------------ScreenSlidePageFragment.scanFragments end---------------/");
    	}
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        int mPageNumber = getArguments().getInt(ARG_PAGE);
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(getString(R.string.title_template_step, mPageNumber));        
        return rootView;
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	scanFragments();
    }
}
