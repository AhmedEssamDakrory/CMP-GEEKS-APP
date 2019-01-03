package com.example.android.cmpgeeks.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.example.android.cmpgeeks.Constants;
import com.example.android.cmpgeeks.CourseActivity;

@SuppressLint("ValidFragment")
public class SubmitِOrDescriptionDialog extends AppCompatDialogFragment {

    private CourseActivity.MODE_COURSE cmode;

    @SuppressLint("ValidFragment")
    public SubmitِOrDescriptionDialog(CourseActivity.MODE_COURSE cmode) {
        this.cmode = cmode;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Open Web")
                .setMessage("For this item")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (cmode == CourseActivity.MODE_COURSE.ASSIGNMENT) {
                            composeEmail(Constants.selectedAssignment.getmSubmitEmail(), "Assignment  " + Integer.toString(Constants.selectedAssignment.getmNumber()), null);
                            Constants.selectedAssignment=null;
                        } else if(cmode==CourseActivity.MODE_COURSE.PROJECT){
                            composeEmail(Constants.selectedProject.getmSubmitEmail(),Constants.selectedProject.getmTitle(), null);
                            Constants.selectedProject=null;
                        }
                    }
                })
                .setNegativeButton("Description", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cmode == CourseActivity.MODE_COURSE.ASSIGNMENT) {
                            openWeb(Constants.selectedAssignment.getmLink());
                            Constants.selectedAssignment =null;
                        } else if(cmode==CourseActivity.MODE_COURSE.PROJECT){
                            openWeb(Constants.selectedProject.getmLink());
                            Constants.selectedProject =null;
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    private void composeEmail(String addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{addresses});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openWeb(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        try {
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(getContext(),"Invalid Link",Toast.LENGTH_SHORT).show();
        }
    }
}
