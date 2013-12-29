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

public class FoundThingFragment extends Fragment {
	// field
	private ListView mFoundList;
	private Button mLoadBtn;
	private FPNewsListAdapter adapter;
	private ArrayList<FPNewsListItem> foundItems;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_found_thing, container, false);
        
        return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// init fields
		mFoundList = (ListView) view.findViewById(R.id.listView_foundthings);
		mLoadBtn = new Button(getActivity());
		foundItems = new ArrayList<FPNewsListItem>();
		loadFoundItems();
		adapter = new FPNewsListAdapter(getActivity(), foundItems);
		
		mLoadBtn.setText("Load More");
		mFoundList.addFooterView(mLoadBtn);
		
		mFoundList.setAdapter(adapter);
		
		// set listview event
		mFoundList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position,
					long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ThingActivity.class);
				intent.putExtra("PARENT_KEY", 12);
				
				getActivity().startActivity(intent);
			}
		});
	}
	
	private void loadFoundItems() {
		FPNewsListItem item;
		
		item = new FPNewsListItem("Bag", "Bag found in Taidong Station", 1);
		foundItems.add(item);
		item = new FPNewsListItem("Shoes", "Shoes found in NCTU Library", 1);
		foundItems.add(item);
	}
	
}
