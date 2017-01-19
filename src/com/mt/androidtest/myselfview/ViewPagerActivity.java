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
	private LinearLayout ll_dot_group = null; //�������СԲ��
	private TextView tv_img_desc = null;//������ʾ����
	private List<String> titles = null;
	private String assetPicDir = "ViewPagerPics";
	private List<ImageView> ivList;
	//
	private int previousPosition = 0; //Ĭ��Ϊ0
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
		mMyPagerAdapter = new MyPagerAdapter();//ViewPager��ʾ�Զ���View
		mViewPager.addOnPageChangeListener(mMyonPageChangeListener);
		mViewPager.setAdapter(mMyPagerAdapter);
		int initialItem = mMyPagerAdapter.getInitialItem();
		mViewPager.setCurrentItem(initialItem);
		ll_dot_group.getChildAt(0).setEnabled(true);
	}
	
	private void doInit(){
		mViewPager = (ViewPager) findViewById(R.id.myviewpager);
		tv_img_desc = (TextView) findViewById(R.id.tv_img_desc);
		ll_dot_group = (LinearLayout) findViewById(R.id.ll_dot_group); //�������СԲ��
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
			//�������ý���Բ��
            dotView = new View(getApplicationContext());
            dotView.setBackgroundResource(R.drawable.selector_dot);
            //����СԲ��Ŀ�͸�
            LayoutParams params = new LayoutParams(20, 20);
            //����ÿ��СԲ��֮�����
            if (i != 0)params.leftMargin = 20;
            dotView.setLayoutParams(params);
            //����СԲ��Ĭ��״̬
            dotView.setEnabled(false);
            //��dotview���뵽���Բ�����
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
     * PagerAdapter:����Զ���View
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
		 * instantiateItem�ķ���ֵΪObject������isViewFromObject�ĵڶ�������Object����ʱ��Object����������
		 * �������ݣ�View��int��
		 */
		@Override  
	    public Object instantiateItem(ViewGroup container, int position) {  //�����������ʵ����ҳ��
			ALog.Log("position:"+position);
			int newposition = position % ivList.size();
			ImageView iv = ivList.get(newposition);
			container.addView(iv);//���ҳ��  
			return iv;  
	    }  

		/**
		 * isViewFromObject������object��ΪinstantiateItem�ķ���ֵ�������������������ݡ�
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		} 
	}
	
	private OnPageChangeListener mMyonPageChangeListener = new OnPageChangeListener(){

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
		
	};
}
