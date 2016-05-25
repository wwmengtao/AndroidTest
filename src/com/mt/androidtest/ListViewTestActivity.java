package com.mt.androidtest;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewTestActivity extends Activity implements RecyclerListener{
	ListView listView;
	ListViewTestAdapter_SingleLayout listAdapter_S;//����ListView��ʾ��itemֻ��һ��layout
	ListViewTestAdapter_MultiLayout listAdapter_M;//����ListView��ʾ��item�ж��ֲ�ͬ��layout
	ArrayList<String> listString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_test);
		listView = (ListView)this.findViewById(R.id.listview);
		listView.setRecyclerListener(this);
	}

	@Override
	public void onResume(){
		super.onResume();
		listAdapter_S = new ListViewTestAdapter_SingleLayout(this);
		listAdapter_M = new ListViewTestAdapter_MultiLayout(this);
		listView.setAdapter(listAdapter_M);
	}

    @Override
    public void onMovedToScrapHeap(View view) {
    	TextView mTextView = ((TextView)view.findViewById(R.id.textview));
    	mTextView.setTextColor(getResources().getColor(R.color.royalblue));    	
    }
}
