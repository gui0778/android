package com.tcy.database.dao;

import android.database.Cursor;

public interface DBdao{
	public Boolean CreateDB(String db);
	public Boolean excute(String sql);
	public void add(String sql);
	public void del(String sql);
	public Cursor query(String sql);
	public void createTabName(String tabname);
	public void execSQL(String sql, Object[] bindArgs);
}
