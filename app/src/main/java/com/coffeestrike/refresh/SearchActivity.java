package com.coffeestrike.refresh;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SearchActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.search);

	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
//	      doMySearch(query);
	    }
	}

	@Override
	public boolean onSearchRequested() {
		// TODO Auto-generated method stub
		return super.onSearchRequested();
	}

	@Override
	public void startSearch(String initialQuery, boolean selectInitialQuery,
			Bundle appSearchData, boolean globalSearch) {
		// TODO Auto-generated method stub
		super.startSearch(initialQuery, selectInitialQuery, appSearchData, globalSearch);
	}
	
	
	
	
}
