package org.alphacloud.wefoundit;

import java.util.ArrayList;
import java.util.List;

import org.alphacloud.wefoundit.logic.SessionManager;
import org.alphacloud.wefoundit.model.DbConn;
import org.alphacloud.wefoundit.util.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private ProgressDialog pDialog;

	JSONParser jp;

	EditText etemail;
	EditText etpassword;
	Button tvlogin;
	Button tvsignup;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		jp = new JSONParser();

		// edit text
		etemail = (EditText) findViewById(R.id.edtxt_loginemail1);
		etpassword = (EditText) findViewById(R.id.edtxt_loginpassword1);

		// text view
		tvlogin = (Button) findViewById(R.id.txtview_loginlogin);
		tvsignup = (Button) findViewById(R.id.txtview_loginsignup);
		
		tvlogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Login().execute();
			}
		});
		
		tvsignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(i);
			}
		});
		
		getActionBar().setDisplayShowHomeEnabled(true);
	}

	public class Login extends AsyncTask<String, String, String> {
		JSONObject user;
		int success;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Logining user, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Login in background thread
		 * */
		protected String doInBackground(String... params) {
			String email = etemail.getText().toString();
			String password = etpassword.getText().toString();
			Log.d("email", email);
			Log.d("password", password);

			// Building Parameters
			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("useremail", email));
			nvp.add(new BasicNameValuePair("userpassword", password));
			Log.d("Building Parameters", "okay");

			// getting login details by making HTTP request
			JSONObject json = jp.makeHttpRequest(DbConn.LOGIN, "POST", nvp);

			// check your log for json response
			Log.d("Login", json.toString());

			// json success tag
			try {
				success = json.getInt("success");

				if (success == 1) {
					Log.d("Success", "success");
					// successfully login
					JSONArray userObj = json.getJSONArray("user"); // JSON Array

					// get first user object from JSON Array
					user = userObj.getJSONObject(0);
					
					SessionManager manager = new SessionManager(LoginActivity.this);
					manager.createLoginSession(user.getString("userusername"), user.getString("useremail"),
							user.getInt("userid"), user.getString("userphone"));
					
					
				} else {
					
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
			if (success == 1) {
				Toast.makeText(getApplicationContext(),
	            		"Login Success", Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(),
	            		"Wrong Username or Password. Try Again", Toast.LENGTH_LONG).show();
			}
		}
	}

}
