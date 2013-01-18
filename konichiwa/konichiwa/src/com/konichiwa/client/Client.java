package com.konichiwa.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.konichi.messages.Chats;
import com.konichi.messages.MessageFilter;
import com.konichi.messages.OpponentProfile;
import com.konichiwa.handler.message.ChatroomHandlerMessages;
import com.konichiwa.handler.message.HM;
import com.konichiwa.images.ChatImage;
import com.konichiwa.server.ServerInfo;
import com.konichiwa.settings.MyProfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

public class Client {

	private Thread blinker; //for main thread
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection;
	private Context context;
	private ArrayList<Chats> al;
	private ArrayList<ChatImage> cal;
	private Handler scrollToBottomHandler;
	private Handler mainHandler;
	private boolean connected = false;
	private ChatroomHandlerMessages chm;
	private OpponentProfile op;
	private boolean receivedProfile = false;

	public Client(Context context) {
		this.context = context;
		chm = new ChatroomHandlerMessages();
	}

	// --------------------------------------------------------------------------
	public void setClientVariables(ArrayList<Chats> al, ArrayList<ChatImage> cal, ArrayList<OpponentProfile> prof, Handler scrollBottom, Handler mainHandler) throws IOException {
		Log.i("con", "worked2");
		this.al = al;
		this.cal = cal;
		this.scrollToBottomHandler = scrollBottom;
		this.mainHandler = mainHandler;
		MainThread mt = new MainThread();
		mt.start();

	}

	// --------------------------------------------------------------------------
	class MainThread extends Thread implements Runnable {
		public MainThread() {
			blinker = this;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Thread thisThread = currentThread();
			try {
				Log.i("con", "worked3");
				connectToServer();
				if (connected) {
					setUpStreams();
					sendProfile();
					Log.i("con", "worked8");
					while (blinker == thisThread) {
						startListening();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("cl", "Found IO Exception!: " + e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e("cl", "Found class not found exception!: " + e);
			}

		}

		private void setUpStreams() throws IOException {
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());
		}

	}

	private void connectToServer() {

		try {
			String SERVER_IP = ServerInfo.getServerIP();
			int PORT = ServerInfo.getPort();
			Log.i("con", "worked4: ip = " + SERVER_IP + "  PORT: " + PORT);
			Pattern pattern = Pattern.compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
			Matcher matcher = pattern.matcher(SERVER_IP);
			boolean validated = (matcher.find() && matcher.group().equals(SERVER_IP));
			//boolean validated = true;
			if (validated) {
				connection = new Socket(SERVER_IP, PORT);
				connected = true;
			} else {
				Log.i("cl", "Failed to connected with the server!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("cl", "Client IO error!: " + e);
		}

	}

	// --------------------------------------------------------------------------

	public void sendProfile() {
		/*
		 * private String gender = ""; private String country = ""; private String interest = ""; private String age = ""; private String icon = "";
		 */
		MyProfile mp = new MyProfile(context);
		String myProfile = MessageFilter.getOpponentProfileCode() + mp.getGender() + "," + mp.getCountry(context) + "," + mp.getInterest() + "," + mp.getAge();
		this.sendString(myProfile);
	}

	// --------------------------------------------------------------------------
	private void startListening() throws OptionalDataException, ClassNotFoundException, IOException {

		String message = null;
		Bitmap b = null;

		if (input.equals(Bitmap.class)) {
			b = (Bitmap) input.readObject();
		} else {
			message = (String) input.readObject();
		}

		if (message.startsWith(MessageFilter.getNormalMessageCode())) {
			String normalMessage = MessageFilter.filterMessage(message);
			al.add(new Chats(normalMessage, UserDetail.getTime(), 1));
			scrollToBottomHandler.sendEmptyMessage(1);

		} else if (message.startsWith(MessageFilter.getImageFileCode())) {
			String imageName = MessageFilter.filterMessage(message);
			String url = ServerInfo.getServerImagePath() + imageName;

			ChatImage ch = new ChatImage(context.getApplicationContext(), getBitmapFromUrl(url), al.size());
			al.add(new Chats(ch.getCompressedImage(), 4, ch, UserDetail.getTime()));
			cal.add(ch);
			scrollToBottomHandler.sendEmptyMessage(1);

		}

		else if (message.startsWith(MessageFilter.getOpponentProfileCode())) {
			String[] userProfile = MessageFilter.filterMessage(message).split(",");
			op = new OpponentProfile(userProfile[0], userProfile[1], userProfile[2], userProfile[3]);
			receivedProfile = true;

		}

		else if (message.startsWith(MessageFilter.getConnectionSuccess())) {
			Log.i("ls", "connection success");
			new ProfileWaitingThread().start();
		}

		else if (message.equals(MessageFilter.getFinishCode())) {
			Log.i("cl", "Finished!");
			stopThread();
		}

		else if (message.equals(MessageFilter.getHeartBeatCode())) {
			//String heartBeatMessage = MessageFilter.filterMessage(message);

			Log.i("cl", "Received heart beat from the server");
		} else {
			ChatImage ch = new ChatImage(context.getApplicationContext(), b, al.size());
			al.add(new Chats(ch.getCompressedImage(), 4, ch, UserDetail.getTime()));
			scrollToBottomHandler.sendEmptyMessage(1);
		}

	}

	// --------------------------------------------------------------------------

	class ProfileWaitingThread extends Thread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (true) {
				Log.i("pt", "looping!");
				if (receivedProfile == true) {
					String profile = "Age: " + op.getAge() + "   /  Gender: " + op.getGender() + "\nCountry: " + op.getCountry() + " \nInterest: " + op.getInterest();
					//al.add(new Chats(profile, 5));
					HM.MessageSender(mainHandler, chm.getSuccessMessage() + profile);
					//scrollToBottomHandler.sendEmptyMessage(1);
					receivedProfile = false;
					break;
				}
			}
		}

	}

	// --------------------------------------------------------------------------

	public void sendString(String msg) {
		try {
			output.writeObject(msg);

			Log.i("cl", "Successfully written a message");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("cl", "Found Error while sending string!");
		}
	}

	// --------------------------------------------------------------------------

	public void stopThread() {
		//Main Thread
		try {
			Thread tmp = blinker;
			blinker = null;

			if (tmp != null) {
				tmp.interrupt();

				if (connected) {
					HM.MessageSender(mainHandler, chm.getDisconnectMessage());
					connection.close();
					input.close();
					output.close();
				}
			}

		} catch (IOException e) {
			Log.i("cl", "Problem found while trying to shutting down Main thread!: " + e);
		}

	}
	// --------------------------------------------------------------------------

	private Bitmap getBitmapFromUrl(String url) {
		try {
			URL myUrl = new URL(url);
			return BitmapFactory.decodeStream(myUrl.openConnection().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
