package com.coffeestrike.refresh.api;

import android.graphics.Bitmap;

import com.coffeestrike.refresh.datatypes.IFLWallpaper;
import com.coffeestrike.refresh.datatypes.Resolution;
import com.coffeestrike.refresh.datatypes.Wallpaper;
import com.coffeestrike.refresh.datatypes.WallpaperDownload;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of methods to interface with 
 * IFL endpoints.
 * @author Benjamin Daschel
 *
 */
public class InterfaceLiftAPI implements API {

	private static final String BASE_URL = "https://interfacelift-interfacelift-wallpapers.p.mashape.com/v1?limit=%d&start=%d";
	private static final String LIMIT = "?limit=";
	private static final String START = "&start=";
	
	
	/**
	 * Retrieves 10 wallpapers from the most recent category.
	 * @return a list containing Wallpapers
	 */
	public List<Wallpaper> getWallpapers(WallpaperRequest request){
		List<Wallpaper> list = new ArrayList<Wallpaper>();
//		String urlString = String.format(BASE_URL, request.limit, request.start);
//		try {
//			HttpResponse<JsonNode> response = Unirest.get(urlString)
//					.header("X-Mashape-Authorization", InterfaceLiftApiKey.API_KEY_TESTING)
//					.asJson();
//			if(response.getCode() == 200){
//				JSONArray body = response.getBody().getArray();
//
//				for(int i = 0; i < body.length(); i++){
//					IFLWallpaper wallpaper = new IFLWallpaper(body.getJSONObject(i));
//					list.add(wallpaper);
//				}
//
//			}
//
//		} catch (UnirestException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//
//		}
		
		return list;
	}
	
	public static ArrayList<IFLWallpaper> getWallpapers(int start, int limit){
		ArrayList<IFLWallpaper> list = new ArrayList<IFLWallpaper>();
//
//		try {
//			HttpResponse<JsonNode> response = Unirest.get(BASE_URL + "/wallpapers/"+ LIMIT
//					+ limit + START + start)
//					.header("X-Mashape-Authorization", InterfaceLiftApiKey.API_KEY_TESTING)
//					.asJson();
//			if(response.getCode() == 200){
//				list = new ArrayList<IFLWallpaper>();
//				JSONArray body = response.getBody().getArray();
//
//				for(int i = 0; i < body.length(); i++){
//					IFLWallpaper wallpaper = new IFLWallpaper(body.getJSONObject(i));
//					list.add(wallpaper);
//				}
//
//			}
//
//		} catch (UnirestException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
		return list;
	}
	
	public static WallpaperDownload getWallpaperDownload(IFLWallpaper wallpaper, Resolution resolution){
		
//		String downloadUrl = makeDownloadUrl(wallpaper, resolution.toString());
//		try {
//			HttpResponse<JsonNode> request = Unirest.get(downloadUrl)
//					  .header("X-Mashape-Authorization", InterfaceLiftApiKey.API_KEY_TESTING)
//					  .asJson();
//			if(request.getCode() == 200){
//				return new WallpaperDownload( request.getBody().getObject());
//
//			}
//
//
//		} catch (UnirestException e) {
//			e.printStackTrace();
//		}
		
		return null;
	}
	

	private static String makeDownloadUrl(IFLWallpaper wallpaper, String resolution) {
		return BASE_URL + "/wallpaper_download" + "/" 
				+ wallpaper.getWallpaperId()+ "/" + resolution +"/";
	}

	@Override
	public Bitmap downloadFullSizedImage(Wallpaper wallpaper) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
