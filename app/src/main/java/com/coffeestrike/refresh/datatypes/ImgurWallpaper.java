package com.coffeestrike.refresh.datatypes;

import org.json.JSONObject;

import android.os.Parcel;

public class ImgurWallpaper extends RedditWallpaper {

	public ImgurWallpaper(Parcel source) {
		super(source);
	}
	
	public ImgurWallpaper(JSONObject object){
		super(object);
	}
	
	@Override
	public String getPreviewUrl(){
		//see https://api.imgur.com/models/image for resizing rules
		StringBuilder sb = new StringBuilder(getPreviewUrl());
		// 'l' refers to large preview. 
		sb.insert(sb.length() -5, 'l');
		
		return sb.toString();
	}

}
