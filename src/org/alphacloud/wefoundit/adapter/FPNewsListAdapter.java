package org.alphacloud.wefoundit.adapter;

import java.util.List;

import org.alphacloud.wefoundit.adapter.model.FPNewsListItem;
import org.alphacloud.wefoundit.R;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FPNewsListAdapter extends BaseAdapter {
	// fields
	private Context context;
	private List<FPNewsListItem> items;
	//ImageLoader imageLoader;;
	
	public FPNewsListAdapter(Context context, List<FPNewsListItem> items) {
		super();
		this.context = context;
		this.items = items;
		//imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_item_fpnews, null);
		}

		ImageView imgIcon = (ImageView) convertView
				.findViewById(R.id.img_list_fpnews);
		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.txt_fpnews_title);
		TextView txtDesc = (TextView) convertView
				.findViewById(R.id.txt_fpnews_desc);

		// imgIcon.setImageResource(items.get(position).getIcon());
		txtTitle.setText(items.get(position).getTitle());
		txtDesc.setText(items.get(position).getDesc());
		String icon = items.get(position).getIcon();
		if(icon != "") {
			Log.d("ICON_ADAPTER", position + ": " + icon);
			Picasso.with(context).load(icon).resize(100, 100).into(imgIcon);
			//imageLoader.DisplayImage(icon, imgIcon);
		}

		return convertView;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		//imageLoader.clearCache();
	}
	
}
