import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Server implements CommandList {

	private static final int PORT = 443;
	// private static final int PORT = 53321;
	private Thread blinker;
	private Thread checkBlinker;
	private LinkedList<OneOnOneChatHandler> ooList;
	private boolean monitor = false;
	private int numTmpCli;
	private LinkedList<ClientConnectionHolder> connectionList;

	// --------------------------------------------------------------

	public Server() {
		ooList = new LinkedList<OneOnOneChatHandler>();
		startThread();
		// cliManager();
	}

	// --------------------------------------------------------------

	public void startThread() {
		new AcceptingClass().start();
		new CheckingChatProcess().start();
	}

	// --------------------------------------------------------------

	class AcceptingClass extends Thread implements Runnable {

		private static final int NUM_USER_LIMIT = 2;
		private TempHeartBeat thb;

		public AcceptingClass() {
			blinker = Thread.currentThread();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			numTmpCli = 0;
			connectionList = new LinkedList<ClientConnectionHolder>();
			// Socket[] s = new Socket[NUM_USER_LIMIT];
			System.out.println("Started to accepting user");
			ServerSocket ss = null;
			
			try {
				ss = new ServerSocket(PORT, 10);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while (blinker != null) {
				try {
					Socket connection = ss.accept();
					monitorConnection(connection);
					// s[numCli] = connection;
					// setting up streams
					ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
					output.flush();
					ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
					connectionList.add(new ClientConnectionHolder(connection, output, input));
					numTmpCli++;

					if (numTmpCli < 2) {
						System.out.println("Added a temp user!");
						thb = new TempHeartBeat(output);
						thb.start();
					}

					System.out.println("accepted a client!");
					if (numTmpCli == NUM_USER_LIMIT) {
						thb.destroyThread();
						OneOnOneChatHandler oc = new OneOnOneChatHandler(
								connectionList);
						ooList.add(oc);
						numTmpCli = 0;
						connectionList.clear();
					}

					//ss.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					numTmpCli = 0;
					connectionList.clear();
				}
			}
		}

	}

	// --------------------------------------------------------------

	class TempHeartBeat extends Thread implements Runnable {

		private ObjectOutputStream output;
		private Thread blinker;

		public TempHeartBeat(ObjectOutputStream o) {
			this.output = o;
			blinker = currentThread();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {

				while (blinker != null) {
					output.writeObject(MessageFilter.getHeartBeatCode());
					// System.out.println("Sent tmp message");
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				connectionList.clear();
				numTmpCli = 0;
			}

		}

		public void destroyThread() {
			Thread tmpBlinker = blinker;
			blinker = null;
			if (tmpBlinker != null) {

				tmpBlinker.interrupt();
			}
		}
	}

	// --------------------------------------------------------------

	/*
	 * SCENARIO The class is required to temporary check first user's current
	 * connection to the server The class will be garbage collected when other
	 * partner client has been connected and sockets are successfully passed to
	 * OneOnOneChatHandler class
	 */

	class CheckingChatProcess extends Thread implements Runnable {

		public CheckingChatProcess() {
			checkBlinker = currentThread();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (checkBlinker != null) {
				try {
					for (OneOnOneChatHandler l : ooList) {
						if (l.getChatEnded()) {
							System.out.println("Chat has ended: "
									+ ooList.size());

							l.stopThread();
							ooList.remove(l);
						}
					}

					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// --------------------------------------------------------------

	private void stopThread() {
		Thread tmpBlinker = blinker;
	}

	// --------------------------------------------------------------

	public static void main(String[] args) {

		new Server();
	}

	// --------------------------------------------------------------

	private void cliManager() {
		this.getHomeScreen();
		while (true) {

			Scanner scan = new Scanner(System.in);
			String command = scan.nextLine();
			if (command.equals(CommandList.COMMANDS_LIST)) {
				this.getCommandsList();
			}

			else if (command.equals(CommandList.ERASE_ALL)) {

				this.eraseAll();
				this.getHomeScreen();
			} else if (command.equals(CommandList.GLOBAL_MESSAGE)) {

			} else if (command.equals(CommandList.NUMBER_OF_CHATS)) {

			}

			else if (command.equals(CommandList.START_SERVER)) {
				startThread();
				System.out.println("Started the server!");
			}

			else if (command.equals(CommandList.MONITOR_CONNECTION)) {
				this.setMonitoringConnection(true);
			}

			else if (command.equals(CommandList.STOP_MONITORING_CONNECTION)) {
				this.setMonitoringConnection(false);
			}

			// If command doesn't match any
			else {
				System.out
						.println("You have typed in a invalid command. \nPlease retry typing in a new command or see -c for list of commands available");

			}

		}
	}

	// --------------------------------------------------------------

	@Override
	public void eraseAll() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			System.out.println("");
		}
	}

	// --------------------------------------------------------------
	@Override
	public void getNumberOfUsers() {
		// TODO Auto-generated method stub

	}

	// --------------------------------------------------------------

	@Override
	public void getNumberOfOngoingChat() {
		// TODO Auto-generated method stub

	}

	// --------------------------------------------------------------

	@Override
	public void getHomeScreen() {
		// TODO Auto-generated method stub
		this.eraseAll();
		System.out
				.println("..................................................\n"
						+ ".            EC2 Konichiwa project chat          .\n"
						+ ".           SERVER  CURRENTLY RUNNING            .\n"
						+ ".                   JASON SHIN                   .\n"
						+ ".                 -c to see list of commands     .\n"
						+ "..................................................");
	}

	// --------------------------------------------------------------

	@Override
	public void startServer() {
		// TODO Auto-generated method stub

	}

	// --------------------------------------------------------------

	@Override
	public void terminateServer() {
		// TODO Auto-generated method stub

	}

	// --------------------------------------------------------------

	@Override
	public void sendGlobalAnnouncement(String m) {
		// TODO Auto-generated method stub

	}

	// --------------------------------------------------------------

	@Override
	public void getCommandsList() {
		System.out
				.println("\n\n=======================================================================\n"
						+ "1. '-c'  -> browse command list \n"
						+ "2. '-e'  -> erase all from screen\n"
						+ "3. '-s'  -> start server\n"
						+ "4. '-t'  -> terminate server\n"
						+ "5. '-g'  -> send a global message\n"
						+ "6. '-n'  -> see number of chats\n"
						+ "7. '-o'  -> get number of users\n"
						+ "8. '-sm' -> start monitoring connection\n"
						+ "9. '-tm' -> stop monitoring connection\n"
						+ "\n=====================================================================");
	}

	// --------------------------------------------------------------

	// --------------------------------------------------------------

	@Override
	public void monitorConnection(Socket connection) {
		// TODO Auto-generated method stub
		if (monitor) {
			System.out.println("New user connected: \n" + "           IP: "
					+ connection.getLocalSocketAddress() + "\n      PORT: "
					+ connection.getPort());
		}
	}

	// --------------------------------------------------------------

	@Override
	public void stopAllMonitoring() {
		// TODO Auto-generated method stub

	}

	// --------------------------------------------------------------

	@Override
	public void setMonitoringConnection(boolean tf) {
		// TODO Auto-generated method stub
		monitor = tf;
	}

	// --------------------------------------------------------------

}
