package com.mt.androidtest.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.tool.DataBaseHelper;
import com.mt.androidtest.tool.XmlOperator;

public class ContentResolverDemoActivity extends BaseActivity {
	private boolean isLogRun = true;
	private String ContProvider_URI = "content://";
	private String [] mMethodNameFT={
			"readContentProviderFile",
			"insert","update","query","delete",
			"globalUriGrant","globalUriGrant2","getType"};
	private ContentResolver mContentResolver=null;
	private ArrayList<Uri>uriCPFiles=null;
	//
	private ArrayList<String>mAttrAL=null;
	private ArrayList<String>mTextAL=null;
	//
	private Uri sqliteUri=null;
	private Uri grantUri=null;	
	private String sqlitekey=null;
	private String sqliteValue=null;
	//列写该Activity需要申请的权限
    private final String []permissionsRequired = new String[]{
    	Manifest.permission.READ_EXTERNAL_STORAGE,
    	Manifest.permission.WRITE_EXTERNAL_STORAGE,
    	Manifest.permission.READ_CALENDAR,
		Manifest.permission.WRITE_CALENDAR,
		Manifest.permission.READ_CONTACTS,
		Manifest.permission.WRITE_CONTACTS,
    };    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		permissionsRequiredBase = permissionsRequired;
		ALog.Log("CRDA_onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
		//
		ContProvider_URI += ContentProviderDemo.authority;
		mContentResolver = getContentResolver();
		inituriCPFiles();
		//
		initSqliteOperator();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}

	/**
	 * 以下将内部/外部存储中的共享文件对应的Uri加入uriCPFiles中
	 */
	public void inituriCPFiles(){
		uriCPFiles = new ArrayList<Uri>();
		uriCPFiles.add(Uri.parse(ContProvider_URI+"/myAssets_FilesDir/test/test.txt"));
		uriCPFiles.add(Uri.parse(ContProvider_URI+"/test.txt"));
		uriCPFiles.add(Uri.parse(ContProvider_URI+"/Documents/mt.txt"));
		uriCPFiles.add(Uri.parse(ContProvider_URI+"/Download/mt.txt"));
		uriCPFiles.add(Uri.parse(ContProvider_URI+"/mt.txt"));
	}
	
	/**
	 * 以下为写入数据库做数据准备
	 */
	public void initSqliteOperator(){
		readXmlForSqlite(getApplicationContext());
		sqliteUri=Uri.parse(ContProvider_URI+ContentProviderDemo.SqliteURI_sqlite);
		sqlitekey=DataBaseHelper.getKeyName();
		sqliteValue=DataBaseHelper.getValueName();
		//
		grantUri=Uri.parse(ContProvider_URI+ContentProviderDemo.GrantURI_grant);
	}
	
	public void readXmlForSqlite(Context context){
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
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "readContentProviderFile":
				for(Uri mUri : uriCPFiles){
					readContentProviderFile(mUri);
				}
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
			case "globalUriGrant":
				globalUriGrant();
				break;
			case "globalUriGrant2":
				globalUriGrant2();
				break;					
			case "getType":
				getType();
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
			inputStream = mContentResolver.openInputStream(mUri);//会调用ContentProviderDemo的openFile函数
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
	public void insert() {
		if(isLogRun)ALog.Log2("CRDemoActivity_insert");
		ContentValues values = null;
		for(int i=0;i<mAttrAL.size();i++){
			values = new ContentValues();
			values.put(sqlitekey,mAttrAL.get(i));
			values.put(sqliteValue, mTextAL.get(i));
			mContentResolver.insert(sqliteUri, values);
		}
		//测试UriMatcher用语句，附加功能测试
		mContentResolver.insert(grantUri, null);
		
	}

	public void update() {
		if(isLogRun)ALog.Log2("CRDemoActivity_update");
		// 创建一个ContentValues对象
		ContentValues values = new ContentValues();
		values.put(sqliteValue, "mt");
		mContentResolver.update(sqliteUri, values, sqlitekey+" = ?", new String[]{"string_name5"});
		//测试UriMatcher用语句，附加功能测试
		mContentResolver.update(grantUri, null, null, null);
	}

	public void query() {
		if(isLogRun)ALog.Log2("CRDemoActivity_query");
		String id,name=null;
		Cursor cursor = mContentResolver.query(sqliteUri, null, null, null, null);
		// 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
		try{
			while (cursor.moveToNext()) {
				id = cursor.getString(cursor.getColumnIndex(sqlitekey));
				name = cursor.getString(cursor.getColumnIndex(sqliteValue));
				ALog.Log("sqlitekey: "+id+" sqliteValue: "+name);
			}
		}finally{
			cursor.close();
		}
		//测试UriMatcher用语句，附加功能测试
		mContentResolver.query(grantUri, null, null, null, null);
	}

	public void delete() {
		if(isLogRun)ALog.Log2("CRDemoActivity_delete");
		//调用SQLiteDatabase对象的delete方法进行删除操作
		//第一个参数String：表名
		//第二个参数String：条件语句
		//第三个参数String[]：条件值
		mContentResolver.delete(sqliteUri	, sqlitekey+" = ?", new String[]{"string_name4"});
		//测试UriMatcher用语句，附加功能测试
		mContentResolver.delete(grantUri, null, null);
	}
	
	/**
	 * 此时会导致ContentProviderDemo.getType函数的调用，工作原理请对照ContentProviderDemo.getType处的说明
	 */
	public void getType(){
		Uri mUri=Uri.parse("content://com.mt.androidtest.cpdemo/sqlite");
		Intent mIntent=new Intent("com.mt.androidtest.ContentResolver",mUri);
		mIntent.addCategory(Intent.CATEGORY_DEFAULT);
		startActivity(mIntent);
	}
	
	/**
	以下为某Uri开启临时权限，前提是AndroidManifest.xml中有<grant-uri-permission android:pathPrefix="/grant" />
	如果想开启其他Uri的临时权限，比如/sqlite，那么需在AndroidManifest.xml中加上grant-uri-permission或者删除所有
	grant-uri-permission，将android:grantUriPermissions设置为"true"。
	*/
	public void globalUriGrant(){
		Intent intent = new Intent(); 
		intent.setClassName("com.mt.androidtest2", "com.mt.androidtest2.data.ContentResolverDemoActivity");
		intent.setData(Uri.parse("content://com.mt.androidtest.cpdemo/grant"));
		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		startActivity(intent);
	}
	
	public void globalUriGrant2(){
		Intent intent = new Intent(); 
		intent.setClassName("com.mt.androidtest2", "com.mt.androidtest2.data.ContentResolverDemoActivity");
		grantUriPermission("com.mt.androidtest2", Uri.parse("content://com.mt.androidtest.cpdemo/grant"), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);		
		startActivity(intent);
	}

}
