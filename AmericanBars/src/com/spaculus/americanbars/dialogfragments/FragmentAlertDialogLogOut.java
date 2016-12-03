package com.spaculus.americanbars.dialogfragments;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class FragmentAlertDialogLogOut extends DialogFragment {

	/* A flag to know from which screen user is redirected. */
	public static FragmentAlertDialogLogOut newInstance(String title, String message, String posButtonText,
			String negButtonText) {
		FragmentAlertDialogLogOut frag = new FragmentAlertDialogLogOut();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putString("posButtonText", posButtonText);
		args.putString("negButtonText", negButtonText);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		String message = getArguments().getString("message");
		String posButtonText = getArguments().getString("posButtonText");
		String negButtonText = getArguments().getString("negButtonText");

		return new AlertDialog.Builder(getActivity()).setIcon(R.drawable.alert).setTitle(title).setMessage(message)
				.setPositiveButton(posButtonText, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						try {
							// Logout functionality
							((BaseActivity) getActivity()).doPositiveClick();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).setNegativeButton(negButtonText, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						try {
							// In this case, do not need to do anything
							((BaseActivity) getActivity()).doNegativeClick();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).create();
	}
}
