package com.example.testdca;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Sender extends Activity implements SensorEventListener{

	public double ax,ay,az;
	public double a_norm;
	public int i=0;
	static int BUFF_SIZE=50;
	static public double[] window = new double[BUFF_SIZE];
	double sigma=0.5,th=10,th1=5,th2=2;
	private SensorManager sensorManager;
	public static String curr_state,prev_state;





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
		initialize();
		sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
		
	}

	private void initialize() {
		for(i=0;i<BUFF_SIZE;i++){
			window[i]=0;
		}
		prev_state="none";
		curr_state="none";
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

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			ax=event.values[0];
			ay=event.values[1];
			az=event.values[2];
			AddData(ax,ay,az);
			posture_recognition(window,ay);
			fallDetection();
			SystemState(curr_state,prev_state);
			if(!prev_state.equalsIgnoreCase(curr_state)){
				prev_state=curr_state;
			}

		}		
	}

	private void fallDetection() {
		double[] minmax = minmax();
		if(minmax[1]-minmax[0] > 2 * android.hardware.SensorManager.GRAVITY_EARTH){
			curr_state = "fall";
		}
	}

	private void posture_recognition(double[] window2,double ay2) {
		// TODO Auto-generated method stub
		int zrc=compute_zrc(window2);
		if(zrc==0){

			if(Math.abs(ay2)<th1){
				curr_state="sitting";
			}else{
				curr_state="standing";
			}

		}else{

			if(zrc>th2){
				curr_state="walking";
			}else{
				curr_state="none";
			}

		}



	}
	private int compute_zrc(double[] window2) {
		// TODO Auto-generated method stub
		int count=0;
		for(i=1;i<=BUFF_SIZE-1;i++){

			if((window2[i]-th)<sigma && (window2[i-1]-th)>sigma){
				count=count+1;
			}

		}
		return count;
	}
	
	
	private void SystemState(String curr_state1,String prev_state1) {

		if(!prev_state1.equalsIgnoreCase(curr_state1)){
			if(curr_state1.equalsIgnoreCase("fall")){
				Toast.makeText(Sender.this,"FALL DETECTED!!!!!" ,Toast.LENGTH_LONG).show();
			}
		}


	}
	
	
	private void AddData(double ax2, double ay2, double az2) {
		// TODO Auto-generated method stub
		a_norm=Math.sqrt(ax*ax+ay*ay+az*az);
		for(i=0;i<=BUFF_SIZE-2;i++){
			window[i]=window[i+1];
		}
		window[BUFF_SIZE-1]=a_norm;

	}

	private double[] minmax(){
		double[] result = {1000,0};

		for(double num : window){
			if(num < result[0]){
				result[0]=num;
			}
			if(num > result[1]){
				result[1] = num;
			}
		}

		return result;
	}

}
