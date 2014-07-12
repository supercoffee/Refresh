package com.coffeestrike.refresh.datatypes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Wraps a JSONObject and provides a convenient
 * way for subclasses to access that data.
 * Implementing Parcelable forces subclasses to be parcelable as well
 * @author Benjamin Daschel
 *
 */
public abstract class WrappedData implements Parcelable{
	
	public static abstract class KeySet{
		public abstract String[] getKeys();
	}
	
	private JSONObject mData;
		
	public final String getString(String key){
		try {
			return mData.getString(key);
		} catch (JSONException e) {
			return null;
		}
	}
	
	protected final List<String> getStringList(String key){
		List<String> list = null;
		try {
			JSONArray jArray = mData.getJSONArray(key);
			list = new ArrayList<String>();
			for(int i = 0; i< jArray.length(); i++){
				
				list.add(jArray.getString(i));
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public WrappedData(Parcel in){
		try {
			mData = new JSONObject(in.readString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	protected WrappedData(JSONObject obj){
		mData = obj;
	}

	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(mData.toString());
		
	}
	
	
}
