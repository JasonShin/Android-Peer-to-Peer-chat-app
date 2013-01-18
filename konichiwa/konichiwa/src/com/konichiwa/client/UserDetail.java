package com.konichiwa.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class UserDetail {
	private static final String TIME_ZONE = "Australia/Sydney";
	private static Context context;

	public UserDetail(Context c) {
		context = c;
	}

	public boolean wifiAvailability() {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

	public String getCountry() {

		String countryResult = "Your wifi is not connected";

		if (wifiAvailability()) {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder;
			Document doc = null;

			try {
				builder = domFactory.newDocumentBuilder();
				doc = builder.parse("http://freegeoip.net/xml/");
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			if (doc != null) {
				XPath xpath = XPathFactory.newInstance().newXPath();
				countryResult = getNodeNameAndValue(doc, xpath);
			}
		}
		
		return countryResult;
	}

	private static String getNodeNameAndValue(Document doc, XPath xpath) {
		XPathExpression expr;
		Object result = null;

		try {
			expr = xpath.compile("//Response/CountryName//text()");

			result = expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		NodeList nodes = (NodeList) result;
		return nodes.item(0).getNodeValue();

	}

	public static long getAusFullTime() {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(TIME_ZONE));
		Date d = cal.getTime();
		long secsInMill = d.getTime() / 1000;
		//String time = String.format("%d:%02d:%02d", hour12, minutes, seconds);
		return secsInMill;
	}

	public static String getTime() {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(TIME_ZONE));
		Date d = cal.getTime();
		int hour = d.getHours();
		int minutes = d.getMinutes();
		int seconds = d.getSeconds();
		String time = String.format("%d:%02d:%02d", hour, minutes, seconds);
		return time;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("foo", ex.toString());
		}
		return null;
	}

	public static String getMacAddress() {
		WifiManager manager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
		WifiInfo wifiInfo = manager.getConnectionInfo();
		String mac = wifiInfo.getMacAddress();
		return mac;
	}

}
