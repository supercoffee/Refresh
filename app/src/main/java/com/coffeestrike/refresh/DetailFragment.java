package com.coffeestrike.refresh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coffeestrike.refresh.RefreshDownloadManager.Listener;
import com.coffeestrike.refresh.datatypes.SourceFactory;
import com.coffeestrike.refresh.datatypes.SourceFactory.Source;
import com.coffeestrike.refresh.datatypes.Wallpaper;
import com.coffeestrike.refresh.datatypes.WallpaperDownloadRequest;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment implements Listener {
		
	public static final String EXTRA_WALLPAPER = "wallpaper";
	private static final String TAG = "DetailFragment";
	protected Wallpaper mWallpaper;
	private ImageView mUpdateView;
	private ProgressBar mProgress;
	
	private TextView mTitleText;
	private SlidingUpPanelLayout mLayout;
	private TextView mArtistText;
	private TextView mInfoText;
	private TextView mLinkText;

	private boolean mDownloadInprogress = false;
	private AsyncTask<Void, Void, Bitmap> mImagePreviewTask;
	private Source mSource;


	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		setHasOptionsMenu(true);
		Bundle args = getArguments();
		mWallpaper = args.getParcelable(EXTRA_WALLPAPER);
		mSource = SourceFactory.instance(activity).getSource(args.getString(Constants.EXTRA_SOURCE));
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		RefreshDownloadManager.registerListener(this);

	}
	
	
	@Override
	public void onDestroy(){
		RefreshDownloadManager.unregisterListener(this);
		
		super.onDestroy();
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.detail_menu, menu);
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.wallpaper_detail_with_slider, container, false);
		mLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
		mLayout.setAnchorPoint(0.3f);
		
		mUpdateView = (ImageView) v.findViewById(R.id.image_preview_big);
		mProgress = (ProgressBar) v.findViewById(R.id.progressBar1);

		mTitleText = (TextView) v.findViewById(R.id.image_title_text);
		mTitleText.setText(mWallpaper.getTitle());
		
		mLayout.setDragView(mTitleText);
		
		mArtistText = (TextView) v.findViewById(R.id.image_artist_text);
		mArtistText.setText("Artist: "+ mWallpaper.getAuthor());
		
		mInfoText = (TextView) v.findViewById(R.id.image_info_text);
		mInfoText.setText(mWallpaper.getDescription());
		
		mLinkText = (TextView) v.findViewById(R.id.image_ifl_link);
		mLinkText.setText(mWallpaper.getWebPageUrl());
		mLinkText.setClickable(true);
		Linkify.addLinks(mLinkText, Linkify.WEB_URLS);
		mLinkText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchBrowser(mWallpaper.getWebPageUrl());
            }
        });
	
		return v;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		Picasso.with(getActivity())
			.load(mWallpaper.getPreviewUrl())
			.into(mUpdateView);
	}
	
	@Override
	public void onPause(){
		if(mImagePreviewTask != null){
			mImagePreviewTask.cancel(true);
		}
		super.onPause();
	}
	
	private void launchBrowser(String url) {
		Uri link = Uri.parse(url);
		Intent webIntent = new Intent(Intent.ACTION_VIEW, link);
		
		startActivity(webIntent);

	}
	
	
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
			case R.id.action_download:
				 if(! mDownloadInprogress){
					pickBestResAndDownload(false);
				}
				else{
					Toast.makeText(getActivity(), R.string.already_downloading, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.action_share:
				launchShareIntent();
				break;
			case R.id.action_set:
				pickBestResAndDownload(true);
				break;
			default:
				break;
	
		}
		return true;
	}


	private void launchShareIntent() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, mWallpaper.getWebPageUrl());
		
		getActivity().startActivity(Intent.createChooser(intent, "Share"));
		
	}

//	private void showDownloadDialog() {
//		AlertDialog dialog = new AlertDialog.Builder(getActivity())
//			
//			.setMessage(R.string.download_again)
//			.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					pickBestResAndDownload(false);
//					
//				}
//			})
//			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					
//				}
//			})
//			.setNeutralButton(R.string.view_in_gallery, new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					
//				}
//			})
//			.create();
//		dialog.show();
//		
//	}

	private void pickBestResAndDownload(boolean setWhenFinished) {
		
		WallpaperDownloadRequest request = new WallpaperDownloadRequest(mWallpaper, setWhenFinished);
		
		try {
			RefreshDownloadManager.requestDownload(request, mSource.getAPI());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void onDownloadCompleted(WallpaperDownloadRequest completedDownload) {
		Log.i(TAG, "DetailFragment received download");
		
	}



}
