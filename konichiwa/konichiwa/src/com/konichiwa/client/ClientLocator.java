package com.konichiwa.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class ClientLocator {

	private static final String WEB_SITE = "http://whatismyipaddress.com";


	public ClientLocator() {

	}
	
	// --------------------------------------------------------------------------------------------------

	public String getCountry() throws Exception {
		String theCountry = "";
		String HTML = this.getInternetData();
		Log.e("main", "The HTML: \n" + HTML);
		//FINDS THE STRIGHT BESIDES (.*?)
		//Pattern pattern = Pattern.compile( "Country:</th><td>(.*?)<img src=\"http://cdn.whatismyipaddress.com");
		Pattern pattern = Pattern.compile("Country:</th><td>(.*?)</td></tr>");
		Matcher m = pattern.matcher(HTML);
		if( m.find() ) {
		    theCountry = m.group(1);
		   
		} else {
			theCountry = "Unknown";
		}
		Log.e("main", "The country: " + theCountry);
		return theCountry;
	}
	
	// --------------------------------------------------------------------------------------------------
	public String getInternetData() throws Exception {
		BufferedReader in = null;
		String stuffs = "";
		try {
			HttpClient cl = new DefaultHttpClient();
			URI website = new URI(WEB_SITE);
			HttpGet request = new HttpGet();
			request.setURI(website);
			HttpResponse response = cl.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("line.separator");
			while ((l = in.readLine()) != null) {
				sb.append(l + nl);
			}
			
			in.close();
			stuffs = sb.toString();
			return stuffs;
		} finally {
			if (in != null) {
				try {
					in.close();
					return stuffs;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	// --------------------------------------------------------------------------------------------------
}
