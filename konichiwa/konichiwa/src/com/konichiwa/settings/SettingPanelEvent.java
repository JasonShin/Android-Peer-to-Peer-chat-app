package com.konichiwa.settings;

import android.os.Handler;
import android.util.Log;

import com.konichiwa.handler.message.HM;

public class SettingPanelEvent {

	private Handler openPanelHandler;
	private static boolean opening = false;
	
	public SettingPanelEvent(Handler h) {
		this.openPanelHandler = h;
		new OpenPanel().start();
	}
	// -------------------------------------------------------------------------------------

	class OpenPanel extends Thread implements Runnable {
		private static final int MAX_HEIGHT = 450;
		private static final int MAX_WIDTH = 350;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Log.i("st", "works 2.5");
			HM.MessageSender(openPanelHandler, "V");
			int width = 0;
			int height = 0;
			Log.i("st", "works 3");
			//300, 400
			while (height != MAX_HEIGHT) {

				if (width != MAX_WIDTH)
					width += 1;

				height += 1;

				HM.MessageSender(openPanelHandler, "W" + width);
				HM.MessageSender(openPanelHandler, "H" + height);
				Log.i("sl", "w: " + width + " | h: " + height);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	
	public static boolean isOpening() {
		return opening;
	}

	public static void setOpening(boolean opening) {
		SettingPanelEvent.opening = opening;
	}
	
	

}
