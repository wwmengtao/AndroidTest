package com.mt.androidtest.showview.fragmentdemo;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

public class InfoContent{

	public static List<Info> ITEMS = new ArrayList<Info>();
	public static SparseArray<Info> ITEM_MAP 	= new SparseArray<Info>();
	public static Info mInfo=null;
	public static String[] fragmentSubclass={
			"DialogFragment",
			"ListFragment",
			"PreferenceFragment"
	};
	
	public static String[] fragmentSubclassDes={
			"A fragment that displays a dialog window, floating on top of its activity's window.  This fragment contains a Dialog object, which it displays as appropriate based on the fragment's state.",
			"A fragment that displays a list of items by binding to a data source such as an array or Cursor, and exposes event handlers when the user selects an item.",
			"Shows a hierarchy of {@link Preference} objects as lists."
	};	
	static{
		for(int i=0;i<fragmentSubclass.length;i++){
			mInfo = new Info(i+1,fragmentSubclass[i],fragmentSubclassDes[i]);
			ITEMS.add(mInfo);
			ITEM_MAP.put(mInfo.id, mInfo);
		}
	}
	
	public static class Info{

		public Integer id;
		public String title;
		public String desc;

		public Info(Integer id, String title, String desc){
			this.id = id;
			this.title = title;
			this.desc = desc;
		}

		@Override
		public String toString(){
			return title;
		}
	}
}
