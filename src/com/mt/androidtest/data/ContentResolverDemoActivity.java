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
import com.mt.androidtest.tool.XmlOperator;

public class ContentResolverDemoActivity extends BaseActivity {
	private boolean isLogRun = true;
	private String ContProvider_URI = "content://";
	private String [] mMethodNameFT={
			"readContentProviderFile",
			"insert","update","query","delete",
			"globalUriGrant"};
	private ContentResolver mContentResolver=null;
	private ArrayList<Uri>uriCPFile=null;
	//
	private ArrayList<String>mAttrAL=null;
	private ArrayList<String>mTextAL=null;
	//
	private Uri sqliteUri=null;
	private Uri grantUri=null;	
	private String sqlitekey=null;
	private String sqliteValue=null;
	//��д��Activity��Ҫ�����Ȩ��
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
		initUriCPFile();
		//
		initSqliteOperator();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}

	/**
	 * ���½��ڲ�/�ⲿ�洢�еĹ����ļ���Ӧ��Uri����uriCPFile��
	 */
	public void initUriCPFile(){
		uriCPFile=new ArrayList<Uri>();
		uriCPFile.add(Uri.parse(ContProvider_URI+"/myAssets_FilesDir/test/test.txt"));
		uriCPFile.add(Uri.parse(ContProvider_URI+"/test.txt"));
		uriCPFile.add(Uri.parse(ContProvider_URI+"/Documents/mt.txt"));
		uriCPFile.add(Uri.parse(ContProvider_URI+"/Download/mt.txt"));
		uriCPFile.add(Uri.parse(ContProvider_URI+"/mt.txt"));
	}
	
	/**
	 * ����Ϊд�����ݿ�������׼��
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
				for(Uri mUri : uriCPFile){
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
		}
	}
	
	/**
	 * readContentProviderFile����ContentProvider��ʶ���ļ��ж�ȡ����
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
	 * ���������߳���ִ��getWritableDatabase()����getWritableDatabase()��������ʱ������������ContentProvider.onCreate()�п�����
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
		//
		mContentResolver.insert(grantUri, null);
		
	}

	public void update() {
		if(isLogRun)ALog.Log2("CRDemoActivity_update");
		// ����һ��ContentValues����
		ContentValues values = new ContentValues();
		values.put(sqliteValue, "mt");
		mContentResolver.update(sqliteUri, values, sqlitekey+" = ?", new String[]{"string_name5"});
		//
		mContentResolver.update(grantUri, null, null, null);
	}

	public void query() {
		if(isLogRun)ALog.Log2("CRDemoActivity_query");
		String id,name=null;
		Cursor cursor = mContentResolver.query(sqliteUri, null, null, null, null);
		// ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false
		while (cursor.moveToNext()) {
			id = cursor.getString(cursor.getColumnIndex(sqlitekey));
			name = cursor.getString(cursor.getColumnIndex(sqliteValue));
			ALog.Log("sqlitekey: "+id+" sqliteValue: "+name);
		}
		cursor.close();
		//
		mContentResolver.query(grantUri, null, null, null, null);
	}

	public void delete() {
		if(isLogRun)ALog.Log2("CRDemoActivity_delete");
		//����SQLiteDatabase�����delete��������ɾ������
		//��һ������String������
		//�ڶ�������String���������
		//����������String[]������ֵ
		mContentResolver.delete(sqliteUri	, sqlitekey+" = ?", new String[]{"string_name4"});
		//
		mContentResolver.delete(grantUri, null, null);
	}
	
	/**
	����ΪĳUri������ʱȨ�ޣ�ǰ����AndroidManifest.xml����<grant-uri-permission android:pathPrefix="/grant" />
	����뿪������Uri����ʱȨ�ޣ�����/sqlite����ô����AndroidManifest.xml�м���grant-uri-permission����ɾ������
	grant-uri-permission����android:grantUriPermissions����Ϊ"true"��
	*/
	public void globalUriGrant(){
		Intent intent = new Intent("com.mt.androidtest2.data.ContentResolverDemoActivity"); 
		intent.setClassName("com.mt.androidtest2", "com.mt.androidtest2.data.ContentResolverDemoActivity");
		intent.setData(Uri.parse("content://com.mt.androidtest.cpdemo/grant"));
		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		startActivity(intent); 
	}
}
