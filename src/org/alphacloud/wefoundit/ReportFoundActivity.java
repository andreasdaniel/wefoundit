package org.alphacloud.wefoundit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.alphacloud.wefoundit.adapter.ReportPhotoListAdapter;
import org.alphacloud.wefoundit.logic.GPSTracker;
import org.alphacloud.wefoundit.logic.ImageHandler;
import org.alphacloud.wefoundit.logic.LocationAddress;
import org.alphacloud.wefoundit.logic.SessionManager;
import org.alphacloud.wefoundit.logic.ShareData;
import org.alphacloud.wefoundit.model.Category;
import org.alphacloud.wefoundit.util.JSONParser;
import org.alphacloud.wefoundit.util.UtilTool;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class ReportFoundActivity extends Activity implements OnDateSetListener, OnTimeSetListener {
	// fields
	private final int GALLERY_RCODE = 36;
	private final int CAMERA_RCODE = 37;	
	private final int MAX_PIC_NUMBER = 3;
	
	private Spinner mCatSpinner;
	private TextView mDateTextView;
	private TextView mTimeTextView;
	private EditText mLocationEditText;
	private EditText mDescEditText;
	private EditText mEmailEditText;
	private EditText mPhoneEditText;
	@SuppressWarnings("deprecation")
	private Gallery mGallery;
	private ImageButton mAccessGalleryButton;
	private ImageButton mCameraButton;
	
	private List<Bitmap> uploadedImages;
	private List<String> imagesLocation;
	private ReportPhotoListAdapter galleryAdapter;
	
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, MMM dd, yyyy");
	private final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("hh:mma");
	private ImageButton mGpsButton;
	private GPSTracker gps;
	
	private ProgressDialog pDialog;
	private JSONParser jsonParser;
	private String urlRepFound = "http://140.113.210.89/wefoundit/reportfound.php";

	private double lat, lon; // gps information

	private SessionManager manager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_found);
		
		jsonParser = new JSONParser();
		
		// init fields
		mCatSpinner = (Spinner) findViewById(R.id.spinner_foundcat);
		mDateTextView = (TextView) findViewById(R.id.txt_founddate);
		mTimeTextView = (TextView) findViewById(R.id.txt_foundtime);
		mGpsButton = (ImageButton) findViewById(R.id.btn_foundgps);
		mLocationEditText = (EditText) findViewById(R.id.edtxt_foundloc);
		mDescEditText = (EditText) findViewById(R.id.edtxt_founddesc);
		mEmailEditText = (EditText) findViewById(R.id.edtxt_foundemail);
		mPhoneEditText = (EditText) findViewById(R.id.edtxt_foundphone);
		mGallery = (Gallery) findViewById(R.id.gallery_foundpics);
		mAccessGalleryButton= (ImageButton) findViewById(R.id.imagBtn_foundaccgal);
		mCameraButton = (ImageButton) findViewById(R.id.imgBtn_foundcamera);
		
		manager = new SessionManager(this);
		if(manager.isLogin()) {
			mEmailEditText.setText(manager.getUserEmail());
			mPhoneEditText.setText(manager.getUserPhone());
		}
		
		initCustomActionBar();
		initPictureGallery();
		
		mLocationEditText.setEnabled(false);
		
		// date event listener
		mDateTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDateDialog();
			}
		});
		// set current date
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		mDateTextView.setText(DATE_FORMATTER.format(cal.getTime()));
		mTimeTextView.setText(TIME_FORMATTER.format(cal.getTime()));
		
		// time event listener
		mTimeTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTimeDialog();
			}
		});
		
		// gps button
		mGpsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getGPSLocation();
			}
		});
		
		// accessing gallery button
		mAccessGalleryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				accessPhotoGallery();
			}
		});
		
		// launch camera button
		mCameraButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startNativeCamera();
			}
		});
		
		// dummy
		//String[] cats = getResources().getStringArray(R.array.foundcat_strings);
		ArrayAdapter<Category> adpt = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, ShareData.CATEGORIES);
		adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCatSpinner.setAdapter(adpt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.report_found, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(this.gps != null) {
			this.gps.stopUsingGPS();
		}
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		mDateTextView.setText(DATE_FORMATTER.format(cal.getTime()));
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		mTimeTextView.setText(TIME_FORMATTER.format(cal.getTime()));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			switch (requestCode) {
			case GALLERY_RCODE:
				Log.d("IMG_PATH", data.getData().getPath());
				if(uploadedImages.size() < MAX_PIC_NUMBER){
					String location = UtilTool.getRealPathFromURI(this, data.getData());
					Bitmap image = ImageHandler.decodeFile(location, 500, 500);
					uploadedImages.add(image);
					imagesLocation.add(location);
					galleryAdapter.notifyDataSetChanged();
				}
				else {
					Toast.makeText(this, "You only can upload 3 pics", Toast.LENGTH_LONG).show();
				}
				break;
			case CAMERA_RCODE:
				break;
	
			default:
				break;
			}
		}
	}
	
	private void initCustomActionBar() {
		View customActionBar = getLayoutInflater().inflate(R.layout.action_bar_edit_discarddone, new LinearLayout(this), false);
		View cancelActionView = customActionBar.findViewById(R.id.action_custombar_cancel);
		View doneActionView = customActionBar.findViewById(R.id.action_custombar_done);
		cancelActionView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		doneActionView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean satisfy = false;
				
				int cat = mCatSpinner.getSelectedItemPosition();
				String email = mEmailEditText.getText().toString().trim();
				String phone = mPhoneEditText.getText().toString().trim();
				String desc = mDescEditText.getText().toString().trim();
				
				if(cat > 0 && email != "" && phone != "" & desc != "")
					satisfy = true;
				
				if(satisfy)
					new ReportFound().execute();
				else
					Toast.makeText(getApplicationContext(),
		            		"Please check your field!", Toast.LENGTH_LONG).show();
				
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
		imagesLocation = new ArrayList<String>(MAX_PIC_NUMBER);
		
		mGallery.setSpacing(10);
		mGallery.setAdapter(galleryAdapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				
				startImageDetail(position);
			}
		});
		
		mGallery.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				deleteAction(position);
				return false;
			}
			
		});
	}
	
	private void deleteAction(int position) {
		DialogInterface.OnClickListener dialogClickListener = new DialogClickListener(position);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to delete this picture?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
	}
	
	private class DialogClickListener implements DialogInterface.OnClickListener {
		int pos;
		
		public DialogClickListener(int position) {
			this.pos = position;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	            imagesLocation.remove(pos);
	            uploadedImages.remove(pos);
	            galleryAdapter.notifyDataSetChanged();
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
		intent.putExtra("IMAGE_LOC", imagesLocation.get(position));
		
		startActivity(intent);
	}
	
	private void showDateDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = new DatePickerDialogFragment(ReportFoundActivity.this);
		newFragment.show(ft, "date_dialog");
	}

	private void showTimeDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = new TimePickerDialogFragment(ReportFoundActivity.this);
		newFragment.show(ft, "time_dialog");
	}
	
	private void getGPSLocation() {
		// create class object
        gps = new GPSTracker(this);

        // check if GPS enabled     
        if(gps.canGetLocation()){
             
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
             
            // \n is for new line
            Toast.makeText(getApplicationContext(),
            		"Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            
            String dummy = (new LocationAddress(this, latitude, longitude)).getAddress();
            if(dummy != null) {
            	System.out.println(dummy);
            	 mLocationEditText.setText(dummy);
            };
        } else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
	}
	
	private void accessPhotoGallery() {
		Intent intent = new Intent();  
		intent.setType("image/*");  
		intent.setAction(Intent.ACTION_GET_CONTENT);  
		startActivityForResult(Intent.createChooser(intent, "Choose Picture"), GALLERY_RCODE);
	}
	
	private void startNativeCamera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(intent, CAMERA_RCODE);
	}
	
	class ReportFound extends AsyncTask<String, String, String> {
		int success = 0;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReportFoundActivity.this);
			pDialog.setMessage("Upload report found, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			int cat = ShareData.CATEGORIES.get(
					mCatSpinner.getSelectedItemPosition()).getId();
			String date = mDateTextView.getText().toString();
			String time = mTimeTextView.getText().toString();
			int user = manager.getUserID();
			String email = mEmailEditText.getText().toString();
			String phone = mPhoneEditText.getText().toString();
			String desc = mDescEditText.getText().toString();
			// photo

			//Log.d("report_found_data",cat + " " + date + " " + time + " " + user + " " + email + " " + phone + " " + desc);
			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("rfcat", cat + ""));
			params.add(new BasicNameValuePair("rfdate", date + " " + time));
			params.add(new BasicNameValuePair("rfuser", user + ""));
			params.add(new BasicNameValuePair("rflat", String.valueOf(lat)));
			params.add(new BasicNameValuePair("rflong", String.valueOf(lon)));
			params.add(new BasicNameValuePair("rfemail", email));
			params.add(new BasicNameValuePair("rfphone", phone));
			params.add(new BasicNameValuePair("rfdesc", desc));

			// getting JSON Object
			JSONObject json = jsonParser.makeHttpRequest(urlRepFound, "POST",
					params);

			// check log cat from response
			Log.d("Add report found", json.toString());

			// check for success tag
			try {
				success = json.getInt("success");

				if (success == 1) {
					// successfully add report found

				} else {
					// failed add report found
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
			// check for success tag
			if (success == 1) {
				Toast.makeText(getApplicationContext(),
	            		"Found Report Succesfully", Toast.LENGTH_LONG).show();
				finish();

			} else {
				Toast.makeText(getApplicationContext(),
	            		"Failed to Report Found", Toast.LENGTH_LONG).show();
			}
		}
	}
	
}
