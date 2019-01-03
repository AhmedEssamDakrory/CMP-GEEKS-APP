package com.example.android.cmpgeeks;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgetPassword extends AppCompatActivity {

    private Button mSendButton;
    private EditText mEditText;
    private TextView mWrongEmailMess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSendButton =(Button)findViewById(R.id.send_button_forget_password);
        mEditText =(EditText)findViewById(R.id.get_email_forget_password);
        mWrongEmailMess= (TextView)findViewById(R.id.wrong_email_text_foget);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put your logic here
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
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
