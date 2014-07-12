package com.coffeestrike.refresh;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.coffeestrike.refresh.datatypes.SourceFactory;

public class ImageListActivity extends ActionBarActivity {
	
	
	private FragmentManager mFragMan;
	private Fragment mListFragment;
	private ActionBar mActionBar;
	private ViewPager mPager;
	private ListFragmentPagerAdapter mPagerAdapter;
	private List<String> mSources;
	private TabListener mTabListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mSources = SourceFactory.instance(getApplicationContext()).getSourceNames();
		
		mActionBar = getSupportActionBar();
		
		mPager = (ViewPager)findViewById(R.id.pager);
		
		mFragMan = getSupportFragmentManager();
		
		mPagerAdapter = new ListFragmentPagerAdapter(mFragMan);
		
		mPager.setAdapter(mPagerAdapter);
		
		final ActionBar actionBar = getSupportActionBar();

	    // Specify that tabs should be displayed in the action bar.
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // Create a tab listener that is called when the user changes tabs.
	    mTabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	        	mPager.setCurrentItem(tab.getPosition(), true);
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // hide the given tab
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // probably ignore this event
	        }
	    };
	    // Add 3 tabs, specifying the tab's text and TabListener
	    for(String name: mSources){
	    	actionBar.addTab(
	    			actionBar.newTab()
	    			.setText(name)
	    			.setTabListener(mTabListener));
	    }
	    
	    mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				getSupportActionBar().setSelectedNavigationItem(position);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
//		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME |
//				ActionBar.DISPLAY_SHOW_TITLE);
//		mActionBar.setCustomView(R.layout.action_bar_number);
		
//		mFragMan = getSupportFragmentManager(); 
		 
//		mListFragment = mFragMan.findFragmentById(R.id.fragment_container);
//		if(mListFragment == null){
//			mFragMan.beginTransaction()
//				.add(R.id.fragment_container, new ImageListFragment())
//				.commit();
//			
//		}
		
	}

	@Override
	protected void onResume() {
	
		super.onResume();
//	
//		TextView chapterNumber = (TextView) getSupportActionBar().getCustomView();
//		int remaining = RefreshDownloadManager.getDownloadsRemaining();
//		chapterNumber.setText(String.valueOf(remaining));
	
	}


	@Override
	public void onBackPressed() {
//		if(mFragMan.findFragmentById(R.id.fragment_container) != null)
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_settings:
				openSettings();
				return true;
				default:
					return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
		
	}
	
	
	private class ListFragmentPagerAdapter extends FragmentStatePagerAdapter{

		public ListFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			String source = mSources.get(position);
			Bundle args = new Bundle();
			args.putString(Constants.EXTRA_SOURCE, source);
			Fragment frag = new ImageListFragment();
			frag.setArguments(args);
			return frag;
		}

		@Override
		public int getCount() {
			return mSources.size();
		}
		
	}
	
	
	

}
