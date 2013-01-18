import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;

public class FTPHandler {

	private static final String URL = "*********************";
	private static final String FILE_PATH = "******************";

	public void deleteAssociatedFiles(ArrayList<String> l) {

		FTPClient con = null;
		con = new FTPClient();
		try {
			con.connect(URL);
			if (con.login("************", "**********")) {

				for(String file : l){
					boolean deleted = con.deleteFile(FILE_PATH + file);
					System.out.println("Deleted: " + deleted);
				}
				
				con.logout();
				con.disconnect();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
