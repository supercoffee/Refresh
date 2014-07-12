package com.coffeestrike.refresh.datatypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.coffeestrike.refresh.datatypes.DataSchema.WallpaperKeys;


/**
 * An wrapper for the data representing a wallpaper.
 * 
 * @author Benjamin Daschel
 *
 */
public class IFLWallpaper extends WrappedData implements WallpaperKeys, Wallpaper{


	public static final Parcelable.Creator<IFLWallpaper> CREATOR 
		= new Creator<IFLWallpaper>() {
			
			@Override
			public IFLWallpaper[] newArray(int size) {
				return new IFLWallpaper[size];
			}
			
			@Override
			public IFLWallpaper createFromParcel(Parcel source) {
				return new IFLWallpaper(source);
			}
		};

	private long mDownloadId;
	
	public IFLWallpaper(Parcel in) {
		super(in);
		mDownloadId = in.readLong();
	}
	
	public IFLWallpaper(JSONObject obj){
		super(obj);
	}

	@Override
	public void writeToParcel(Parcel out, int flags){
		super.writeToParcel(out, flags);
		out.writeLong(mDownloadId);
	}
	
	public List<Resolution> getAvailableResolutions(){
		List<Resolution> resList = new ArrayList<Resolution>();
		
		for(String s: getStringList(AVAILABLE_RESOLUTIONS)){
			resList.add(new Resolution(s));
		}
		return resList;
	}
	
	public String getBigPreviewUrl(){
		return (String) getString(BIG_PREVIEW_URL);
	}
	
	public String getPreviewUrl(){
		return (String) getString(PREVIEW_URL);
	}

	
	public String getTitle(){
		return (String) getString(TITLE);
	}
	
	public String getWallpaperId(){
		return getString(ID).toString();
	}

	public String getArtistName() {
		return (String) getString(USER_NAME);
	}
	
	public String getDescription(){
		return (String) getString(DESCRIPTION);
	}
	
	public String getIflUrl(){
		return (String) getString(URL_IFL);
	}
	
	public long getDownloadId(){
		return mDownloadId;
	}
	
	public void setDownloadId(long downloadId){
		mDownloadId = downloadId;
	}
	
	@Override 
	public int hashCode(){
		return getWallpaperId().hashCode();
	}

	@Override
	public String getAuthor() {
		return (String) getString(USER_NAME);
	}

	@Override
	public String getThumbnailUrl() {
		return (String) getString(PREVIEW_URL);
	}

	@Override
	public String getDownloadUrl() {
		return null;
	}

	@Override
	public String getWebPageUrl() {
		return (String) getString(URL_IFL);
	}

	@Override
	public String getId() {
		return getString(ID).toString();
	}

	@Override
	public String getDomain() {
		return "http://interfacelift.com";
	}

	@Override
	public Date getDate() {
		return null;
	}

	
}
