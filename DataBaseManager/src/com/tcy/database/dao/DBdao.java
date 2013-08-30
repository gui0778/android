package com.tcy.database.dao;

import android.database.Cursor;

public interface DBdao {
	public Boolean CreateDB(String db);
	public Boolean excute(String sql);
	public Cursor query(String sql);
}
