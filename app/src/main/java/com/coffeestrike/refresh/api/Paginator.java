package com.coffeestrike.refresh.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.AsyncTask;

import com.coffeestrike.refresh.datatypes.Wallpaper;

public class Paginator implements I_Paginator<Wallpaper> {
	
	@SuppressWarnings("unused")
	private static final String TAG = "WallpaperListPaginator";
	
	private static final int DEFAULT_PAGE_SIZE = 10;

	
	/*
	 * How many items to load at one time.
	 */
	protected int mPageSize = DEFAULT_PAGE_SIZE;
	
	/*
	 * Which item do we load first?
	 */
	private int mStart = 0;
	/*
	 * Callback when the page has been loaded.
	 */
	private Set<PaginatorListener> mListeners;
	
	/*
	 * Have any requests been made?
	 */
	private boolean mRequestPending;
	
	private boolean mPendingRequestIsRefresh;
	
	private List<Wallpaper> mWallpaperList;

	private WallpaperRequest mLastRequest;
	
	private API mApi;
	
	public Paginator(API api){
		mRequestPending = false;
		mListeners = new HashSet<PaginatorListener>();
		mWallpaperList = new ArrayList<Wallpaper>();
		mApi = api;
	}
	
	/**
	 * Load the next set of wallpapers.
	 * When the set is loaded, the callback 
	 * is called.
	 */
	@Override
	public void requestNextSet(){
		if(! mRequestPending){
			mRequestPending = true;
			if(mLastRequest == null){
				mLastRequest = new WallpaperRequest();
			}
			mPendingRequestIsRefresh = false;
			mLastRequest.start = mStart;
			new WallpaperRequestTask().execute(mLastRequest);
		}
	}
	

	@Override
	public List<Wallpaper> getList() {
		return mWallpaperList;
	}

	@Override
	public void refresh() {
		if(! mRequestPending){
			mRequestPending = true;
			mLastRequest = new WallpaperRequest();
			mPendingRequestIsRefresh = true;
			mLastRequest.start = 0;
			new WallpaperRequestTask().execute(mLastRequest);
		}
		
	}

	@Override
	public void registerListener(PaginatorListener listener) {
		mListeners.add(listener);
	}

	@Override
	public void unregisterListener(PaginatorListener listener) {
		mListeners.remove(listener);
	}

	@Override
	public void setPageSize(int pageSize) {
		mPageSize = pageSize;
	}

	@Override
	public int getPageSize() {
		return mPageSize;
	}

	@Override
	public boolean isRequestPending() {
		return mRequestPending;
	}
	
	private class WallpaperRequestTask extends AsyncTask<WallpaperRequest, Void, List<? extends Wallpaper>>{

		WallpaperRequest request;	
		
		@Override
		protected List<? extends Wallpaper> doInBackground(WallpaperRequest... params) {
			this.request  = params[0];
			return mApi.getWallpapers(request);
		}
		
		@Override
		protected void onPostExecute(List<? extends Wallpaper> wallpapers){
			if(wallpapers != null){
				if(mPendingRequestIsRefresh){
					mWallpaperList.clear();
				}
				for(Wallpaper w: wallpapers){
					mWallpaperList.add(w);
				}	
				//increment the next starting wallpaper
				mStart += wallpapers.size();
			}
			
			if(mPendingRequestIsRefresh){
				for(PaginatorListener listener: mListeners){
					listener.onRefeshComplete();
				}
			}
			else{
				for(PaginatorListener listener: mListeners){
					listener.onPageReady();
				}
			}
			
			mRequestPending = false;			
		}

	}
	

}
