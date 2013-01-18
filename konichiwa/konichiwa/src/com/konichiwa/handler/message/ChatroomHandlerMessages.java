package com.konichiwa.handler.message;

public class ChatroomHandlerMessages {
	private final String successMessage = "SUCCS";
	private final String disconnectMessage = "FAILD";
	private final String showStartButMessage = "START";
	private final String showFinishButMessage = "FINIS";

	public String getSuccessMessage() {
		return successMessage;
	}
	public String getDisconnectMessage() {
		return disconnectMessage;
	}
	public String getShowStartButMessage() {
		return showStartButMessage;
	}
	public String getShowFinishButMessage() {
		return showFinishButMessage;
	}

	public String filterHandlerMessage(String m) {
		return m.substring(6, m.length());
	}

}
