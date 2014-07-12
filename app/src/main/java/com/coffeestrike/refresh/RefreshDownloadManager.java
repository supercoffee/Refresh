package com.coffeestrike.refresh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;

import com.coffeestrike.refresh.api.API;
import com.coffeestrike.refresh.datatypes.WallpaperDownloadRequest;

public class RefreshDownloadManager {
	
	public interface Listener{
		public void onDownloadCompleted(WallpaperDownloadRequest completedDownload);
	}

	private static final String DOWNLOADS_REM = "downloads_remaining";
	
	private static final String LAST_DOWNLOAD = "last_download_time";

	private static final int DEFAULT_LIMIT = 5;
	
	private static RefreshDownloadManager sInstance;
	
	/**
	 * Initialize the Download manager.
	 * 
	 * This only needs to be done once during the lifetime
	 * of the app. 
	 * @param context
	 */
	public static void init(Context context){
		sInstance = new RefreshDownloadManager(context);
		
		IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		
		sInstance.mContext.registerReceiver(sInstance.mReceiver, filter);
		
		sInstance.mDownloadManager = (DownloadManager) sInstance.mContext
				.getSystemService(Context.DOWNLOAD_SERVICE);
	}
	
	private Context mContext; 
	
	private List<RefreshDownloadManager.Listener> mListeners;
	
	private Map<Long, WallpaperDownloadRequest> mDownloadsInProgress;
	
	private DownloadManager mDownloadManager;
	
	private SharedPreferences mDownloadPrefs;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			final long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			
			if(downloadId != 0){
				if(mDownloadsInProgress.containsKey(downloadId)){
					final WallpaperDownloadRequest finished = mDownloadsInProgress.get(downloadId);
					
					if(finished.isSetWhenFinished()){
						
						setWallpaper(downloadId);
						
					}
					
					new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... arg0) {

							return null;
						}
						
						@Override
						protected void onPostExecute(Void arg){

							//alert all listeners
							notifyListeners(finished);
							//remove the download
							mDownloadsInProgress.remove(downloadId);
							
						}
						
					};//.execute();
					
					
				}
			}
		}
	};

	private RefreshDownloadManager(Context context) {
		mContext = context;
		mListeners = new ArrayList<Listener>(); 
		mDownloadsInProgress = new ArrayMap<Long,WallpaperDownloadRequest>();
		mDownloadPrefs = context.getSharedPreferences("downloads", Context.MODE_PRIVATE);
	}
	
	
	/**
	 * Register a new listener for completed wallpaper downloads.
	 * When the wallpaper is finished downloading, listeners will
	 * be notified and provided with the WallpaperDownload object 
	 * associated with the download.
	 * @param listener
	 */
	public static void registerListener(Listener listener){
		
		if(! sInstance.mListeners.contains(listener)){
			sInstance.mListeners.add(listener);
		}
		
	}
	
	/**
	 * Unregister a listener.
	 * Unregistering a listener means that the specified listener will
	 * no longer be notified when a download completes.
	 * Listeners may be re-registered at any time. 
	 * @param listener
	 */
	public static void unregisterListener(Listener listener){
		
		if(sInstance.mListeners.contains(listener)){
			sInstance.mListeners.remove(listener);
		}
		
	}
	
	public static boolean requestDownload(WallpaperDownloadRequest downloadRequest, API api){
		
		//don't enqueue downloads twice
		if(! sInstance.mDownloadsInProgress.containsValue(downloadRequest)){
			
			Uri downloadUri = Uri.parse(downloadRequest.getWallpaper().getDownloadUrl());
			Request request = new  Request(downloadUri);
			request.setTitle(downloadRequest.getWallpaper().getTitle());
			
			File dir = Environment.getExternalStoragePublicDirectory(Constants.DIRECTORY_REFRESH);
			if(!dir.exists()){
				
				dir.mkdir();
				
			}
			
			request.setDestinationInExternalPublicDir(Constants.DIRECTORY_REFRESH, 
					downloadRequest.getWallpaper().getId()+".jpg");
			
			long id = sInstance.mDownloadManager.enqueue(request);
			
			sInstance.mDownloadsInProgress.put(id, downloadRequest);
			
			sInstance.decrementDownloads();
			
			return true;
			
		}
		
		return false;
	}
	
	/*
	 * Alert all listeners to the new arrival of a new wallpaper download.
	 */
	private void notifyListeners(WallpaperDownloadRequest completedDownload){
		
		for(Listener l: mListeners){
			
			l.onDownloadCompleted(completedDownload);
			
		}
		
	}
	
	@SuppressLint("NewApi")
	private void setWallpaper(long downloadId){
		
		WallpaperManager wpm = WallpaperManager.getInstance(mContext);
		Uri uri = null;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			 uri = mDownloadManager.getUriForDownloadedFile(downloadId);
		}
		else{
			DownloadManager.Query query  = new DownloadManager.Query();
			query.setFilterById(downloadId);
			
			Cursor cursor = mDownloadManager.query(query);
			
			if(cursor.getCount() != 1){
				return;
			}
			else{
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
				uri = Uri.parse(cursor.getString(columnIndex));
			}
			
		}
		
		
		try {
			Bitmap bm = BitmapFactory.decodeFile(uri.getPath());
			
			DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
			int imgWidth = bm.getWidth();
			
			int screenHeight = dm.heightPixels;
			
			int tgtHeight = screenHeight;
			 
			int tgtWidth = imgWidth * screenHeight / imgWidth;
			
			//scale the bitmap to fit the height of the screen
			Bitmap scaled = Bitmap.createScaledBitmap(bm, tgtWidth, tgtHeight, false);
			
			wpm.setBitmap(scaled);
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e){
			e.printStackTrace();
		}
		
	}
	
	public static int getDownloadsRemaining(){
		SharedPreferences prefs = sInstance.mDownloadPrefs;
		long lastDownload = prefs.getLong(LAST_DOWNLOAD, 0);
		
		//Reset download limit if more than 24 hours have passed since last download
		if(System.currentTimeMillis() > lastDownload + 24 * 60 * 60 * 1000){
			
			prefs.edit().putInt(DOWNLOADS_REM, DEFAULT_LIMIT).commit();
			
		}
		
		int remaining = prefs.getInt(DOWNLOADS_REM, DEFAULT_LIMIT);
		
		return remaining;
		
	}
	
	private void decrementDownloads(){
		long time = System.currentTimeMillis();
		int downloads = mDownloadPrefs.getInt(DOWNLOADS_REM, DEFAULT_LIMIT);
		
		mDownloadPrefs.edit().putLong(LAST_DOWNLOAD, time)
			.putInt(DOWNLOADS_REM, --downloads).commit();
		
	}


}
