package com.tcy.database.service;

import android.database.Cursor;

public interface DBservice {
	public Boolean CreateDB(String db);
	public Boolean excute(String sql);
	public Cursor query(String sql);

}
