package com.coffeestrike.refresh.api;

import java.util.HashMap;

public class WallpaperRequest {
	
	public String url;
	public int start;
	public int limit;
	public HashMap<String, String> extras;
	
	
	public WallpaperRequest(){
		extras = new HashMap<String, String>();
	}
	
	public HashMap<String, String> getExtras(){
		return extras;
	}
	
	public void putExtra(String key, String value){
		extras.put(key, value);
	}
	
	public String getExtra(String key){
		return extras.get(key);
	}


}
