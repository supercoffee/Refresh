package com.coffeestrike.refresh;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.coffeestrike.refresh.PreviewAdapter.LoadTrigger;
import com.coffeestrike.refresh.RefreshDownloadManager.Listener;
import com.coffeestrike.refresh.api.I_Paginator;
import com.coffeestrike.refresh.api.I_Paginator.PaginatorListener;
import com.coffeestrike.refresh.datatypes.I_Source;
import com.coffeestrike.refresh.datatypes.SourceFactory;
import com.coffeestrike.refresh.datatypes.Wallpaper;
import com.coffeestrike.refresh.datatypes.WallpaperDownloadRequest;

/**
 * 
 * Shows a list of image previews.
 * @author Benjamin Daschel
 *
 */
public class ImageListFragment extends ListFragment implements PaginatorListener, LoadTrigger, Listener, OnRefreshListener{

	private static final String TAG = "ImageListFragment";
	
	private List<Wallpaper> mWallpaperList;

	private ListView mListView;

	private I_Paginator<Wallpaper> mPaginator;
	
	private boolean mIsLoading;

	private PreviewAdapter mAdapter;

	private ProgressBar mFooterView;

	private AdAdapter mAdAdapter;
	
	private I_Source mSource;


	private SwipeRefreshLayout mSwipeView;


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRetainInstance(true);	

		//get the wallpaper source
		String source = getArguments().getString(Constants.EXTRA_SOURCE);
		
		mSource = SourceFactory.instance(getActivity()).getSource(source);
		
		mPaginator = mSource.getPaginator();
		mPaginator.registerListener(this);
		
		mWallpaperList = mPaginator.getList();
		mAdapter = new PreviewAdapter(getActivity(), this, mWallpaperList);
		mAdAdapter  = new AdAdapter(getActivity(), mAdapter, this);
		
		RefreshDownloadManager.registerListener(this);
		
	}	
	
	@Override
	public void onDestroy(){
		RefreshDownloadManager.unregisterListener(this);
		mPaginator.unregisterListener(this);
		
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.image_list, container, false);
		
		mSwipeView = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshView);
	    mSwipeView.setOnRefreshListener(this);
	    mSwipeView.setColorScheme(android.R.color.holo_blue_bright,
	            android.R.color.holo_green_light,
	            android.R.color.holo_orange_light,
	            android.R.color.holo_red_light);
		
		mListView = (ListView) v.findViewById(android.R.id.list);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mListView.setFriction(0.05f);
		}
		mFooterView = new ProgressBar(getActivity());
		mListView.addFooterView(mFooterView);

		return v;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(mWallpaperList.isEmpty()){
			mPaginator.requestNextSet();
		}else{
			onPageReady();
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id){
		/*
		 * Start another fragment to display more information about
		 * the wallpaper.
		 */
		Bundle args = new Bundle();
		args.putInt(Constants.EXTRA_INDEX, AdAdapter.calculateActualPosition(pos));
		args.putString(Constants.EXTRA_SOURCE, mSource.getName());
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		intent.putExtras(args);
		startActivity(intent);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}


	@Override
	public void onPageReady() {
		if(getListAdapter() == null){
			setListAdapter(mAdAdapter);
		}
		
		mAdAdapter.notifyDataSetChanged();

		mIsLoading = false;
	}

	@Override
	public void loadTriggered() {
		if(! mIsLoading){
			mIsLoading = true;
			mPaginator.requestNextSet();
		}
		
	}

	@Override
	public void onDownloadCompleted(WallpaperDownloadRequest completedDownload) {
		Log.i(TAG, "ImageListFragment received download");
		
		mAdAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		mPaginator.refresh();
	}

	@Override
	public void onRefeshComplete() {
		mSwipeView.setRefreshing(false);
		onPageReady();
	}



}
