package com.mt.androidtest.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mt.androidtest.ALog;

/**
 * SQLiteOpenHelper��һ�������࣬�����������ݿ�Ĵ����Ͱ汾�����ṩ��������Ĺ���
 * ��һ��getReadableDatabase()��getWritableDatabase()���Ի��SQLiteDatabase����ͨ���ö�����Զ����ݿ���в���
 * �ڶ����ṩ��onCreate()��onUpgrade()�����ص����������������ٴ������������ݿ�ʱ�������Լ��Ĳ���
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "dbHelper.db";	
	private static DataBaseHelper sInstance = null;
	private static final String tableName="M_T";
	private static final String keyName="Id";
	private static final String valueName="Value";
    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	static synchronized DataBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHelper(context);
        }
        return sInstance;
    }
	
	public static String getTableName(){
		return tableName;
	}
	
	public static String getKeyName(){
		return keyName;
	}
	
	public static String getValueName(){
		return valueName;
	}
	
	//�ú������ڵ�һ�δ�����ʱ��ִ�У�ʵ�����ǵ�һ�εõ�SQLiteDatabase�����ʱ��Ż�����������
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		ALog.Log("create a database");
		//execSQL����ִ��SQL���
		db.execSQL("create table "+tableName+"("+keyName+" TEXT PRIMARY KEY,"+valueName+" TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		ALog.Log("upgrade a database");
        try {
            db.execSQL("drop table if exists "+tableName);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
