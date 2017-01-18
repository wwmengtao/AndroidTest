package com.mt.androidtest.myselfview;

import static com.mt.androidtest.myselfview.MySelfViewActivity.NUM_PAGES;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
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
import com.mt.androidtest.R;
import com.mt.androidtest.image.ImageDecodeInfo;
import com.mt.androidtest.image.ImageProcess;
import com.mt.androidtest.image.ImageProcess.StreamType;
import com.mt.androidtest.image.ImageProcess.ViewSize;

public class ViewPagerComponets {
	
	/**
	 * FragmentPagerAdapter与FragmentStatePagerAdapter的主要区别就在与对于fragment是否销毁，下面细说：
	 * 区别1)FragmentPagerAdapter：对于不再需要的fragment，选择调用onDestroyView方法，仅销毁视图，并不会销毁fragment实例。
	 * 因此之前加载过的fragment会保存在内存中，如果数量达到一定值会显著消耗内存。
	 * 区别2)FragmentStatePagerAdapter：会销毁不再需要的fragment，当当前事务提交以后，会彻底的将fragmeng从当前Activity
	 * 的FragmentManager中移除，当然也会从内存中移除(保留当前项目左右各一个，其余的调用onDestroyView、onDestroy、onDetach彻底销毁)。state标明，
	 * 销毁时，会将其onSaveInstanceState(Bundle outState)中的bundle信息保存下来，当用户切换回来，可以通过该bundle恢复生成新的fragment，
	 * 也就是说，你可以在onSaveInstanceState(Bundle outState)方法中保存一些数据，在onCreate中进行恢复创建。
	 * 总之：使用FragmentStatePagerAdapter当然更省内存，但是销毁新建也是需要时间的。一般情况下，如果你是制作主页面，
	 * 就3、4个Tab，那么可以选择使用FragmentPagerAdapter，如果你是用于ViewPager展示数量特别多的条目时，那么建议使用
	 * FragmentStatePagerAdapter。
	 */
	
	/**
	 * FragmentStatePagerAdapter
	 * @author Mengtao1
	 *
	 */
	public static class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

		public MyFragmentStatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment mFragment = ScreenSlidePageFragment.get(position);
			ALog.Log1("FragmentStatePagerAdapter.getItem_position: "+position);
//			ScreenSlidePageFragment.scanFragments();
			return mFragment;
		}
		
		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
	
	/**
	 * FragmentPagerAdapter：专为添加Fragment设计
	 * @author Mengtao1
	 *
	 */
	public static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
			Fragment mFragment = ScreenSlidePageFragment.get(position);
			ALog.Log1("FragmentPagerAdapter.getItem_position: "+position);
//			ScreenSlidePageFragment.scanFragments();
			return mFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
        
    	@Override  
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡 
             return super.instantiateItem(container, position); 
        }  
    }
    
    /**
     * PagerAdapter:添加自定义View
     * @author Mengtao1
     *
     */
	public static class MyPagerAdapter extends PagerAdapter implements OnPageChangeListener{
		private static String assetPicDir = "ViewPagerPics";
		private List<ImageView> ivList;
		private ViewPager mViewPager = null;
		private Activity mActivity = null;
		private int picNum = -1;
		private TextView tv_img_desc = null;//用于显示标题
		private List<String> titles = null;
		private LinearLayout ll_dot_group = null; //用来添加小圆点
		private int previousPosition = 0; //默认为0
		
		public MyPagerAdapter(Activity mActivity, ViewPager mViewPager){
			this.mActivity = mActivity;
			this.mViewPager = mViewPager;
			picNum = ImageDecodeInfo.getFilesNum(assetPicDir);
			ALog.Log("picNum:"+picNum);
			if(picNum <= 0)return;
			doInit();
		}
		
		private void doInit(){
			tv_img_desc = (TextView) mActivity.findViewById(R.id.tv_img_desc); tv_img_desc.setVisibility(View.VISIBLE);
			ll_dot_group = (LinearLayout) mActivity.findViewById(R.id.ll_dot_group); //用来添加小圆点
			ll_dot_group.removeAllViews();ll_dot_group.setVisibility(View.VISIBLE);
			ViewSize mViewSize = ImageProcess.getViewSize(mViewPager);
			ImageView mImageView = null;
			Bitmap mBitmap = null;
			ivList = new ArrayList<>();
			titles = new ArrayList<>();
			View dotView = null;
			for(int i = 0; i < picNum; i++){
				mImageView = new ImageView(mActivity);
				mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
				mBitmap = ImageProcess.decodeSampledBitmap(assetPicDir+File.separator+(i+1)+".png", StreamType.Asset, mViewSize.getWidth(), mViewSize.getHeight(),true);
				if(null != mBitmap){
					mImageView.setImageBitmap(mBitmap);
				}else{
					mImageView.setBackgroundResource(R.drawable.not_found);
				}
				ivList.add(mImageView);
				titles.add("item: "+i);
				//以下设置进度圆点
	            dotView = new View(mActivity.getApplicationContext());
	            dotView.setBackgroundResource(R.drawable.selector_dot);
	            //设置小圆点的宽和高
	            LayoutParams params = new LayoutParams(20, 20);
	            //设置每个小圆点之间距离
	            if (i != 0)params.leftMargin = 20;
	            dotView.setLayoutParams(params);
	            //设置小圆点默认状态
	            if (i != 0){
	            	dotView.setEnabled(false);
	            }else{
	            	dotView.setEnabled(true);
	            }
	            //把dotview加入到线性布局中
	            ll_dot_group.addView(dotView);
			}
			tv_img_desc.setText(titles.get(0));
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

	@Override  
	/**
	 * instantiateItem的返回值为Object，就是isViewFromObject的第二个参数Object。此时的Object可以是任意
	 * 类型数据，View、int。
	 */
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
		int newposition = position % ivList.size();
		ImageView iv = ivList.get(newposition);
		container.addView(iv);//添加页卡  
//		ScreenSlidePageFragment.scanFragments();
		return iv;  
    }  

		@Override
		/**
		 * isViewFromObject：参数object即为instantiateItem的返回值，可以是任意类型数据。
		 */
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}
		
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
	    
	}
	
	/**
	 * ViewPager+Fragment形式的页面变动时的监听器
	 * @author Mengtao1
	 *
	 */
	public static class MyFragmentOnPageChangeListener implements OnPageChangeListener{
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
//			ALog.Log("onPageScrollStateChanged: "+arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
//			ALog.Log("onPageScrolled_arg0: "+arg0+" arg1: "+arg1+" arg2: "+arg2);
		}

		@Override
		public void onPageSelected(int currentIndex) {
			// TODO Auto-generated method stub
			ALog.Log1("mViewPager.getCurrentIndex :"+currentIndex+" "+ScreenSlidePageFragment.get(currentIndex));
//			ScreenSlidePageFragment.scanFragments();
		}
		
	}
}
