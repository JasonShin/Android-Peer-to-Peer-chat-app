import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import org.apache.commons.io.IOUtils;

public class ClientHandler {
	private LinkedList<Object> chatLog;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Thread mainBlinker;
	private Thread heartBeatBlinker;

	public ClientHandler(ObjectOutputStream o, ObjectInputStream i) {
		chatLog = new LinkedList<Object>();
		this.output = o;
		this.input = i;
		startThread();
	}

	public void startThread() {
		// setUpStreams();
		ListeningClass lc = new ListeningClass();
		lc.start();
		HeartBeat hb = new HeartBeat();
		hb.start();
	}

	class ListeningClass extends Thread implements Runnable {

		public ListeningClass() {
			mainBlinker = currentThread();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				// setUpStreams();
				while (mainBlinker != null) {
					Object o = input.readObject();
					chatLog.addFirst(o.toString());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
			}
		}

	}

	class HeartBeat extends Thread implements Runnable {

		private static final long SLEEP_INTERVAL = 1000;

		public HeartBeat() {
			heartBeatBlinker = currentThread();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (heartBeatBlinker != null) {
				try {
					Thread.sleep(SLEEP_INTERVAL);
					output.writeObject(MessageFilter.getHeartBeatCode());
				} catch (Exception e) {
					chatLog.addFirst(MessageFilter.getFinishCode());
					break;
				}
			}
		}

	}


	public void writeObject(Object o) {
		try {

			output.writeObject(o);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block

		}
	}

	public int getChatLogSize() {
		return chatLog.size();
	}

	public Object popChatLog() {
		return chatLog.pop();
	}

	public Object pollLastChatLog() {
		return chatLog.pollLast();
	}

	public void destroyThread() {
		Thread tmpBlinker = mainBlinker;
		mainBlinker = null;

		if (tmpBlinker != null) {
			tmpBlinker.interrupt();
			try {
				output.close();
				input.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				// e.printStackTrace();
			} finally {

				IOUtils.closeQuietly(output);
				IOUtils.closeQuietly(input);
				System.out.println("Closed quietly!");
			}
		}

		Thread tmpHBBlinker = heartBeatBlinker;
		heartBeatBlinker = null;
		if (tmpHBBlinker != null) {
			tmpHBBlinker.interrupt();
		}

	}

}
