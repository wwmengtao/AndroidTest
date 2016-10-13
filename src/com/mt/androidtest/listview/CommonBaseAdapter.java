package com.mt.androidtest.listview;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class CommonBaseAdapter<T> extends BaseAdapter
{
	protected Context mContext;
	protected List<T> mDatas;

	public CommonBaseAdapter(Context context, List<T> mDatas){
		this.mContext = context;
		this.mDatas = mDatas;
	}

	@Override
	public int getCount()	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position){
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position){
		return position;
	}
}
