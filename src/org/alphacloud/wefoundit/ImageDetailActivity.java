package org.alphacloud.wefoundit;

import org.alphacloud.wefoundit.logic.ImageHandler;

import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageDetailActivity extends Activity {
	// fields
	private final String PARENT_KEY = "PARENT_KEY";
	private final int FP_PARENT = 11;
	private final int RP_PARENT = 12;
	
	private int parent;
	private String imageLocation;
	
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_detail);
		
		//init fields
		mImageView = (ImageView) findViewById(R.id.imageView_detailPic);
		
		getPassingArg();
		initCustomActionBar();
		
		// init image
		Log.d("IMAGE_DETAIL", imageLocation);
		if(imageLocation.contains("http://")) {
			Picasso.with(this).load(imageLocation).resize(500, 500).into(mImageView);
		}
		else {
			Bitmap image = ImageHandler.decodeFile(imageLocation, 500, 500);
			mImageView.setImageBitmap(image);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_detail, menu);
		return true;
	}

	private void getPassingArg() {
		Bundle bundle = getIntent().getExtras();
		parent = bundle.getInt(PARENT_KEY);
		imageLocation = bundle.getString("IMAGE_LOC");
		
	}
	
	private void initCustomActionBar() {
		View customActionBar = getLayoutInflater().inflate(R.layout.action_bar_edit_discarddone, new LinearLayout(this), false);
		View cancelActionView = customActionBar.findViewById(R.id.action_custombar_cancel);
		View doneActionView = customActionBar.findViewById(R.id.action_custombar_done);
		TextView doneTextView = (TextView) customActionBar.findViewById(R.id.txt_saveaction);
		
		doneTextView.setEnabled(false);
		
		cancelActionView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		doneActionView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ActionBar ab = getActionBar();
		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setCustomView(customActionBar);
	}
	
}
