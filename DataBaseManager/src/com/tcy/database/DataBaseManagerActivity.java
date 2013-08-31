package com.tcy.database;

import com.tcy.database.entity.User;
import com.tcy.database.service.UserService;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DataBaseManagerActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private Button chaxun_btn,add_btn,del_btn,upt_btn;
	private ListView listview;
	private EditText input_et;
	private TextView show_tv;
	private Cursor cursor;
	private SimpleCursorAdapter simpleCursorAdapter;
	private UserService us;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ininView();
    }
    public void ininView()
    {
    	listview=(ListView)findViewById(R.id.list_view);
    	chaxun_btn=(Button)findViewById(R.id.select_btn);
    	chaxun_btn.setOnClickListener(this);
    	add_btn=(Button)findViewById(R.id.add_btn);
    	add_btn.setOnClickListener(this);    	
    	del_btn=(Button)findViewById(R.id.del_btn);
    	del_btn.setOnClickListener(this);
    	upt_btn=(Button)findViewById(R.id.up_btn);
    	upt_btn.setOnClickListener(this);
    	input_et=(EditText)findViewById(R.id.input_text);
    	show_tv=(TextView)findViewById(R.id.show_text);
		us=new UserService(this,null);

    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.add_btn:
			User u=new User();
			u.setName("tcy");
			u.setPasswd("1234");
			us.add(u);
			initListData();
			break;
		}
		
	}
	public void initListData()
	{
		User u=new User();
		u.setName("tcy");
		Cursor cursor=us.getUser(u);
		simpleCursorAdapter=new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, 
				new String[]{"name"},new int[]{android.R.id.text1});
		listview.setAdapter(simpleCursorAdapter);
		
	}
}