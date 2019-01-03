package com.example.android.cmpgeeks;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.cmpgeeks.Adapters.CourseAdapter;
import com.example.android.cmpgeeks.Adapters.PostAdapter;
import com.example.android.cmpgeeks.Adapters.StudentAdapter;
import com.example.android.cmpgeeks.Adapters.TeacherAdapter;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateCourse;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdatePost;
import com.example.android.cmpgeeks.DataBaseEntity.Course;
import com.example.android.cmpgeeks.DataBaseEntity.Post;
import com.example.android.cmpgeeks.DataBaseEntity.Student;
import com.example.android.cmpgeeks.DataBaseEntity.Teacher;
import com.example.android.cmpgeeks.DataBaseEntity.Year;
import com.example.android.cmpgeeks.Dialogs.UpdateOrDeleteDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UpdateOrDeleteDialog.DialogListenerHomeScreen {

    private boolean doubleBackToExitPressedOnce = false;
    private MODE mCurrentMode;

    //layout components
    private TextView mUserNameView;
    private TextView mUserEmailView;
    private ListView mListView;
    private FloatingActionButton mFloatingButton;
    private RelativeLayout emptyText;


    //Data lists
    private ArrayList<Post> mPosts;
    private ArrayList<Course> mCourses;
    private ArrayList<Student> mStudents;
    private ArrayList<Teacher> mTeachers;

    //Adapters
    private PostAdapter mPostAdapter;
    private CourseAdapter mCourseAdapter;
    private TeacherAdapter mTeacherAdapter;
    private StudentAdapter mStudentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setSubtitle("Posts");


        //Initialization of Constants


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_first).setTitle("Posts");
            menu.findItem(R.id.nav_second).setTitle("Courses");
            menu.findItem(R.id.nav_third).setTitle("Teachers contacts");
            menu.findItem(R.id.nav_fourth).setTitle("Student contacts");
            menu.findItem(R.id.nav_fifth).setVisible(false);

        }

        //Initialization
        mCurrentMode = MODE.POST;
        mPosts = new ArrayList<Post>();
        mStudents = new ArrayList<Student>();
        mTeachers = new ArrayList<Teacher>();
        mCourses = new ArrayList<Course>();
        GetCourses();
        GetContactsTeacher();
        getContactsStudents();

        mListView = (ListView) findViewById(R.id.list);
        mFloatingButton = (FloatingActionButton) findViewById(R.id.fab);
        mUserNameView = (TextView) hView.findViewById(R.id.user_name);
        mUserEmailView = (TextView) hView.findViewById(R.id.user_email);
        mUserEmailView.setText(Constants.user.getmEMail());
        mUserNameView.setText(Constants.user.getmName());
        emptyText = (RelativeLayout) findViewById(android.R.id.empty);
        mCourseAdapter = new CourseAdapter(this, mCourses);
        mTeacherAdapter = new TeacherAdapter(this, mTeachers);
        mStudentAdapter = new StudentAdapter(this, mStudents);
        mFloatingButton.setVisibility(View.GONE);


        mListView.setEmptyView(emptyText);
        //Listeners
        if (Constants.user_type != Constants.USER_TYPE.STUDENT) {
            mFloatingButton.setVisibility(View.VISIBLE);
            Toast.makeText(HomeScreen.this, "Click long on any element to update or delet", Toast.LENGTH_LONG).show();
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mCurrentMode == MODE.POST) {
                        Constants.selectedPost = mPosts.get(position);
                        UpdateOrDeleteDialog dialog = new UpdateOrDeleteDialog(mCurrentMode);
                        dialog.show(getSupportFragmentManager(), "Data Manipulation");
                    } else if (mCurrentMode == MODE.COURSE && Constants.user_type == Constants.USER_TYPE.REPRESENTATIVE) {
                        Constants.selectedCourse = mCourses.get(position);
                        UpdateOrDeleteDialog dialog = new UpdateOrDeleteDialog(mCurrentMode);
                        dialog.show(getSupportFragmentManager(), "Data Manipulation");
                    }


                    return true;
                }
            });
            mFloatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurrentMode == MODE.POST) {
                        Intent intent = new Intent(HomeScreen.this, CreateUpdatePost.class);
                        startActivityForResult(intent, Constants.CREATE_POST_RESULT_OK);
                    } else if (mCurrentMode == MODE.COURSE) {
                        Intent intent = new Intent(HomeScreen.this, CreateUpdateCourse.class);
                        startActivityForResult(intent, Constants.CREATE_COURSE_RESULT_OK);
                    }
                }
            });
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentMode == MODE.COURSE) {
                    Constants.selectedCourse = mCourses.get(position);
                    Intent intent = new Intent(HomeScreen.this, CourseActivity.class);
                    startActivityForResult(intent, Constants.LOG_OUT);
                } else if (mCurrentMode == MODE.TEACHER_CONTACT || mCurrentMode == MODE.STUDENT_CONTACT) {
                    composeEmail(mStudents.get(position).getmEMail(), null, null);
                } else if (mCurrentMode == MODE.TEACHER_CONTACT) {
                    composeEmail(mTeachers.get(position).getmEMail(), null, null);
                }
            }
        });
        //Binding adapters
        mPostAdapter = new PostAdapter(HomeScreen.this, mPosts);
        mListView.setAdapter(mPostAdapter);

        getPosts();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
            Intent intent = new Intent(HomeScreen.this, SettingsActivity.class);
            startActivityForResult(intent, Constants.UPDAE_INFO_SUCCESSSFULLY);
            return true;
        } else if (id == R.id.action_log_out) {
            setResult(Constants.LOG_OUT);
            finish();
            return true;

        } else if (id == R.id.home) {
            Toast.makeText(getApplicationContext(), "Back button clicked", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.action_statistics) {
            Intent intent = new Intent(this,StatisticsActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_first) {
            mFloatingButton.setVisibility((Constants.user_type == Constants.USER_TYPE.STUDENT) ? View.GONE : View.VISIBLE);
            getSupportActionBar().setSubtitle("Posts");
            mCurrentMode = MODE.POST;
            mListView.setAdapter(mPostAdapter);
            getPosts();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();


        } else if (id == R.id.nav_second) {
            mFloatingButton.setVisibility((Constants.user_type == Constants.USER_TYPE.REPRESENTATIVE) ? View.VISIBLE : View.GONE);
            getSupportActionBar().setSubtitle("Courses");
            mCurrentMode = MODE.COURSE;
            mListView.setAdapter(mCourseAdapter);
            GetCourses();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();

        } else if (id == R.id.nav_third) {
            mFloatingButton.setVisibility(View.GONE);
            getSupportActionBar().setSubtitle("Teachers contact");
            Toast.makeText(HomeScreen.this, "Click on any contact to send a mail", Toast.LENGTH_LONG).show();
            mCurrentMode = MODE.TEACHER_CONTACT;
            mListView.setAdapter(mTeacherAdapter);
            GetContactsTeacher();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();

        } else if (id == R.id.nav_fourth) {
            mFloatingButton.setVisibility(View.GONE);
            getSupportActionBar().setSubtitle("Students contact");
            Toast.makeText(HomeScreen.this, "Click on any contact to send a mail", Toast.LENGTH_LONG).show();
            mCurrentMode = MODE.STUDENT_CONTACT;
            mListView.setAdapter(mStudentAdapter);
            getContactsStudents();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeScreen.this, SettingsActivity.class);
            startActivityForResult(intent, Constants.UPDAE_INFO_SUCCESSSFULLY);

        } else if (id == R.id.nav_log_out) {
            setResult(Constants.LOG_OUT);
            finish();
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(this,StatisticsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void Update(MODE mode) {
        if (mode == MODE.POST) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    getPosts();
                    ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
                }
            }, 1000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    GetCourses();
                    ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
                }
            }, 1000);
        }
    }

    static public enum MODE {
        POST,
        COURSE,
        TEACHER_CONTACT,
        STUDENT_CONTACT
    }

    public void composeEmail(String addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{addresses});
        //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.FINISH) {
            setResult(Constants.FINISH);
            finish();
        } else if (resultCode == Constants.LOG_OUT) {
            setResult(Constants.LOG_OUT);
            finish();
        } else if (resultCode == Constants.UPDAE_INFO_SUCCESSSFULLY) {
            Toast.makeText(this, "Information was updated successfully", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.UPDATE_INFO_FAILED) {
            Toast.makeText(this, "Information wasn't updated successfully", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.BACK_TO_HOMESCREEN) {

        } else if (resultCode == Constants.CREATE_POST_RESULT_OK) {
            getPosts();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
                }
            }, 500);
        } else if (resultCode == Constants.CREATE_POST_RESULT_FAILD) {
            getPosts();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Create post wasn't successfully", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.UPDAE_POST_SUCCESSSFULLY) {
            //Toast.makeText(this, "Update post was successfully", Toast.LENGTH_SHORT).show();
            getPosts();
            Constants.selectedPost = null;
        } else if (resultCode == Constants.UPDATE_POST_FAILED) {
            getPosts();
            Toast.makeText(this, "Update post wasn't successfully", Toast.LENGTH_SHORT).show();
            Constants.selectedPost = null;
        } else if (resultCode == Constants.CREATE_COURSE_RESULT_OK) {
            GetCourses();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
        } else if (resultCode == Constants.CREATE_COURSE_RESULT_FAILED) {
            GetCourses();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Create course wasn't successfully", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.UPDATE_COURSE_RESULT_OK) {
            GetCourses();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Constants.selectedCourse = null;
        } else if (resultCode == Constants.UPDATE_COURSE_RESULT_FAILED) {
            GetCourses();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Update course wasn't successfully", Toast.LENGTH_SHORT).show();
            Constants.selectedCourse = null;
        }
    }

    private void getPosts() {
        final String Email = Constants.user.getmEMail();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.GET_POST_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mPosts.clear();
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray Posts = jsonobject.getJSONArray(Constants.Posts);
                            for (int i = Posts.length() - 1; i >= 0; i--) {
                                Log.v("Home screen : ", Posts.getJSONObject(i).getString(Constants.PostText));
                                mPosts.add(new Post(HomeScreen.this, Integer.parseInt(Posts.getJSONObject(i).getString(Constants.PostId)), Posts.getJSONObject(i).getString(Constants.Fname), Posts.getJSONObject(i).getString(Constants.Lname), 2201, null, Posts.getJSONObject(i).getString(Constants.PostText), R.color.black, Posts.getJSONObject(i).getString(Constants.PostDate)));

                            }
                            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
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
                params.put(Constants.Email, Email);
                params.put(Constants.YearNumber, Integer.toString(Constants.YearId));
                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }

    private void GetContactsTeacher() {

        final RequestQueue r = Volley.newRequestQueue(this);

        StringRequest stringrequest = new StringRequest(Request.Method.GET,
                (Constants.GET_DOCRTORS_INFO_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray contancts = jsonobject.getJSONArray("info");
                            mTeachers.clear();
                            for (int i = contancts.length() - 1; i >= 0; i--) {
                                String Fnmae = contancts.getJSONObject(i).getString(Constants.Fname);
                                String Lname = contancts.getJSONObject(i).getString(Constants.Lname);
                                String Email = contancts.getJSONObject(i).getString(Constants.Email);
                                mTeachers.add(new Teacher(Fnmae, Lname, 0, Teacher.TeacherTypes.doctor, Email));
                            }
                            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
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
                return params;
            }
        };

        final StringRequest stringrequest2 = new StringRequest(Request.Method.GET,
                (Constants.GET_TA_INFO_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray contancts = jsonobject.getJSONArray("info");
                            for (int i = contancts.length() - 1; i >= 0; i--) {
                                String Fnmae = contancts.getJSONObject(i).getString(Constants.Fname);
                                String Lname = contancts.getJSONObject(i).getString(Constants.Lname);
                                String Email = contancts.getJSONObject(i).getString(Constants.Email);
                                mTeachers.add(new Teacher(Fnmae, Lname, 0, Teacher.TeacherTypes.teacherAssistant, Email));
                            }
                            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
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
                return params;
            }
        };

        r.add(stringrequest);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                r.add(stringrequest2);
            }
        }, 500);


    }

    private void getContactsStudents() {
        final String YearNumber = Integer.toString(Constants.YearId);
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.CONTACTS_STUDENT_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray contancts = jsonobject.getJSONArray(Constants.Contacts);
                            mStudents.clear();
                            for (int i = contancts.length() - 1; i >= 0; i--) {
                                String Fnmae = contancts.getJSONObject(i).getString("Fname");
                                String Lname = contancts.getJSONObject(i).getString("Lname");
                                String Email = contancts.getJSONObject(i).getString("Email");
                                mStudents.add(new Student(Fnmae + " " + Lname, Email, 0, 0, Constants.YearId));
                            }
                            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
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
                params.put(Constants.YearNumber, YearNumber);
                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }

    private void GetCourses() {
        final String YearNumber = Integer.toString(Constants.YearId);
        final String Email = Constants.user.getmEMail();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.GET_COURSES_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray courses = jsonobject.getJSONArray(Constants.Courses);
                            mCourses.clear();
                            for (int i = courses.length() - 1; i >= 0; i--) {
                                String courseId = courses.getJSONObject(i).getString("CourseId");
                                String courseNmae = courses.getJSONObject(i).getString("CourseName");
                                mCourses.add(new Course(getBaseContext(),courseId, courseNmae, null, null, null));
                            }
                            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
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
                params.put(Constants.YearNumber, YearNumber);
                params.put(Constants.Email, Email);
                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }
}

