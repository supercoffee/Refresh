package com.coffeestrike.refresh;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Simple viewholder to improve scrolling performance
 * of the listview.
 * @author Benjamin Daschel
 *
 */
public class ViewHolder{
	public ImageView previewImage;
	public TextView titleText;
	public ImageView downloadedCheck;
	public int position = -1;
	
	
	public Handler handler;
	public LinearLayout imageLayout;
	public ImageView check;
	
}