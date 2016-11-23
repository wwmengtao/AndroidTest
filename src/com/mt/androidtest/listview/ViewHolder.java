package com.mt.androidtest.listview;

import java.util.concurrent.atomic.AtomicInteger;

import com.mt.androidtest.ALog;
import com.mt.androidtest.image.ImageLoader;
import com.mt.androidtest.image.PicConstants.Type;

import static com.mt.androidtest.image.ImageLoader.IsLogRun;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ViewHolder{
	private final SparseArray<View> mViews;
	protected View mConvertView;
	private Context mContext = null;
	protected ViewHolder(Context context, ViewGroup parent, int layoutId,	int position){
		mContext = context.getApplicationContext();
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
		// setTag
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position){
		ViewHolder holder = null;
		if (convertView == null){
			holder = new ViewHolder(context, parent, layoutId, position);
		} else{
			holder = (ViewHolder) convertView.getTag();
		}
		return holder;
	}

	public View getConvertView(){
		return mConvertView;
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if (view == null){
			view = mConvertView.findViewById(viewId);
			if(null!=view)mViews.put(viewId, view);
		}
		return (T) view;
	}
	
	public static class ImageViewParas{
		public static int defaultWidth=0;
		public static int defaultHeight=0;
		public ImageView mImageView=null;
		public String url=null;
		public Bitmap mBitmap = null;
	}
	
	public ViewHolder setImageByUrl(int viewId, String url){
		ImageViewParas mImageViewParas = new ImageViewParas();
		mImageViewParas.mImageView = (ImageView) getView(viewId);
		if(IsLogRun)ALog.Log("widthOfIV1:"+mImageViewParas.mImageView.getMeasuredWidth()+" heightOfIV:"+mImageViewParas.mImageView.getMeasuredHeight());
		mImageViewParas.url = url;
		ImageLoader.getInstance(mContext).setQueueType(Type.LIFO);
		ImageLoader.getInstance(mContext).loadImage(mImageViewParas);
		return this;
	}
}
