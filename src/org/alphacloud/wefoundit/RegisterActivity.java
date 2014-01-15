package org.alphacloud.wefoundit;

import java.util.ArrayList;
import java.util.List;

import org.alphacloud.wefoundit.model.DbConn;
import org.alphacloud.wefoundit.util.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

public class RegisterActivity extends Activity{
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText username;
	EditText userusername;
	EditText useremail;
	EditText userpassword;
	EditText userphone;
	Button reg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// Edit Text
		username = (EditText) findViewById(R.id.edtxt_regfullname);
		userusername = (EditText) findViewById(R.id.edtxt_regusername);
		useremail = (EditText) findViewById(R.id.edtxt_regemail);
		userpassword = (EditText) findViewById(R.id.edtxt_regpassword);
		userphone = (EditText) findViewById(R.id.edtxt_regphone);

		// Create button
		reg = (Button) findViewById(R.id.but_regsignup);
	}
	
	public void signup(View view) {
		new Register().execute();
		Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
		startActivity(i);
	}

	/**
	 * Background Async Task to Register User
	 * */
	class Register extends AsyncTask<String, String, String> {
		int success = 0;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage("Registering user, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String name = username.getText().toString();
			String username = userusername.getText().toString();
			String email = useremail.getText().toString();
			String password = userpassword.getText().toString();
			String phone = userphone.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", name));
			params.add(new BasicNameValuePair("userusername", username));
			params.add(new BasicNameValuePair("useremail", email));
			params.add(new BasicNameValuePair("userpassword", password));
			params.add(new BasicNameValuePair("userphone", phone));

			// getting JSON Object
			// Note that register user url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(DbConn.REGISTER,
					"POST", params);
			
			// check log cat from response
			Log.d("Register", json.toString());

			// check for success tag
			try {
				success = json.getInt("success");

				
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
				Toast.makeText(getApplicationContext(),
	            		"Registration Success", Toast.LENGTH_LONG).show();
				
			} else {
				Toast.makeText(getApplicationContext(),
	            		"Registration Failed", Toast.LENGTH_LONG).show();
			}
		}
	}
}
