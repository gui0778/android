package com.tcy.database.config;

public class DBString {
	public final static String DBNAME="test.db";
	public final static int DBVERSION=1;
	public final static String TBNNAME_USER="user";
	public final static String CREATETB_USER="user";
	public final static String createtable_user="CREATE TABLE if not EXISTS user (" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, passwd VARCHAR,email VARCHAR)";


}
