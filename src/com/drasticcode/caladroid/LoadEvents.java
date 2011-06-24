package com.drasticcode.caladroid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class LoadEvents extends AsyncTask<String, String, JsonArray> {
	public String responseString;
	public Activity mContext;


	protected void onPostExecute(JsonArray result) {

		Intent i = new Intent(mContext,UpcomingActivity.class);
		mContext.startActivity(i);
		mContext.finish();
	}

	@Override
	protected JsonArray doInBackground(String... params) {

		SimpleDateFormat writer = new SimpleDateFormat( "yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DATE,20);
		String format = writer.format(c1.getTime());
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
			// FIXME: These need to run on UI thread
			Toast.makeText(mContext, "Couldn't load events", Toast.LENGTH_LONG);
		} catch (IOException e) {
			// FIXME: These need to run on UI thread
			Toast.makeText(mContext, "Couldn't load events", Toast.LENGTH_LONG);
		}
		Gson gson = new Gson ();
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(responseString).getAsJsonArray();
		UpcomingActivity.events = array;

		return array;
	}
}


