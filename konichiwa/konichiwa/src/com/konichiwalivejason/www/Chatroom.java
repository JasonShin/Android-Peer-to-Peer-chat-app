package com.konichiwalivejason.www;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.konichi.messages.Chats;
import com.konichi.messages.MessageFilter;
import com.konichi.messages.OpponentProfile;
import com.konichiwa.client.Client;
import com.konichiwa.client.UserDetail;
import com.konichiwa.handler.message.ChatroomHandlerMessages;
import com.konichiwa.handler.message.HM;
import com.konichiwa.images.ChatImage;
import com.konichiwa.settings.BackgroundSetter;
import com.konichiwa.settings.MyProfile;
import com.konichiwa.settings.SettingPanelEvent;

public class Chatroom extends ListActivity implements OnClickListener, OnScrollListener, OnCancelListener, OnItemClickListener {

	private ArrayList<Chats> al;
	private CustomAdapter adapter;
	private ArrayList<ChatImage> cal;
	private ArrayList<OpponentProfile> oppo_profile;
	private Client client;
	private ProgressDialog loading_pd;
	private Dialog quit_app_dialog;
	private Dialog picture_dialog;
	private ChatroomHandlerMessages chm;
	private LinearLayout panel;
	private Uri capturedImageUri;
	private LinearLayout ll_bg;

	private Handler scrollToBottomHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
			scrollToBottomOnCondition(true);
		}

	};

	//This is for connect and disconnect button
	private Handler buttonHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			TextView startBut = (TextView) findViewById(R.id.tv_start_but);
			TextView disconnectBut = (TextView) findViewById(R.id.tv_discon_but);
			Bundle b = msg.getData();
			String key = b.getString(HM.getKey());

			Log.i("but", "key: " + key);
			if (key.startsWith(chm.getShowFinishButMessage())) {
				startBut.setVisibility(View.INVISIBLE);
				disconnectBut.setVisibility(View.VISIBLE);
			} else if (key.startsWith(chm.getShowStartButMessage())) {
				startBut.setVisibility(View.VISIBLE);
				disconnectBut.setVisibility(View.INVISIBLE);
			}

		}

	};

	private Handler mainHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle b = msg.getData();
			String key = b.getString(HM.getKey());
			Log.i("ls", "mainhandler: " + key);
			if (key.startsWith(chm.getSuccessMessage())) {
				String userProfileData = key.substring(5);
				setGuiInputMode(true);
				loading_pd.dismiss();
				al.add(new Chats("Connection successful\n" + userProfileData, 2));
				adapter.notifyDataSetChanged();
				HM.MessageSender(buttonHandler, chm.getShowFinishButMessage());
			} else if (key.startsWith(chm.getDisconnectMessage())) {
				al.add(new Chats("You have cancelled to connect to an user!", 2));
				adapter.notifyDataSetChanged();
				setGuiInputMode(false);
				HM.MessageSender(buttonHandler, chm.getShowStartButMessage());
			}
			scrollToBottomOnCondition(false);
		}

	};

	private Handler openPanelHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle b = msg.getData();
			String key = b.getString(HM.getKey());
			LayoutParams params = panel.getLayoutParams();
			if (key.charAt(0) == 'W') {

				int width = Integer.parseInt(key.substring(1, key.length()));
				params.width = width;
				panel.setLayoutParams(params);
			} else if (key.charAt(0) == 'H') {

				int height = Integer.parseInt(key.substring(1, key.length()));
				Log.i("sh", "H: " + height);
				params.height = height;
				panel.setLayoutParams(params);
			} else if (key.charAt(0) == 'C') {
				panel.setVisibility(View.GONE);
				params.height = 0;
				params.width = 0;
				panel.setLayoutParams(params);
			} else {
				Log.i("sh", "V");
				panel.setVisibility(View.VISIBLE);
			}

		}

	};

	// -------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.onechatlayout);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setList();
		setGuiContents();
		chm = new ChatroomHandlerMessages();
		setGuiInputMode(false);

		configurePreferences();
		
	}

	// -------------------------------------------------------------------------------------

	public void configurePreferences() {
		MyProfile mp = new MyProfile(getApplicationContext());
		new BackgroundSetter(mp.getBackground(), getApplicationContext(), this.ll_bg);
	}

	// -------------------------------------------------------------------------------------

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		Log.i("or", "changed! from config");
	}

	// -------------------------------------------------------------------------------------

	@Override
	public void onBackPressed() {
		Log.i("p", "works1");
		if (SettingPanelEvent.isOpening()) {
			HM.MessageSender(openPanelHandler, "C");
			SettingPanelEvent.setOpening(false);
		} else {
			quit_app_dialog = new Dialog(Chatroom.this);
			quit_app_dialog.setContentView(R.drawable.custom_quit_dialog_layout);
			quit_app_dialog.setTitle("Are you sure you want to quit?");
			Button but_cancel = (Button) quit_app_dialog.findViewById(R.id.but_quit_cancel);
			Button but_ok = (Button) quit_app_dialog.findViewById(R.id.but_quit_ok);
			but_cancel.setOnClickListener(Chatroom.this);
			but_ok.setOnClickListener(Chatroom.this);
			quit_app_dialog.show();
		}
	}

	// -------------------------------------------------------------------------------------

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	// -------------------------------------------------------------------------------------

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("One", "Activity has been destroyed");
		stopConnection();
	}

	// -------------------------------------------------------------------------------------
	private void setList() {
		al = new ArrayList<Chats>();
		al.add(new Chats("Welcome to GoodDay Chat\nRandom chat 4ever", 2));
		adapter = new CustomAdapter(this, 0, al);
		setListAdapter(adapter);
		this.getListView().setOnScrollListener(this);

		cal = new ArrayList<ChatImage>();
		oppo_profile = new ArrayList<OpponentProfile>();
		scrollToBottomOnCondition(false);
	}

	// -------------------------------------------------------------------------------------

	private void setGuiContents() {
		Button ok = (Button) findViewById(R.id.but_ok);
		Button add = (Button) findViewById(R.id.but_additional);
		TextView startBut = (TextView) findViewById(R.id.tv_start_but);
		TextView disconnectBut = (TextView) findViewById(R.id.tv_discon_but);
		ImageView option = (ImageView) findViewById(R.id.iv_option);
		panel = (LinearLayout) findViewById(R.id.setting_panel);
		ListView settings_lv = (ListView) findViewById(R.id.lv_option);
		ll_bg = (LinearLayout) findViewById(R.id.ll_chat_stuffs);
		ok.setOnClickListener(this);
		add.setOnClickListener(this);
		startBut.setOnClickListener(this);
		disconnectBut.setOnClickListener(this);
		option.setOnClickListener(this);
		settings_lv.setOnItemClickListener(this);
	}

	// -------------------------------------------------------------------------------------

	private void setGuiInputMode(boolean b) {
		EditText userInput = (EditText) findViewById(R.id.et_userinput);
		Button ok = (Button) findViewById(R.id.but_ok);
		Button add = (Button) findViewById(R.id.but_additional);
		userInput.setClickable(b);
		userInput.setCursorVisible(b);
		userInput.setFocusable(b);
		userInput.setFocusableInTouchMode(b);
		ok.setClickable(b);
		ok.setCursorVisible(b);
		ok.setFocusable(b);
		ok.setFocusableInTouchMode(b);
		add.setClickable(b);
		add.setCursorVisible(b);
		add.setFocusable(b);
		add.setFocusableInTouchMode(b);
	}

	// -------------------------------------------------------------------------------------

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getTag() != null) {
			if (v.getTag().toString().startsWith("bg")) {
				int choice = Integer.parseInt("" + v.getTag().toString().charAt(2));
				new BackgroundSetter(choice, getApplicationContext(), this.ll_bg);

			} else {
				int pos = (Integer) v.getTag();
				Log.i("lv", "index: " + pos);
				picture_dialog = new Dialog(Chatroom.this);

				picture_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				picture_dialog.setContentView(R.drawable.custom_image_dialog_layout);
				ImageView iv = (ImageView) picture_dialog.findViewById(R.id.iv_cus_diag);
				iv.setImageBitmap(cal.get(pos).getBigImage());
				picture_dialog.show();
			}
		} else {

			switch (v.getId()) {
				case R.id.but_ok :
					EditText et_user = (EditText) findViewById(R.id.et_userinput);
					String stuff = et_user.getText().toString();
					et_user.setText("");
					sendChat(stuff);
					scrollToBottomOnCondition(false);
					scrollToBottomHandler.sendEmptyMessage(1);
					break;
				case R.id.but_additional :
					Dialog dialog = new Dialog(Chatroom.this);
					dialog.setContentView(R.drawable.customdialoglayout);
					dialog.setTitle("Additional stuffs!");
					Button but_cam = (Button) dialog.findViewById(R.id.but_camera);
					Button but_load = (Button) dialog.findViewById(R.id.but_image_load);
					but_cam.setOnClickListener(Chatroom.this);
					but_load.setOnClickListener(Chatroom.this);
					dialog.show();
					break;
				case R.id.iv_option :
					new SettingPanelEvent(openPanelHandler);
					SettingPanelEvent.setOpening(true);
					break;
				case R.id.tv_start_but :
					if (new UserDetail(getApplicationContext()).wifiAvailability()) {
						loading_pd = ProgressDialog.show(Chatroom.this, "", "Currently looking for an user!");
						loading_pd.setCancelable(true);
						loading_pd.setOnCancelListener(this);
						buttonFilter(v.getId());
					} else {
						Toast.makeText(getApplicationContext(), "Your wifi is not connected :(", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.tv_discon_but :
					buttonFilter(v.getId());
					break;
				case R.id.but_camera :
					int CAMERA_PICTURE = 0;
					Intent camIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					capturedImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
					camIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, capturedImageUri);
					camIntent.putExtra("return-data", true);
					startActivityForResult(camIntent, CAMERA_PICTURE);
					break;
				case R.id.but_image_load :
					int SELECT_PICTURE = 1;
					Intent imgIntent = new Intent(Intent.ACTION_PICK);
					imgIntent.setType("image/*");
					imgIntent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(imgIntent, "Please select your picture =p"), SELECT_PICTURE);
					break;

				case R.id.but_quit_cancel :
					quit_app_dialog.dismiss();
					break;

				case R.id.but_quit_ok :
					quit_app_dialog.dismiss();
					finish();
					break;
			}
		}
	}

	// -------------------------------------------------------------------------------------

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {

			final int CAMERA = 0;
			final int GALLERY = 1;
			Log.i("bp", "req: " + requestCode);
			switch (requestCode) {
				case CAMERA :
					//Uri selectedImage1 = data.getData();
					//Toast.makeText(getApplicationContext(), "uri: " + this.capturedImageUri, Toast.LENGTH_LONG).show();
					try {
						Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), capturedImageUri);
						ChatImage ch1 = new ChatImage(getApplicationContext(), photo, al.size());
						cal.add(ch1);
						al.add(new Chats(ch1.getCompressedImage(), 3, ch1, UserDetail.getTime()));
						new sendingImageThread("camera", ch1).start();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case GALLERY :
					Uri selectedImage = data.getData();
					String[] filePathColumn = {MediaStore.Images.Media.DATA};
					Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String filePath = cursor.getString(columnIndex);
					cursor.close();
					
					Bitmap bmp = BitmapFactory.decodeFile(filePath);
					ChatImage ch = new ChatImage(getApplicationContext(), bmp, al.size());
					cal.add(ch);
					al.add(new Chats(ch.getCompressedImage(), 3, ch, UserDetail.getTime()));
					new sendingImageThread("gallery", ch).start();
					break;
			}
			adapter.notifyDataSetChanged();
			scrollToBottomOnCondition(false);
		}
	}

	// -------------------------------------------------------------------------------------

	class sendingImageThread extends Thread implements Runnable {

		private String whichProcess;
		private ChatImage ch;

		public sendingImageThread(String wp, ChatImage c) {
			whichProcess = wp;
			ch = c;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				if (whichProcess.equals("camera")) {
					sendImageUrl(ch.uploadFile());
					new File(new URI(capturedImageUri.toString())).delete();
					Log.i("cam", "deleted the file!");
					capturedImageUri = null;
				} else {
					sendImageUrl(ch.uploadFile());
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// -------------------------------------------------------------------------------------

	private synchronized void buttonFilter(int id) {

		class EventHandler extends Thread implements Runnable {
			private int id;
			public EventHandler(int i) {
				id = i;
			}
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				switch (id) {
					case R.id.tv_start_but :
						HM.MessageSender(buttonHandler, chm.getShowStartButMessage());
						startConnection();
						break;
					case R.id.tv_discon_but :
						HM.MessageSender(buttonHandler, chm.getShowFinishButMessage());
						stopConnection();
						break;
				}
			}
		}

		EventHandler ev = new EventHandler(id);
		ev.start();

	}

	// -------------------------------------------------------------------------------------

	private void startConnection() {
		Thread t1 = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					Log.i("con", "worked1");
					client = new Client(getApplicationContext());
					client.setClientVariables(al, cal, oppo_profile, scrollToBottomHandler, mainHandler);

				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					Log.i("One", "Failed to start chat!: " + e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.i("One", "IO Exception detected!: " + e);
				}

			}

		};
		t1.run();
	}

	private void stopConnection() {
		Thread t2 = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if (client != null) {
					client.stopThread();
				}

			}

		};
		t2.start();

	}

	// -------------------------------------------------------------------------------------

	private void sendChat(String m) {
		if (!m.equals(null) && !m.equals("")) {
			String message = MessageFilter.setNormalMessage(m);
			client.sendString(message);

			al.add(new Chats(m, UserDetail.getTime(), 0));
			scrollToBottomHandler.sendEmptyMessage(1);
		}
	}

	private void sendImageUrl(String url) {
		String imgUrl = MessageFilter.setImageFileMessage(url);
		client.sendString(imgUrl);
	}

	// -------------------------------------------------------------------------------------

	private void scrollToBottomOnCondition(boolean condition) {

		int lastSecondVisible = this.getListView().getLastVisiblePosition();
		if (lastSecondVisible >= al.size() - 2 || !condition) {
			this.getListView().setSelection(al.size() - 1);
		}
	}

	// -------------------------------------------------------------------------------------

	public void onScroll(AbsListView view, int firstVisible, int count, int total) {
		// TODO Auto-generated method stub

	}

	public void onScrollStateChanged(AbsListView view, int state) {
		// TODO Auto-generated method stub

	}

	// -------------------------------------------------------------------------------------

	static class ChatHolder {
		TextView txtTitle;
		TextView txtTime;
		ImageView bmp_image;
	}

	public class CustomAdapter extends ArrayAdapter<Chats> {
		private static final int MY_CHAT = 0;
		private static final int OPPO_CHAT = 1;
		private static final int ANNOUNCEMENT = 2;
		private static final int MY_IMAGE = 3;
		private static final int OPPO_IMAGE = 4;
		private static final int OPPO_PROFILE = 5;
		Context context;
		ArrayList<Chats> list;

		public CustomAdapter(Context context, int resource, ArrayList<Chats> l) {
			super(context, 0, l);
			this.context = context;
			this.list = l;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = null; //by forcing passed in row to be null, it will refresh all the time
			ChatHolder holder = null;

			if (row == null) {

				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				int bmpimg = 0;
				int txt = 0;
				int time = 0;

				switch (list.get(position).getChoice()) {

					case MY_CHAT :
						row = inflater.inflate(R.layout.custom_one_me_row, parent, false);
						txt = R.id.my_txtTitle;
						time = R.id.my_txtTime;
						break;
					case OPPO_CHAT :
						row = inflater.inflate(R.layout.custom_one_oppo_row, parent, false);
						txt = R.id.oppo_txtTitle;
						time = R.id.oppo_txtTime;
						break;
					case ANNOUNCEMENT :
						row = inflater.inflate(R.layout.custom_one_announce_row, parent, false);
						txt = R.id.my_announcementTxt;
						break;
					case OPPO_PROFILE :
						row = inflater.inflate(R.layout.custom_one_profile_row, parent, false);
						txt = R.id.tv_oppo_prof;
						break;
					case MY_IMAGE :
						row = inflater.inflate(R.layout.custom_my_image_row, parent, false);
						bmpimg = R.id.iv_selected_image;
						time = R.id.my_img_txtTime;
						break;
					case OPPO_IMAGE :
						row = inflater.inflate(R.layout.custom_oppo_image_row, parent, false);
						bmpimg = R.id.iv_selected_oppo_image;
						time = R.id.oppo_img_txtTime;
						break;
				}

				holder = new ChatHolder();
				if (list.get(position).getChoice() != MY_IMAGE && list.get(position).getChoice() != OPPO_IMAGE) {
					Log.i("opf", "worked 1");
					holder.txtTitle = (TextView) row.findViewById(txt);
					Log.i("opf", "worked 2");
					holder.txtTitle.setText(list.get(position).getText());
					Log.i("opf", "worked 3");
				}
				if (list.get(position).getChoice() != ANNOUNCEMENT && list.get(position).getChoice() != OPPO_PROFILE) {
					holder.txtTime = (TextView) row.findViewById(time);
					holder.txtTime.setText(list.get(position).getTime());
				}
				if (list.get(position).getChoice() == MY_IMAGE || list.get(position).getChoice() == OPPO_IMAGE) {

					holder.bmp_image = (ImageView) row.findViewById(bmpimg);
					holder.bmp_image.setImageBitmap(list.get(position).getCamBitmap());
					setListElementClickListener(holder.bmp_image);
					holder.bmp_image.setTag(getImageIndexPos(position));
					holder.bmp_image.setFocusable(true);
					holder.bmp_image.setClickable(true);
				}
				row.setTag(holder);
			}

			//Chats chat = list.get(position);

			return row;
		}

	}

	// -------------------------------------------------------------------------------------

	private void setListElementClickListener(View v) {

		v.setOnClickListener(this);

	}

	// -------------------------------------------------------------------------------------

	private int getImageIndexPos(int pos) {
		for (int i = 0; i < cal.size(); i++) {
			if (cal.get(i).getPosition() == pos) {
				return i;
			}
		}
		return 0;
	}

	public void onCancel(DialogInterface arg0) {
		// TODO Auto-generated method stub
		HM.MessageSender(buttonHandler, chm.getShowStartButMessage());
		this.stopConnection();
	}

	//option panel eventhandler
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		// TODO Auto-generated method stub
		/*
		 * 0 == change icon 1 == set background 2 == set random background 3 == developer's comments
		 */
		Dialog dialog = new Dialog(Chatroom.this);

		switch (position) {
			case 0 :
				HM.MessageSender(openPanelHandler, "C");
				SettingPanelEvent.setOpening(false);
				Intent profileIntent = new Intent("android.intent.action.MYPROFILE");
				startActivity(profileIntent);
				break;
			case 1 :
				dialog.setContentView(R.drawable.custom_dialog_background_layout);
				dialog.setTitle("Backgrounds!");
				
				ImageView bg1 = (ImageView) dialog.findViewById(R.id.bg_one);
				ImageView bg2 = (ImageView) dialog.findViewById(R.id.bg_two);
				ImageView bg3 = (ImageView) dialog.findViewById(R.id.bg_three);
				bg1.setOnClickListener(this);
				bg2.setOnClickListener(this);
				bg3.setOnClickListener(this);
				bg1.setTag("bg0");
				bg2.setTag("bg1");
				bg3.setTag("bg2");
				dialog.show();
				break;
			case 2 :
				dialog.setContentView(R.drawable.custom_dialog_message_layout);
				dialog.setTitle("Developer's message");
				dialog.show();
				break;
			case 3 :
				break;
		}

	}

}
