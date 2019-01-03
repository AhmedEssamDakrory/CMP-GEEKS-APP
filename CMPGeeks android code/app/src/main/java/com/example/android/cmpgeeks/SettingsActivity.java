package com.example.android.cmpgeeks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private EditText mFName;
    private EditText mLName;
    private EditText mEmail;
    private EditText mPassword;
    private Spinner mSpinner;
    private EditText mForthEditText;
    private EditText mFifthEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Constants.user_type == Constants.USER_TYPE.TEACHER) {
            setContentView(R.layout.activity_sign_up_doctors);
            mSpinner = (Spinner) findViewById(R.id.year_sign_up);
            mForthEditText = (EditText) findViewById(R.id.gy_sign_up);
            mForthEditText.setText(Integer.toString(Constants.user.getmGY()));
            mFifthEditText = null;
            mSpinner.setSelection(Constants.user.getmType().ordinal());
        } else {
            setContentView(R.layout.activity_sign_up);
            mSpinner = (Spinner) findViewById(R.id.year_sign_up);
            mForthEditText = (EditText) findViewById(R.id.sec_sign_up);
            mFifthEditText = (EditText) findViewById(R.id.bn_sign_up);
            mForthEditText.setText(Integer.toString(Constants.user.getmSEC()));
            mFifthEditText.setText(Integer.toString(Constants.user.getmBN()));
            mSpinner.setSelection(Constants.user.getmYear() - 1);
        }

        mFName = (EditText) findViewById(R.id.first_name);
        mLName = (EditText) findViewById(R.id.last_name_edittext);
        mEmail = (EditText) findViewById(R.id.email_sign_up);
        mPassword = (EditText) findViewById(R.id.password_sign_up);
        mButton = (Button) findViewById(R.id.email_sign_in_button);

        mFName.setText(Constants.user.getmFName());
        mLName.setText(Constants.user.getmLName());
        mEmail.setText(Constants.user.getmEMail());
        mPassword.setText(Constants.user.getmPassword());
        mButton.setText("Update");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Constants.UPDAE_INFO_SUCCESSSFULLY);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(Constants.UPDATE_INFO_FAILED);
        super.onBackPressed();
        return;
    }
}
