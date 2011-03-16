/* AsRoot.java: helper bits running, theoretically, as root.
 * Part of Zysploit, an exploit for unchecked setuid() in Android Zygote
 * Copyright (C) 2011 Joshua Wise and the unrevoked Development Team
 * 
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program, contained in the file LICENSE.  If not, see
 * <http://www.gnu.org/licenses/>.
 * 
 * Additionally, this program may also be licensed by the unrevoked
 * Development Team for other purposes.
 */

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

public class AsRoot extends IntentService {
	private final int EXTRACTBUF_SIZE = 1024;
	
	private void say(String s)
	{
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}
	
	private void runhelper()
	{
		Process p;
		String home = getApplicationContext().getFilesDir().getAbsolutePath();
		BufferedReader reader;
		String s;
		
		Log.v("Zysploit:AsRoot", "Starting helper, hopefully as root.");
		
		try {
			p = Runtime.getRuntime().exec(home+"/helper root");
		} catch (Exception e) {
			Log.e("Zysploit:AsRoot", "Failed to exec helper.", e);
			return;
		}
		
		try {
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((s = reader.readLine()) != null)
			{
				switch (s.charAt(0))
				{
				case 'L':
					Log.v("Zysploit:AsRoot", "Helper: "+(s.substring(1)));
					break;
				case 'E':
					Log.e("Zysploit:AsRoot", "Helper error: "+(s.substring(1)));
					break;
				default:
					Log.e("Zysploit:AsRoot", "Unknown helper string: "+s);
					break;
				}
			}
		} catch (Exception e) {
			Log.e("Zysploit:AsRoot", "Unexpected IOException reading from helper.", e);
			return;
		}
		
		Log.v("Zysploit:AsRoot", "Helper done.");
	}
	
	@Override
	protected void onHandleIntent(Intent i)
	{
		Log.v("Zysploit:AsRoot", "Inside main service thread.");
		
		runhelper();
		
		Log.v("Zysploit:AsRoot", "Main service thread done.");
	}
	
	@Override
	public void onStart(Intent i, int startId)
	{
		super.onStart(i, startId);
		
		Log.v("Zysploit:AsRoot", "Started.");
	}
	
	@Override
	public IBinder onBind(Intent i)
	{
		super.onBind(i);
		
		Log.v("Zysploit:AsRoot", "Bound.");
		
		return null;
	}
	
	public AsRoot(String s)
	{
		super(s);
	}
	
	public AsRoot()
	{
		super("");
	}
}
