package org.alphacloud.wefoundit;

import org.alphacloud.wefoundit.adapter.FPSpinnerAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultActivity extends Activity {
	// fields
	private ListView mSearchList;
	private RadioGroup mRadioGroup;
	
	// front-page spinner
	private String[] mSpinnerNames;
	private FPSpinnerAdapter mSpinnerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		
		// init
		mSearchList = (ListView) findViewById(R.id.listView_search);
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_type);
		
		mRadioGroup.check(R.id.radioButton_found);
		
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
             * Use this query to display search results like 
             * 1. Getting the data from SQLite and showing in listview 
             * 2. Making webrequest and displaying the data 
             * For now we just display the query only
             */
            int selected = mRadioGroup.getCheckedRadioButtonId();
            
            if(selected == R.id.radioButton_found) {
            	Toast.makeText(this,
        				"Found", Toast.LENGTH_SHORT).show();
            }
            else {
            	
            }
 
        }
 
    }
    
}
