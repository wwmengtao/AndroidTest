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
	 * FragmentPagerAdapter��FragmentStatePagerAdapter����Ҫ������������fragment�Ƿ����٣�����ϸ˵��
	 * ����1)FragmentPagerAdapter�����ڲ�����Ҫ��fragment��ѡ�����onDestroyView��������������ͼ������������fragmentʵ����
	 * ���֮ǰ���ع���fragment�ᱣ�����ڴ��У���������ﵽһ��ֵ�����������ڴ档
	 * ����2)FragmentStatePagerAdapter�������ٲ�����Ҫ��fragment������ǰ�����ύ�Ժ󣬻᳹�׵Ľ�fragmeng�ӵ�ǰActivity
	 * ��FragmentManager���Ƴ�����ȻҲ����ڴ����Ƴ�(������ǰ��Ŀ���Ҹ�һ��������ĵ���onDestroyView��onDestroy��onDetach��������)��state������
	 * ����ʱ���Ὣ��onSaveInstanceState(Bundle outState)�е�bundle��Ϣ�������������û��л�����������ͨ����bundle�ָ������µ�fragment��
	 * Ҳ����˵���������onSaveInstanceState(Bundle outState)�����б���һЩ���ݣ���onCreate�н��лָ�������
	 * ��֮��ʹ��FragmentStatePagerAdapter��Ȼ��ʡ�ڴ棬���������½�Ҳ����Ҫʱ��ġ�һ������£��������������ҳ�棬
	 * ��3��4��Tab����ô����ѡ��ʹ��FragmentPagerAdapter�������������ViewPagerչʾ�����ر�����Ŀʱ����ô����ʹ��
	 * FragmentStatePagerAdapter��
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
	 * FragmentPagerAdapter��רΪ���Fragment���
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
        public Object instantiateItem(ViewGroup container, int position) {  //�����������ʵ����ҳ�� 
             return super.instantiateItem(container, position); 
        }  
    }
    
    /**
     * PagerAdapter:����Զ���View
     * @author Mengtao1
     *
     */
	public static class MyPagerAdapter extends PagerAdapter implements OnPageChangeListener{
		private static String assetPicDir = "ViewPagerPics";
		private List<ImageView> ivList;
		private ViewPager mViewPager = null;
		private Activity mActivity = null;
		private int picNum = -1;
		private TextView tv_img_desc = null;//������ʾ����
		private List<String> titles = null;
		private LinearLayout ll_dot_group = null; //�������СԲ��
		private int previousPosition = 0; //Ĭ��Ϊ0
		
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
			ll_dot_group = (LinearLayout) mActivity.findViewById(R.id.ll_dot_group); //�������СԲ��
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
				//�������ý���Բ��
	            dotView = new View(mActivity.getApplicationContext());
	            dotView.setBackgroundResource(R.drawable.selector_dot);
	            //����СԲ��Ŀ�͸�
	            LayoutParams params = new LayoutParams(20, 20);
	            //����ÿ��СԲ��֮�����
	            if (i != 0)params.leftMargin = 20;
	            dotView.setLayoutParams(params);
	            //����СԲ��Ĭ��״̬
	            if (i != 0){
	            	dotView.setEnabled(false);
	            }else{
	            	dotView.setEnabled(true);
	            }
	            //��dotview���뵽���Բ�����
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
	 * instantiateItem�ķ���ֵΪObject������isViewFromObject�ĵڶ�������Object����ʱ��Object����������
	 * �������ݣ�View��int��
	 */
    public Object instantiateItem(ViewGroup container, int position) {  //�����������ʵ����ҳ��
		int newposition = position % ivList.size();
		ImageView iv = ivList.get(newposition);
		container.addView(iv);//���ҳ��  
//		ScreenSlidePageFragment.scanFragments();
		return iv;  
    }  

		@Override
		/**
		 * isViewFromObject������object��ΪinstantiateItem�ķ���ֵ�������������������ݡ�
		 */
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}
		
		//����ΪOnPageChangeListener���ֵ�ʵ�ַ���
	    //���µ�ҳ�汻ѡ�е�ʱ�����
	    @Override
	    public void onPageSelected(int position) {
	        //����positionλ�� % ����.size
	        int newposition = position % ivList.size();
	        //ȡ��postionλ�õ�СԲ������Ϊtrue
	        ll_dot_group.getChildAt(newposition).setEnabled(true);
	        //��һ��СԲ������Ϊfalse
	        ll_dot_group.getChildAt(previousPosition).setEnabled(false);
	        tv_img_desc.setText(titles.get(newposition));
	        previousPosition = newposition;
	    }

	    @Override
	    public void onPageScrollStateChanged(int state) {
	    }

	    //��ҳ�濪ʼ����
	    @Override
	    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	    }
	    
	}
	
	/**
	 * ViewPager+Fragment��ʽ��ҳ��䶯ʱ�ļ�����
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
