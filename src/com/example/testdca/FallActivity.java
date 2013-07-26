package com.example.testdca;

import java.util.Timer;
import java.util.TimerTask;

import com.example.testdca.Sender;
import com.example.testdca.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FallActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fall_detected);
		
		final Timer timer = new Timer();
		
		
		final TextView t = (TextView)findViewById(R.id.editText1);
		final Button changebutton = (Button)findViewById(R.id.button1);
		timer.schedule(new Alarm(timer,changebutton), 0,1000);
        changebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				timer.cancel();
				timer.purge();
				Intent myIntent = new Intent(v.getContext(), Sender.class);
                startActivityForResult(myIntent, 0);
			}
		});
        
	}
	
	class Alarm extends TimerTask{

		final Timer timer;
		final Button b;
		
		public Alarm(Timer t, Button b){
			this.timer = t;
			this.b = b;
		}
		
		@Override
		public void run() {
			FallActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	TextView t = (TextView)findViewById(R.id.editText1);
                   int contador = Integer.valueOf(t.getText().toString());
                   t.setText(String.valueOf(--contador));
                   if(contador == 0){
                	   b.setEnabled(false);
                	   Toast.makeText(FallActivity.this,"Se ha avisado a su contacto de emergencia" ,Toast.LENGTH_LONG).show();
                	   new Enviador().execute("caida");
                	   timer.cancel();
                	   timer.purge();
                	   Intent myIntent = new Intent(FallActivity.this,Sender.class);
                       startActivityForResult(myIntent, 0);
                   }
                }
            });
		}
		
	}
	
}
