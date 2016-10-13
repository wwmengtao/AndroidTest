package com.mt.androidtest.listview;

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

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;
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
	public int getCount() {//������ListView�ж��ٸ�item
		// TODO Auto-generated method stub
		return listString.size();
	}

	//ÿ��convert view������ô˷�������õ�ǰ����Ҫ��view��ʽ
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
	public int getViewTypeCount() {//����ListView��ʾ��item�Ĳ�������
		// TODO Auto-generated method stub
		ALog.Log("getViewTypeCount");
		return 3;
	}

	@Override
	public Object getItem(int arg0) {//�����Զ����ã���Ҫ�û��Լ�����
		// TODO Auto-generated method stub
		return listString.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	private ViewHolder mViewHolder = null;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		boolean needDoAdditionalWork = (null==convertView)?true:false;
		int viewType = getItemViewType(position);
		switch(viewType){
			case TYPE_1:
				mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview_test_1, position);
				break;
			case TYPE_2:
				mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview_test_2, position);
				break;
			case TYPE_3:
				mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview_test_3, position);			
				break;
		}
		if(needDoAdditionalWork)doAdditionalWork();
		if(!needDoAdditionalWork){
			CheckBox checkBox = mViewHolder.getView(R.id.checkbox);
			if(null!=checkBox)checkBox.setChecked(true);//��ӦTYPE_1
		}
		TextView textView = null;
		textView = mViewHolder.getView(R.id.textview);
		textView.setText(""+Integer.toString(position));
		return mViewHolder.getConvertView();
	}
	
	public void doAdditionalWork(){
		View mConvertView = mViewHolder.getConvertView();
		mConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		CheckBox checkBox = mViewHolder.getView(R.id.checkbox);
		if(null!=checkBox)checkBox.setChecked(false);//��ӦTYPE_1
		ImageView imageView = mViewHolder.getView(R.id.imageview);
		if(null!=imageView)imageView.setBackgroundResource(R.drawable.icon);	//��ӦTYPE_3
	}
}