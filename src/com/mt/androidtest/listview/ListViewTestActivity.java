package com.mt.androidtest.listview;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ListView;
import android.widget.TextView;
import com.mt.androidtest.R;

public class ListViewTestActivity extends Activity implements RecyclerListener{
	ListView listView;
	ListViewTestAdapter_SingleLayout listAdapter_S;//表明ListView显示的item只有一种layout
	ListViewTestAdapter_MultiLayout listAdapter_M;//表明ListView显示的item有多种不同的layout
	ArrayList<String> listString;
	final int Menu_Single=0x00;
	final int Menu_Multi=0x01;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_test);
		listView = (ListView)this.findViewById(R.id.listview);
		listView.setRecyclerListener(this);
		listAdapter_S = new ListViewTestAdapter_SingleLayout(this);
		listAdapter_M = new ListViewTestAdapter_MultiLayout(this);
	}

	@Override
	public void onResume(){
		super.onResume();
		listView.setAdapter(listAdapter_S);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// -------------向menu中添加字体大小的子菜单-------------
		super.onCreateOptionsMenu(menu);
		addMenu(menu);
		return true;
	}
	
	public void addMenu(Menu menu){
		menu.add(0, Menu_Single, 0, "SingleLayout");
		menu.add(0, Menu_Multi, 0, "MultiLayout");
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)
	{
		switch (mi.getItemId()){
		case Menu_Single:
			listView.setAdapter(listAdapter_S);
			break;
		case Menu_Multi:
			listView.setAdapter(listAdapter_M);
			break;			
		}
		return true;
	}
	
    @Override
    public void onMovedToScrapHeap(View view) {
    	TextView mTextView = ((TextView)view.findViewById(R.id.textview));
    	mTextView.setTextColor(getResources().getColor(R.color.royalblue));    	
    }
}
