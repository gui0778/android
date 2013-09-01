package com.tcy.database.util;

public class StringUtil {
	public static String CoverFirstWordUp(String string)
	{
		String str =null;
		if(string!=null&&!"".equals(string))
		{
			str  = string.substring(0,1).toUpperCase()+string.substring(1);
		
		}
		return str;
	}

}
