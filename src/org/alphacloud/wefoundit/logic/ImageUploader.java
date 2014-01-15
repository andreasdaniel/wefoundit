package org.alphacloud.wefoundit.logic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.alphacloud.wefoundit.model.DbConn;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ImageUploader {
	// fields
	private Context context;
	private String json;

	public ImageUploader(Context context) {
		this.context = context;
		this.json = "";
	}

	public boolean uploadImage(String path, String name) {
		Log.d("UPLOAD_IMG", path + " -> " + name);

		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;

		String pathToOurFile = path;
		String urlServer = DbConn.UPLOAD_IMAGE;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					pathToOurFile));

			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ name + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			String serverResponseMessage = connection.getResponseMessage();

			fileInputStream.close();
			outputStream.flush();
			outputStream.close();

			Log.d("UPLOAD_IMG", serverResponseCode + ":"
					+ serverResponseMessage);

			// JSONObject json = new JSONObject(serverResponseMessage);
			if (serverResponseMessage.compareToIgnoreCase("OK") == 0) {
				// Toast.makeText(context, "Image is uploaded successfully",
				// Toast.LENGTH_LONG).show();

				//return true;
			} else {
				// Toast.makeText(context, "Image cannot be upload",
				// Toast.LENGTH_LONG).show();

				//return false;
			}

			inputStream = new DataInputStream(connection.getInputStream());
			int success = 0;
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				inputStream.close();
				json = sb.toString();
				
				JSONObject js = new JSONObject(json);
				
				success = js.getInt("success");
				
				Log.d("UPLOAD_IMG",js.getString("message"));
				
				
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			if(success == 1) {
				return true;
			}
			else {
				return false;
			}
			
		} catch (Exception ex) {
			Log.d("UPLOAD_IMG", ex.getMessage());
			// Toast.makeText(context, "Error in uploading Image",
			// Toast.LENGTH_LONG).show();

			return false;
		}
	}

}
