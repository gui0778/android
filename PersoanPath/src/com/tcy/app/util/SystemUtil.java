package com.tcy.app.util;

import android.content.Context;
import android.widget.Toast;

public class SystemUtil {

	public SystemUtil() {
		// TODO Auto-generated constructor stub
	}
	public static void showToast(Context context,String string)
	{
		Toast.makeText(context, string, Toast.LENGTH_LONG).show();
	}

}
