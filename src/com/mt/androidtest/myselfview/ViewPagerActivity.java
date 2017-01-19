package com.mt.androidtest.myselfview;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SlidingPaneLayout.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.image.ImageDecodeInfo;
import com.mt.androidtest.image.ImageProcess;
import com.mt.androidtest.image.ImageProcess.StreamType;
import com.mt.androidtest.image.ImageProcess.ViewSize;

public class ViewPagerActivity extends BaseActivity {
    //
	private ViewPager mViewPager = null;
	private int ViewPagerItemNum = Integer.MAX_VALUE>>0xF;
	private LinearLayout ll_dot_group = null; //用来添加小圆点
	private TextView tv_img_desc = null;//用于显示标题
	private List<String> titles = null;
	private String assetPicDir = "ViewPagerPics";
	private List<ImageView> ivList;
	//
	private int previousPosition = 0; //默认为0
	private int initialItem = -1;
    //
    private MyPagerAdapter mMyPagerAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager);
		init();
	}
	
	private void init(){
		doInit();
		mMyPagerAdapter = new MyPagerAdapter();//ViewPager显示自定义View
		mViewPager.addOnPageChangeListener(mMyonPageChangeListener);
		mViewPager.setAdapter(mMyPagerAdapter);
		int initialItem = mMyPagerAdapter.getInitialItem();
		mViewPager.setCurrentItem(initialItem);
		ll_dot_group.getChildAt(0).setEnabled(true);
	}
	
	private void doInit(){
		mViewPager = (ViewPager) findViewById(R.id.myviewpager);
		tv_img_desc = (TextView) findViewById(R.id.tv_img_desc);
		ll_dot_group = (LinearLayout) findViewById(R.id.ll_dot_group); //用来添加小圆点
		ViewSize mViewSize = ImageProcess.getInstance(ViewPagerActivity.this).getViewSize(mViewPager);
		ImageView mImageView = null;
		Bitmap mBitmap = null;
		ivList = new ArrayList<>();
		titles = new ArrayList<>();
		View dotView = null;
		int picNum = ImageDecodeInfo.getInstance(getApplicationContext()).getFilesNum(assetPicDir);
		if(picNum <= 0)return;
		for(int i = 0; i < picNum; i++){
			mImageView = new ImageView(getApplicationContext());
			mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
			mBitmap = ImageProcess.getInstance(ViewPagerActivity.this).decodeSampledBitmap(assetPicDir+File.separator+(i+1)+".png", StreamType.Asset, mViewSize.getWidth(), mViewSize.getHeight(),true);
			if(null != mBitmap){
				mImageView.setImageBitmap(mBitmap);
			}else{
				mImageView.setBackgroundResource(R.drawable.not_found);
			}
			ivList.add(mImageView);
			titles.add("item: "+i);
			//以下设置进度圆点
            dotView = new View(getApplicationContext());
            dotView.setBackgroundResource(R.drawable.selector_dot);
            //设置小圆点的宽和高
            LayoutParams params = new LayoutParams(20, 20);
            //设置每个小圆点之间距离
            if (i != 0)params.leftMargin = 20;
            dotView.setLayoutParams(params);
            //设置小圆点默认状态
            dotView.setEnabled(false);
            //把dotview加入到线性布局中
            ll_dot_group.addView(dotView);
		}
		tv_img_desc.setText(titles.get(0));
		initialItem = ViewPagerItemNum/2 - (ViewPagerItemNum/2 % ivList.size()); 
	}
	
	
	@Override
	public void onStop(){
		super.onStop();
		mViewPager.removeOnPageChangeListener(mMyonPageChangeListener);
	}
	
    /**
     * PagerAdapter:添加自定义View
     * @author Mengtao1
     *
     */
	public class MyPagerAdapter extends PagerAdapter{

		public int getInitialItem(){
			return initialItem;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return ViewPagerItemNum;
		}

		
		/**
		 * instantiateItem的返回值为Object，就是isViewFromObject的第二个参数Object。此时的Object可以是任意
		 * 类型数据，View、int。
		 */
		@Override  
	    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
			ALog.Log("position:"+position);
			int newposition = position % ivList.size();
			ImageView iv = ivList.get(newposition);
			container.addView(iv);//添加页卡  
			return iv;  
	    }  

		/**
		 * isViewFromObject：参数object即为instantiateItem的返回值，可以是任意类型数据。
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		} 
	}
	
	private OnPageChangeListener mMyonPageChangeListener = new OnPageChangeListener(){

		//以下为OnPageChangeListener部分的实现方法
	    //当新的页面被选中的时候调用
	    @Override
	    public void onPageSelected(int position) {
	        //拿着position位置 % 集合.size
	        int newposition = position % ivList.size();
	        //取出postion位置的小圆点设置为true
	        ll_dot_group.getChildAt(newposition).setEnabled(true);
	        //把一个小圆点设置为false
	        ll_dot_group.getChildAt(previousPosition).setEnabled(false);
	        tv_img_desc.setText(titles.get(newposition));
	        previousPosition = newposition;
	    }

	    @Override
	    public void onPageScrollStateChanged(int state) {
	    }

	    //当页面开始滑动
	    @Override
	    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	    }
		
	};
}
