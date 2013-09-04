package com.tcy.app.ui;

import com.tcy.app.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

public class Welcome extends Activity {
	private ImageView imagev;
	private AnimationDrawable rocketAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		imagev=(ImageView)findViewById(R.id.show_imageview);
		imagev.setBackgroundResource(R.anim.welcome);
		rocketAnimation = (AnimationDrawable) imagev.getBackground();
		new Thread(new Runnable()
		{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
			}
			
		}).start();
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		  if (event.getAction() == MotionEvent.ACTION_DOWN) {
			  rocketAnimation.stop();
			  rocketAnimation.start();
			    return true;
			  }
		return super.onTouchEvent(event);
	}

}
