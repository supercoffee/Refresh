package com.coffeestrike.refresh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class EventReceiver extends BroadcastReceiver{

	private static final String TAG = "EventReceiver";

	@Override
	public void onReceive(Context context, Intent startIntent) {
				
			
		Log.i(TAG, "received boot complete broadcast");
		
		Intent intent = new Intent(context, WallpaperChangerService.class);
		context.startService(intent);
		
	}
	
	

}
