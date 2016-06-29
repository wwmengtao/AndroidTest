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
	public void createDatabase(){
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
	}
	
	public void updateDatabase(){
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
	}
	
	public void insert() {
		// ����DatabaseHelper����
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// ����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��
		// ��һ������:������
		// �ڶ���������SQl������һ�����У����ContentValues�ǿյģ���ô��һ�б���ȷ��ָ��ΪNULLֵ
		// ������������ContentValues����
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
		// ����һ��ContentValues����
		ContentValues values = new ContentValues();
		values.put("name", "mt");
		// ����update����
		// ��һ������String������
		// �ڶ�������ContentValues��ContentValues����
		// ����������String��where�־䣬�൱��sql���where�������䣬������ռλ��
		// ���ĸ�����String[]��ռλ����ֵ
		ALog.Log("-----------update------------");
		db.update(tableName, values, "id=?", new String[] { "string_name1" });
	}

	public void query() {
		String id = null;
		String name = null;
		SQLiteDatabase db = mSqlOpenHelper.getWritableDatabase();
		// �õ�һ��ֻ����SQLiteDatabase����
		// ����SQLiteDatabase�����query�������в�ѯ������һ��Cursor���������ݿ��ѯ���صĽ��������
		// ��һ������String������
		// �ڶ�������String[]:Ҫ��ѯ������
		// ����������String����ѯ����
		// ���ĸ�����String[]����ѯ�����Ĳ���
		// ���������String:�Բ�ѯ�Ľ�����з���
		// ����������String���Է���Ľ����������
		// ���߸�����String���Բ�ѯ�Ľ����������
		
		Cursor cursor = db.query(tableName, new String[] { "id","name" }, "id=?", new String[] { "string_name1" }, null, null, null);
		// ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false
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
		//����SQLiteDatabase�����delete��������ɾ������
		//��һ������String������
		//�ڶ�������String���������
		//����������String[]������ֵ
		db.delete(tableName, "id=?", new String[]{"string_name1"});
		ALog.Log("----------delete----------");
	}
}
