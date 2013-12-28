package org.alphacloud.wefoundit;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;

@SuppressLint("ValidFragment")
public class DatePickerDialogFragment extends DialogFragment {
	// fields
	private OnDateSetListener mDateListener;
	
	public DatePickerDialogFragment() {
		
	}
	
	public DatePickerDialogFragment(OnDateSetListener callback) {
		mDateListener = callback;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Calendar cal = Calendar.getInstance();
		
		return new DatePickerDialog(getActivity(), mDateListener,
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
	}
}
