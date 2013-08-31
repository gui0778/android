package com.tcy.app.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

public class HttpGetMain {

	public HttpGetMain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public  String doGet(String url) 
	{
	        URL getUrl = null;
			try {
				getUrl = new URL(url);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
	        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
	        HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection) getUrl.openConnection();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        connection.setReadTimeout(20000);
	        connection.setDoOutput(true);
	        connection.setDoInput(true);
	        connection.setUseCaches(false);
	        connection.setConnectTimeout(20000);
	        //connection.setRequestProperty("Content-type", "application/json");
	        try {
				connection.setRequestMethod("GET");
			} catch (ProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try{
	        	connection.connect();
	        }
	        catch(IOException e)
	        {
	        	Log.v("HTTP",e.getMessage());
	        }
	        
	        try {
				DataOutputStream out  =   new  DataOutputStream(connection
				         .getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        // 取得输入流，并使用Reader读取
	        try {
				int code=connection.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//设置编码,否则中文乱码
	        StringBuffer sb=new StringBuffer();
	        String lines=null;
	        try {
				while ((lines = reader.readLine()) != null){
					lines = new String(lines.getBytes(), "utf-8");
				    sb.append(lines);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        // 断开连接
	        connection.disconnect();
	        return sb.toString();
		
	}
	public static void doPost() throws Exception
	{
	      //String getURL = "http://localhost:8047/Main/res/welcome.jsp";
	      String getURL = "http://localhost:8047/Main/auth/userinfo";
	        URL getUrl = new URL(getURL);
	        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
	        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
	        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
	        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	        connection.setDoOutput(true);
	        connection.setDoInput(true);
	        connection.setUseCaches(false);
	        connection.setInstanceFollowRedirects(true);
	        connection.setRequestMethod("POST");
	        connection.connect();

	          //  DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面 
	        // out.writeBytes(content); 
	        //String content = "firstname=" + URLEncoder.encode("一个大肥人", "utf-8");
	         OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");     
	         out.write("name=kevin");
	         out.flush();
	         out.close();  //  flush and close 
	        // 取得输入流，并使用Reader读取
	        InputStream in=connection.getInputStream();
	        InputStreamReader ins=new InputStreamReader(in, "UTF-8");
	        BufferedReader reader = new BufferedReader(ins);//设置编码,否则中文乱码
	        System.out.println("=============================");
	        System.out.println("Contents of get request");
	        System.out.println("=============================");
	        String lines;
	        while ((lines = reader.readLine()) != null){
	        	//lines = new String(lines.getBytes(), "utf-8");
	            System.out.println(lines);
	        }
	        reader.close();
	        // 断开连接
	        connection.disconnect();
		
	}
	public String httpget(String httpUrl)
	{
		 HttpGet httpRequest = new HttpGet(httpUrl);  
         //取得HttpClient对象  
            HttpClient httpclient = new DefaultHttpClient();  
            //请求HttpClient，取得HttpResponse  
            HttpResponse httpResponse = null;
			try {
				httpResponse = httpclient.execute(httpRequest);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
            //请求成功  
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)  
            {  
                //取得返回的字符串  
                String strResult = null;
				try {
					strResult = EntityUtils.toString(httpResponse.getEntity());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
                return strResult; 
            }  
            else 
            {  
            	return null;
            }  
        } 
	

}
