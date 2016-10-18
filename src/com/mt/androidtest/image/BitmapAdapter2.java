package com.mt.androidtest.image;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;
import com.mt.androidtest.listview.ViewHolder;
import com.mt.androidtest.listview.ViewHolder.ImageViewParas;
import com.mt.androidtest.tool.ExecutorHelper;

/**
 *
 * @author Mengtao1
 *
 */
public class BitmapAdapter2 extends CommonBaseAdapter<String>{
	private Context mContext = null;
    private int widthOfIV = 0;
    private int heightOfIV = 0;
    
	public BitmapAdapter2(Context context, List<String> mDatas){
		super(context, mDatas);
		mContext = context.getApplicationContext();
	}

	private ViewHolder mViewHolder=null;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        boolean needDoAdditionalWork = (null==convertView)?true:false;
        mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview_bitmap, position);
        if(needDoAdditionalWork)doAdditionalWork();
        String url = getItem(position);
		mViewHolder.setImageByUrl(R.id.myimageview, url);
		return mViewHolder.getConvertView();
	}
	
	public void doAdditionalWork(){
			View convertView = mViewHolder.getConvertView();
			ImageView mImageView =mViewHolder.getView(R.id.myimageview);
			mImageView.setScaleType(ScaleType.FIT_XY);//非等比例缩放，铺满整个ImageView
			if(0==widthOfIV && 0==heightOfIV){//获取mImageView的测量宽高
				convertView.measure(0, 0);
				widthOfIV = mImageView.getMeasuredWidth();
				heightOfIV = mImageView.getMeasuredHeight();
				ImageViewParas.width = widthOfIV;
				ImageViewParas.height = heightOfIV;
			}
	}
	
}
