package com.tcy.database.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DBHelper {
	public static String getWhereForObject(Object object)
	{
		StringBuffer sb=new StringBuffer();
		Class<?> cl=object.getClass();
		Field[] fields=cl.getFields();
		for(Field f:fields)
		{
			String fieldName=f.getName();
			Class<?> type=f.getType();
			try {
				Method method=cl.getMethod("get"+StringUtil.CoverFirstWordUp(fieldName));
				try {
					Object object2=method.invoke(object);
					if(object2!=null)
					{
						sb.append("and "+fieldName+"='"+object2+"' ");
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		sb=sb.replace(0, 4, "");
		return sb.toString();
	}

}
