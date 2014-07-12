package com.coffeestrike.refresh;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coffeestrike.refresh.datatypes.Wallpaper;
import com.squareup.picasso.Picasso;

public class PreviewAdapter extends BaseAdapter{
	
	public interface LoadTrigger{
		public void loadTriggered();
	}

	private static final int THRESHOLD = 5;
	
	private Context mContext;
	private List<Wallpaper> mList;
	private LoadTrigger mLoadTrigger;
	private Drawable mTempImage;
	
	public PreviewAdapter(Context context, LoadTrigger trigger, List<Wallpaper> list){
		mContext = context;
		mList = list;
		mLoadTrigger = trigger;
		mTempImage = context.getResources().getDrawable(R.drawable.mountains);
		

	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mList.get(position).getId().hashCode();
	}
	
	@Override
	public boolean hasStableIds(){
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Wallpaper wallpaper = (Wallpaper) getItem(position);
		ViewHolder holder;
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item, parent, false);
			holder = new ViewHolder();
			holder.titleText =(TextView) convertView.findViewById(R.id.title_text);
			holder.previewImage = (ImageView) convertView.findViewById(R.id.image_view);
			holder.imageLayout = (LinearLayout) convertView.findViewById(R.id.image_layout);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}

		
		/*
		 * Don't bother changing the image and loading a new
		 * one of we are re-using the view in the same spot.
		 */
		if(position != holder.position){
			holder.previewImage.setImageDrawable(mTempImage);
			holder.titleText.setText(wallpaper.getTitle());
			holder.position = position;
			
			Picasso.with(mContext)
				.load(wallpaper.getThumbnailUrl())
				.placeholder(R.drawable.mountains)
				.into(holder.previewImage);
		}
		

		
		
		return convertView;
	}

	
	


}
