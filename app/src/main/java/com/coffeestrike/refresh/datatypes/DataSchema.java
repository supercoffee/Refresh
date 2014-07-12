package com.coffeestrike.refresh.datatypes;

public class DataSchema {
	
	public interface WallpaperKeys{
		public static final String ID = "id";
		public static final String PREVIEW_URL = "preview_url";
		public static final String AVAILABLE_RESOLUTIONS = "resolutions_available_array";
		public static final String TITLE = "title";
		public static final String BIG_PREVIEW_URL = "preview_url@2x";
		public static final String USER_NAME = "user_name";
		public static final String DESCRIPTION = "description";
		public static final String URL_IFL = "url_ifl";
		
	}
	
	public static String[] getWallpaperKeys(){
		return new String[]{
				WallpaperKeys.ID,
				WallpaperKeys.PREVIEW_URL,
				WallpaperKeys.AVAILABLE_RESOLUTIONS,
				WallpaperKeys.TITLE,
//				WallpaperKeys.BIG_PREVIEW_URL,
				WallpaperKeys.USER_NAME,
				WallpaperKeys.DESCRIPTION,
				WallpaperKeys.URL_IFL
				
		};
		
	}
	
	public interface DownloadKeys{
		public static final String DOWNLOAD_URL = "download_url";
		public static final String FILENAME = "filename";
		public static final String WALLPAPER_ID = "wallpaper_id";
		
	}
	
	public static String[] getDownloadKeys(){
		return new String[]{
				DownloadKeys.DOWNLOAD_URL,
				DownloadKeys.FILENAME,
				DownloadKeys.WALLPAPER_ID
				
		};
	}

}
