package com.tcy.app.ui;

import javax.xml.ws.Response;

import org.json.JSONObject;

import com.baidu.platform.comapi.map.l;
import com.tcy.app.AppContext;
import com.tcy.app.MainActivity;
import com.tcy.app.R;
import com.tcy.app.api.AppClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener {
	private Button login_btn;
	private EditText username,userpasswd;
	private Handler loginhandler;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.tcy.app.R.layout.login);
	}
	public void initView()
	{
		login_btn=(Button)findViewById(R.id.login_btn);		
		login_btn.setOnClickListener(this);
		loginhandler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				//super.handleMessage(msg);
				switch(msg.what)
				{
				case 1:
					JSONObject json=(JSONObject)msg.obj;
					String token=json.optString("access_token", "");
					AppClient.setAcces_token(token);
					Intent intent=new Intent(Login.this, MainActivity.class);
					startActivity(intent);
					Login.this.finish();
					break;
					
				}

			}
			
		};
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==login_btn)
		{
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String name=username.getText().toString();
					String pass=userpasswd.getText().toString();
					
					JSONObject json=new AppClient(AppContext.auth_server).login(name, pass);
					Message msg=new Message();
					msg.obj=json;
					msg.what=1;
					loginhandler.sendMessage(msg);
					
				}
				
			}).start();
		}
		
	}
	public void saveLoginInfo()
	{
		
	}
	public void readLoginInfo()
	{
		
	}

}
