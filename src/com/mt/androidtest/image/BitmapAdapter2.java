package com.mt.androidtest.image;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;
import com.mt.androidtest.listview.ViewHolder;
import com.mt.androidtest.listview.ViewHolder.ImageViewParas;

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
        ALog.Log("position:"+position+" needDoAdditionalWork:"+needDoAdditionalWork);
        mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview_bitmap, position);
        if(needDoAdditionalWork)doAdditionalWork();
        String url = getItem(position);
		mViewHolder.setImageByUrl(R.id.myimageview, url);
		return mViewHolder.getConvertView();
	}
	
	public void doAdditionalWork(){
			View convertView = mViewHolder.getConvertView();
			//����GridView������߶�
			int heightOfGridCell = 80;
			AbsListView.LayoutParams params = new AbsListView.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					heightOfGridCell);
			convertView.setLayoutParams(params);
			ImageView mImageView =mViewHolder.getView(R.id.myimageview);
			mImageView.setScaleType(ScaleType.FIT_XY);//�ǵȱ������ţ���������ImageView
			if(0==ImageViewParas.defaultWidth||0==ImageViewParas.defaultHeight){
				convertView.measure(0, 0);
				//������ֵ������Ϊ�����õĿ�߲ο�ֵ(һ�㲻������ʵ��ֵ)��һ���С�� displayMetrics.widthPixels�� displayMetrics.heightPixels
				ImageViewParas.defaultWidth = mImageView.getMeasuredWidth();
				ImageViewParas.defaultHeight = mImageView.getMeasuredHeight();
			}
	}
}
