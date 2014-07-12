package com.coffeestrike.refresh.datatypes;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.coffeestrike.refresh.datatypes.DataSchema.DownloadKeys;



public class WallpaperDownload extends WrappedData implements DownloadKeys{

	
	public static final Parcelable.Creator<WallpaperDownload> CREATOR 
		= new Creator<WallpaperDownload>() {
			
			@Override
			public WallpaperDownload[] newArray(int size) {
				return new WallpaperDownload[size];
			}
			
			@Override
			public WallpaperDownload createFromParcel(Parcel source) {
				return new WallpaperDownload(source);
			}
		};
	
	
	public WallpaperDownload(Parcel source) {
		super(source);
	}
	
	public WallpaperDownload(JSONObject obj){
		super(obj);
	}

	public String getDownloadUrl(){
		return getString(DOWNLOAD_URL);
	}
	
	public String getFileName(){
		return getString(FILENAME);
	}
	
	public String getWallpaperId(){
		return getString(WALLPAPER_ID);
	}
	
	@Override
	public String toString(){
		return getDownloadUrl();
	}


}
