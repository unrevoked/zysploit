package com.unrevoked.zysploit;

import java.lang.*;
import android.app.Service;
import android.app.IntentService;
import android.widget.Toast;
import android.util.Log;
import android.os.Looper;
import android.os.IBinder;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.Resources;
import android.content.Context;

public class Receiver extends BroadcastReceiver {
	@Override
	public void onReceive (Context context, Intent intent)
	{
		android.content.ComponentName cn;
		
		cn = context.startService(new Intent("com.unrevoked.zysploit"));
		if (cn == null)
			Log.e("Zysploit:broadcast", "Failed to start main Zysploit service.");
		else
			Log.v("Zysploit:broadcast", "Started service"+cn.toString()+".");
	}
}
