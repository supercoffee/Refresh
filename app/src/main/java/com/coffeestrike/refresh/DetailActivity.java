package com.coffeestrike.refresh;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.coffeestrike.refresh.api.I_Paginator.PaginatorListener;
import com.coffeestrike.refresh.api.Paginator;
import com.coffeestrike.refresh.datatypes.SourceFactory;
import com.coffeestrike.refresh.datatypes.Wallpaper;
import com.coffeestrike.refresh.datatypes.SourceFactory.Source;

public class DetailActivity extends ActionBarActivity implements OnPageChangeListener {

	protected static final int BUFFER = 5;
	private ViewPager mPager;
	private DetailFragmentPagerAdapter mPagerAdapter;
	private FragmentManager mFragmentManager;
	private Paginator mPaginator;
	private String mSource;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpaper_detail_pager);
		mFragmentManager = getSupportFragmentManager();
		mPager = (ViewPager) findViewById(R.id.pager);
		
		
		Bundle extras = getIntent().getExtras();
		
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		mSource = getIntent().getExtras().getString(Constants.EXTRA_SOURCE);
		Source source = SourceFactory.instance(getApplicationContext()).getSource(mSource);
		mPaginator = source.getPaginator();
		
		mPagerAdapter = new DetailFragmentPagerAdapter(mFragmentManager, mPaginator);
		
		mPager.setAdapter(mPagerAdapter);
		
		mPager.setCurrentItem(extras.getInt(Constants.EXTRA_INDEX));
		
		mPager.setOnPageChangeListener(this);
		


	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
		case android.R.id.home:
			//snagged from stack exchange
			Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, upIntent);
            }

            return true;
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onDestroy(){
		mPaginator.unregisterListener(mPagerAdapter);
		super.onDestroy();
	}
	
	
	private class DetailFragmentPagerAdapter extends FragmentStatePagerAdapter implements PaginatorListener{

		private List<Wallpaper> mWallpaperList;
		private int mCount;
		
		public DetailFragmentPagerAdapter(FragmentManager fm, Paginator paginator) {
			super(fm);
			mWallpaperList = paginator.getList();
			mCount = mWallpaperList.size();
			paginator.registerListener(this);
		}

		@Override
		public Fragment getItem(int position) {
			
			Fragment fragment = new DetailFragment();
			Wallpaper wallpaper = mWallpaperList.get(position);
			Bundle bundle = new Bundle();
			bundle.putParcelable(Constants.EXTRA_WALLPAPER, wallpaper);
			bundle.putString(Constants.EXTRA_SOURCE, mSource);
			fragment.setArguments(bundle);
			
			return fragment;
		}

		@Override
		public int getCount() {
			return mCount;
		}

		@Override
		public void onPageReady() {
			mCount = mWallpaperList.size();
			notifyDataSetChanged();
			
		}

		@Override
		public void onRefeshComplete() {
			mCount = mWallpaperList.size();
			notifyDataSetChanged();
		}
		
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int page) {
		if(page >= mPagerAdapter.getCount() - BUFFER){
			mPaginator.requestNextSet();
		}
		
	}


	
}
