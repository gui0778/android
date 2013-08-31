package com.tcy.database.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tcy.database.config.DBString;
import com.tcy.database.dao.DBdao;
import com.tcy.database.dao.DBdaoImpl;
import com.tcy.database.entity.User;

public class UserService extends DBdaoImpl {

	public UserService(Context context, String name) {
		super(context, name);
		super.createTabName(DBString.createtable_user);
		// TODO Auto-generated constructor stub
	}
	public void add(User user)
	{
		getWriteable().insert(DBString.TBNNAME_USER, null, CoverUser(user));
	}
	public ContentValues CoverUser(User user)
	{
		ContentValues cv=new ContentValues();
		cv.put("name", user.getName());
		cv.put("passwd", user.getPasswd());
		cv.put("email", user.getEmail());
		return cv;
	}
	public Cursor getUser(User user)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("select * from user ");
		if(user!=null)
		{
			if(user.get_id()!=0)
			{
				sb.append("where _id="+user.get_id());
				
			}
			if(user.getName()!=null)
			{
				if(sb.indexOf("where")!=-1)
				{
					sb.append(" and name='"+user.getName()+"'");
				}
				else
				{
					sb.append(" where name='"+user.getName()+"'");
				}
				
			}
			
		}
		Cursor cv=getWriteable().rawQuery(sb.toString(),null);

		return cv;
	}

}
