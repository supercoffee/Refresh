package com.coffeestrike.refresh.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.coffeestrike.refresh.datatypes.IFLWallpaper;
import com.coffeestrike.refresh.datatypes.Wallpaper;

public class StorageProvider{
	
	public static final int FLAG_NO_OVERWRITE = 0;
	
	public static final String DIR_DOWNLOADS = "Refresh";
	
	private static final long DEFAULT_CACHE_SIZE = 16 * 1024 * 1024;
	
	/*
	 * Sort files by oldest first.
	 */
	private static final Comparator<File> FILEDATECOMPARATOR = new Comparator<File>(){

		@Override
		public int compare(File lhs, File rhs) {
			if(lhs.lastModified() > rhs.lastModified()){
				return 1;
			}
			if(lhs.lastModified() == rhs.lastModified()){
				return 0;
			}
			return -1;
		}
		
		
	};

	private static final String TAG = "StorageProvider";
	
	private Context mContext;
	
	private long mCacheSize;

	public StorageProvider(Context context){
		mContext = context;
		mCacheSize = getCacheSize();
	}
	
	private long getMaxCacheSize(){
		SharedPreferences  prefs = mContext.getSharedPreferences("cache", Context.MODE_PRIVATE);
		return prefs.getLong("cache_size", DEFAULT_CACHE_SIZE);	
	}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public Bitmap getPreviewBitmap(Wallpaper wallpaper){
		String filename = wallpaper.getId();
		
		File dir = getCacheDir();
		File imageFile = new File(dir, filename);
		if(imageFile.exists()){
			InputStream iStream;
			try {
				iStream = new FileInputStream(imageFile);
				return BitmapFactory.decodeStream(iStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		return null;
		
	}

	private File getCacheDir() {
		File dir = mContext.getCacheDir();
		return dir;
	}
	
	/*
	 * Calculate the total size of the image cache
	 */
	private long getCacheSize(){
		File cacheDir = getCacheDir();
		long size = 0;
		
		for(File file : cacheDir.listFiles()){
			size += file.length();
		}

		return size;
	}
	
	/*
	 * Delete the oldest files in the cache.
	 */
	private void clearOldest(int howMany){
		if(howMany < 1){
			return;
		}
		
		File cacheDir = getCacheDir();
		
		File[] listing = cacheDir.listFiles();
		
		if(howMany > listing.length){
			return;
		}
		
		Arrays.sort(listing, FILEDATECOMPARATOR);
		
		for(int i = 0; i < howMany; i++){
			mCacheSize -= listing[i].length();
			listing[i].delete();
		}
		
	}
	

	public Bitmap getBigPreviewBitmap(Wallpaper wallpaper){
		String filename = wallpaper.getId()+ "2x";
		
		File dir = getCacheDir();
		File imageFile = new File(dir, filename);
		if(imageFile.exists()){
			InputStream iStream;
			try {
				iStream = new FileInputStream(imageFile);
				return BitmapFactory.decodeStream(iStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public void saveBigPreviewBitmap(Wallpaper wallpaper, Bitmap image, int ... flags){
		savePreviewBitmap(wallpaper.getId()+"2x", image, flags);
	}
	
	public void savePreviewBitmap(Wallpaper wallpaper, Bitmap image, int ... flags){
		savePreviewBitmap(wallpaper.getId(), image, flags);
	}
	
	private void savePreviewBitmap(String filename, Bitmap image, int ... flags){
		if(mCacheSize > getMaxCacheSize()){
			clearOldest(10);
		}
		
		File dir = getCacheDir();
		File imageFile = new File(dir, filename);
		FileOutputStream fout = null;
		/*
		 * Don't overwrite existing files if the caller specifies against it
		 */
		if(flags != null && flags[0] == FLAG_NO_OVERWRITE && imageFile.exists()){
			return;
		}
		
		try{
			fout = new FileOutputStream(imageFile);
			image.compress(Bitmap.CompressFormat.JPEG, 100, fout);
			
			/*
			 * update the image cache size.
			 * It doesn't have to be exact, just reasonably accurate.
			 */
			mCacheSize += imageFile.length();
		}
		catch(IOException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
			Log.e(TAG, "Null image", e);
		}
		finally{
			if(fout != null){
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
