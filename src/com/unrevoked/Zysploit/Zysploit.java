package com.unrevoked.Zysploit;

import java.lang.*;
import android.app.Service;
import android.widget.Toast;
import android.util.Log;
import android.os.Looper;
import android.os.IBinder;
import android.content.Intent;

public class Zysploit extends Service implements Runnable {
	private Thread me;
	
	private void say(String s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void run()
	{
		Looper.prepare();
		
		Log.v("Zysploit", "Inside thread.");
		
		say("Inside thread.");
		
	}
	
	@Override
	public void onStart(Intent i, int startId) {
		Log.v("Zysploit", "Started.");
		
		say("Outisde thread.");
		
		me = new Thread(this, "Zysploit thread");
		me.start();
	}
	
	@Override
	public IBinder onBind(Intent i) {
		Log.v("Zysploit", "Bound.");
		
		return null;
	}
}