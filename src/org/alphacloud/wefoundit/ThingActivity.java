package org.alphacloud.wefoundit;

import java.util.ArrayList;
import java.util.List;

import org.alphacloud.wefoundit.adapter.ReportPhotoListAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ThingActivity extends Activity {
	// fields
	private final int MAX_PIC_NUMBER = 3;
	private final String PARENT_KEY = "PARENT_KEY";
	private final int FP_PARENT = 11;
	private final int RP_PARENT = 12;
	
	private TextView mCategoryTextView;
	private TextView mDescTextView;
	private TextView mDateTextView;
	private TextView mPlaceTextView;
	private TextView mClaimTextView;
	private Gallery mGallery;
	
	private List<Bitmap> uploadedImages;
	private String[] imagesLocation;
	private ReportPhotoListAdapter galleryAdapter;
	
	private int parent;
	
	String email = "user@foundit.com";
	String phone = "+886 988811676";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thing);
		
		// init fields
		mCategoryTextView = (TextView) findViewById(R.id.txtview_thingcategory1);
		mDescTextView = (TextView) findViewById(R.id.txtview_thingdescription1);
		mDateTextView = (TextView) findViewById(R.id.txtview_thingdate1);
		mPlaceTextView = (TextView) findViewById(R.id.txtview_thingplace1);
		mClaimTextView = (TextView) findViewById(R.id.txtview_thingclaim);
		mGallery = (Gallery) findViewById(R.id.gallery_thingpics);
		
		getPassingArg();
		initCustomActionBar();
		initPictureGallery();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.thing, menu);
		return true;
	}

	private void getPassingArg() {
		Bundle bundle = getIntent().getExtras();
		parent = bundle.getInt(PARENT_KEY);
	}
	
	private void initCustomActionBar() {
		
		View customActionBar = getLayoutInflater().inflate(R.layout.action_bar_edit_discarddone, new LinearLayout(this), false);
		View cancelActionView = customActionBar.findViewById(R.id.action_custombar_cancel);
		View doneActionView = customActionBar.findViewById(R.id.action_custombar_done);
		TextView doneTextView = (TextView) customActionBar.findViewById(R.id.txt_saveaction);
		
		switch (parent) {
		case RP_PARENT:
			doneTextView.setText("Mark as Done");
			break;
		case FP_PARENT:
			doneTextView.setText("Try Claming");
			break;

		default:
			break;
		}
		
		cancelActionView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		doneActionView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (parent) {
				case RP_PARENT:
					break;
				case FP_PARENT:
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					DialogFragment newFragment = new ClaimDialogFragment(email,phone);
					newFragment.show(ft, "idenity_dialog");
					break;
				}
			}
		});
		
		ActionBar ab = getActionBar();
		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setCustomView(customActionBar);
	}

	private void initPictureGallery() {
		uploadedImages = new ArrayList<Bitmap>(MAX_PIC_NUMBER);
		galleryAdapter = new ReportPhotoListAdapter(this, uploadedImages);
		imagesLocation = new String[MAX_PIC_NUMBER];
		
		mGallery.setSpacing(10);
		mGallery.setAdapter(galleryAdapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				startImageDetail(position);
				
			}
		});
	}
	
	private void startImageDetail(int position) {
		Intent intent = new Intent();
		intent.setClass(this, ImageDetailActivity.class);
		intent.putExtra("PARENT_KEY", 11);
		intent.putExtra("IMAGE_LOC", imagesLocation[position]);
		
		startActivity(intent);
	}
}
