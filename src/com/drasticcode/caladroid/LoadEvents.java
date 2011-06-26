package com.drasticcode.caladroid;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class LoadEvents extends AsyncTask<Boolean, String, JsonArray> {
	public String responseString;
	public Activity mContext;
	public Boolean error = false;
	private SimpleDateFormat dateWriter = new SimpleDateFormat( "yyyy-MM-dd");

	protected void onPostExecute(JsonArray result) {
		if ( error ) {
			mContext.setContentView(R.layout.error);
		} else {
			Intent i = new Intent(mContext,UpcomingActivity.class);
			mContext.startActivity(i);
			mContext.finish();
		}
	}

	protected JsonArray doInBackground(Boolean... params) {
		System.out.println(params);
		boolean skipCache = false;
        for (Boolean b : params) {
        	skipCache = b;
        }

		Date lastCached = getlastCachedDate();
		Date today = new Date();
		
		if ( !skipCache && dateWriter.format(today).equals(dateWriter.format(lastCached))) {
			System.out.println("loading from cache");
			loadEventsFromCache();
		} else {
			System.out.println("loading from web");
			loadEventsFromWeb();
		}

		Gson gson = new Gson ();
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(responseString).getAsJsonArray();
		UpcomingActivity.events = array;
		return array;
	}

	private boolean loadEventsFromCache() {
		
		try {
			FileInputStream cachedResponseFh = mContext.openFileInput("events.json");
			try {
				StringBuffer strContent = new StringBuffer("");
				int ch;
				while( (ch = cachedResponseFh.read()) != -1)
					strContent.append((char)ch);
				responseString = new String(strContent);
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cachedResponseFh.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private Date getlastCachedDate() {
		Date lastCached = new Date(0);
		try {
			FileInputStream fh = mContext.openFileInput("cached_at.txt");
			try {
				StringBuffer strContent = new StringBuffer("");
				int ch;
				while( (ch = fh.read()) != -1)
					strContent.append((char)ch);
				
				lastCached = dateWriter.parse(new String(strContent));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fh.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastCached;
	}

	private void loadEventsFromWeb() {
		error = false;
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DATE,20);
		String format = dateWriter.format(c1.getTime());
		String uri = "http://calagator.org/events.json?date[end]=".concat(format);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = httpclient.execute(new HttpGet(uri));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else{
				//Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			error = true;
		} catch (IOException e) {
			error = true;
		}

		saveResponseString();
	}

	private void saveResponseString() {
		try {
			FileOutputStream fh = mContext.openFileOutput("events.json", 0);
			fh.write(responseString.getBytes());
			fh.close();
			FileOutputStream fh1 = mContext.openFileOutput("cached_at.txt", 0);
			fh1.write(dateWriter.format(Calendar.getInstance().getTime()).getBytes());
			fh1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


