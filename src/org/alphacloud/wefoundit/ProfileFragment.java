package org.alphacloud.wefoundit;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	// fields
	private TextView mUserTextView;
	private EditText mNameEditText;
	private EditText mPhoneEditText;
	private EditText mEmailEditText;
	private TextView mDonationTextView;
	private TextView mBumpTextView;
	private Button mUpdateButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);	
		
        return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// init fields
		mUserTextView = (TextView) view.findViewById(R.id.txtView_profuser1);
		mNameEditText = (EditText) view.findViewById(R.id.edtxt_profname);
		mPhoneEditText = (EditText) view.findViewById(R.id.edtxt_profphone);
		mEmailEditText = (EditText) view.findViewById(R.id.edtxt_profemail);
		mDonationTextView = (TextView) view.findViewById(R.id.txtView_profdonation1);
		mBumpTextView = (TextView) view.findViewById(R.id.txtView_profbump1);
		mUpdateButton = (Button) view.findViewById(R.id.btn_profupdate);
		
		
	}
	
}
