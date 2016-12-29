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
	public int getCount() {//������ListView�ж��ٸ�item
		// TODO Auto-generated method stub
		return listString.size();
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
		boolean needDoAdditionalWork = (null==convertView)?true:false;
		mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview_test_3, position);
		if(needDoAdditionalWork)doAdditionalWork();
		TextView mTextView = mViewHolder.getView(R.id.textview);
		mTextView.setText(""+Integer.toString(position));
		/**
		 * ���ListView����android:choiceMode="singleChoice"����convertView��������ѡ��״̬�µ�������ɫ����ʱ
		 * ע��convertView��View.OnClickListener��������ʹ��ѡ��״̬��������ɫ�仯������ʧ
		 */
		return mViewHolder.getConvertView();
	}
	
	public void doAdditionalWork(){
		View mConvertView = mViewHolder.getConvertView();
		mConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		ImageView mImageView = mViewHolder.getView(R.id.imageview);
		mImageView.setBackgroundResource(R.drawable.icon);
		ALog.Log("doAdditionalWork");
	}
}
