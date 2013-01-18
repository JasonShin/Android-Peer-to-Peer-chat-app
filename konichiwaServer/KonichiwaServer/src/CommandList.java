import java.net.Socket;

public interface CommandList {

	public static final String COMMANDS_LIST = "-c";
	public static final String ERASE_ALL= "-e";
	public static final String START_SERVER = "-s";
	public static final String TERMINCATE_SERVER = "-t";
	public static final String GLOBAL_MESSAGE = "-g";
	public static final String NUMBER_OF_CHATS = "-n";
	public static final String NUMBER_OF_USERS = "-o";
	public static final String MONITOR_CONNECTION = "-sm";
	public static final String STOP_MONITORING_CONNECTION = "tm";
	
	public void eraseAll();
	public void getNumberOfUsers();
	public void getNumberOfOngoingChat();
	public void getHomeScreen();
	public void startServer();
	public void terminateServer();
	public void sendGlobalAnnouncement(String m);
	public void getCommandsList();
	public void monitorConnection(Socket connection);
	public void stopAllMonitoring();
	public void setMonitoringConnection(boolean tf);
}
