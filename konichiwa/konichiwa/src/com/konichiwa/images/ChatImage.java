package com.konichiwa.images;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;

import org.apache.commons.net.ftp.FTPClient;

import com.konichiwa.client.UserDetail;

public class ChatImage {

	Context context;
	private int position;
	private static final String url = "jasondatabase.uphero.com";
	private static final String FILE_PATH = "/public_html/konimages/";
	private Bitmap bmp;

	public ChatImage(Context c, Bitmap b, int p) {

		context = c;
		position = p;

		bmp = b;
	}
	
	

	public String uploadFile() {
		// TODO Auto-generated method stub

		FTPClient con = null;
		String fileName = "";
		try {
			con = new FTPClient();
			con.connect(url);
			if (con.login("a9576965", "ftppassword123")) {
				con.enterLocalPassiveMode();
				con.setFileType(FTP.BINARY_FILE_TYPE); //  Very Important
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				Bitmap tmpbmp = bmp;
				tmpbmp.compress(CompressFormat.JPEG, 75, bos);
				byte[] byteData = bos.toByteArray();
				ByteArrayInputStream bai = new ByteArrayInputStream(byteData);
				
				String refinedTime = Long.toString(UserDetail.getAusFullTime());
				String refinedIP = UserDetail.getLocalIpAddress().replaceAll("[.]", "");
				//fileName = refiledTime + "_" + refinedIP + ".jpg";
				FTPFile[] f = con.listFiles(FILE_PATH);
				
				Log.i("wi", "List files: " + f.length + "\nlist names: " + con.listNames());
				fileName = "img_" + f.length + "_" + refinedTime + "_" + refinedIP;
				con.storeFile(FILE_PATH + fileName, bai);

				bai.close();

				con.logout();
				con.disconnect();
			}
		} catch (Exception e) {
			Log.i("wi", "Exception while uploading: " + e);
		}
		return fileName;
	}

	public Bitmap getBigImage() {
		return bmp;
	}

	public Bitmap getCompressedImage() {

		final int REQUIRED_SIZE = 50;
		Bitmap bigTmp = bmp;
		Bitmap tmpbmp = bmp;

		int width_tmp = tmpbmp.getWidth(), height_tmp = tmpbmp.getHeight();
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
		}
		tmpbmp = Bitmap.createScaledBitmap(bigTmp, width_tmp, height_tmp, true);

		return tmpbmp;

	}

	public int getPosition() {
		return position;
	}

	
}
