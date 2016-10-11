package com.mt.androidtest.image;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

/**
 * 专为加载大图片设计的高性能图片适配器
 * @author Mengtao1
 *
 */
public class BitmapAdapter extends BaseAdapter{
	private Context mContext = null;
	private ArrayList<String>largeNumPicsAL = null;
	private PicConstants mPicConstants = null;
	private int picNum = 50;
    private LayoutInflater mLayoutInflater= null;
	private AssetManager mAssetManager=null;    
    private ImageView mImageView = null;
    private InputStream mInputStream = null;
    //
    private int widthOfIV = 0;
    private int heightOfIV = 0;
	public BitmapAdapter(Context context){
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mAssetManager = mContext.getResources().getAssets();
		mPicConstants = new PicConstants();
		largeNumPicsAL=mPicConstants.createLargeNumHDPics(picNum);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return largeNumPicsAL.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return largeNumPicsAL.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mViewHolder=null;
		if(null==convertView){
			convertView = mLayoutInflater.inflate(R.layout.item_getview_bitmap, parent,false);
			mViewHolder=new ViewHolder();
			mViewHolder.mImageView = (ImageView)convertView.findViewById(R.id.myimageview);
			mViewHolder.mImageView.setScaleType(ScaleType.FIT_XY);
			if(0==widthOfIV && 0==heightOfIV){//获取mImageView的测量宽高
				convertView.measure(0, 0);
				widthOfIV = mViewHolder.mImageView.getMeasuredWidth();
				heightOfIV = mViewHolder.mImageView.getMeasuredHeight();
			}
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder)convertView.getTag();
		}
		try {
			mInputStream = mAssetManager.open(largeNumPicsAL.get(position));//从Asset文件夹中读取高清图片
		}catch (Exception e) {
			
		}
		mImageView = mViewHolder.mImageView;
		mImageView.setImageBitmap(BitmapProcess.decodeSampledBitmap(mInputStream, widthOfIV, heightOfIV,true));  
		return convertView;
	}
	
	private class ViewHolder{
		ImageView mImageView = null;
	}
}
