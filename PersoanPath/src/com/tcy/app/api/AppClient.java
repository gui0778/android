package com.tcy.app.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.tcy.app.AppContext;

import android.os.AsyncTask;
import android.util.Log;



public class AppClient {
	public static final String TAG=AppClient.class.getSimpleName();
	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";
	private static final int TIMEOUT_CONNECTION = 20000;
	private static final int TIMEOUT_SOCKET = 20000;
	protected static final int RETRY_TIME = 3;

	private ArrayList<NameValuePair> headers;
	private String server = "";

	private static String appToken;
	private static String acces_token="";

	public static String getAcces_token() {
		return acces_token;
	}

	public static void setAcces_token(String acces_token) {
		AppClient.acces_token = acces_token;
	}
	private String appUserAgent;

	public AppClient(String server) {
		this();
		this.server = server;
		
	}

	public AppClient() {
		
		headers = new ArrayList<NameValuePair>();
		
		StringBuilder ua = new StringBuilder();
		ua.append('/'
				+ AppContext.getInstance().getPackageInfo().versionName
				+ '_'
				+ AppContext.getInstance().getPackageInfo().versionCode);// App鐗堟湰
		ua.append("/Android");// 鎵嬫満绯荤粺骞冲彴
		ua.append("/" + android.os.Build.VERSION.RELEASE);// 鎵嬫満绯荤粺鐗堟湰
		ua.append("/" + android.os.Build.MODEL); // 鎵嬫満鍨嬪彿
		//ua.append("/" + AppContext.getInstance().getAppId());// 瀹㈡埛绔敮涓�爣璇�
		appUserAgent = ua.toString();
		
		
		
		headers.add(new BasicNameValuePair("Connection", "Keep-Alive"));
		headers.add(new BasicNameValuePair("User-Agent", appUserAgent));
		
		headers.add(new BasicNameValuePair("Accept", "application/json,text/html,application/xhtml+xml,application/xml"));
		headers.add(new BasicNameValuePair("Accept-Charset", "UTF-8,*;q=0.5"));
		//headers.add(new BasicNameValuePair("Accept-Encoding", "gzip,deflate,sdch"));
		headers.add(new BasicNameValuePair("Accept-Language", "zh-CN,en-US;q=0.8,zh;q=0.6"));
		
	}

	public String getAppToken() {
		return appToken;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	public void addHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
	public List<NameValuePair> fromMap(Map<String,Object> params){
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for(Map.Entry<String, Object> en: params.entrySet()){
			pairs.add(new BasicNameValuePair(en.getKey(), en.getValue().toString()));
		}
		return pairs;
	}
	private URI buildURI(String url){
		String eurl = getServer() + url;
		if(appToken != null){
			if(eurl.contains("?")){
				eurl += "&access_token="+appToken;
			} else {
				eurl += "?access_token="+appToken;
			}
		}
		try {
			URI uri = new URI(eurl);
			return uri;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private HttpResponse send(HttpUriRequest request) {

		HttpClient client = getHttpClient();
	
		for (NameValuePair h : headers) {
			request.addHeader(h.getName(), h.getValue());
			
		}
		Log.d(TAG, request.getURI().toString());
		//request.addHeader("Host", server.toString());
		try {
			HttpResponse httpResponse;
			
			httpResponse = client.execute(request);
			Log.d(TAG, "status:"+httpResponse.getStatusLine().getStatusCode());
			return httpResponse;
		

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}
	
	public HttpResponse get(URI url) {
		
		HttpGet request = new HttpGet(url);
		return send(request);
		
		
	}
	public HttpResponse get(String url) {
		
		HttpGet request = new HttpGet(buildURI(url));
		return send(request);
		
		
	}
	public void get(String url, RestListener onReponse) {
		URI uri = buildURI(url);
		Log.d(TAG, uri.toASCIIString());
		HttpGet request = new HttpGet(uri);
		buildAndSend(request, onReponse);
		
	}
	public HttpResponse post(String url, String content) {

		

		try {
			
			HttpPost request = new HttpPost(buildURI(url));
			
			StringEntity se = new StringEntity(content,"UTF-8");
			request.setEntity(se);
			return send(request);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}


	}

	public void post(String url, String content,
			RestListener onReponse) {

		

		try {
			
			HttpPost request = new HttpPost(buildURI(url));
			request.setEntity(new StringEntity(content,"UTF8"));
			request.setHeader("Content-type", "application/json");
			buildAndSend(request, onReponse);

		} catch (UnsupportedEncodingException e) {
			onReponse.onError(501, e.getLocalizedMessage());
		}
	
		
	}
	
	public void post(String url, HttpEntity entity,
			RestListener onReponse) {

		

		HttpPost request = new HttpPost(buildURI(url));
		request.setEntity(entity);

		buildAndSend(request, onReponse);
	
		
	}
	public HttpResponse put(String url, String content) {

		

		try {
			
			HttpPut request = new HttpPut(buildURI(url));
			request.setEntity(new StringEntity(content,"UTF-8"));
			return send(request);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		
	}

	public void put(String url, String content,
			RestListener onReponse) {
		try {
			
			HttpPut request = new HttpPut(buildURI(url));
			request.setEntity(new StringEntity(content,"UTF-8"));
			buildAndSend(request, onReponse);
		} catch (UnsupportedEncodingException e) {
			onReponse.onError(501, e.getLocalizedMessage());
		}

	
	}
	
	public HttpResponse delete(String url) {
		
		HttpDelete request = new HttpDelete(buildURI(url));

		return send(request);
		
	}
	
	public void delete(String url, 	RestListener onReponse) {

		HttpDelete request = new HttpDelete(getServer() + url);

		buildAndSend(request, onReponse);
	}

	private void buildAndSend(HttpUriRequest request, RestListener onReponse) {

		// request.setHeader("Authorization" , getKey());

		for (NameValuePair h : headers) {
			request.addHeader(h.getName(), h.getValue());
			
		}
		request.addHeader("Host", server.toString());
		RestRequest restRequest = new RestRequest();
		restRequest.setOnReponse(onReponse);
		restRequest.setRequest(request);
		restRequest.execute();

	}
	
	
	private class RestRequest extends AsyncTask<String, Integer, String> {

		private HttpUriRequest request;
		private RestListener onReponse;
		private Integer errorCode;
		private String errorMessage;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (this.onReponse != null) {
				this.onReponse.onBeforeRequest(request.getURI());
			}
		}

		public HttpUriRequest getRequest() {
			return request;
		}

		public void setRequest(HttpUriRequest request) {
			this.request = request;
		}

		public RestListener getOnReponse() {
			return onReponse;
		}

		public void setOnReponse(RestListener onReponse) {
			this.onReponse = onReponse;
		}

		public Integer getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(Integer errorCode) {
			this.errorCode = errorCode;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		@Override
		protected String doInBackground(String... params) {

			HttpClient client = getHttpClient();
			HttpResponse httpResponse;
			String response = null;

			try {

				httpResponse = client.execute(getRequest());

				setErrorCode(httpResponse.getStatusLine().getStatusCode());
				setErrorMessage(httpResponse.getStatusLine().getReasonPhrase());

				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					response = EntityUtils.toString(entity,"utf-8");
				}

			} catch (Exception e) {

				setErrorCode(e.hashCode());
				setErrorMessage(e.getLocalizedMessage());

			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			if (getOnReponse() != null) {
				if (result != null && errorCode == HttpStatus.SC_OK) {
					getOnReponse().onResponse(result);
				} else {
					getOnReponse().onError(getErrorCode(), getErrorMessage());
				}
			}
		}

	}

	
	protected HttpClient getHttpClient() {

		HttpParams httpParameters = new BasicHttpParams();

		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = TIMEOUT_CONNECTION;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = TIMEOUT_SOCKET;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		// 璁剧疆 瀛楃闆�
		httpClient.getParams().setParameter(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
		return httpClient;
	}
	public JSONObject login(String name,String pass)
	{
		String query = "/open_api.php?grant_type=%s&client_id=%s&client_secret=%s&scope=%s&username=%s&password=%s";
		query = String.format(query,"login","phone","api407","path", name, pass);
    	
		HttpResponse res = get(query);
    	if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
    		HttpEntity entity = res.getEntity();
            if (entity != null) {
            	String sres;
				try {
					sres = EntityUtils.toString(entity, "utf-8");
					Log.d(TAG, sres);
					return new JSONObject(sres);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }	            
    		
    	}
    	return null;
	}

	
}