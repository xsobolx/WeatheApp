package com.sablegmail.masta.stromy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by admin on 29.09.2015.
 */
public class NetworkUnavailableDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.error_title)
                .setMessage(R.string.network_unavailable_message)
                .setPositiveButton(R.string.error_ok_button_text, null);
        return builder.create();
    }
}
