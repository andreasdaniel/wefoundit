package org.alphacloud.wefoundit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.alphacloud.wefoundit.adapter.ReportPhotoListAdapter;
import org.alphacloud.wefoundit.logic.ImageHandler;
import org.alphacloud.wefoundit.logic.ImageUploader;
import org.alphacloud.wefoundit.logic.SessionManager;
import org.alphacloud.wefoundit.logic.ShareData;
import org.alphacloud.wefoundit.model.Category;
import org.alphacloud.wefoundit.model.City;
import org.alphacloud.wefoundit.model.Country;
import org.alphacloud.wefoundit.model.DbConn;
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
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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
public class ReportLostActivity extends Activity implements OnDateSetListener,
		OnTimeSetListener {
	// fields
	private final int GALLERY_RCODE = 36;
	private final int MAX_PIC_NUMBER = 3;

	private TextView mUser;
	private TextView mTimeTextView;
	private TextView mDateTextView;
	private EditText mLocEditText;
	private EditText mDescription;
	private Spinner mCountrySpinner;
	private Spinner mCitySpinner;
	private Spinner mCatSpinner;
	private ImageButton mAccessGalleryButton;
	private Gallery mGallery;

	private List<Bitmap> uploadedImages;
	private List<String> imagesLocation;
	private ReportPhotoListAdapter galleryAdapter;

	private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			"EEE, MMM dd, yyyy");
	private final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(
			"hh:mma");

	private ProgressDialog pDialog;
	private JSONParser jsonParser;
	private SessionManager manager;
	protected String curDate;
	protected String curTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_lost);

		jsonParser = new JSONParser();

		// init fields
		mUser = (TextView) findViewById(R.id.txt_lostusid);
		mTimeTextView = (TextView) findViewById(R.id.txt_losttime);
		mDateTextView = (TextView) findViewById(R.id.txt_lostdate);
		mCountrySpinner = (Spinner) findViewById(R.id.spinner_lostcountry);
		mCitySpinner = (Spinner) findViewById(R.id.spinner_lostcity);
		mCatSpinner = (Spinner) findViewById(R.id.spinner_lostcat);
		mAccessGalleryButton = (ImageButton) findViewById(R.id.imagBtn_lostaccgal);
		mGallery = (Gallery) findViewById(R.id.gallery_lostpics);
		mLocEditText = (EditText) findViewById(R.id.txt_lostloc);
		mDescription = (EditText) findViewById(R.id.edtxt_lostdesc);

		initCustomActionBar();
		initPictureGallery();

		manager = new SessionManager(this);
		if (manager.isLogin()) {
			mUser.setText(manager.getUsername());
		}

		// set current date
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		curDate = DATE_FORMATTER.format(cal.getTime());
		curTime = TIME_FORMATTER.format(cal.getTime());
		mDateTextView.setText(curDate);
		mTimeTextView.setText(curTime);

		// date event listener
		mDateTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDateDialog();
			}
		});

		// time event listener
		mTimeTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimeDialog();
			}
		});

		// init Spinner
		ArrayAdapter<Category> adpt = new ArrayAdapter<Category>(this,
				android.R.layout.simple_spinner_item, ShareData.CATEGORIES);
		adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCatSpinner.setAdapter(adpt);

		mCitySpinner.setEnabled(false);

		ArrayAdapter<Country> countryAdpt = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, ShareData.COUNTRIES);
		countryAdpt
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCountrySpinner.setAdapter(countryAdpt);
		mCountrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				inCountrySelected(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// TODO Auto-generated method stub

			}

		});

		// accessing gallery button
		mAccessGalleryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				accessPhotoGallery();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.report_lost, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case GALLERY_RCODE:
				Log.d("IMG_PATH", data.getData().getPath());
				if (uploadedImages.size() < MAX_PIC_NUMBER) {
					String location = UtilTool.getRealPathFromURI(this,
							data.getData());
					Bitmap image = ImageHandler.decodeFile(location, 500, 500);
					uploadedImages.add(image);
					imagesLocation.add(location);
					galleryAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, "You only can upload 3 pics",
							Toast.LENGTH_LONG).show();
				}
				break;

			default:
				break;
			}
		}
	}

	private void initCustomActionBar() {
		View customActionBar = getLayoutInflater().inflate(
				R.layout.action_bar_edit_discarddone, new LinearLayout(this),
				false);
		View cancelActionView = customActionBar
				.findViewById(R.id.action_custombar_cancel);
		View doneActionView = customActionBar
				.findViewById(R.id.action_custombar_done);
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
				int city = mCitySpinner.getSelectedItemPosition();
				String desc = mDescription.getText().toString().trim();
				String add = mLocEditText.getText().toString().trim();

				if (cat > 0 && city > 0 && desc != "" && add != "")
					satisfy = true;

				if (satisfy)
					new ReportLost().execute();
				else
					Toast.makeText(getApplicationContext(),
							"Please check your field!", Toast.LENGTH_LONG)
							.show();

			}
		});
		ActionBar ab = getActionBar();
		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setCustomView(customActionBar);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		mTimeTextView.setText(TIME_FORMATTER.format(cal.getTime()));

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		mDateTextView.setText(DATE_FORMATTER.format(cal.getTime()));
	}

	private void showDateDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = new DatePickerDialogFragment(
				ReportLostActivity.this);
		newFragment.show(ft, "date_dialog");
	}

	private void showTimeDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = new TimePickerDialogFragment(
				ReportLostActivity.this);
		newFragment.show(ft, "date_dialog");
	}

	private void inCountrySelected(int position) {
		if (position == 0) {
			mCitySpinner.setEnabled(false);
		} else {
			mCitySpinner.setEnabled(true);
			String code = ShareData.COUNTRIES.get(position).getCode();
			List<City> selectedCity = City.getCitiesByCountry(ShareData.CITIES,
					code);

			ArrayAdapter<City> cityAdpt = new ArrayAdapter<City>(this,
					android.R.layout.simple_spinner_item, selectedCity);
			cityAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mCitySpinner.setAdapter(cityAdpt);
		}
	}

	private void accessPhotoGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Choose Picture"),
				GALLERY_RCODE);
	}

	private void initPictureGallery() {
		uploadedImages = new ArrayList<Bitmap>(MAX_PIC_NUMBER);
		galleryAdapter = new ReportPhotoListAdapter(this, uploadedImages);
		imagesLocation = new ArrayList<String>(MAX_PIC_NUMBER);

		mGallery.setSpacing(10);
		mGallery.setAdapter(galleryAdapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

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
		DialogInterface.OnClickListener dialogClickListener = new DialogClickListener(
				position);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to delete this picture?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	private class DialogClickListener implements
			DialogInterface.OnClickListener {
		int pos;

		public DialogClickListener(int position) {
			this.pos = position;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
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

	class ReportLost extends AsyncTask<String, String, String> {
		int success = 0;
		int img_success = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReportLostActivity.this);
			pDialog.setMessage("Upload report lost, please wait...");
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
			int city = ((City) mCitySpinner.getAdapter().getItem(
					mCitySpinner.getSelectedItemPosition())).getId();
			String desc = mDescription.getText().toString();
			String add = mLocEditText.getText().toString();
			// photo

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("rlcat", cat + ""));
			params.add(new BasicNameValuePair("rldate", date + " " + time));
			params.add(new BasicNameValuePair("rluploaddate", curDate + " "
					+ curTime));
			params.add(new BasicNameValuePair("rluser", user + ""));
			params.add(new BasicNameValuePair("rlcity", city + ""));
			params.add(new BasicNameValuePair("rldesc", desc));
			params.add(new BasicNameValuePair("rladdress", add));

			// getting JSON Object
			JSONObject json = jsonParser.makeHttpRequest(DbConn.REPORT_LOST,
					"POST", params);

			// check log cat from response
			// Log.d("Add report lost", json.toString());

			// check for success tag
			try {
				success = json.getInt("success");

				if (success == 1) {
					// successfully add report lost
					int id = json.getInt("id");
					String[] iNames = new String[imagesLocation.size()];
					for (int i = 0; i < iNames.length; i++) {
						Calendar cal = Calendar.getInstance(TimeZone
								.getDefault());
						String timeStamp = new SimpleDateFormat(
								"yyyyMMdd_HHmmss").format(cal.getTime());
						iNames[i] = "l" + id + "_" + timeStamp + "_" + i
								+ ".jpg";
					}
					// upload image
					ImageUploader imgUploader = new ImageUploader(
							ReportLostActivity.this);
					int i = 0;
					for (String path : imagesLocation) {
						if (imgUploader.uploadImage(path, iNames[i++])) {
							Log.d("UPLOAD_IMG", "Uploaded");
							uploadImagesDB(id, iNames[i-1]);
						} else
							Log.d("UPLOAD_IMG", "Cannot upload");
					}

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
			if (success == 1 && img_success == 1) {
				Toast.makeText(getApplicationContext(),
						"Lost Report Succesfully", Toast.LENGTH_LONG).show();
				
				finish();

			} else {
				Toast.makeText(getApplicationContext(),
						"Failed to Report Lost", Toast.LENGTH_LONG).show();
			}
		}
		
		private void uploadImagesDB(int id, String filename) {
			Log.d("PIC_DB", "Uploading " + filename + " to " + id);
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", id + ""));
			params.add(new BasicNameValuePair("filename", filename));

			// getting JSON Object
			JSONObject json = jsonParser.makeHttpRequest(DbConn.SAVE_PIC_LOST,
					"POST", params);
			
			try {
				img_success = json.getInt("success");
				Log.d("IMG_DB_LOST", json.getString("message"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


}
