import java.util.ArrayList;
import java.util.LinkedList;

import org.konichiwa.jdbc.JdbcImpl;


public class OneOnOneChatHandler {

	private Thread blinker;
	private boolean chatEnded = false;
	private ArrayList<ClientHandler> userList;
	private ArrayList<String> imageNameList;

	public OneOnOneChatHandler(LinkedList<ClientConnectionHolder> conList) {
		userList = new ArrayList<ClientHandler>();
		imageNameList = new ArrayList<String>();

		for (ClientConnectionHolder connection : conList) {
			userList.add(new ClientHandler(connection.getOuput(), connection
					.getInput()));
			System.out.println("Added user to user list!: "
					+ connection.getConnection());
		}
		sendSuccessMessage();
		startThread();
		
		new JdbcImpl();
	}

	public void sendSuccessMessage() {
		for (int i = 0; i < userList.size(); i++) {

			userList.get(i).writeObject(
					MessageFilter.setConnectionSucceedMessage(""));
			System.out.println("sent a success message! " + userList.get(i));
		}
		
		
	}

	private void startThread() {

		ChatHandlingClass chc = new ChatHandlingClass();
		chc.start();
	}

	class ChatHandlingClass extends Thread implements Runnable {

		public ChatHandlingClass() {
			blinker = currentThread();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (blinker != null) {
				try {
					for (ClientHandler ul : userList) {
						messageHandler(ul);
					}

					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}

			}
		}

	}

	private void messageHandler(ClientHandler ul) {
		if (ul.getChatLogSize() != 0) {
			Object stuff = ul.pollLastChatLog();

			if (stuff.toString().startsWith(MessageFilter.getImageFileCode())) {

				System.out.println("name: "
						+ MessageFilter.filterMessage((String) stuff));
				String fileName = MessageFilter.filterMessage((String) stuff);
				imageNameList.add(fileName);
			}
			for (ClientHandler ule : userList) {

				if (!ule.equals(ul)) {
					ule.writeObject(stuff);

					if (stuff.toString().equals(MessageFilter.getFinishCode())) {
						System.out.println("finish chat detected");
						ule.destroyThread();
						chatEnded = true;
					}
				}

			}
		}

	}

	public void stopThread() {
		new FTPHandler().deleteAssociatedFiles(imageNameList);
		Thread tmpThread = blinker;
		blinker = null;

		if (tmpThread != null) {
			tmpThread.interrupt();
		}
	}

	public boolean getChatEnded() {
		return chatEnded;
	}

}
