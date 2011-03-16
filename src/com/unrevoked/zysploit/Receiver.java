/* Receiver.java: Broadcast intent receiver for 'am' commandline start.
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
