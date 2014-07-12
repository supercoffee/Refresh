package com.coffeestrike.refresh.datatypes;

public class WallpaperDownloadRequest {
	
	private Wallpaper mWallpaper;
	private boolean mSetWhenFinished;
	
	public WallpaperDownloadRequest(Wallpaper wallpaper, boolean setWhenFinished) {
		mWallpaper = wallpaper;
		mSetWhenFinished = setWhenFinished;
	}
	
	
	public Wallpaper getWallpaper() {
		return mWallpaper;
	}

	public boolean isSetWhenFinished() {
		return mSetWhenFinished;
	}
	
	@Override
	public boolean equals(Object object){
		
		if(this.getClass().equals(object.getClass())){
			
			WallpaperDownloadRequest request = (WallpaperDownloadRequest) object;
			
			if(request.mWallpaper.getId().equals(this.mWallpaper.getId())){
				return true;
			}
			
		}
		
		return false;
	}
	

}
