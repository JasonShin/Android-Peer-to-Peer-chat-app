package com.konichiwa.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class MySql {

	private static final String PATH = "***********************";
	private static final int TIMEOUT_MILLISEC = 10000;
	private static final String GET_SERVER_INFO = "my_query_server_info.php";

	public MySql() {
	}
	// --------------------------------------------------------------------------------------------------

	public String[] getServerData() {
		String[] info = new String[2];

		InputStream is = requestQuery(GET_SERVER_INFO, null);
		String sresult = this.responseToString(is);

		JSONArray jArray;
		try {
			jArray = new JSONArray(sresult);

			JSONObject jo = jArray.getJSONObject(0);

			info[0] = jo.getString("SERVER_IP").toString();
			info[1] = jo.getString("PORT").toString();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i("sql", "Error found server detail: " + e);
		}
		return info;
	}

	// --------------------------------------------------------------------------------------------------

	private InputStream requestQuery(String FILENAME, JSONObject json) {
		InputStream is = null;
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			HttpClient client = new DefaultHttpClient(setHttpParams());
			HttpPost request = new HttpPost(PATH + FILENAME);
			if (json != null) {
				request.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));
				request.setHeader("json", json.toString());
			} else {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			if (entity != null) {
				is = entity.getContent();
			}
		} catch (Throwable t) {
			Log.e("rq", "The request has failed :( = " + t.toString());
		}
		return is;
	}

	// --------------------------------------------------------------------------------------------------

	private String responseToString(InputStream is) {
		String result = "";
		try {
			//========= convert response to string ========= //
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			is.close();
			result = sb.toString();
			Log.i("s", "rsult: " + result);
		} catch (Exception e) {
			Log.i("s", "Error converting result " + e.toString());
		}
		return result;
	}

	// --------------------------------------------------------------------------------------------------

	private HttpParams setHttpParams() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		return httpParams;
	}

	// --------------------------------------------------------------------------------------------------

}
