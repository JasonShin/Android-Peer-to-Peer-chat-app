package com.konichiwa.handler.message;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HM {
	
	private static final String KEY = "TheKey";
	
	
	public static void MessageSender(Handler handler, String message){
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putString(KEY, message);
		msg.setData(b);
		handler.sendMessage(msg);
	}

	public static String getKey() {
		return KEY;
	}
	
}
