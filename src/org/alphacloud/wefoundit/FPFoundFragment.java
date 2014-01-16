package org.alphacloud.wefoundit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.alphacloud.wefoundit.adapter.FPNewsListAdapter;
import org.alphacloud.wefoundit.adapter.model.FPNewsListItem;
import org.alphacloud.wefoundit.logic.LocationAddress;
import org.alphacloud.wefoundit.logic.ShareData;
import org.alphacloud.wefoundit.model.DbConn;
import org.alphacloud.wefoundit.model.FoundThing;
import org.alphacloud.wefoundit.util.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class FPFoundFragment extends Fragment {
	// fields
	private ListView mFoundList;
	private Button mLoadBtn;
	private FPNewsListAdapter adapter;
	private ArrayList<FPNewsListItem> foundItems;
	private ProgressDialog pDialog;
	private JSONParser jsonParser;
	private LinkedList<FoundThing> ftList;
	
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
		foundItems = new ArrayList<FPNewsListItem>();
		//loadFoundItems();
		adapter = new FPNewsListAdapter(getActivity(), foundItems);
		
		ftList = new LinkedList<FoundThing>();
		
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
				intent.putExtra("PARENT_KEY", 11);
				intent.putExtra("DATA", ftList.get(position));
				
				getActivity().startActivity(intent);
			}
		});
		
		jsonParser = new JSONParser();
		
		new HomeFound().execute();
	}
	
	private void loadFoundItems() {
		FPNewsListItem item;
		
		Log.d("FT_LIST#", ftList.size()+"");
		
		for(FoundThing ft : ftList) {
//			String location = "";
//			try	{
//				location = (new LocationAddress(getActivity(), ft.getFoundLat(), ft.getFoundLong())).getAddress();
//			} catch(Exception e) {
//				location = "not found";
//			}
			
			ft.cat = ShareData.CATEGORIES.get(ft.getFoundCat()).getName();
			//ft.loc = location.replace("null", "").replace("  ", " ");
			
			String title = ft.cat + " on " + ft.getFoundDate();
			item = new FPNewsListItem(title, " in " + ft.loc);
			
			if(!ft.getPicURLs().isEmpty()) {
				item.setIcon(ft.getPicURLs().get(0));
			}
			
			Log.d("FP_ICON", item.getIcon());
			
			foundItems.add(item);
		}
		
		adapter.notifyDataSetChanged();
		//loadimg();
//		item = new FPNewsListItem("Bag", "Bag found in Taichung park", 1);
//		foundItems.add(item);
//		item = new FPNewsListItem("Smartphone", "Smartphone found in Hsinchu Station", 1);
//		foundItems.add(item);
	}
	
	class HomeFound extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog((MainActivity)getActivity());
			pDialog.setMessage("Get list found, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// getting JSON Object
			JSONObject json = jsonParser.makeHttpRequest(DbConn.LIST_FOUND, "POST",
					params);

			// check log cat from response
			Log.d("List found", json.toString());

			// check for success tag
			try {
				int success = json.getInt("success");

				if (success == 1) {
					// successfully get list home found
					JSONArray found = json.getJSONArray("foundthing");
					//ftList = new LinkedList<FoundThing>();
					Log.d("FOUND_JSON#", found.length()+"");
					for (int i = 0; i < found.length(); i++) {
                        JSONObject c = found.getJSONObject(i);
                        
                        // Storing each json item in variable
                        FoundThing ft = new FoundThing();
                        ft.setFoundCat(c.getInt("foundcategory"));
                        ft.setFoundDate(c.getString("founddate"));
                        ft.setFoundDesc(c.getString("founddescription"));
                        ft.setFoundId(c.getInt("foundid"));
                        ft.setFoundIsClaim(c.getInt("foundisclaim"));
                        ft.setFoundLat(c.getDouble("foundlatitude"));
                        ft.loc = c.getString("foundaddress");
                        ft.setFoundLong(c.getDouble("foundlongitude"));
                        ft.setFoundTempEmail(c.getString("foundtempemail"));
                        ft.setFoundTempPhone(c.getString("foundtempphone"));
                        ft.setFoundUser(c.getInt("founduser"));
                        
                        ft.setPicURLs(getPics(ft.getFoundId()));
                        
                        ftList.add(ft);                       
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
			loadFoundItems();
			// dismiss the dialog once done
			pDialog.dismiss();
		}
		
		private List<String> getPics(int id) {
			List<String> pics = new ArrayList<String>();
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", id+""));
			
			JSONObject json = jsonParser.makeHttpRequest(DbConn.PIC_FOUND, "GET",
					params);
			
			try {
				int success = json.getInt("success");
				
				Log.d("fetch_PICs", id+": "+success+"");
				
				if (success == 1) {
					// successfully get list home found
					JSONArray found = json.getJSONArray("foundpics");
					//ftList = new LinkedList<FoundThing>();
					Log.d("FOUND_PIC#", found.length()+"");
					for (int i = 0; i < found.length(); i++) {
                        JSONObject c = found.getJSONObject(i);
                        
                        // Storing each json item in variable
                        String url = c.getString("picturefoundlocation");
                        
                        pics.add(url);                       
                    }
					
				} else {
					// failed add report found
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return pics;
		}
	}
	
}
