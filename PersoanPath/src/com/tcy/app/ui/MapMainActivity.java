package com.tcy.app.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.tcy.app.AppContext;
import com.tcy.app.R;
import com.tcy.app.api.AppClient;
import com.tcy.app.api.HttpGetMain;
import com.tcy.app.socket.client.SocketClient;
import com.tcy.app.socket.client.SocketClientHandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MapMainActivity extends Activity {

	private MapView mapview;
	private MapController mapController;
	public BMapManager bMapManager;
	private BDLocationListener bdlocationlistener = new MyBDlocationListener();
	private LocationClient locationClient;
	private ImageButton send_pos_btn, send_socket_btn, send_netty_btn;
	private GeoPoint lastestgeop;
	public String TAG = "MapMainActivity";
	private String getURL = "http://1.tancyu407.sinaapp.com/pinphp/index.php?name=tcyoww";
	// private String getURL="http://www.baidu.com";
	private HttpGetMain httpget;
	private Handler handler;
	private TextView show_tv;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_main);
		mapview = (MapView) findViewById(R.id.mapview_main);
		mapController = mapview.getController();
		mapview.setBuiltInZoomControls(true);
		mapController.setZoom(16);
		mapController.enableClick(true);
		locationClient = new LocationClient(this);
		locationClient.registerLocationListener(bdlocationlistener);
		setPer();
		locationClient.start();
		mapview.refresh();
		initView();
		httpget = new HttpGetMain();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyLongPress(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	class MyBDlocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			if (arg0 == null) {
				return;
			}
			String string = arg0.getAddrStr();
			GeoPoint gpoint = new GeoPoint((int) (arg0.getLatitude() * 1e6),
					(int) (arg0.getLongitude() * 1e6));
			lastestgeop = gpoint;
			mapController.animateTo(gpoint);

		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void setPer() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(3000);// 设置发起定位请求的间隔时间为1000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1); // poi查询距离
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		option.setPriority(LocationClientOption.GpsFirst);
		;
		locationClient.setLocOption(option);
	}

	public void sendMylocation(GeoPoint p) {
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				// super.handleMessage(msg);
				if (msg.what == 1) {
					String str = (String) msg.obj;
					// Toast.makeText(MapMainActivity.this, str,
					// Toast.LENGTH_LONG).show();
					show_tv.setText(str);
				}
			}

		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String res = httpget.doGet(getURL);
				Message msg = new Message();
				msg.what = 1;
				msg.obj = res;
				handler.sendMessage(msg);

			}

		}).start();
	}

	public void initView() {
		send_pos_btn = (ImageButton) findViewById(R.id.send_http);
		send_pos_btn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// if(lastestgeop!=null)
				// {
				sendMylocation(lastestgeop);

				// }

			}
		});
		send_netty_btn = (ImageButton) findViewById(R.id.send_netty);
		send_netty_btn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// if(lastestgeop!=null)
				// {
				// sendMylocation(lastestgeop);

				// }

			}
		});
		send_socket_btn = (ImageButton) findViewById(R.id.send_socket);
		send_socket_btn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// if(lastestgeop!=null)
				// {
				// sendMylocation(lastestgeop);

				// }

				// TODO Auto-generated method stub
				if(SocketClientHandler.socket==null||SocketClientHandler.socket.isClosed())
				{
					new Thread(new Runnable()
					{

						@Override
						public void run() {
							// TODO Auto-generated method stub
							new SocketClient("192.168.1.229", 8090);
							
						}
						
					}).start();
					
				}
				String data = SocketClientHandler.sendMessage("hello");
				show_tv.setText(data);

			}
		});
		show_tv = (TextView) findViewById(R.id.show_textview);

	}

}
