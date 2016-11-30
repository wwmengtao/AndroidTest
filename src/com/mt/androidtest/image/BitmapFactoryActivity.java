package com.mt.androidtest.image;

import java.io.InputStream;
import java.util.List;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.image.ImageProcess.StreamType;

import static com.mt.androidtest.image.Images_UIL.imageThumbUrls_GL;

public class BitmapFactoryActivity extends BaseActivity {
	private AssetManager mAssetManager=null;
	private Bitmap mBitmap,mBitmap2=null;
	private ImageView mImageView = null;
	private ImageView mImageView2 = null;
	private TextView mTextView = null;
	private BaseAdapter mBitmapAdapter = null;
	private List<String>largeNumPicsAL = null;
	private List<String>largeNumPicsAL2 = null;
	private PicConstants mPicConstants = null;
	private int picNum = 3000;

	private ListView mListView = null;
	private GridView mGridView = null;	
	private int index = 0;
	//
	private  static final int Menu_PIC = 1;
	private  static final int Menu_ListView = 2;
	private  static final int Menu_GridView = 3;
	//
	private boolean pauseOnScroll = false;
	private boolean pauseOnFling = true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAssetManager = getAssetManager();
		ImageDecodeInfo.setAssetManager(mAssetManager);
		mPicConstants = new PicConstants();
		largeNumPicsAL = mPicConstants.createLargeNumHDPics(picNum);
		largeNumPicsAL2 = mPicConstants.createLargeNumHDPics(picNum);
		//在开头插入6张来自网络的图片
		for(int i=3;i<=8;i++){
			largeNumPicsAL2.add(0, imageThumbUrls_GL[i]);
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// -------------向menu中添加字体大小的子菜单-------------
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu_PIC, 0, "Pics");
		menu.add(0, Menu_ListView, 0, "ListView");
		menu.add(0, Menu_GridView, 0, "GridView");		
		return true;
	}
	
	private boolean showPic = false;
	public boolean onOptionsItemSelected(MenuItem mi)
	{
		showPic = false;
		switch (mi.getItemId()){
		case Menu_PIC:
			showPic = true;
			setContentView(R.layout.activity_bitmap_factory);
			mImageView=(ImageView)findViewById(R.id.myimageview);
			mImageView2=(ImageView)findViewById(R.id.myimageview2);
			mTextView=(TextView)findViewById(R.id.mytextview);
			break;
		case Menu_ListView:
			setContentView(R.layout.activity_listview_test);
			mListView = (ListView)this.findViewById(R.id.listview);
			mBitmapAdapter = new BitmapAdapter(this, largeNumPicsAL);
			mListView.setAdapter(mBitmapAdapter);
			break;				
		case Menu_GridView:
			setContentView(R.layout.activity_gridview);
			mGridView = (GridView)this.findViewById(R.id.gridview);
			mBitmapAdapter = new BitmapAdapter2(this, largeNumPicsAL2);			
			mGridView.setAdapter(mBitmapAdapter);
			mGridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(this), pauseOnScroll, pauseOnFling));
			break;
		}
		return super.onOptionsItemSelected(mi);
	}
	
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if(hasWindowFocus){
			if(showPic)showPic();
		}
	}
	 
	/**
	 * 显示压缩和未压缩图片的示意图
	 */
	public void showPic(){
		InputStream is_HD=null, is=null;
		mAssetManager = getAssetManager();
		try {
			is_HD = mAssetManager.open(PicConstants.assetHDPicNames[0]);//从Asset文件夹中读取高清图片
			is = mAssetManager.open(PicConstants.assetPicPicNames[0]);//从Asset文件夹中读取图片
		}catch (Exception e) {
			e.printStackTrace();
		}
		//一、高清图片处理
		//1.1、不对图片进行压缩，直接显示到mImageView
		mBitmap = ImageProcess.decodeSampledBitmap(PicConstants.assetHDPicNames[0], StreamType.Asset, mImageView.getWidth(),mImageView.getHeight(),false);
		mImageView.setImageBitmap(mBitmap);
		//1.2、对图片进行压缩，显示到mImageView2
		//注意：mImageView2.getWidth或者getHeight获取数值单位是像素
		mBitmap2 = ImageProcess.decodeSampledBitmap(PicConstants.assetHDPicNames[0], StreamType.Asset, mImageView2.getWidth(),mImageView2.getHeight(),true);
		mImageView2.setImageBitmap(mBitmap2);
		//二、非高清图片，不设置采样率，ScaleType测试用
		index = 0;
		mTextView.setOnClickListener(this);
		mBitmap = ImageProcess.decodeSampledBitmap(PicConstants.assetPicPicNames[0], StreamType.Asset, mImageView.getWidth(),mImageView.getHeight(),false);
		mBitmap2 = ImageProcess.decodeSampledBitmap(PicConstants.assetPicPicNames[0], StreamType.Asset, mImageView.getWidth(),mImageView.getHeight(),false);
	}

	//以下罗列出所有ScaleType类型
	private String[] mScaleTypeDes={
			"ScaleType.MATRIX",
			"ScaleType.FIT_XY",
			"ScaleType.FIT_START",
			"ScaleType.FIT_CENTER",
			"ScaleType.FIT_END",
			"ScaleType.CENTER",
			"ScaleType.CENTER_CROP",
			"ScaleType.CENTER_INSIDE"
	};

	/**
	 * 一、CENTER类
	 * ScaleType.CENTER：             图片大小为原始大小(不做缩放)，如果图片大小超出了xml中设置的ImageView控件大小，则截取图片中间部分，若小于ImageView则图片居中显示；
	 * ScaleType.CENTER_CROP：  将图片等比例缩放，让图片的短边与ImageView的对应边长度相同(例如1024*768图片显示在540*960的ImageView上，图片短边为768<960，将其放大到960后整体图片跟着放大，然后截取中间部分），即不能留空白，最终缩放后截取中间部分进行显示。
	 * ScaleType.CENTER_INSIDE：大图等比例缩小，直到整幅图能够居中显示在ImageView中。小于ImageView的图片不变，直接居中显示。
	 * 二、FIT类
	 * ScaleType.FIT_CENTER：ImageView的默认状态，大图等比例缩小，使整幅图能够居中显示在ImageView中，小图等比例放大，同样要整体居中显示在ImageView中。
	 * ScaleType.FIT_END：缩放方式同FIT_CENTER，只是将图片显示在右方或下方，而不是居中。
	 * ScaleType.FIT_START：缩放方式同FIT_CENTER，只是将图片显示在左方或上方，而不是居中。
	 * ScaleType.FIT_XY：将图片非等比例缩放到大小与ImageView相同。
	 * 三、ScaleType.MATRIX：是根据一个3×3的矩阵对其中图片进行缩放。
	 */
	private ScaleType[] mScaleTypeArray={
			ScaleType.MATRIX,
			ScaleType.FIT_XY,
			ScaleType.FIT_START,
			ScaleType.FIT_CENTER,
			ScaleType.FIT_END,
			ScaleType.CENTER,
			ScaleType.CENTER_CROP,
			ScaleType.CENTER_INSIDE
	};
	
	@Override
	public void onClick(View v) {
		mTextView.setText(mScaleTypeDes[index%mScaleTypeArray.length]);
		//
		mImageView.setScaleType(mScaleTypeArray[index%mScaleTypeArray.length]);
		mImageView.setImageBitmap(mBitmap);
		//
		mImageView2.setScaleType(mScaleTypeArray[index%mScaleTypeArray.length]);
		mImageView2.setImageBitmap(mBitmap2);  
		//
		index++;
	}
	
    @Override
    protected void onStop() {
    	ImageLoader.getInstance(getApplicationContext()).stop();
        super.onStop();
    }
}
