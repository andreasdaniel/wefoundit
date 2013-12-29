package org.alphacloud.wefoundit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.alphacloud.wefoundit.adapter.ReportPhotoListAdapter;
import org.alphacloud.wefoundit.logic.ImageHandler;
import org.alphacloud.wefoundit.logic.ShareData;
import org.alphacloud.wefoundit.logic.UtilTool;
import org.alphacloud.wefoundit.model.Category;
import org.alphacloud.wefoundit.model.City;
import org.alphacloud.wefoundit.model.Country;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
public class ReportLostActivity extends Activity implements OnDateSetListener, OnTimeSetListener {
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
	
	private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, MMM dd, yyyy");
	private final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("hh:mma");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_lost);
		
		// init fields
		mTimeTextView = (TextView) findViewById(R.id.txt_losttime);
		mDateTextView = (TextView) findViewById(R.id.txt_lostdate);
		mCountrySpinner = (Spinner) findViewById(R.id.spinner_lostcountry);
		mCitySpinner = (Spinner) findViewById(R.id.spinner_lostcity);
		mCatSpinner = (Spinner) findViewById(R.id.spinner_lostcat);
		mAccessGalleryButton = (ImageButton) findViewById(R.id.imagBtn_lostaccgal);
		mGallery =(Gallery) findViewById(R.id.gallery_lostpics);
		
		initCustomActionBar();
		initPictureGallery();
		
		// set current date
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		mDateTextView.setText(DATE_FORMATTER.format(cal.getTime()));
		mTimeTextView.setText(TIME_FORMATTER.format(cal.getTime()));
		
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
		ArrayAdapter<Category> adpt = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, ShareData.CATEGORIES);
		adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCatSpinner.setAdapter(adpt);
		
		mCitySpinner.setEnabled(false);
		
		ArrayAdapter<Country> countryAdpt = new ArrayAdapter<Country>(this, android.R.layout.simple_spinner_item, ShareData.COUNTRIES);
		countryAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCountrySpinner.setAdapter(countryAdpt);
		mCountrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
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
		//getMenuInflater().inflate(R.menu.report_lost, menu);
		return true;
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
				// TODO Auto-generated method stub
				
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
		DialogFragment newFragment = new DatePickerDialogFragment(ReportLostActivity.this);
		newFragment.show(ft, "date_dialog");
	}

	private void showTimeDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = new TimePickerDialogFragment(ReportLostActivity.this);
		newFragment.show(ft, "date_dialog");
	}
	
	private void inCountrySelected(int position) {
		if(position == 0) {
			mCitySpinner.setEnabled(false);
		}
		else {
			mCitySpinner.setEnabled(true);
			String code = ShareData.COUNTRIES.get(position).getCode();
			List<City> selectedCity = City.getCitiesByCountry(ShareData.CITIES, code);
			
			ArrayAdapter<City> cityAdpt = new ArrayAdapter<City>(this, android.R.layout.simple_spinner_item, selectedCity);
			cityAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mCitySpinner.setAdapter(cityAdpt);
		}
	}
	
	private void accessPhotoGallery() {
		Intent intent = new Intent();  
		intent.setType("image/*");  
		intent.setAction(Intent.ACTION_GET_CONTENT);  
		startActivityForResult(Intent.createChooser(intent, "Choose Picture"), GALLERY_RCODE);
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
}
