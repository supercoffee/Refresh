package com.coffeestrike.refresh.datatypes;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class RedditWallpaper extends WrappedData implements Wallpaper {
	
	private static final String URL = "url";
	private static final String TITLE = "title";
	private static final String THUMBNAIL = "thumbnail";
	private static final String AUTHOR = "author";
	private static final String DOMAIN = "domain";
	private static final String ID = "id";
	private static final String DATE = "created_utc";
	
	public static final Parcelable.Creator<RedditWallpaper> CREATOR 
	= new Creator<RedditWallpaper>() {
		
		@Override
		public RedditWallpaper[] newArray(int size) {
			return new RedditWallpaper[size];
		}
		
		@Override
		public RedditWallpaper createFromParcel(Parcel source) {
			return new RedditWallpaper(source);
		}
	};
	private static final String PERMALINK = "permalink";
	
	public RedditWallpaper(JSONObject obj) {
		super(obj);
	}


	public RedditWallpaper(Parcel source) {
		super(source);
	}


	@Override
	public String getTitle() {
		String title = getString(TITLE);
		if(title.length() > 40){
			String[] words = title.split(" ");
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < words.length && sb.length() < 40; i++){
				sb.append(words[i]);
				sb.append(" ");
			}
			if(sb.length() != title.length()){
				sb.append("...");
			}
			title = sb.toString();
		}
		
		return title;
	}


	@Override
	public String getAuthor() {
		return getString(AUTHOR);
	}


	@Override
	public String getDescription() {
		return getString(TITLE);
	}


	@Override
	public String getThumbnailUrl() {
		String url = getDownloadUrl();
		StringBuilder sb = new StringBuilder(url);
		if(getDomain().equals("i.imgur.com") || getDomain().equals("imgur.com")){
			sb.insert(sb.length() -4,"m");
			return sb.toString();
		}
		else if(getDomain().equals("ppcdn.500px.org")){
			sb.replace(sb.lastIndexOf("/"), sb.length(), "/2.jpg");
			return sb.toString();
		}
		return getString(THUMBNAIL);
	}


	@Override
	public String getPreviewUrl() {
		String url = getDownloadUrl();
		StringBuilder sb = new StringBuilder(url);
		//reddit wallpapers don't have preview size images explicitly. 
		
		//some images from imgur do have nicely sized previews though
		if(getDomain().equals("i.imgur.com") || getDomain().equals("imgur.com")){
			sb.insert(sb.length() -4,"l");
		}
		else if(getDomain().equals("ppcdn.500px.org")){
			sb.replace(sb.lastIndexOf("/"), sb.length(), "/3.jpg");
		}
		return sb.toString();
	}


	@Override
	public String getDownloadUrl() {
		
		return getString(URL);
	}


	@Override
	public String getId() {
		return getString(ID);
	}


	@Override
	public String getDomain() {
		
//		"domain" : "i.imgur.com",
		return getString(DOMAIN);
	}


	@Override
	public Date getDate() {
		String dateString = getString(DATE);
		int millis = Integer.parseInt(dateString);
		return new Date(millis);
	}


	@Override
	public String getWebPageUrl() {
		return "http://reddit.com" + getString(PERMALINK);
	}
	
	

}
