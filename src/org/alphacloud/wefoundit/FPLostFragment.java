package org.alphacloud.wefoundit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.alphacloud.wefoundit.adapter.FPNewsListAdapter;
import org.alphacloud.wefoundit.adapter.model.FPNewsListItem;
import org.alphacloud.wefoundit.logic.LocationAddress;
import org.alphacloud.wefoundit.logic.ShareData;
import org.alphacloud.wefoundit.model.City;
import org.alphacloud.wefoundit.model.Country;
import org.alphacloud.wefoundit.model.FoundThing;
import org.alphacloud.wefoundit.model.LostThing;
import org.alphacloud.wefoundit.util.JSONParser;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class FPLostFragment extends Fragment {
	private ListView mLostList;
	private Button mLoadBtn;
	private FPNewsListAdapter adapter;
	private ArrayList<FPNewsListItem> lostItems;
	
	private ProgressDialog pDialog;
	private JSONParser jsonParser;
	private LinkedList<LostThing> ltList;
	private String urlMyLost;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_fp_lost, container, false);
        
        return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// init fields
		jsonParser = new JSONParser();
		ltList = new LinkedList<LostThing>();
		urlMyLost = "http://140.113.210.89/wefoundit/listlost.php";
		
		mLostList = (ListView) view.findViewById(R.id.listView_lostnews);
		mLoadBtn = new Button(getActivity());
		lostItems = new ArrayList<FPNewsListItem>();
		//loadFoundItems();
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
				intent.putExtra("DATA", ltList.get(position));
				
				getActivity().startActivity(intent);
			}
		});
		
		new HomeLost().execute();
	}
	
	private void loadLostItems() {
		FPNewsListItem item;
		
		Log.d("LT_LIST#", ltList.size()+"");
		
		for(LostThing lt : ltList) {
			City c = ShareData.CITIES.get(lt.getLostCity());
			String city = c.getName() + ", " + 
					Country.getCountryNameByCode(c.getCountryCode());
			
			lt.cat = ShareData.CATEGORIES.get(lt.getLostCat()).getName();
			lt.loc = city;
			
			String title = lt.cat + " on " + lt.getLostDate();
			item = new FPNewsListItem(title, " in " + lt.loc, 1);
			lostItems.add(item);
		}
		
		adapter.notifyDataSetChanged();
		
//		item = new FPNewsListItem("Dog", "Dog found in taipei park", 1);
//		lostItems.add(item);
//		item = new FPNewsListItem("Smartphone", "Smartphone found in Hsinchu Station", 1);
//		lostItems.add(item);
	}
	
	class HomeLost extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog((MainActivity)getActivity());
			pDialog.setMessage("Get list lost, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// getting JSON Object
			JSONObject json = jsonParser.makeHttpRequest(urlMyLost, "POST",
					params);

			// check log cat from response
			Log.d("List lost", json.toString());

			// check for success tag
			try {
				int success = json.getInt("success");

				if (success == 1) {
					// successfully get list home found
					JSONArray found = json.getJSONArray("lostthing");
					for (int i = 0; i < found.length(); i++) {
                        JSONObject c = found.getJSONObject(i);
                        
                        // Storing each json item in variable
                        LostThing lt = new LostThing();
                        lt.setLostAdd(c.getString("lostaddress"));
                        lt.setLostCat(c.getInt("lostcategory"));
                        lt.setLostCity(c.getInt("lostcity"));
                        lt.setLostDate(c.getString("lostdate"));
                        lt.setLostDesc(c.getString("lostdescription"));
                        lt.setLostId(c.getInt("lostid"));
                        lt.setLostIsFound(c.getInt("lostisfound"));
                        lt.setLostUploadDate(c.getString("lostuploaddate"));
                        lt.setLostUser(c.getInt("lostuser"));
                        lt.setLostEmail(c.getString("lostemail"));
                        lt.setLostPhone(c.getString("lostphone"));
                        ltList.add(lt);                       
                    }
					
				} else {
					// failed add report found
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			loadLostItems();
		}
	}
	
}
