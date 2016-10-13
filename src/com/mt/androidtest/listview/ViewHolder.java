package com.mt.androidtest.listview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ViewHolder
{
	private final SparseArray<View> mViews;
	protected View mConvertView;
	protected ViewHolder(Context context, ViewGroup parent, int layoutId,	int position){
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
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
}
