package com.konichi.messages;

import com.konichiwa.images.ChatImage;
import android.graphics.Bitmap;

public class Chats {
	private String texts;
	private int choice;
	private Bitmap bitmap;
	private ChatImage chatimage;
	private String time;
	
	public Chats() {
		super();
	}
	
	
	//my image
	public Chats(Bitmap b, int c, ChatImage ch, String time){
		bitmap = b;
		choice = c;
		chatimage = ch;
		this.time = time;
	}


	//announcement || profile
	public Chats(String t, int c){
		texts = t;
		choice = c;
	}
	
	
	//chat
	public Chats(String t, String time, int c) {

		texts = t;
		choice = c;
		this.time = time;
	}
	
	
	
	
	public int getChoice(){return choice;}
	public String getText(){return texts;}
	public Bitmap getCamBitmap(){return bitmap;}
	public String getTime(){return this.time;}
}
