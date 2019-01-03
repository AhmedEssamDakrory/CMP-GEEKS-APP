package com.example.android.cmpgeeks.CreateUpdateActivity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.cmpgeeks.Constants;
import com.example.android.cmpgeeks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateUpdateMaterial extends AppCompatActivity {
    //layout Components
    private EditText mTopicEdit;
    private EditText mLinkEdit;
    private Spinner mSpinner;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_material);
        getSupportActionBar().setTitle("Create Material");
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTopicEdit = (EditText) findViewById(R.id.material_topic_edittext);
        mLinkEdit = (EditText) findViewById(R.id.material_link_edittext);
        mSpinner = (Spinner) findViewById(R.id.select_type_spinner);
        mButton = (Button) findViewById(R.id.create_material_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constants.selectedMaterial != null) {
                    setResult(Constants.UPDATE_MATERIAL_RESULT_OK);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                } else {
                    if (mTopicEdit.getText().toString().length()>3) {
                        InsertMaterial();
                        setResult(Constants.CREATE_MATERIAL_RESULT_OK);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    } else {
                        Toast.makeText(getBaseContext(),"Invalid Topic",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        if (Constants.selectedMaterial != null) {
            mTopicEdit.setText(Constants.selectedMaterial.getTopic());
            mLinkEdit.setText(Constants.selectedMaterial.getmLink());
            mSpinner.setSelection(Constants.selectedMaterial.getmMaterialType().ordinal());
            mButton.setText("Update");
            getSupportActionBar().setTitle("Update Material");
        }
    }

    @Override
    public void onBackPressed() {
        if (Constants.selectedMaterial != null) {
            Constants.selectedMaterial=null;
            setResult(Constants.UPDATE_MATERIAL_RESULT_FAILED);
        } else {
            setResult(Constants.CREATE_MATERIAL_RESULT_FAILED);
        }
        super.onBackPressed();

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

    private void InsertMaterial() {

        final String link = mLinkEdit.getText().toString();
        final String type = mSpinner.getSelectedItem().toString();
        final String topic = mTopicEdit.getText().toString();
        final String courseId = Constants.selectedCourse.getmID();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.ADD_MATERIALS_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonobject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Link", link);
                params.put("Type", type);
                params.put("CourseId", courseId);
                params.put("Topic", topic);
                return params;
            }
        };

        try {
            //set time in mili
            Thread.sleep(100);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }
}
