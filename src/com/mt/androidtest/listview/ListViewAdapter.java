package com.mt.androidtest.listview;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mt.androidtest.R;

public class ListViewAdapter extends BaseAdapter {
	public static final String TAG_ITEM_TEXT = "itemText";
    public ArrayList <HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    ArrayList <Method> mMethodList = new ArrayList<Method>();
    private LayoutInflater mLayoutInflater;
    private int mDensityDpi = 0;
    private DisplayMetrics metric=null;
    private int mMode=0;
    private Context mContext=null;
    private boolean isListViewRolling = false;
    public ListViewAdapter(Context context) {
    	mContext=context.getApplicationContext();
    	mLayoutInflater = LayoutInflater.from(mContext);
        metric  = mContext.getResources().getDisplayMetrics();
        mDensityDpi = metric.densityDpi;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setMode(int mode){
    	mMode = mode;
    }
    
    public void setupList(ArrayList<HashMap<String, Object>> list) {
    	mList.clear();
    	for(int i = 0;i<list.size();i++){
    		mList.add(list.get(i));
    	}
    }

	public void setupList(String [] mArrayFT){
		mList.clear();
		HashMap<String, Object> map = null;
		for(int i=0;i<mArrayFT.length;i++){
			map = new HashMap<String, Object>();
			map.put(TAG_ITEM_TEXT, mArrayFT[i]);
			mList.add(map);
		}
	}
    
	/**
	 * 反射获取对象内部的所有方法
	 * @param obj
	 */
	public void setupList(Object obj){
		mList.clear();
		mMethodList.clear();
		String methodName=null;
		HashMap<String, Object> map = null;
		Class<?> mClass = obj.getClass();
		Method [] mMethods = mClass.getDeclaredMethods();
		for(Method mMethod:mMethods){
			methodName = mMethod.getName();
			map = new HashMap<String, Object>();
			map.put(TAG_ITEM_TEXT, methodName);
			mList.add(map);
			mMethodList.add(mMethod);
		}
	}
	
	public void setScrollState(boolean isListViewRolling){
		this.isListViewRolling = isListViewRolling;
	}
	
    public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder = null;
        switch(mMode){
        	case 1:
        		mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview, position);
           	break;
        	case 2:
        		mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview_function, position);
           	break;           	
        }
        String mText = null;
        TextView mTextView = null;
        switch(mMode){
	    	case 1:
				ImageView image = mViewHolder.getView(R.id.menu_img);
				mTextView = mViewHolder.getView(R.id.menu_label);
				mText = (String) mList.get(position).get(TAG_ITEM_TEXT);
		        Object obj = mList.get(position).get("itemImage");
		        if(isListViewRolling){
		        	image.setImageResource(R.drawable.loading);
		        	image.setTag(obj);
		        	//
		        	mTextView.setText("Loading...");
	        		mTextView.setTag(mText);
		        }else{
			        if(obj instanceof Drawable){
			        	image.setImageDrawable((Drawable)obj);
			        }else if(obj instanceof Integer){
			        	image.setImageResource((Integer)obj);
			        }
			        image.setTag(null);
			        //
			        mTextView.setText(mText);
			        mTextView.setTag(null);
		        }
		        setLayoutParams(image);
		        break;
	    	case 2:
	    		mTextView = mViewHolder.getView(R.id.text_ft);
	        	mText = (String) mList.get(position).get(TAG_ITEM_TEXT);
	        	if(isListViewRolling){
	        		mTextView.setText("Loading...");
	        		mTextView.setTag(mText);
	        	}else{
	        		mTextView.setText(mText);
	        		mTextView.setTag(null);
	        	}
	        	break;
        }
        return mViewHolder.getConvertView();
    }
    /**
     * setLayoutParams: Define the LayoutParams of mView to avoid being too big to display
     * @param mView
     */
    public void setLayoutParams(View mView){
    	ViewGroup.LayoutParams lp = mView.getLayoutParams();
    	lp.width= (int)(mDensityDpi*0.3);//144;
    	lp.height = (int)(mDensityDpi*0.3);//144;
    	mView.setLayoutParams(lp);
    }
}
