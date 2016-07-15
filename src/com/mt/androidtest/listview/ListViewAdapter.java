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
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public class ListViewAdapter extends BaseAdapter {
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
			map.put("itemText", mArrayFT[i]);
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
			map.put("itemText", methodName);
			mList.add(map);
			mMethodList.add(mMethod);
		}
	}
	
	public void setScrollState(int scrollState){
		isListViewRolling = (OnScrollListener.SCROLL_STATE_FLING==scrollState);//SCROLL_STATE_FLING表示手指做了抛的动作（手指离开屏幕前，用力滑了一下，屏幕产生惯性滑动）
	}
	
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder.viewHolder2 holder2 = null;
		ViewHolder.viewHolder3 holder3 = null;
        if (convertView == null) {
            switch(mMode){
	        	case 1:
	        		convertView = mLayoutInflater.inflate(R.layout.item_getview, parent,false);
	        		holder3 = new ViewHolder.viewHolder3();
	        		holder3.imageView = (ImageView)convertView.findViewById(R.id.menu_img);
	        		holder3.textView = (TextView)convertView.findViewById(R.id.menu_label);
	        		convertView.setTag(R.id.holder1, holder3);
	           	break;
	        	case 2:
	        		convertView = mLayoutInflater.inflate(R.layout.item_getview_function, parent,false);
	        		holder2 = new ViewHolder.viewHolder2();
	        		holder2.textView = (TextView)convertView.findViewById(R.id.text_ft);
	        		convertView.setTag(R.id.holder2, holder2);
	           	break;           	
            }
        }else {
            switch(mMode){
	        	case 1:
	        		holder3 = (ViewHolder.viewHolder3)convertView.getTag(R.id.holder1);
	               	break;
	        	case 2:
	        		holder2 = (ViewHolder.viewHolder2)convertView.getTag(R.id.holder2);
	        		break;
            }
        }
        ALog.Log("here1");
        if(isListViewRolling){
        	return convertView;//如果当前ListView处于滑动状态，那么停止加载图片、文字等内容
        }
        switch(mMode){
	    	case 1:
				ImageView image = holder3.imageView;
		        TextView title = holder3.textView;
		        Object obj = mList.get(position).get("itemImage");
		        if(obj instanceof Drawable){
		        	image.setImageDrawable((Drawable)obj);
		        }else if(obj instanceof Integer){
		        	image.setImageResource((Integer)obj);
		        }
		        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.wheat));
		        title.setText((String) mList.get(position).get("itemText"));
		        setLayoutParams(image);
		        break;
	    	case 2:
	        	TextView mTvFT = holder2.textView;
	        	mTvFT.setText((String) mList.get(position).get("itemText"));
	        	break;
        }
        ALog.fillInStackTrace("here2");
        return convertView;
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
