package com.tcy.database.dao;

import java.io.IOException;
import java.security.MessageDigestSpi;

import com.tcy.database.config.DBString;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBdaoImpl extends SQLiteOpenHelper implements DBdao {
	private Context context;
	public static SQLiteDatabase db;
	public static DBdaoImpl dbdao;
	public DBdaoImpl(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DBString.DBNAME, factory, version);
		this.context=context;
		this.dbdao=this;
		// TODO Auto-generated constructor stub
	}
	public DBdaoImpl(Context context, String name) {
		super(context,DBString.DBNAME,null,DBString.DBVERSION);
		this.context=context;
		this.dbdao=this;
		// TODO Auto-generated constructor stub
	}

	public DBdaoImpl getDao()
	{
		if(dbdao==null)
		{
			dbdao=new DBdaoImpl(this.context,DBString.DBNAME,null,DBString.DBVERSION);
		}
		return dbdao;
	}
	public SQLiteDatabase getWriteable()
	{
		if(db==null)
		{
			db=dbdao.getWritableDatabase();
		}
		return db;
		
	}
	public Boolean CreateDB(String db) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean excute(String sql) {
		// TODO Auto-generated method stub
		getWriteable().execSQL(sql);
		return true;
	}

	public Cursor query(String sql) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void createTabName(String tabname) {
		// TODO Auto-generated method stub
			getWriteable().execSQL(tabname);
	}
	@Override
	public void add(String sql) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void del(String sql) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void execSQL(String sql, Object[] bindArgs) {
		// TODO Auto-generated method stub
		
	}

}
