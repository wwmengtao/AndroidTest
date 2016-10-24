package com.mt.androidtest.myselfview;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.listview.ListViewTestAdapter_MultiLayout;
import com.mt.androidtest.listview.ListViewTestAdapter_SingleLayout;
/**
 * �Զ���ؼ���Ϊ���ࣺ�Լ����ơ���ϡ��̳У���activity_customed_controller�е�SelfDrawnView��CombinedView��InheritedView��
 * @author Mengtao1
 *
 */
public class MySelfViewActivity extends BaseActivity {
	private static final int Menu_Common = 0;
	private static final int Menu_Scroll = 1;	
	private static final int Menu_TabHost = 2;		
	//
    private static final int NUM_PAGES = 5;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;	
    private TextView mTextView;
    //
    private ListView mListView;
    private ListViewTestAdapter_SingleLayout mListAdapter_SingleLayout;
    private ListViewTestAdapter_MultiLayout mListAdapter_MultiLayout;
	//
    private TabHost mTabHost;
    private TabWidget mTabWidget;
    private ListView mListViewTabHost;
    //
    GridView mGridView=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mListAdapter_SingleLayout = new ListViewTestAdapter_SingleLayout(this);
        mListAdapter_MultiLayout = new ListViewTestAdapter_MultiLayout(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// -------------��menu����������С���Ӳ˵�-------------
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu_Common, 0, "Common");
		menu.add(0, Menu_Scroll, 0, "ScrollView");
		menu.add(0, Menu_TabHost, 0, "TabHost");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)	{
		switch (mi.getItemId()){
		case Menu_Common:
			setContentView(R.layout.activity_myselfview_common);
			initHorizontalScrollView();
			break;
		case Menu_Scroll:
			setContentView(R.layout.activity_myselfview_scrollview);
			initMyScrollView();
			break;			
		case Menu_TabHost:
			setContentView(R.layout.activity_myselfview_tabhost);
			initTabHostView();
			break;				
		}
		return super.onOptionsItemSelected(mi);
	}	
	//HorizontalScrollView��ʹ��
	public void initHorizontalScrollView(){
		int scale = (int) (0.03125 * getDensityDpi());
		int columnWidth = scale * 26;
		int horizontalSpacing = scale;
		//GridView���Ը���mListAdapter_SingleLayout����Ŀ�����Լ���ʾ����NumColumns���Զ�ȷ������
		int NumColumns = mListAdapter_SingleLayout.getCount()/2;
		mGridView = (GridView) findViewById(R.id.mygridview);
		ViewGroup.LayoutParams lp = mGridView.getLayoutParams();
		lp.width = NumColumns * (columnWidth+horizontalSpacing);
		mGridView.setLayoutParams(lp);
		mGridView.setColumnWidth(columnWidth);
		mGridView.setHorizontalSpacing(horizontalSpacing);
		mGridView.setVerticalSpacing(horizontalSpacing/2);
		mGridView.setStretchMode(GridView.NO_STRETCH);
		mGridView.setNumColumns(NumColumns);
		mGridView.setAdapter(mListAdapter_SingleLayout);
	}
	//
	public void initTabHostView(){
	    mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mListViewTabHost = (ListView) findViewById(android.R.id.list);
        //TabHost��ʹ��
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(mTabListener);
        mTabHost.clearAllTabs();
        mTabHost.addTab(buildTabSpec("0","title0"));
        mTabHost.addTab(buildTabSpec("1","title1"));
        mTabHost.addTab(buildTabSpec("2","title2"));
        mTabHost.setCurrentTab(0);
        //mTabWidget��ÿ��TextView�����ݶ���ҪĬ��Ϊ��д
        if(null!=mTabWidget && mTabWidget.getChildCount()>0){
	        for (int i = 0; i < mTabWidget.getChildCount(); i++) {
	            TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(android.R.id.title);
	            tv.setTransformationMethod(null);//������Ϊ��д
	        }
        }
	}
	
    private OnTabChangeListener mTabListener = new OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
        	ALog.Log("tabId:"+tabId);
        	final int slotId = Integer.parseInt(tabId);
        	switch(slotId){
	        	case 0:
	        		mListViewTabHost.setAdapter(mListAdapter_SingleLayout);
	        		break;
	        	default:
	        		mListViewTabHost.setAdapter(mListAdapter_MultiLayout);
        	}
        }
    };
	
    private TabContentFactory mEmptyTabContent = new TabContentFactory() {
        @Override
        public View createTabContent(String tag) {
            return new View(mTabHost.getContext());
        }
    };

    private TabSpec buildTabSpec(String tag, String title) {
        return mTabHost.newTabSpec(tag).setIndicator(title).setContent(
                mEmptyTabContent);
    }
    
    
    private List<Fragment> mFragments=null;
    private List<View> mListViews=null;
	//ViewPager��ʹ��
	public void initMyScrollView(){
		mTextView = (TextView)findViewById(R.id.mytextview);
		mTextView.setOnClickListener(mOnClickListener);
		mFragments = new ArrayList<Fragment>();
		mListViews = new ArrayList<View>();
		for(int i=0;i<NUM_PAGES;i++){
			mFragments.add(ScreenSlidePageFragment.create(i));
			mListViews.add(new CombinedView(this));
		}
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        mPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager(),mFragments);//ViewPager��ʾFragment���ȸ��Ƽ�����
        mViewPager.setAdapter(mPagerAdapter);
        //
        mListView = (ListView) findViewById(R.id.mylistview);
        mListView.setAdapter(mListAdapter_SingleLayout);
        setListViewHeightBasedOnChildren(mListView);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener(){
		private int index=0;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int value = (index++%3);
			ALog.Log("value:"+value);
			switch(value){
			case 0:
				mTextView.setText("FragmentPagerAdapter");
				mPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager(),mFragments);//ViewPager��ʾFragment���ȸ��Ƽ�����
				break;
			case 1:
				mTextView.setText("PagerAdapter");
				mPagerAdapter = new MyPagerAdapter(mListViews);//ViewPager��ʾ�Զ���View
				break;
			case 2:
				mTextView.setText("FragmentStatePagerAdapter");
				mFragments.clear();
				for(int i=0;i<NUM_PAGES;i++){
					mFragments.add(ScreenSlidePageFragment.create(i));
				}
				mPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager(),mFragments);//ViewPager��ʾFragment���ȸ��Ƽ�����
				break;
			}
	        mViewPager.setAdapter(mPagerAdapter);
		}
	};
	
	/**
	 * FragmentPagerAdapter��FragmentStatePagerAdapter����Ҫ������������fragment�Ƿ����٣�����ϸ˵��
	 * ����1)FragmentPagerAdapter�����ڲ�����Ҫ��fragment��ѡ�����detach��������������ͼ������������fragmentʵ����
	 * ����2)FragmentStatePagerAdapter�������ٲ�����Ҫ��fragment������ǰ�����ύ�Ժ󣬻᳹�׵Ľ�fragmeng�ӵ�ǰActivity
	 * ��FragmentManager���Ƴ���state����������ʱ���Ὣ��onSaveInstanceState(Bundle outState)�е�bundle��Ϣ����������
	 * ���û��л�����������ͨ����bundle�ָ������µ�fragment��Ҳ����˵���������onSaveInstanceState(Bundle outState)
	 * �����б���һЩ���ݣ���onCreate�н��лָ�������
	 * ��֮��ʹ��FragmentStatePagerAdapter��Ȼ��ʡ�ڴ棬���������½�Ҳ����Ҫʱ��ġ�һ������£��������������ҳ�棬
	 * ��3��4��Tab����ô����ѡ��ʹ��FragmentPagerAdapter�������������ViewPagerչʾ�����ر�����Ŀʱ����ô����ʹ��
	 * FragmentStatePagerAdapter��
	 */
	
	/**
	 * FragmentStatePagerAdapter
	 * @author Mengtao1
	 *
	 */
	private class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> mFragments; 
		public MyFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			mFragments = fragments;
		}
		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}
		
		@Override
		public int getCount() {
			return mFragments.size();
		}
	}
	
	/**
	 * FragmentPagerAdapter��רΪ���Fragment���
	 * @author Mengtao1
	 *
	 */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    	private List<Fragment> mFragments; 
        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
    
    /**
     * PagerAdapter:����Զ���View
     * @author Mengtao1
     *
     */
	private class MyPagerAdapter extends PagerAdapter {
        private List<View> mListViews;  
        
        public MyPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;//���췽�������������ǵ�ҳ���������ȽϷ��㡣  
        }  
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

	@Override  
    public Object instantiateItem(ViewGroup container, int position) {  //�����������ʵ����ҳ��         
         container.addView(mListViews.get(position), 0);//���ҳ��  
         return mListViews.get(position);  
    }  

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}
