package org.alphacloud.wefoundit;

import java.util.ArrayList;
import java.util.List;

import org.alphacloud.wefoundit.util.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
	// fields
	private TextView mUserTextView;
	private EditText mNameEditText;
	private EditText mPhoneEditText;
	private EditText mEmailEditText;
	private TextView mDonationTextView;
	private TextView mBumpTextView;
	private Button mUpdateButton;
	private ProgressDialog pDialog;
	
	private JSONParser jp;
	private String urlProf;
	private String urlUpProf;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);	
		
        return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		jp = new JSONParser();
		urlProf = "http://140.113.210.89/wefoundit/profile.php";
		urlUpProf = "http://140.113.210.89/wefoundit/updateprofile.php";
		
		// init fields
		mUserTextView = (TextView) view.findViewById(R.id.txtView_profuser1);
		mNameEditText = (EditText) view.findViewById(R.id.edtxt_profname);
		mPhoneEditText = (EditText) view.findViewById(R.id.edtxt_profphone);
		mEmailEditText = (EditText) view.findViewById(R.id.edtxt_profemail);
		mDonationTextView = (TextView) view.findViewById(R.id.txtView_profdonation1);
		mBumpTextView = (TextView) view.findViewById(R.id.txtView_profbump1);
		mUpdateButton = (Button) view.findViewById(R.id.btn_profupdate);	
		
		mUpdateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new UpdateProfile().execute();
			}
		});
		
		new Profile().execute();
	}
	
	public class Profile extends AsyncTask<String, String, String> {
		JSONObject user;
		int success;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// ??
			pDialog = new ProgressDialog(ProfileFragment.this.getActivity());
			pDialog.setMessage("See profile, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... params) {
			int id = ((MainActivity) getActivity()).sessionManager.getUserID();
			Log.d("id", id + "");

			// Building Parameters
			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("userid", id + ""));
			Log.d("Building Parameters", "okay");

			// getting login details by making HTTP request
			JSONObject json = jp.makeHttpRequest(urlProf, "POST", nvp);

			// json success tag
			try {
				// check your log for json response
				Log.d("Login", json.toString());
				
				success = json.getInt("success");

				if (success == 1) {
					Log.d("success", "success");
					// successfully get user profile
					JSONArray userObj = json.getJSONArray("user"); // JSON Array

					// get first user object from JSON Array
					user = userObj.getJSONObject(0);
				} else {
					// unsuccessful

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
			try {
				if (success == 1) {
					if (user != null) {
						mUserTextView.setText(user.getString("userusername"));
						mNameEditText.setText(user.getString("username"));
						mPhoneEditText.setText(user.getString("userphone"));
						mEmailEditText.setText(user.getString("useremail"));
						mDonationTextView.setText(user.getString("userdonation"));
						mBumpTextView.setText(user.getString("userprivilege"));
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
		            		"Cannot Get Profile Info", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class UpdateProfile extends AsyncTask<String, String, String> {
		JSONObject user;
		int success;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// ??
			pDialog = new ProgressDialog(ProfileFragment.this.getActivity());
			pDialog.setMessage("Update profile, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... params) {
			int id = ((MainActivity) getActivity()).sessionManager.getUserID();
			String name = mNameEditText.getText().toString();
			String phone = mPhoneEditText.getText().toString();
			String email = mEmailEditText.getText().toString();
			Log.d("id", id + "");

			// Building Parameters
			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("userid", id + ""));
			nvp.add(new BasicNameValuePair("username", name));
			nvp.add(new BasicNameValuePair("userphone", phone));
			nvp.add(new BasicNameValuePair("useremail", email));
			Log.d("Building Parameters", "okay");

			// getting login details by making HTTP request
			JSONObject json = jp.makeHttpRequest(urlUpProf, "POST", nvp);

			// check your log for json response
			Log.d("Login", json.toString());

			// json success tag
			try {
				success = json.getInt("success");

				if (success == 1) {
					Log.d("success", "success");
					// successfully get user profile
					JSONArray userObj = json.getJSONArray("user"); // JSON Array

					// get first user object from JSON Array
					user = userObj.getJSONObject(0);
				} else {
					// unsuccessful

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
			try {
				if (success == 1) {
					Toast.makeText(getActivity().getApplicationContext(),
		            		"Profile Updated", Toast.LENGTH_LONG).show();
					if (user != null) {
						mUserTextView.setText(user.getString("userusername"));
						mNameEditText.setText(mNameEditText.getText().toString());
						mPhoneEditText.setText(mPhoneEditText.getText().toString());
						mEmailEditText.setText(mEmailEditText.getText().toString());
						mDonationTextView.setText(user.getString("userdonation"));
						mBumpTextView.setText(user.getString("userprivilege"));
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
		            		"Failed Update Profile", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
