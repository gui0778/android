package com.tcy.app;

import java.util.logging.Logger;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MapView;
import com.tcy.app.ui.MapMainActivity;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.ViewManager;

public class AppContext extends Application implements MKGeneralListener {
	public static AppContext appContext;
	public static String baiduKey="703CF7BF668F7487C376DE29514570E1D1DC7A76";
	public BMapManager bMapManager;
	public  String TAG="AppContext";
	private static AppContext instance;
	public static final String auth_server="";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.v(TAG, "AppContext onCreate");
		bMapManager=new BMapManager(this);
		bMapManager.init(baiduKey, this);
		instance=this;
		
	}
	public static AppContext getAppContext() {
		return appContext;
	}
	public static void setAppContext(AppContext appContext) {
		AppContext.appContext = appContext;
	}
	public static String getBaiduKey() {
		return baiduKey;
	}
	public static void setBaiduKey(String baiduKey) {
		AppContext.baiduKey = baiduKey;
	}
	public AppContext(Context context) {
		// TODO Auto-generated constructor stub
	}
	public AppContext() {
		// TODO Auto-generated constructor stub
	}
	public static AppContext getInstance(){
		return instance;
	}
	public static AppContext getApp()
	{
		if(appContext==null)
		{
			appContext=new AppContext();
		}
		return appContext;
		
	}
	/***
	 * network error
	 */
	@Override
	public void onGetNetworkState(int arg0) {
		// TODO Auto-generated method stub
		Log.v(TAG, "network error--"+arg0);
		
	}
	/***
	 * key error
	 */
	
	@Override
	public void onGetPermissionState(int arg0) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Permission error--"+arg0);
		
	}
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try { 
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}

}
