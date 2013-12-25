package org.alphacloud.wefoundit;

import java.util.ArrayList;

import org.alphacloud.wefoundit.adapter.FPFoundListAdapter;
import org.alphacloud.wefoundit.adapter.model.FPFoundListItem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class FPFoundFragment extends Fragment {
	// fields
	private ListView mFoundList;
	private Button mLoadBtn;
	private FPFoundListAdapter adapter;
	private ArrayList<FPFoundListItem> foundItems;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_fp_found, container, false);
        				
        return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// init fields
		mFoundList = (ListView) view.findViewById(R.id.listView_foundnews);
		mLoadBtn = new Button(getActivity());
		foundItems = new ArrayList<FPFoundListItem>();
		loadFoundItems();
		adapter = new FPFoundListAdapter(getActivity(), foundItems);
		
		mLoadBtn.setText("Load More");
		mFoundList.addFooterView(mLoadBtn);
		
		mFoundList.setAdapter(adapter);
	}
	
	private void loadFoundItems() {
		FPFoundListItem item;
		
		item = new FPFoundListItem("Dog", "Dog found in taipei park", 1);
		foundItems.add(item);
		item = new FPFoundListItem("Smartphone", "Smartphone found in Hsinchu Station", 1);
		foundItems.add(item);
	}
}
