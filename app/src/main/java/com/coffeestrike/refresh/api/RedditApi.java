package com.coffeestrike.refresh.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.coffeestrike.refresh.RefreshApplication;
import com.coffeestrike.refresh.datatypes.RedditWallpaper;
import com.coffeestrike.refresh.datatypes.Wallpaper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RedditApi implements API{

	//substitute %s with the subreddit
	private static final String URL = "http://reddit.com/r/%s/new.json";
	private String mSubreddit;
	
	//Domains that don't play nicely with image downloads
	private String[] IGNORE = {
			"mediafire.com",
			"flickr.com",
	};
	
	public RedditApi(String subreddit){
		mSubreddit = subreddit;
	}
	
	public List<Wallpaper> getWallpapers(WallpaperRequest request){
		List<Wallpaper> list = new ArrayList<Wallpaper>();
		StringBuilder sb = new StringBuilder(String.format(URL, mSubreddit));
		
		if(request.getExtra("after") != null){
			sb.append("?after=").append(request.getExtra("after"));
		}

        Context context = RefreshApplication.getContext();
		try {
//			HttpResponse<JsonNode> response = Unirest.get(sb.toString()).asJson();
            JsonObject body = Ion.with(context)
                    .load(sb.toString())
                    .setLogging("RedditApi", Log.DEBUG)
                    .asJsonObject()
                    .get();
				
				String after = body.getAsJsonObject("data").get("after").getAsString();
				//update the "after" field which will be used by the paginator for
				//tracking page position
				request.putExtra("after", after);
				
				JsonArray results  = body.getAsJsonObject("data").getAsJsonArray("children");
				
				int size = results.size();
				
				for(int i = 0; i < size; i++){
					JsonObject o = results.get(i).getAsJsonObject().getAsJsonObject("data");
                    JSONObject crap = new JSONObject(o.toString());
					RedditWallpaper w = new RedditWallpaper(crap);
					if(! ignore(w)){
						list.add(w);
					}
				}

		} catch (JSONException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return list;
	}
	
	private boolean ignore(RedditWallpaper w){
		String domain = w.getDomain();
		for(String s: IGNORE){
			if(domain.equalsIgnoreCase(s) ){
				return true;
			}
		}
		
		//ignore links that don't end with jpg
		if(! w.getDownloadUrl().endsWith(".jpg")){
			return true;
		}
		return false;
	}

	@Override
	public Bitmap downloadFullSizedImage(Wallpaper wallpaper) {
		// TODO Auto-generated method stub
		return null;
	}

}
