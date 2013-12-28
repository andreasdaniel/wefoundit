package org.alphacloud.wefoundit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("SimpleDateFormat")
public class ReportLostActivity extends Activity implements OnDateSetListener, OnTimeSetListener {
	// fields
	private TextView mTimeTextView;
	private TextView mDateTextView;
	
	private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, MMM dd, yyyy");
	private final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("hh:mma");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_lost);
		
		// init fields
		mTimeTextView = (TextView) findViewById(R.id.txt_losttime);
		mDateTextView = (TextView) findViewById(R.id.txt_lostdate);
		
		initCustomActionBar();
		
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.report_lost, menu);
		return true;
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
}
