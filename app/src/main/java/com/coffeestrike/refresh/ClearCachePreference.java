package com.coffeestrike.refresh;

import java.io.File;

import android.content.Context;
import android.preference.Preference;
import android.widget.Toast;

public class ClearCachePreference extends Preference {

	public ClearCachePreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onClick() {
		super.onClick();
		
		File cacheDir = getContext().getCacheDir();
		for(File cacheFile : cacheDir.listFiles()){
			cacheFile.delete();
		}
		
		Toast.makeText(getContext(), R.string.cache_cleared, Toast.LENGTH_SHORT).show();
	}
	
	

}
