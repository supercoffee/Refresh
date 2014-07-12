package com.coffeestrike.refresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.coffeestrike.refresh.PreviewAdapter.LoadTrigger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdAdapter extends BaseAdapter {
	
	private static final int AD_INTERVAL = 10;

	private static final int THRESHOLD = 10;
	
	private AdView mAdView;
	
	private Context mContext;
	private BaseAdapter mDelegate;

	private LoadTrigger mLoadTrigger;

	public AdAdapter(Context context, BaseAdapter delegate, LoadTrigger trigger){
		mContext = context;
		mDelegate = delegate;
		mLoadTrigger = trigger;
		
	}

	@Override
	public int getCount() {
		return mDelegate.getCount();
	}

	@Override
	public Object getItem(int position) {
		/*
		 * Calculate the offset. For every 10 items, the offset 
		 * increases by one because of the extra ad in the list.
		 */
		int offset = position / AD_INTERVAL + 1;
		return mDelegate.getItem(position - offset);
	}

	@Override
	public long getItemId(int position) {
		return mDelegate.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(position > getCount() - THRESHOLD){
			mLoadTrigger.loadTriggered();
		}
		
		/*
		 * Create new ads a the specified interval
		 */
		if(position % AD_INTERVAL == 0){
			
			
			/*
			 * This keeps a reference to the same ad.
			 * By keeping an adView loaded at all times,
			 * there is always an add displayed on screen, even if it isn't
			 * fresh.  
			 */
			if(mAdView == null){
				 // Create a new AdView
		       mAdView = new AdView(mContext);
		        float density = mContext.getResources().getDisplayMetrics().density;
		        int height = Math.round(AdSize.BANNER.getHeight() * density);
		        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels / (int)density;
		        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
		            AbsListView.LayoutParams.MATCH_PARENT,
		            height);
		        
	
		        
		        mAdView.setLayoutParams(params);
				
		        mAdView.setAdUnitId(AdmobKeys.KEY_LISTADS);
		        mAdView.setAdSize(new AdSize(screenWidth -32, AdSize.AUTO_HEIGHT));
			}
			
			mAdView.loadAd(new AdRequest.Builder().addTestDevice("80BF1078AEA06BB173F52934D8F7BBDD").build());
	        
	        return mAdView;

			
			
		}
		
		else{
			
			/*
			 * If the existing convert view contains an AdView, we need to destroy it.
			 */
			if(convertView instanceof AdView){
				convertView = null;
			}
			
			convertView = mDelegate.getView(calculateActualPosition(position),
					convertView, parent);
			
		}

		
		return convertView;
	}
	
	public static int calculateActualPosition(int clickedPosition){
		return clickedPosition - (int) Math.ceil(clickedPosition / AD_INTERVAL) -1;
	}

}
