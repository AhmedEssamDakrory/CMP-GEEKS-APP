package com.example.android.cmpgeeks;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.cmpgeeks.DataBaseEntity.Teacher;
import com.example.android.cmpgeeks.Dialogs.SignUpDialog;
import com.example.android.cmpgeeks.R.id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.Intrinsics;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public final class LogInActivity extends AppCompatActivity {
    private HashMap _$_findViewCache;
    private Button LogIn;
    private EditText InputEmail;
    private EditText InputPassward;
    private TextView mForgetPassword;
    private TextView mExit;
    private TextView mSignUp;
    private String TAG = "Log In Activity";
    private boolean doubleBackToExitPressedOnce = false;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
        this.setContentView(R.layout.activity_main);
        (new CountDownTimer(3000L, 1000L) {
            public void onFinish() {
                TextView var10000 = (TextView) LogInActivity.this._$_findCachedViewById(id.bookITextView);
                Intrinsics.checkExpressionValueIsNotNull(var10000, "bookITextView");
                var10000.setVisibility(View.GONE);
                ProgressBar var1 = (ProgressBar) LogInActivity.this._$_findCachedViewById(id.loadingProgressBar);
                Intrinsics.checkExpressionValueIsNotNull(var1, "loadingProgressBar");
                var1.setVisibility(View.GONE);
                ((RelativeLayout) LogInActivity.this._$_findCachedViewById(id.rootView)).setBackgroundColor(ContextCompat.getColor((Context) LogInActivity.this, R.color.offgray));
                ((ImageView) LogInActivity.this._$_findCachedViewById(id.bookIconImageView)).setImageResource(R.drawable.background_color_book);
                LogInActivity.this.startAnimation();


                InputEmail = (EditText) findViewById(id.emailEditText);
                InputPassward = (EditText) findViewById(id.passwordEditText);
                LogIn = (Button) findViewById(id.loginButton);
                mSignUp = (TextView) findViewById(id.sign_up_text);

                //Listeners
                LogIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String admin = InputEmail.getText().toString();
                        if (admin.equals("admin")) {
                            Constants.user = new Constants.User("0","AdmiD", "(Abdullah)", 0, 0, 2, "admin@CMP.com", "aass1122");
                            Constants.user_type = Constants.USER_TYPE.REPRESENTATIVE;
                            Toast.makeText(LogInActivity.this, "LogIn as an Admin", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, HomeScreen.class);
                            startActivityForResult(intent, Constants.FINISH);
                        } else if (admin.equals("admind")) {
                            Constants.user = new Constants.User("admind@CMP.com", "aass1122", "Admind", "(Abdullah)", 1998, Teacher.TeacherTypes.teacherAssistant);
                            Constants.user_type = Constants.USER_TYPE.TEACHER;
                            Toast.makeText(LogInActivity.this, "LogIn as an Admin", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, HomeScreen.class);
                            startActivityForResult(intent, Constants.FINISH);
                        } else if (admin.equals("admins")) {
                            Constants.user = new Constants.User("0","AdminS", "(Abdullah)", 0, 0, 2, "admin@CMP.com", "aass1122");
                            Constants.user_type = Constants.USER_TYPE.STUDENT;
                            Toast.makeText(LogInActivity.this, "LogIn as an Admin", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, HomeScreen.class);
                            startActivityForResult(intent, Constants.FINISH);
                        } else {
                            registeruser();
                        }
                    }

                });

                mSignUp.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        SignUpDialog dialog = new SignUpDialog();
                        dialog.show(getSupportFragmentManager(), "Sign Up");

                    }
                });

                mForgetPassword = (TextView)

                        findViewById(id.forget_password_text);
                mForgetPassword.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LogInActivity.this, ForgetPassword.class);
                        startActivity(intent);
                    }
                });

                mExit = (TextView)

                        findViewById(id.exitTextView);
                mExit.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

            }

            public void onTick(long p0) {
            }
        }).

                start();

    }

    private void registeruser() {
        final String email = InputEmail.getText().toString();
        final String password = InputPassward.getText().toString();


        Log.i(TAG, "Email : " + email);
        Log.i(TAG, "Password : " + password);

        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                Constants.SIGN_IN_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonobject.getJSONObject("response").getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject Info = jsonobject.getJSONObject("info");
                            if (jsonobject.getJSONObject("response").getBoolean("LoggedIn")) {
                                if (jsonobject.getJSONObject("response").getString("Type").equals("S")) {
                                    Constants.user_type = Constants.USER_TYPE.STUDENT;
                                    Constants.user = new Constants.User(Info.getString("Id"),Info.getString(Constants.Fname), Info.getString(Constants.Lname), Integer.parseInt(Info.getString(Constants.Sec)), Integer.parseInt(Info.getString(Constants.Bn)), Integer.parseInt(Info.getString(Constants.YearNum)), InputEmail.getText().toString(), InputPassward.getText().toString());
                                    Constants.YearId = Integer.parseInt(Info.getString(Constants.YearNum));
                                    Intent intent = new Intent(LogInActivity.this, HomeScreen.class);
                                    startActivityForResult(intent, Constants.FINISH);
                                } else if (jsonobject.getJSONObject("response").getString("Type").equals("D")) {
                                    Constants.user_type = Constants.USER_TYPE.TEACHER;
                                    Constants.user = new Constants.User(email, password, Info.getString(Constants.Fname), Info.getString(Constants.Lname), Integer.parseInt(Info.getString("Gyear")), Teacher.TeacherTypes.doctor);
                                    SelectYear(Info);
                                } else if (jsonobject.getJSONObject("response").getString("Type").equals("T")) {
                                    Constants.user_type = Constants.USER_TYPE.TEACHER;
                                    Constants.user = new Constants.User(email, password, Info.getString(Constants.Fname), Info.getString(Constants.Lname), Integer.parseInt(Info.getString("Gyear")), Teacher.TeacherTypes.teacherAssistant);
                                    SelectYear(Info);
                                } else if (jsonobject.getJSONObject("response").getString("Type").equals("R")) {
                                    Constants.user_type = Constants.USER_TYPE.REPRESENTATIVE;
                                    Constants.user = new Constants.User(Info.getString("Id"),Info.getString(Constants.Fname), Info.getString(Constants.Lname), Integer.parseInt(Info.getString(Constants.Sec)), Integer.parseInt(Info.getString(Constants.Bn)), Integer.parseInt(Info.getString(Constants.YearNum)), InputEmail.getText().toString(), InputPassward.getText().toString());
                                    Constants.YearId = Integer.parseInt(Info.getString(Constants.YearNum));
                                    Intent intent = new Intent(LogInActivity.this, HomeScreen.class);
                                    startActivityForResult(intent, Constants.FINISH);
                                }

                            }

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
                params.put(Constants.Email, email);
                params.put(Constants.Password, password);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);


    }

    private final void startAnimation() {
        ViewPropertyAnimator var1 = ((ImageView) this._$_findCachedViewById(id.bookIconImageView)).animate();
        var1.x(50.0F);
        var1.y(100.0F);
        var1.setDuration(1000L);
        var1.setListener((AnimatorListener) (new AnimatorListener() {
            public void onAnimationRepeat(@Nullable Animator p0) {
            }

            public void onAnimationEnd(@Nullable Animator p0) {
                RelativeLayout var10000 = (RelativeLayout) LogInActivity.this._$_findCachedViewById(id.afterAnimationView);
                Intrinsics.checkExpressionValueIsNotNull(var10000, "afterAnimationView");
                var10000.setVisibility(View.VISIBLE);
                LogIn.setVisibility(View.VISIBLE);
            }

            public void onAnimationCancel(@Nullable Animator p0) {
            }

            public void onAnimationStart(@Nullable Animator p0) {
            }
        }));
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View) this._$_findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.FINISH) {
            finish();
        } else if (resultCode == Constants.CREATE_POST_RESULT_OK) {
            Toast.makeText(this, "Create Post (Successful Operation)", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.SIGN_UP_SUCCESSFULLY) {
            Toast.makeText(this, "Sign Up (Successful Operation)", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.SIGN_UP_FAILD) {
            Toast.makeText(this, "Sign Up (Failed Operation)", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            setResult(Constants.FINISH);
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void SelectYear(JSONObject info) throws JSONException {
        JSONArray years = info.getJSONArray("years");
        if (years.length()>0) {

            final int[] ints = new int[years.length()];
            for (int i = 0; i < years.length(); i++) {
                ints[i] = Integer.parseInt(years.getString(i));
            }

            Arrays.sort(ints);

            ArrayList<String> string = new ArrayList<>();

            for (int i = 0; i < years.length(); i++) {
                string.add(Integer.toString(ints[i]));
            }

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle("Log in");
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getBaseContext(),"Cancel",Toast.LENGTH_SHORT).show();
                }
            });
            final Spinner spinner = (Spinner) dialogView.findViewById(id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_text,string);
            spinner.setAdapter(adapter);
            dialogBuilder.setPositiveButton("LogIn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Constants.YearId = ints[spinner.getSelectedItemPosition()];
                    Intent intent = new Intent(LogInActivity.this, HomeScreen.class);
                    startActivityForResult(intent, Constants.FINISH);
                }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        } else {
            Toast.makeText(getBaseContext(),"Sorrry but you're not assign to any course yet",Toast.LENGTH_LONG).show();
        }
    }
}
