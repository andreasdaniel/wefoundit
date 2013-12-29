package org.alphacloud.wefoundit;

import java.util.ArrayList;

import org.alphacloud.wefoundit.adapter.FPNewsListAdapter;
import org.alphacloud.wefoundit.adapter.model.FPNewsListItem;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LostThingFragment extends Fragment {
	// field
	private ListView mLostList;
	private Button mLoadBtn;
	private FPNewsListAdapter adapter;
	private ArrayList<FPNewsListItem> lostItems;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_lost_thing, container, false);
        
        return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// init fields
		mLostList = (ListView) view.findViewById(R.id.listView_lostthings);
		mLoadBtn = new Button(getActivity());
		lostItems = new ArrayList<FPNewsListItem>();
		loadFoundItems();
		adapter = new FPNewsListAdapter(getActivity(), lostItems);
		
		mLoadBtn.setText("Load More");
		mLostList.addFooterView(mLoadBtn);
		
		mLostList.setAdapter(adapter);
		
		// set listview event
		mLostList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position,
					long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ThingActivity.class);
				intent.putExtra("PARENT_KEY", 11);
				
				getActivity().startActivity(intent);
			}
		});
	}
	
	private void loadFoundItems() {
		FPNewsListItem item;
		
		item = new FPNewsListItem("Bag", "Bag lost in Taichung park", 1);
		lostItems.add(item);
		item = new FPNewsListItem("Wallet", "Wallet lost in Keelung Station", 1);
		lostItems.add(item);
	}
}
