package com.mt.androidtest;
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
    private DisplayMetrics metric=null;
    private int mMode=0;
    private Context mContext=null;
    public ListViewAdapter(Context context) {
        listContainer = LayoutInflater.from(context);
        metric  = context.getResources().getDisplayMetrics();
        mDensityDpi = metric.densityDpi;
        mContext = context;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            switch(mMode){
        	case 1:
        		view = listContainer.inflate(R.layout.item_getview, parent,false);
           	break;
            }
        }else {
        	view = convertView;
        }
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
