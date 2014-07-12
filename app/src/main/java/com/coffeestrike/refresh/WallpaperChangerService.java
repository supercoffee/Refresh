package com.coffeestrike.refresh;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class WallpaperChangerService extends Service {
	
	protected static final String TAG = "WallpaperService";
	private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "received screen on broadcast");
			//don't change the wallpaper if the user has disabled it
			if(mSharedPrefs.getBoolean(mAutoChangeKey, true)){
				changeWallpaper();
			}
			
		}
	};


	private SharedPreferences mSharedPrefs;
	private String mAutoChangeKey;	@Override
	public void onDestroy() {
		unregisterReceiver(mBroadCastReceiver);
		super.onDestroy();
	}

	protected void changeWallpaper() {
		
		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				// get the refresh wallpaper directory
				File dir = Environment.getExternalStoragePublicDirectory(Constants.DIRECTORY_REFRESH);
				
				//get a listing of all the files in the dir
				File[] files = dir.listFiles();
				
				//pick a random file from the list
				int rand = new Random().nextInt(files.length);
				File randomFile = files[rand];
				
				DisplayMetrics dm = getResources().getDisplayMetrics();
				
				//decode the file
				Bitmap image = BitmapFactory.decodeFile(randomFile.getPath());
				
				int imgWidth = image.getWidth();
				
				int screenHeight = dm.heightPixels;
				
				int tgtHeight = screenHeight;
				
				int tgtWidth = imgWidth * screenHeight / imgWidth;
				
				Bitmap scaled = Bitmap.createScaledBitmap(image, tgtWidth, tgtHeight, false);
				
				return scaled;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				//set the bitmap as the wallpaper
				WallpaperManager wpm = WallpaperManager.getInstance(getApplicationContext());
				try {
					wpm.setBitmap(result);
				} catch (IOException e) {
					Log.e(TAG, "couldn't set the wallpaper.", e);
				}
			}
			
			
		}.execute();
		
	}
	
	

	@Override
	public void onCreate() {

		super.onCreate();
		
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		mAutoChangeKey = getResources().getString(R.string.pref_key_auto_change);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		registerReceiver(mBroadCastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
