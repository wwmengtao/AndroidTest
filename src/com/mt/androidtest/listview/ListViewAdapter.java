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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.mt.androidtest.R;

public class ListViewAdapter extends BaseAdapter {
    public ArrayList <HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    ArrayList <Method> mMethodList = new ArrayList<Method>();
    private LayoutInflater mLayoutInflater;
    private int mDensityDpi = 0;
    private DisplayMetrics metric=null;
    private int mMode=0;
    private Context mContext=null;
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
	
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            switch(mMode){
        	case 1:
        		view = mLayoutInflater.inflate(R.layout.item_getview, parent,false);
           	break;
        	case 2:
        		view = mLayoutInflater.inflate(R.layout.item_getview_function, parent,false);
           	break;           	
            }
        }else {
        	view = convertView;
        }
        if(1==mMode){
			ImageView image = (ImageView)view.findViewById(R.id.menu_img);
	        TextView title = (TextView)view.findViewById(R.id.menu_label);
	        Object obj = mList.get(position).get("itemImage");
	        if(obj instanceof Drawable){
	        	image.setImageDrawable((Drawable)obj);
	        }else if(obj instanceof Integer){
	        	image.setImageResource((Integer)obj);
				view.setBackgroundColor(mContext.getResources().getColor(R.color.wheat));
	        }
	        title.setText((String) mList.get(position).get("itemText"));
	        setLayoutParams(image);
        }else if(2==mMode){
        	TextView mTvFT = (TextView)view.findViewById(R.id.text_ft);
        	mTvFT.setText((String) mList.get(position).get("itemText"));
        }
        return view;
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
