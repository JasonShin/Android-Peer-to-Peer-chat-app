package com.konichiwalivejason.www;

import com.konichiwa.handler.message.HM;
import com.konichiwa.handler.message.SpriteHandlerMessages;
import com.konichiwa.server.ServerInfo;

import android.app.*;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;

public class SpriteActivity extends Activity {

	private SpriteHandlerMessages shm;
	// --------------------------------------------------------------------------------------------------------------

	private Handler spriteHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String key = b.getString(HM.getKey());
			if (key.equals(shm.getHandlerSimpleToast())) {
				Toast.makeText(getApplicationContext(), "works", Toast.LENGTH_LONG).show();
			} else if (key.equals(shm.getHandlerMoveToChat())) {

				moveToChat();

			} else {
				Log.e("s", "Failed to receive handler message!");
			}
		}
	};

	// --------------------------------------------------------------------------------------------------------------

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	// --------------------------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.spritelayout);
		shm = new SpriteHandlerMessages();
	}

	// --------------------------------------------------------------------------------------------------------------

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Thread t = new Thread(new Runnable() {
			public void run() {
				Log.i("s", "ip: " + ServerInfo.getServerIP() + "  port: " + ServerInfo.getPort());

				try {
					Thread.sleep(3000);
					HM.MessageSender(spriteHandler, shm.getHandlerMoveToChat());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, "T1");
		t.setPriority(Thread.NORM_PRIORITY);

		t.start();
	}

	// --------------------------------------------------------------------------------------------------------------

	private void setLoadingImage() {
		ImageView loading = (ImageView) findViewById(R.id.loading_image);
		AnimationDrawable loadingAnimation = (AnimationDrawable) loading.getDrawable();
		loadingAnimation.start();

	}

	// --------------------------------------------------------------------------------------------------

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		setLoadingImage();
	}

	// --------------------------------------------------------------------------------------------------------------

	private void moveToChat() {
		Intent chatIntent = new Intent("android.intent.action.CHATROOM");
		startActivity(chatIntent);
		finish();
	}

	// --------------------------------------------------------------------------------------------------------------

}
