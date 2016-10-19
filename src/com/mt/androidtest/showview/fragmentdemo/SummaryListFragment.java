package com.mt.androidtest.showview.fragmentdemo;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SummaryListFragment extends ListFragment
{
	private Callback mCallbacks;

	public interface Callback{
		public void onItemSelected(Integer id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Ϊ��ListFragment����Adapter
		setListAdapter(new ArrayAdapter<InfoContent.Info>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, InfoContent.ITEMS));
	}

	@Override
	public void onAttach(Activity activity)	{
		super.onAttach(activity);
		// ���Activityû��ʵ��Callbacks�ӿڣ��׳��쳣
		if (!(activity instanceof Callback)){
			throw new IllegalStateException(
				"BookListFragment���ڵ�Activity����ʵ��Callbacks�ӿ�!");
		}
		mCallbacks = (Callback)activity;
	}

	@Override
	public void onDetach(){
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onListItemClick(ListView listView	, View view, int position, long id){
		super.onListItemClick(listView, view, position, id);
		mCallbacks.onItemSelected(InfoContent.ITEMS.get(position).id);
	}
}