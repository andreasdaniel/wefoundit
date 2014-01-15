package org.alphacloud.wefoundit;

import java.util.ArrayList;
import java.util.List;

import org.alphacloud.wefoundit.adapter.FPNewsListAdapter;
import org.alphacloud.wefoundit.adapter.model.FPNewsListItem;
import org.alphacloud.wefoundit.logic.ShareData;
import org.alphacloud.wefoundit.model.City;
import org.alphacloud.wefoundit.model.Country;
import org.alphacloud.wefoundit.model.DbConn;
import org.alphacloud.wefoundit.model.FoundThing;
import org.alphacloud.wefoundit.model.LostThing;
import org.alphacloud.wefoundit.util.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

public class SearchResultActivity extends Activity {
	// fields
	private ListView mSearchList;
	private RadioGroup mRadioGroup;
	private Button mLoadButton;

	private ProgressDialog pDialog;

	private FPNewsListAdapter listAdapter;
	private List<FPNewsListItem> searchItems;
	private List<FoundThing> ftList;
	private List<LostThing> ltList;

	private boolean isFoundChoosen = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);

		// init
		mSearchList = (ListView) findViewById(R.id.listView_search);
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_type);
		mLoadButton = new Button(this);

		searchItems = new ArrayList<FPNewsListItem>();
		listAdapter = new FPNewsListAdapter(this, searchItems);
		ftList = new ArrayList<FoundThing>();
		ltList = new ArrayList<LostThing>();

		mRadioGroup.check(R.id.radioButton_found);
		mLoadButton.setText("Load More");
		mSearchList.addFooterView(mLoadButton);
		mSearchList.setAdapter(listAdapter);

		mSearchList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parentView,
							View selectedItemView, int position, long id) {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								ThingActivity.class);
						intent.putExtra("PARENT_KEY", 11);
						if (isFoundChoosen)
							intent.putExtra("DATA", ftList.get(position));
						else
							intent.putExtra("DATA", ltList.get(position));

						startActivity(intent);
					}
				});

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_result, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		searchView.setIconifiedByDefault(false);

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	/**
	 * Handling intent data
	 */
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			/**
			 * Use this query to display search results like 1. Getting the data
			 * from SQLite and showing in listview 2. Making webrequest and
			 * displaying the data For now we just display the query only
			 */
			int selected = mRadioGroup.getCheckedRadioButtonId();

			if (selected == R.id.radioButton_found) {
				isFoundChoosen = true;
				new FoundSearcher().execute(query);
			} else {
				isFoundChoosen = false;
				new LostSearcher().execute(query);
			}

		}
	}

	private class FoundSearcher extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchResultActivity.this);
			pDialog.setMessage("Searching...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("search", args[0]));

			JSONObject json = jParser.makeHttpRequest(DbConn.SEARCH_FOUND,
					"GET", params);

			// check log cat from response
			Log.d("search found", json.toString());

			// check for success tag
			try {
				int success = json.getInt("success");

				if (success == 1) {
					// successfully get list home found
					JSONArray found = json.getJSONArray("foundthing");
					// ftList = new LinkedList<FoundThing>();
					Log.d("FOUND_JSON#", found.length() + "");
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
						ft.setFoundLong(c.getDouble("foundlongitude"));
						ft.setFoundTempEmail(c.getString("foundtempemail"));
						ft.setFoundTempPhone(c.getString("foundtempphone"));
						ft.setFoundUser(c.getInt("founduser"));
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

		@Override
		protected void onPostExecute(String result) {
			loadFoundItems();
			pDialog.dismiss();
		}
	}

	private class LostSearcher extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchResultActivity.this);
			pDialog.setMessage("Searching...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("search", args[0]));

			JSONObject json = jParser.makeHttpRequest(DbConn.SEARCH_LOST,
					"GET", params);

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

		@Override
		protected void onPostExecute(String result) {
			loadLostItems();
			pDialog.dismiss();
		}
	}

	private void loadFoundItems() {
		FPNewsListItem item;
		searchItems.clear();
		Log.d("FT_LIST#", ftList.size() + "");

		for (FoundThing ft : ftList) {
			String location = "";
			try {
				// location = (new LocationAddress(getActivity(),
				// ft.getFoundLat(), ft.getFoundLong())).getAddress();
			} catch (Exception e) {
				location = "not found";
			}

			ft.cat = ShareData.CATEGORIES.get(ft.getFoundCat()).getName();
			ft.loc = location.replace("null", "").replace("  ", " ");

			String title = ft.cat + " on " + ft.getFoundDate();
			item = new FPNewsListItem(title, " in " + ft.loc, 1);
			searchItems.add(item);
		}

		listAdapter.notifyDataSetChanged();
	}

	private void loadLostItems() {
		FPNewsListItem item;
		searchItems.clear();
		Log.d("LT_LIST#", ltList.size() + "");

		for (LostThing lt : ltList) {
			City c = ShareData.CITIES.get(lt.getLostCity());
			String city = c.getName() + ", "
					+ Country.getCountryNameByCode(c.getCountryCode());

			lt.cat = ShareData.CATEGORIES.get(lt.getLostCat()).getName();
			lt.loc = city;

			String title = lt.cat + " on " + lt.getLostDate();
			item = new FPNewsListItem(title, " in " + lt.loc, 1);
			searchItems.add(item);
		}

		listAdapter.notifyDataSetChanged();
	}

}
