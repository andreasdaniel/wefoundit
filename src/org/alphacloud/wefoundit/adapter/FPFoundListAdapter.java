package org.alphacloud.wefoundit.adapter;

import java.util.ArrayList;

import org.alphacloud.wefoundit.adapter.model.FPFoundListItem;

import org.alphacloud.wefoundit.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FPFoundListAdapter extends BaseAdapter {
	// fields
	private Context context;
	private ArrayList<FPFoundListItem> items;
			
	public FPFoundListAdapter(Context context, ArrayList<FPFoundListItem> items) {
		super();
		this.context = context;
		this.items = items;
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
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_fpfound, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.img_list_fpfound);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.txt_fpfound_title);
        TextView txtDesc = (TextView) convertView.findViewById(R.id.txt_fpfound_desc);
         
        //imgIcon.setImageResource(items.get(position).getIcon());        
        txtTitle.setText(items.get(position).getTitle());
        txtDesc.setText(items.get(position).getDesc());
        
        return convertView;
	}

}
