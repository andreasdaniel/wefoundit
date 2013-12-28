package org.alphacloud.wefoundit.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ReportPhotoListAdapter extends BaseAdapter {
	// fields
	private Context mContext;
	private List<Bitmap> images;
	
	public ReportPhotoListAdapter(Context context, List<Bitmap> images) {
		this.mContext = context;
		this.images = images;
	}
	
	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public Object getItem(int position) {
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// create new Image View
		ImageView view = new ImageView(mContext);
		
		view.setImageBitmap(images.get(position));
		view.setLayoutParams(new Gallery.LayoutParams(100,100));
		
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		
		return view;
	}

}
