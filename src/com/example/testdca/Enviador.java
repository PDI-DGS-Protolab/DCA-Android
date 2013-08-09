package com.example.testdca;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class Enviador extends AsyncTask<String, Void, Void>{

	

	@Override
	protected Void doInBackground(String... arg0) {
	
		
		// Creating HTTP client
        HttpClient httpClient = new DefaultHttpClient();
        // Creating HTTP Post
        HttpPost httpPost = new HttpPost(
                "http://int.dca.tid.es/idas/2.0?apikey=79dv4pktqhrka9c40bectah0o2&ID=device2");
        
        // Building post parameters
        // key and value pair
        try {
        	if(arg0[0].equals("caida")){
        		httpPost.setEntity(new StringEntity("|||urn:x-ogc:def:phenomenon:ehealthDemo:1.0:caida||c|1"));
        	}else if(arg0[0].equals("asistencia")){
        		httpPost.setEntity(new StringEntity("|||urn:x-ogc:def:phenomenon:ehealthDemo:1.0:asistencia||a|0"));
        	}else if(arg0[0].equals("caidaGPS")){
        		httpPost.setEntity(new StringEntity("|||urn:x-ogc:def:phenomenon:ehealthDemo:1.0:caida|pgps|"+arg0[1]+"|"+arg0[2]+"|c|0"));
        	}else if(arg0[0].equals("asistenciaGPS")){
        		httpPost.setEntity(new StringEntity("|||urn:x-ogc:def:phenomenon:ehealthDemo:1.0:asistencia|pgps|"+arg0[1]+"|"+arg0[2]+"|a|1"));
        	}else{
        		System.out.println("Invalid Event Name");
        	}

            HttpResponse response = httpClient.execute(httpPost);
            Log.d("Http Response:", response.toString());
        
	        // Let's also send the location alone, otherwise DCA does not update location for the device
	        if(arg0[0].equals("caidaGPS") || arg0[0].equals("asistenciaGPS")) {
	        	httpPost.setEntity(new StringEntity("|||8:27||gps|"+arg0[1]+"|"+arg0[2]));
	            response = httpClient.execute(httpPost);
	            Log.d("Http Response:", response.toString());
	    	} else {
	    		Log.d("Enviador", "No need to send location");
	    	}
        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			Log.e("Enviador", "Some encoding exception", e1);
	    } catch (IOException e) {
	        // writing exception to log
	        e.printStackTrace();
	    }

		return null;
	}
	protected void onPostExecute() {

		
    }
	
}
