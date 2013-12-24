package org.alphacloud.wefoundit.adapter;

import org.alphacloud.wefoundit.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FPSpinnerAdapter extends BaseAdapter {
	// fields
	private TextView txtSpinner;
    private String[] spinnerNavItem;
    private Context context;
	
	public FPSpinnerAdapter(Context context,
            String[] spinnerNavItem) {
		this.context = context;
		this.spinnerNavItem = spinnerNavItem;
	}
    
	@Override
	public int getCount() {
		return this.spinnerNavItem.length;
	}

	@Override
	public Object getItem(int position) {
		return this.spinnerNavItem[position];
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
            convertView = mInflater.inflate(R.layout.fp_spinner_list_item, null);
        }
         
        txtSpinner = (TextView) convertView.findViewById(R.id.txtSpinner);
         
        txtSpinner.setText(spinnerNavItem[position]);
        return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.fp_spinner_list_item, null);
        }
         
        txtSpinner = (TextView) convertView.findViewById(R.id.txtSpinner);
         
        txtSpinner.setText(spinnerNavItem[position]);
        return convertView;
	}
	
}
