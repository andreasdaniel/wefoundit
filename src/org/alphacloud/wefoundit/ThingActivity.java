package org.alphacloud.wefoundit;

import java.util.ArrayList;
import java.util.List;

import org.alphacloud.wefoundit.adapter.ReportPhotoListAdapter;
import org.alphacloud.wefoundit.logic.ShareData;
import org.alphacloud.wefoundit.model.City;
import org.alphacloud.wefoundit.model.FoundThing;
import org.alphacloud.wefoundit.model.LostThing;
import org.alphacloud.wefoundit.util.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
	private ImageView mImage1;
	private ImageView mImage2;
	private ImageView mImage3;
	private ImageView[] images;

	private String category;
	private String desc;
	private String date;
	private String place;
	private String claim;
	String email = "user@foundit.com";
	String phone = "+886 988811676";

	//private List<Bitmap> uploadedImages;
	private List<String> picURls;
	//private String[] imagesLocation;
	//private ReportPhotoListAdapter galleryAdapter;

	private int parent;

	Parcelable parcel;

	private ProgressDialog pDialog;
	private JSONParser jsonParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thing);

		jsonParser = new JSONParser();

		// init fields
		mCategoryTextView = (TextView) findViewById(R.id.txtview_thingcategory1);
		mDescTextView = (TextView) findViewById(R.id.txtview_thingdescription1);
		mDateTextView = (TextView) findViewById(R.id.txtview_thingdate1);
		mPlaceTextView = (TextView) findViewById(R.id.txtview_thingplace1);
		mClaimTextView = (TextView) findViewById(R.id.txtview_thingclaim);
		mImage1 = (ImageView) findViewById(R.id.imageView_thing1);
		mImage2 = (ImageView) findViewById(R.id.imageView_thing2);
		mImage3 = (ImageView) findViewById(R.id.imageView_thing3);
		images = new ImageView[3];
		images[0] = mImage1;
		images[1] = mImage2;
		images[2] = mImage3;
		
		
		getPassingArg();
		initCustomActionBar();
		
		mImage1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("THING_ACTIVITY", "click event " + picURls.size());
				if(picURls.size() >= 1) {
					startImageDetail(0);
				}
			}
		});

		mImage2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(picURls.size() >= 2) {
					startImageDetail(1);
				}

			}
		});

		mImage3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(picURls.size() >= 3) {
					startImageDetail(2);
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.thing, menu);
		return true;
	}

	private void getPassingArg() {
		Bundle bundle = getIntent().getExtras();
		parent = bundle.getInt(PARENT_KEY);
		parcel = bundle.getParcelable("DATA");
	}

	private void initCustomActionBar() {

		View customActionBar = getLayoutInflater().inflate(
				R.layout.action_bar_edit_discarddone, new LinearLayout(this),
				false);
		View cancelActionView = customActionBar
				.findViewById(R.id.action_custombar_cancel);
		View doneActionView = customActionBar
				.findViewById(R.id.action_custombar_done);
		TextView doneTextView = (TextView) customActionBar
				.findViewById(R.id.txt_saveaction);

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

		loadData();

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
					markAsDoneAction();
					break;
				case FP_PARENT:
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					DialogFragment newFragment = new ClaimDialogFragment(email,
							phone);
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

	private void loadData() {
		if (parcel instanceof FoundThing) {
			FoundThing ft = (FoundThing) parcel;
			category = ft.cat;
			desc = ft.getFoundDesc();
			date = ft.getFoundDate();
			place = ft.loc;
			claim = ft.getFoundIsClaim() == 0 ? "Have not been claimed"
					: "Have been claimed";
			email = ft.getFoundTempEmail();
			phone = ft.getFoundTempPhone();
			if(!ft.getPicURLs().isEmpty())
				picURls = ft.getPicURLs();
			else
				picURls = new ArrayList<String>();
		} else if (parcel instanceof LostThing) {
			LostThing lt = (LostThing) parcel;

			category = lt.cat;
			desc = lt.getLostDesc();
			date = lt.getLostDate();
			place = lt.getLostAdd() + ", " + lt.loc;
			claim = lt.getLostIsFound() == 0 ? "Have not been found"
					: "Have been found";
			email = lt.getLostEmail();
			phone = lt.getLostPhone();
			if(!lt.getPicURLs().isEmpty())
				picURls = lt.getPicURLs();
			else
				picURls = new ArrayList<String>();
		}

		mCategoryTextView.setText(category);
		mDescTextView.setText(desc);
		mDateTextView.setText(date);
		mPlaceTextView.setText(place);
		mClaimTextView.setText(claim);
		
		int i = 0;
		Log.d("THING_ACITIVY", picURls.size()+"");
		for(String url : picURls) {
			Picasso.with(this).load(url).resize(100, 100).into(images[i++]);
		}
	}

	private void markAsDoneAction() {
		DialogInterface.OnClickListener dialogClickListener = new DialogClickListener();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to mark as done?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	private class DialogClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				new ClaimLostFound().execute();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			}

		}

	}

	private void startImageDetail(int position) {
		Intent intent = new Intent();
		intent.setClass(this, ImageDetailActivity.class);
		intent.putExtra("PARENT_KEY", 11);
		intent.putExtra("IMAGE_LOC", picURls.get(position));
		
		Log.d("LOAD_IMAGE", picURls.get(position));
		startActivity(intent);
	}

	class ClaimLostFound extends AsyncTask<String, String, String> {
		int success = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ThingActivity.this);
			pDialog.setMessage("Claiming, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			String url = null;
			int id = 0;
			if (parcel instanceof FoundThing) {
				FoundThing ft = (FoundThing) parcel;
				id = ft.getFoundId();
				url = "http://140.113.210.89/wefoundit/claimfound.php";
			} else if (parcel instanceof LostThing) {
				LostThing lt = (LostThing) parcel;
				id = lt.getLostId();
				url = "http://140.113.210.89/wefoundit/claimlost.php";
			}

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", id + ""));

			// getting JSON Object
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

			// check log cat from response
			Log.d("Claiming", json.toString());

			// check for success tag
			try {
				success = json.getInt("success");

				if (success == 1) {
					// successfully add report lost

				} else {
					// failed add report lost
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			if (success == 1) {
				if (parcel instanceof FoundThing) {
					FoundThing ft = (FoundThing) parcel;
					ft.setFoundIsClaim(1);
				} else if (parcel instanceof LostThing) {
					LostThing lt = (LostThing) parcel;
					lt.setLostIsFound(1);
				}
				Toast.makeText(getApplicationContext(), "Claimed",
						Toast.LENGTH_LONG).show();
				finish();

			} else {
				Toast.makeText(getApplicationContext(), "Failed to claim",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
