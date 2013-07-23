package com.example.testdca;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Sender extends Activity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sender);
		
		Button fall = (Button)findViewById(R.id.btFall);
		Button assist = (Button)findViewById(R.id.btAssist);
		Button gpsFall = (Button)findViewById(R.id.btGpsFall);
		Button gpsAssist = (Button)findViewById(R.id.btGpsAssist);

		fall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Enviador().execute("caida");
			}
		});
		
		assist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Enviador().execute("asistencia");
			}
		});
		
		gpsFall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocationManager lM =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Posicionador l = new Posicionador("caidaGPS");
				lM.requestLocationUpdates( LocationManager.GPS_PROVIDER, 5000, 10, l);
			}
		});
		
		gpsAssist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocationManager lM =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Posicionador l = new Posicionador("asistenciaGPS");
				lM.requestLocationUpdates( LocationManager.GPS_PROVIDER, 5000, 10, l);
		        
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sender, menu);
		return true;
	}
	
	private class Posicionador implements LocationListener{
		
		private String longitud, latitud, tipo;
		
		public Posicionador(String tipo){
			this.tipo = tipo;
		}
		
		@Override
		public void onLocationChanged(Location loc) {
			
	        longitud = Double.toString(loc.getLongitude());
	        latitud = Double.toString(loc.getLatitude());
	        final TextView tv_View = (TextView)findViewById(R.id.textView1);
	        tv_View.setText(longitud+" - "+latitud);
			new Enviador().execute(this.tipo, latitud, longitud);
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
		public String getLongitud(){
			return this.longitud;
		}
		public String getLatitud(){
			return this.latitud;
		}
	}
	
}
