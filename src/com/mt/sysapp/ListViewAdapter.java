package com.mt.sysapp;
import java.util.ArrayList;
import java.util.HashMap;

import com.mt.androidtest.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
//    private Context mContext;
    ArrayList <HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    private LayoutInflater listContainer;
    private int mDensityDpi = 0;
    public ListViewAdapter(Context context) {
        listContainer = LayoutInflater.from(context);
        DisplayMetrics metric  = context.getResources().getDisplayMetrics();
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

    public void setupList(ArrayList<HashMap<String, Object>> list) {
    	mList.clear();
    	for(int i = 0;i<list.size();i++){
    		mList.add(list.get(i));
    	}
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
        	 view = listContainer.inflate(R.layout.app_item, parent,false);
        }else {
        	view = convertView;
        }
		ImageView image = (ImageView)view.findViewById(R.id.menu_img);
        TextView title = (TextView)view.findViewById(R.id.menu_label);
    	image.setImageDrawable((Drawable)mList.get(position).get("itemImage"));
        title.setText((String) mList.get(position).get("label"));
        setLayoutParams(image);
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
