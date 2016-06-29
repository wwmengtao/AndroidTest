package com.mt.androidtest.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.tool.XmlOperator;

import static com.mt.androidtest.data.DataBaseHelper.tableName;

public class ContentResolverDemoActivity extends BaseActivity {
	private String CONTENT_URI = "content://";
	private String cpAuthorities="com.mt.androidtest.cpdemo";
	private String [] mMethodNameFT={
			"readContentProviderFile",
			"createDatabase","updateDatabase","insert","update","query","delete"};
	private ContentResolver mContentResolver=null;
	private ArrayList<Uri>uriCPFile=null;
	private SQLiteOpenHelper mSqlOpenHelper;
	//
	private ArrayList<String>mAttrAL=null;
	private ArrayList<String>mTextAL=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resolver);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
		mContentResolver = getContentResolver();
		CONTENT_URI+=cpAuthorities;
		initUriCPFile();
		mSqlOpenHelper = DataBaseHelper.getInstance(getApplicationContext());
		toReadXml(getApplicationContext());
	}
	
	public void toReadXml(Context context){
		String fileName="locales/strings.xml";
		String tagOfDoc="resources";
		String eleName="string";
		String attr="name";
		XmlOperator mXmlOperator=new XmlOperator(context);
		mXmlOperator.setInfomation(fileName, tagOfDoc, eleName, attr);
		mXmlOperator.readFromXml(1);
		mAttrAL=mXmlOperator.getAttrArrayList();
		mTextAL=mXmlOperator.getTextArrayList();
		if(null==mAttrAL||null==mTextAL){
			throw new IllegalArgumentException("mAttrAL or mTextAL null!");
		}else if(mAttrAL.size()!=mTextAL.size()){
			throw new IllegalArgumentException("mAttrAL and mTextAL size not equal!");
		}
	}
	
	/**
	 * 以下将内部/外部存储中的共享文件对应的Uri加入uriCPFile中
	 */
	public void initUriCPFile(){
		uriCPFile=new ArrayList<Uri>();
		uriCPFile.add(Uri.parse(CONTENT_URI+"/myAssets_FilesDir/test/test.txt"));
		uriCPFile.add(Uri.parse(CONTENT_URI+"/test.txt"));
		uriCPFile.add(Uri.parse(CONTENT_URI+"/Documents/mt.txt"));
		uriCPFile.add(Uri.parse(CONTENT_URI+"/Download/mt.txt"));
		uriCPFile.add(Uri.parse(CONTENT_URI+"/mt.txt"));
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "readContentProviderFile":
				for(Uri mUri : uriCPFile){
					readContentProviderFile(mUri);
				}
				break;
			case "createDatabase":
				createDatabase();
				break;
			case "updateDatabase":
				updateDatabase();
				break;
			case "insert":
				insert();
				break;
			case "update":
				update();
				break;
			case "query":
				query();
				break;
			case "delete":
				delete();
				break;						
		}
	}
	
	/**
	 * readContentProviderFile：从ContentProvider标识的文件中读取内容
	 * @return
	 */
	private boolean readContentProviderFile(Uri mUri) {
		InputStream inputStream = null;
		BufferedReader reader = null;
		StringBuffer builder = null;
		String line = null;
		try {
			inputStream = mContentResolver.openInputStream(mUri);
			if (inputStream != null) {
		        reader = new BufferedReader(new InputStreamReader(inputStream));
		        builder = new StringBuffer();
	            while ((line = reader.readLine()) != null) {
	                builder.append(line);
	                builder.append("\n");
	            }
				ALog.Log("/---File content---/");
				ALog.Log(builder.toString());
				ALog.Log("/---File content---/");
			}
			return true;			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/*
	 * 避免在主线程中执行getWritableDatabase()或者getWritableDatabase()这两个耗时操作，可以在ContentProvider.onCreate()中开启。
    */
	public void createDatabase(){
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
	}
	
	public void updateDatabase(){
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
	}
	
	public void insert() {
		// 创建DatabaseHelper对象
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// 调用insert方法，就可以将数据插入到数据库当中
		// 第一个参数:表名称
		// 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
		// 第三个参数：ContentValues对象
		ContentValues values = null;
		for(int i=0;i<mAttrAL.size();i++){
			values = new ContentValues();
			values.put("id",mAttrAL.get(i));
			values.put("name", mTextAL.get(i));
			db.insert(tableName, "id", values);
		}
	}

	public void update() {
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// 创建一个ContentValues对象
		ContentValues values = new ContentValues();
		values.put("name", "mt");
		// 调用update方法
		// 第一个参数String：表名
		// 第二个参数ContentValues：ContentValues对象
		// 第三个参数String：where字句，相当于sql语句where后面的语句，？号是占位符
		// 第四个参数String[]：占位符的值
		ALog.Log("-----------update------------");
		db.update(tableName, values, "id=?", new String[] { "string_name1" });
	}

	public void query() {
		String id = null;
		String name = null;
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// 得到一个只读的SQLiteDatabase对象
		// 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象
		// 第一个参数String：表名
		// 第二个参数String[]:要查询的列名
		// 第三个参数String：查询条件
		// 第四个参数String[]：查询条件的参数
		// 第五个参数String:对查询的结果进行分组
		// 第六个参数String：对分组的结果进行限制
		// 第七个参数String：对查询的结果进行排序
		
		Cursor cursor = db.query(tableName, new String[] { "id","name" }, "id=?", new String[] { "string_name1" }, null, null, null);
		// 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
		ALog.Log("-------------query------------");
		while (cursor.moveToNext()) {
			id = cursor.getString(cursor.getColumnIndex("id"));
			name = cursor.getString(cursor.getColumnIndex("name"));
			ALog.Log("id: "+id+" name: "+name);
		}
		cursor.close();
	}

	public void delete() {
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		//调用SQLiteDatabase对象的delete方法进行删除操作
		//第一个参数String：表名
		//第二个参数String：条件语句
		//第三个参数String[]：条件值
		db.delete(tableName, "id=?", new String[]{"string_name1"});
		ALog.Log("----------delete----------");
	}
}
