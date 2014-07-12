package com.coffeestrike.refresh.api;

import java.util.List;

import android.graphics.Bitmap;

import com.coffeestrike.refresh.datatypes.Wallpaper;

public interface API {
	
	public List<Wallpaper> getWallpapers(WallpaperRequest request);
	
	public Bitmap downloadFullSizedImage(Wallpaper wallpaper);

}
