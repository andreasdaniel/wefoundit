package org.alphacloud.wefoundit;

import org.alphacloud.wefoundit.adapter.FPSpinnerAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements OnNavigationListener {
	// fields
	// side-bar menu
	private DrawerLayout mDrawerLayout;
	private String[] mMenuNames;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	private boolean isFrontPage;
	private boolean isDrawerOpen;
	// front-page spinner
	private String[] mSpinnerNames;
	private FPSpinnerAdapter mSpinnerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		createFPSpinner();
		createSideBarMenu(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
        return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		
		for(int i = 0; i < menu.size(); i++){
			menu.getItem(i).setVisible(true);
		}
		
		if(!isFrontPage || isDrawerOpen){
			menu.getItem(1).setVisible(false);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.action_main_search:
			Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
	
	// action after click on side-bar menu
	private void displayNextView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		Intent intent;
		isFrontPage = false;
		switch (position) {
		case 0:
			changeSpinnerMenu(0);
			isFrontPage = true;
			break;
		case 1:
			fragment = new ProfileFragment();
			break;
		case 2:
			fragment = new LostThingFragment();
			break;
		case 3:
			fragment = new FoundThingFragment();
			break;
		case 4:
			intent = new Intent(getApplicationContext(), ReportFoundActivity.class);
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(getApplicationContext(), ReportLostActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

		if (fragment != null) {
			loadMainFragment(fragment);
			
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(mMenuNames[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			
			toggleSpinner();
		} 
		else if(isFrontPage) {
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(mMenuNames[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			
			toggleSpinner();
		}
		else {
			// error in creating fragment
			//Log.e("MainActivity", "Error in creating fragment");
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}
	
	private class SideBarClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			displayNextView(position);
		}
		
	}
	
	// for fp (lost/found news stream) spinner purpose
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Fragment fragment = null;
		switch (itemPosition) {
		case 0:
			fragment = new FPFoundFragment();
			break;
		case 1:
			fragment = new FPLostFragment();
			break;
			
		default:
			break;
		}
		
		if (fragment != null) {
			loadMainFragment(fragment);
			
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
			return false;
		}
		
		return true;
	}
	
	private void createSideBarMenu(Bundle savedInstanceState) {
		isFrontPage = true;
		
		mMenuNames = getResources().getStringArray(R.array.menu_names);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		setTitle("Front Page");
		mTitle = getTitle();
		
		// set side-bar menu
		mDrawerList.setOnItemClickListener(new SideBarClickListener());
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_drawer, mMenuNames));
		
		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
				R.string.drawer_open, R.string.drawer_close) {
			
			@Override
			public void onDrawerClosed(View drawerView) {
				isDrawerOpen = false;
				invalidateOptionsMenu();
			}
			
			@Override
			public void onDrawerOpened(View drawerView) {
				isDrawerOpen = true;
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayNextView(0);
		}
	}
	
	private void createFPSpinner() {
		getActionBar().setDisplayShowTitleEnabled(!isFrontPage);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		mSpinnerNames = getResources().getStringArray(R.array.spinner_names);
		mSpinnerAdapter = new FPSpinnerAdapter(getApplicationContext(), mSpinnerNames);
		
		getActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);
	}
	
	private void toggleSpinner() {
		getActionBar().setDisplayShowTitleEnabled(!isFrontPage);
		
		if(isFrontPage) {
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		}
		else {
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}
	
	private void loadMainFragment(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();
	}
	
	private void changeSpinnerMenu(int position) {
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);
		getActionBar().setSelectedNavigationItem(position);
	}
}
