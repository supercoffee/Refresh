package com.coffeestrike.refresh.datatypes;

import java.util.Date;

import android.os.Parcelable;

public interface Wallpaper extends Parcelable{
	
	public String getTitle();
	
	public String getAuthor();
	
	public String getDescription();
	
	public String getThumbnailUrl();
	
	public String getPreviewUrl();
	
	public String getDownloadUrl();
	
	public String getWebPageUrl();
	
	public String getId();
	
	public String getDomain();
	
	public Date getDate();
	

}
