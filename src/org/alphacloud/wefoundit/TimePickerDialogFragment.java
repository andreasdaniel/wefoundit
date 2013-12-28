package org.alphacloud.wefoundit;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class TimePickerDialogFragment extends DialogFragment {
	// fields
	private OnTimeSetListener mTimeListener;
	
	public TimePickerDialogFragment() {
		
	}
	
	public TimePickerDialogFragment(OnTimeSetListener callback) {
		mTimeListener = callback;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Calendar cal = Calendar.getInstance();
		
		return new TimePickerDialog(getActivity(), mTimeListener,
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
	}
}
