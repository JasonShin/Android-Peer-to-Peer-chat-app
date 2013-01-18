public class MessageFilter {
	private static final String IMAGE_FILE_CODE = "!!!!!!";
	private static final String HEART_BEAT_CODE = "@@@@@@";
	private static final String NORMAL_MESSAGE_CODE = "######";
	private static final String CONNECTION_SUCCESS = "%%%%%%";
	private static final String OPPONENT_PROFILE_CODE = "$$$$$$";
	private static final String FINISH_CODE = HEART_BEAT_CODE+"FINISH";

	public static String getImageFileCode() {
		return IMAGE_FILE_CODE;
	}

	public static String getHeartBeatCode() {
		return HEART_BEAT_CODE;
	}

	public static String getNormalMessageCode() {
		return NORMAL_MESSAGE_CODE;
	}
	
	public static String getFinishCode() {
		return FINISH_CODE;
	}

	public static String setImageFileMessage(String m) {
		return IMAGE_FILE_CODE + m;
	}

	public static String setHeartBeatMessage(String m) {
		return HEART_BEAT_CODE + m;
	}

	public static String setNormalMessage(String m) {
		return NORMAL_MESSAGE_CODE + m;
	}
	
	public static String setConnectionSucceedMessage(String m){
		return CONNECTION_SUCCESS + m;
	}
	
	public static String getOpponentProfileCode() {
		return OPPONENT_PROFILE_CODE;
	}

	public static String filterMessage(String m){
		return m.substring(6, m.length());
	}
}
