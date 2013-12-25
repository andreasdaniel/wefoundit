package org.alphacloud.wefoundit;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ReportFoundActivity extends Activity {
	// fields
	private Spinner mCatSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_found);
		
		// init fields
		mCatSpinner = (Spinner) findViewById(R.id.spinner_foundcat);
		
		// dummy
		String[] cats = getResources().getStringArray(R.array.foundcat_strings);
		ArrayAdapter<String> adpt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cats);
		adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCatSpinner.setAdapter(adpt);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
}
