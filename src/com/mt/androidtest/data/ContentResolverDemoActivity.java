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

public class ContentResolverDemoActivity extends BaseActivity {
	private String CONTENT_URI = "content://";
	private String cpAuthorities="com.mt.androidtest.cpdemo";
	private String [] mMethodNameFT={
			"readContentProviderFile",
			"insert","update","query","delete"};
	private ContentResolver mContentResolver=null;
	private ArrayList<Uri>uriCPFile=null;
	//
	private ArrayList<String>mAttrAL=null;
	private ArrayList<String>mTextAL=null;
	//
	private Uri mUri=null;
	private String sqlitekey=null;
	private String sqliteValue=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resolver);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
		mContentResolver = getContentResolver();
		CONTENT_URI+=cpAuthorities;
		mUri=Uri.parse(CONTENT_URI);
		initUriCPFile();
		toReadXml(getApplicationContext());
		sqlitekey=DataBaseHelper.getKeyName();
		sqliteValue=DataBaseHelper.getValueName();
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
	 * ���½��ڲ�/�ⲿ�洢�еĹ����ļ���Ӧ��Uri����uriCPFile��
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
		ContentValues values = null;
		for(int i=0;i<mAttrAL.size();i++){
			values = new ContentValues();
			values.put(sqlitekey,mAttrAL.get(i));
			values.put(sqliteValue, mTextAL.get(i));
			mContentResolver.insert(mUri, values);
		}
	}

	public void update() {
		// ����һ��ContentValues����
		ContentValues values = new ContentValues();
		values.put(sqliteValue, "mt");
		mContentResolver.update(mUri, values, sqlitekey+" = ?", new String[]{"string_name5"});
	}

	public void query() {
		String id,name=null;
		Cursor cursor = mContentResolver.query(mUri, null, null, null, null);
		// ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false
		while (cursor.moveToNext()) {
			id = cursor.getString(cursor.getColumnIndex(sqlitekey));
			name = cursor.getString(cursor.getColumnIndex(sqliteValue));
			ALog.Log("sqlitekey: "+id+" sqliteValue: "+name);
		}
		cursor.close();
	}

	public void delete() {
		//����SQLiteDatabase�����delete��������ɾ������
		//��һ������String������
		//�ڶ�������String���������
		//����������String[]������ֵ
		mContentResolver.delete(mUri	, sqlitekey+" = ?", new String[]{"string_name4"});
	}
}
