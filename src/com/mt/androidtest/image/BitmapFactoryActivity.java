package com.mt.androidtest.image;

import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class BitmapFactoryActivity extends BaseActivity {
	private int maxMemory=0;
	private AssetManager mAssetManager=null;
	private Bitmap mBitmap,mBitmap2=null;
	private ImageView mImageView = null;
	private ImageView mImageView2 = null;
	private TextView mTextView = null;
	private BitmapAdapter mBitmapAdapter = null;
	private ListView mListView = null;
	private GridView mGridView = null;	
	private int index = 0;
	//
	private  static final int Menu_PIC = 1;
	private  static final int Menu_ListView = 2;
	private  static final int Menu_GridView = 3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// -------------��menu����������С���Ӳ˵�-------------
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
			mListView.setAdapter(new BitmapAdapter(this));
			break;				
		case Menu_GridView:
			setContentView(R.layout.activity_gridview);
			mGridView = (GridView)this.findViewById(R.id.gridview);
			mGridView.setAdapter(new BitmapAdapter(this));
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
	 * ��ʾѹ����δѹ��ͼƬ��ʾ��ͼ
	 */
	public void showPic(){
		InputStream is_HD=null, is=null;
		mAssetManager = getAssetManager();
		try {
			is_HD = mAssetManager.open(PicConstants.assetHDPicNames[0]);//��Asset�ļ����ж�ȡ����ͼƬ
			is = mAssetManager.open(PicConstants.assetPicPicNames[0]);//��Asset�ļ����ж�ȡͼƬ
		}catch (Exception e) {
			e.printStackTrace();
		}
		//һ������ͼƬ����
		//1.1������ͼƬ����ѹ����ֱ����ʾ��mImageView
		mBitmap = BitmapProcess.decodeSampledBitmap(is_HD, mImageView.getWidth(),mImageView.getHeight(),false);
		mImageView.setImageBitmap(mBitmap);
		//1.2����ͼƬ����ѹ������ʾ��mImageView2
		//ע�⣺mImageView2.getWidth����getHeight��ȡ��ֵ��λ������
		mBitmap2 = BitmapProcess.decodeSampledBitmap(is_HD, mImageView2.getWidth(),mImageView2.getHeight(),true);
		mImageView2.setImageBitmap(mBitmap2);
		//�����Ǹ���ͼƬ�������ò����ʣ�ScaleType������
		index = 0;
		mTextView.setOnClickListener(this);
		mBitmap = BitmapProcess.decodeSampledBitmap(is, mImageView.getWidth(),mImageView.getHeight(),false);
		mBitmap2 = BitmapProcess.decodeSampledBitmap(is, mImageView.getWidth(),mImageView.getHeight(),false);
	}

	//�������г�����ScaleType����
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
	 * һ��CENTER��
	 * ScaleType.CENTER��             ͼƬ��СΪԭʼ��С(��������)�����ͼƬ��С������xml�����õ�ImageView�ؼ���С�����ȡͼƬ�м䲿�֣���С��ImageView��ͼƬ������ʾ��
	 * ScaleType.CENTER_CROP��  ��ͼƬ�ȱ������ţ���ͼƬ�Ķ̱���ImageView�Ķ�Ӧ�߳�����ͬ(����1024*768ͼƬ��ʾ��540*960��ImageView�ϣ�ͼƬ�̱�Ϊ768<960������Ŵ�960������ͼƬ���ŷŴ�Ȼ���ȡ�м䲿�֣������������հף��������ź��ȡ�м䲿�ֽ�����ʾ��
	 * ScaleType.CENTER_INSIDE����ͼ�ȱ�����С��ֱ������ͼ�ܹ�������ʾ��ImageView�С�С��ImageView��ͼƬ���䣬ֱ�Ӿ�����ʾ��
	 * ����FIT��
	 * ScaleType.FIT_CENTER��ImageView��Ĭ��״̬����ͼ�ȱ�����С��ʹ����ͼ�ܹ�������ʾ��ImageView�У�Сͼ�ȱ����Ŵ�ͬ��Ҫ���������ʾ��ImageView�С�
	 * ScaleType.FIT_END�����ŷ�ʽͬFIT_CENTER��ֻ�ǽ�ͼƬ��ʾ���ҷ����·��������Ǿ��С�
	 * ScaleType.FIT_START�����ŷ�ʽͬFIT_CENTER��ֻ�ǽ�ͼƬ��ʾ���󷽻��Ϸ��������Ǿ��С�
	 * ScaleType.FIT_XY����ͼƬ�ǵȱ������ŵ���С��ImageView��ͬ��
	 * ����ScaleType.MATRIX���Ǹ���һ��3��3�ľ��������ͼƬ�������š�
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
	
}
