package com.mt.androidtest.listview;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public class ListViewTestAdapter_SingleLayout  extends BaseAdapter{
	Context mContext;
	LayoutInflater inflater;
	ArrayList<String> listString = new ArrayList<String>();
	public ListViewTestAdapter_SingleLayout(Context context) {
	// TODO Auto-generated constructor stub
		mContext = context.getApplicationContext();
		inflater = LayoutInflater.from(mContext);
		for(int i = 0 ; i < 20 ; i++){
			listString.add(Integer.toString(i)+"  ++++");
		}
	}
	
	@Override
	public int getCount() {//决定了ListView有多少个item
		// TODO Auto-generated method stub
		return listString.size();
	}
	
	@Override
	public Object getItem(int arg0) {//不会自动调用，需要用户自己调用
		// TODO Auto-generated method stub
		return listString.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder.viewHolder3 holder3;
		if(convertView == null){ 
			convertView = inflater.inflate(R.layout.item_getview_test_3, parent, false);
			holder3 = new ViewHolder.viewHolder3();
			holder3.textView = (TextView)convertView.findViewById(R.id.textview);
			holder3.imageView = (ImageView)convertView.findViewById(R.id.imageview);
			holder3.imageView.setBackgroundResource(R.drawable.icon);
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			convertView.setTag(holder3);
		}else{
			holder3=(ViewHolder.viewHolder3)convertView.getTag();
		}
		holder3.textView.setText(""+Integer.toString(position));
		return convertView;
	}
}
