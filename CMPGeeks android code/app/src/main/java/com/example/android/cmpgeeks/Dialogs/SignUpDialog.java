package com.example.android.cmpgeeks.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import com.example.android.cmpgeeks.Constants;
import com.example.android.cmpgeeks.SignUpActivity;
import com.example.android.cmpgeeks.SignUpDoctorsActivity;

public class SignUpDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        //View view = inflater.inflate();  layout reasors
        builder.setTitle("Sign Up")
                .setMessage("Sign up as ...")
                .setNegativeButton("Doctor or TA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), SignUpDoctorsActivity.class);
                        startActivityForResult(intent, Constants.SIGN_UP_SUCCESSFULLY);
                    }
                })
                .setPositiveButton("Student", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), SignUpActivity.class);
                        startActivityForResult(intent,Constants.SIGN_UP_SUCCESSFULLY);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
