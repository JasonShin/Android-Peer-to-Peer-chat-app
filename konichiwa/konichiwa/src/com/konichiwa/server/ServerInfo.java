package com.konichiwa.server;

public class ServerInfo {

	static final String serverIP = "*****************";
	//static final String serverIP = "****************";
	//for the public network
	static final int port = 443;
	//static final int port = 53321;
	private static final String SERVER_IMAGE_PATH = "***********";

	
	public static String getServerIP() {
		return serverIP;
	}

	public static int getPort() {
		return port;
	}


	public static String getServerImagePath() {
		return SERVER_IMAGE_PATH;
	}
	
}
