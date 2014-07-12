package com.coffeestrike.refresh;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RefreshApplication extends Application {

    private static Context sInstance;

	private BroadcastReceiver mDownloadBcastReceiver
		= new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
					/*
					 * Create an entry in the database to indicate that the user has downloaded
					 * this wallpaper.
					 */
					
					
				}
				else if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
					
					
				}
			}
		};


	@Override
	public void onCreate() {
		super.onCreate();
        sInstance = this;

//		IntentFilter filter = new IntentFilter();
//		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//		filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
//		registerReceiver(mDownloadBcastReceiver, filter);
		
		RefreshDownloadManager.init(this);
		
		startService(new Intent(this, WallpaperChangerService.class));
	}
	
	public static Context getContext(){
        return sInstance;
    }

}
