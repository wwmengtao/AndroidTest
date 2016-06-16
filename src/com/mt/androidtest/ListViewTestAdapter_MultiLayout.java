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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder.viewHolder0 holder0 = null;
		ViewHolder.viewHolder1 holder1 = null;
		ViewHolder.viewHolder2 holder2 = null;
		ViewHolder.viewHolder3 holder3 = null;
		int viewType = getItemViewType(position);
		//��convertView����Ҫnew�������ؼ�
		if(convertView == null){ 
			//����ǰ�������ʽ��ȷ��new�Ĳ���
			ALog.Log("convertViewΪnull,type:"+viewType);
			switch(viewType){
				case TYPE_1:
					convertView = inflater.inflate(R.layout.item_getview_test_1, parent, false);
					holder1 = new ViewHolder.viewHolder1();
					holder1.textView = (TextView)convertView.findViewById(R.id.textview);
					holder0 = new ViewHolder.viewHolder0();
					holder0.checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);
					holder0.checkBox.setChecked(false);
					//һ��View���԰󶨶����ǩ
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
			switch(viewType){//��convertView������ʽ��ȡ�ò��õĲ���
				case TYPE_1:
					ALog.Log("convertView�ǿգ�TYPE_1");
					holder0 = (ViewHolder.viewHolder0) convertView.getTag(id.holder0);//getTagʡȥ��ִ��inflate��findViewById�Ļ���
					holder0.checkBox.setChecked(true);
					holder1 = (ViewHolder.viewHolder1) convertView.getTag(id.holder1);
					break;
				case TYPE_2:
					ALog.Log("convertView�ǿգ�TYPE_2");
					holder2 = (ViewHolder.viewHolder2) convertView.getTag();
					break;
				case TYPE_3:
					ALog.Log("convertView�ǿգ�TYPE_3");
					holder3 = (ViewHolder.viewHolder3) convertView.getTag();
					break;
			}
		}
		
		//������Դ
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