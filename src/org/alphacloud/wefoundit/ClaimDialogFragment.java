package org.alphacloud.wefoundit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ClaimDialogFragment extends DialogFragment {
	// fields
	private String email;
	private String phone;
		
	public ClaimDialogFragment(String email, String phone) {
		this.email = email;
		this.phone = phone;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().setTitle("Identity");
		dialog.setContentView(R.layout.dialog_fragment_claim);
		
		((TextView)dialog.findViewById(R.id.txt_claimemail)).setText(email);
		((TextView)dialog.findViewById(R.id.txt_claimphone)).setText(phone);
		((Button)dialog.findViewById(R.id.button_claimclose)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		
		return dialog;
	}
}
