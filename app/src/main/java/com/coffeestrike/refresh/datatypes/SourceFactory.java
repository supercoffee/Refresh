package com.coffeestrike.refresh.datatypes;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.coffeestrike.refresh.R;
import com.coffeestrike.refresh.R.xml;
import com.coffeestrike.refresh.api.API;
import com.coffeestrike.refresh.api.Paginator;


public class SourceFactory {
	
	private HashMap<String, Source> mSources;

	private static SourceFactory sInstance;
	
	private List<String> mSourceNames;

	private SourceFactory(Context context){
		mSourceNames = new ArrayList<String>();
		mSources = new HashMap<String, Source>();
		loadSources(context);
	};
	
	public static SourceFactory instance(Context context){
		if(sInstance == null){
			sInstance = new SourceFactory(context);
		}
		return sInstance;
	}
	
	public List<String> getSourceNames(){
		return mSourceNames;
	}
	
	public Source getSource(String name){
		return mSources.get(name);
	}

	
	private void loadSources(Context context){
		
		XmlPullParser parser = context.getResources().getXml(R.xml.sources);
		
		try{
			Source source = null;
			for(int eventType = parser.next();
					eventType != XmlPullParser.END_DOCUMENT;
					eventType = parser.next()){
				switch(eventType){

				case XmlPullParser.START_TAG:
					String tagName = parser.getName();
					if(tagName.equals("source")){
						source = new Source();
					}
					if(tagName.equals("name")){
						source.mSourceName = parser.nextText();
					}else if(tagName.equals("api")){
						source.mApiClassName = parser.nextText();
					}else if(tagName.equals("options")){
						source.mOptions = parser.nextText();
					}else if(tagName.equals("sources")){}//don't care
					
					break;
				case XmlPullParser.END_TAG:
					if(parser.getName().equals("source")){
						mSources.put(source.mSourceName, source);
						mSourceNames.add(source.mSourceName);
					}
					break;
				default:
					break;
				}
				
			}
		}catch (XmlPullParserException e) {
				e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		} 
		
		
	}
	
	public static class Source implements I_Source{
		private String mSourceName;
		private String mApiClassName;
		private String mOptions;
		private API mApi;
		private Paginator mPaginator;
		
		public API getAPI() throws ClassNotFoundException{
			if(mApi != null){
				return mApi;
			}
			//create the class for the API
			Class<?> apiClass = Class.forName(mApiClassName);
			
			Constructor[] ctors = apiClass.getConstructors();
			API api = null;
			for(Constructor ctor: ctors){
				//constructor has one String parameter
				try{
					if(ctor.getParameterTypes().length ==1 && ctor.getParameterTypes()[0] == String.class){
						api = (API) ctor.newInstance(mOptions);
					}else{
						api = (API) ctor.newInstance();
					}
					break;
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

			}
			
			mApi = api;
			
			return api;
		}
		
		public Paginator getPaginator(){
			if(mPaginator == null){
				try {
					mPaginator = new Paginator(getAPI());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return mPaginator;
		}

		@Override
		public String getName() {
			return mSourceName;
		}

	}

}
