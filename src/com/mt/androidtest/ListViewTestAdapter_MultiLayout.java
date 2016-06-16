package com.mt.androidtest;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mt.androidtest.R.id;

public class ListViewTestAdapter_MultiLayout extends BaseAdapter{
	Context mContext;
	LinearLayout linearLayout = null;
	LayoutInflater inflater;
	TextView tex;
	final int VIEW_TYPE = 3;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	final int TYPE_3 = 2;
	ArrayList<String> listString = new ArrayList<String>();
	public ListViewTestAdapter_MultiLayout(Context context) {
	// TODO Auto-generated constructor stub
		mContext = context.getApplicationContext();
		inflater = LayoutInflater.from(mContext);
		for(int i = 0 ; i < 100 ; i++){
			listString.add(Integer.toString(i)+"  ++++");
		}
	}

	@Override
	public int getCount() {//决定了ListView有多少个item
		// TODO Auto-generated method stub
		return listString.size();
	}

	//每个convert view都会调用此方法，获得当前所需要的view样式
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		//ALog.Log("getItemViewType position:"+position);
		int p = position%6;
		if(p == 0)
			return TYPE_1;
		else if(p < 3)
			return TYPE_2;
		return TYPE_3;
	}

	@Override
	public int getViewTypeCount() {//返回ListView显示的item的布局种类
		// TODO Auto-generated method stub
		ALog.Log("getViewTypeCount");
		return 3;
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
		// TODO Auto-generated method stub
		ViewHolder.viewHolder0 holder0 = null;
		ViewHolder.viewHolder1 holder1 = null;
		ViewHolder.viewHolder2 holder2 = null;
		ViewHolder.viewHolder3 holder3 = null;
		int viewType = getItemViewType(position);
		//无convertView，需要new出各个控件
		if(convertView == null){ 
			//按当前所需的样式，确定new的布局
			ALog.Log("convertView为null,type:"+viewType);
			switch(viewType){
				case TYPE_1:
					convertView = inflater.inflate(R.layout.item_getview_test_1, parent, false);
					holder1 = new ViewHolder.viewHolder1();
					holder1.textView = (TextView)convertView.findViewById(R.id.textview);
					holder0 = new ViewHolder.viewHolder0();
					holder0.checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);
					holder0.checkBox.setChecked(false);
					//一个View可以绑定多个标签
					convertView.setTag(id.holder0,holder0);
					convertView.setTag(id.holder1,holder1);
					break;
				case TYPE_2:
					convertView = inflater.inflate(R.layout.item_getview_test_2, parent, false);
					holder2 = new ViewHolder.viewHolder2();
					holder2.textView = (TextView)convertView.findViewById(R.id.textview);
					convertView.setTag(holder2);
					break;
				case TYPE_3:
					convertView = inflater.inflate(R.layout.item_getview_test_3, parent, false);
					holder3 = new ViewHolder.viewHolder3();
					holder3.textView = (TextView)convertView.findViewById(R.id.textview);
					holder3.imageView = (ImageView)convertView.findViewById(R.id.imageview);
					holder3.imageView.setBackgroundResource(R.drawable.icon);					
					convertView.setTag(holder3);
					break;
			}
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		}else{
			switch(viewType){//有convertView，按样式，取得不用的布局
				case TYPE_1:
					ALog.Log("convertView非空：TYPE_1");
					holder0 = (ViewHolder.viewHolder0) convertView.getTag(id.holder0);//getTag省去了执行inflate和findViewById的花销
					holder0.checkBox.setChecked(true);
					holder1 = (ViewHolder.viewHolder1) convertView.getTag(id.holder1);
					break;
				case TYPE_2:
					ALog.Log("convertView非空：TYPE_2");
					holder2 = (ViewHolder.viewHolder2) convertView.getTag();
					break;
				case TYPE_3:
					ALog.Log("convertView非空：TYPE_3");
					holder3 = (ViewHolder.viewHolder3) convertView.getTag();
					break;
			}
		}
		
		//设置资源
		switch(viewType){
			case TYPE_1:
				holder1.textView.setText(""+Integer.toString(position));
				break;
			case TYPE_2:
				holder2.textView.setText(""+Integer.toString(position));
				break;
			case TYPE_3:
				holder3.textView.setText(""+Integer.toString(position));
				break;
		}
		return convertView;
	}
}