package com.example.android.cmpgeeks;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.android.cmpgeeks.Adapters.AssignmentAdapter;
import com.example.android.cmpgeeks.Adapters.MaterialAdapter;
import com.example.android.cmpgeeks.Adapters.OfficeHourAdapter;
import com.example.android.cmpgeeks.Adapters.ProjectAdapter;
import com.example.android.cmpgeeks.Adapters.StudentAdapter;
import com.example.android.cmpgeeks.Adapters.TeamAdapter;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateAssignment;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateMaterial;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateOfficeHour;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateProject;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateTeam;
import com.example.android.cmpgeeks.DataBaseEntity.Assignment;
import com.example.android.cmpgeeks.DataBaseEntity.Course;
import com.example.android.cmpgeeks.DataBaseEntity.Material;
import com.example.android.cmpgeeks.DataBaseEntity.OfficeHours;
import com.example.android.cmpgeeks.DataBaseEntity.Project;
import com.example.android.cmpgeeks.DataBaseEntity.Student;
import com.example.android.cmpgeeks.DataBaseEntity.Teacher;
import com.example.android.cmpgeeks.DataBaseEntity.Team;
import com.example.android.cmpgeeks.Dialogs.SubmitِOrDescriptionDialog;
import com.example.android.cmpgeeks.Dialogs.UpdateOrDeleteDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.Duration;

public class CourseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UpdateOrDeleteDialog.DialogListenerActivity {

    private MODE_COURSE mode;
    private Course mCurrentCourse;
    //layout components
    private TextView mUserNameView;
    private TextView mUserEmailView;
    private ListView mListView;
    private FloatingActionButton mFloatingButton;
    private RelativeLayout emptyText;
    private Spinner mSelectorSpinner;
    private RelativeLayout mRelativeLayout;


    //DataList
    private ArrayList<Material> mMaterials;
    private ArrayList<Assignment> mAssignments;
    private ArrayList<Project> mProjects;
    private ArrayList<Team> mTeams;
    private ArrayList<OfficeHours> mOfficeHours;

    //adapters
    private MaterialAdapter materialAdapter;
    private AssignmentAdapter assignmentAdapter;
    private ProjectAdapter projectAdapter;
    private OfficeHourAdapter officeHourAdapter;
    private TeamAdapter teamAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCurrentCourse = Constants.selectedCourse;
        getSupportActionBar().setTitle(mCurrentCourse.getmName());
        getSupportActionBar().setSubtitle("Materials");
        mode = MODE_COURSE.MATERIAL;


        //demo dat(teams)


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        //Initialization
        mOfficeHours = new ArrayList<OfficeHours>();
        mTeams = new ArrayList<Team>();
        mProjects = new ArrayList<Project>();
        mMaterials = new ArrayList<>();
        mAssignments = new ArrayList<Assignment>();
        mListView = (ListView) findViewById(R.id.list);
        mFloatingButton = (FloatingActionButton) findViewById(R.id.fab);
        mUserNameView = (TextView) hView.findViewById(R.id.user_name);
        mUserEmailView = (TextView) hView.findViewById(R.id.user_email);
        mUserEmailView.setText(Constants.user.getmEMail());
        mUserNameView.setText(Constants.user.getmName());
        emptyText = (RelativeLayout) findViewById(android.R.id.empty);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_laoyout);
        mSelectorSpinner = (Spinner) findViewById(R.id.selector_spinner);
        mFloatingButton.setVisibility(View.GONE);
        assignmentAdapter = new AssignmentAdapter(this, mAssignments);
        materialAdapter = new MaterialAdapter(this, mMaterials);
        projectAdapter = new ProjectAdapter(this, mProjects);
        officeHourAdapter = new OfficeHourAdapter(this, mOfficeHours);
        teamAdapter = new TeamAdapter(this, mTeams);


        //bind adapter
        mListView.setAdapter(materialAdapter);
        mListView.setEmptyView(emptyText);

        FillSpinner();
        GetMaterials();
        GetAssignments();
        GetProjectss();
        GetOfficeHour();

        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_first).setTitle("Materials");
            menu.findItem(R.id.nav_second).setTitle("Assignments");
            menu.findItem(R.id.nav_third).setTitle("Projects");
            menu.findItem(R.id.nav_fourth).setTitle("Teams");
            menu.findItem(R.id.nav_fifth).setVisible(true);
            menu.findItem(R.id.nav_fifth).setTitle("Office hours");

        }


        //Listeners
        if (Constants.user_type != Constants.USER_TYPE.STUDENT) {
            mFloatingButton.setVisibility(View.VISIBLE);
            Toast.makeText(CourseActivity.this, "Click long on any element to update or delet", Toast.LENGTH_LONG).show();
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    if (mode == MODE_COURSE.MATERIAL) {
                        Constants.selectedMaterial = mMaterials.get(position);
                    } else if (mode == MODE_COURSE.PROJECT) {
                        Constants.selectedProject = mProjects.get(position);
                    } else if (mode == MODE_COURSE.ASSIGNMENT) {
                        Constants.selectedAssignment = mAssignments.get(position);
                    } else if (mode == MODE_COURSE.TEAMS) {
                        Constants.selectedTeam = mTeams.get(position);
                    } else if (mode == MODE_COURSE.OFFICE_HOURS) {
                        Constants.selectedOfficeHour = mOfficeHours.get(position);
                    }
                    UpdateOrDeleteDialog dialog = new UpdateOrDeleteDialog(mode);
                    dialog.show(getSupportFragmentManager(), "Data Manipulation");

                    return true;
                }
            });

        }

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == MODE_COURSE.MATERIAL) {
                    Intent intent = new Intent(CourseActivity.this, CreateUpdateMaterial.class);
                    startActivityForResult(intent, Constants.CREATE_MATERIAL_RESULT_OK);
                } else if (mode == MODE_COURSE.ASSIGNMENT) {
                    Intent intent = new Intent(CourseActivity.this, CreateUpdateAssignment.class);
                    startActivityForResult(intent, Constants.CREATE_ASSIGNMENT_RESULT_OK);
                } else if (mode == MODE_COURSE.PROJECT) {
                    Intent intent = new Intent(CourseActivity.this, CreateUpdateProject.class);
                    startActivityForResult(intent, Constants.CREATE_PROJECT_RESULT_OK);
                } else if (mode == MODE_COURSE.OFFICE_HOURS) {
                    Intent intent = new Intent(CourseActivity.this, CreateUpdateOfficeHour.class);
                    startActivityForResult(intent, Constants.CREATE_OFFICE_HOUR_RESULT_OK);
                } else if (mode == MODE_COURSE.TEAMS) {
                    Intent intent = new Intent(CourseActivity.this, CreateUpdateTeam.class);
                    startActivityForResult(intent, Constants.CREATE_TEAM_RESULT_OK);
                }

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mode == MODE_COURSE.MATERIAL) {
                    openWeb(mMaterials.get(position).getmLink());
                } else if (mode == MODE_COURSE.ASSIGNMENT) {
                    Constants.selectedAssignment = mAssignments.get(position);
                    new SubmitِOrDescriptionDialog(mode).show(getSupportFragmentManager(), "CourseActivitylAssignment");
                } else if (mode == MODE_COURSE.PROJECT) {
                    Constants.selectedProject = mProjects.get(position);
                    new SubmitِOrDescriptionDialog(mode).show(getSupportFragmentManager(), "CourseActivitylProject");
                } else if (mode == MODE_COURSE.OFFICE_HOURS) {
                    //nothing
                } else if (mode == MODE_COURSE.TEAMS) {
                    DisplayTeamMembers(position);
                }
            }
        });
        mSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetTeams(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Constants.selectedCourse = null;
            setResult(Constants.BACK_TO_HOMESCREEN);
            super.onBackPressed();
        }
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
            Intent intent = new Intent(CourseActivity.this, SettingsActivity.class);
            startActivityForResult(intent, Constants.UPDAE_INFO_SUCCESSSFULLY);
            return true;
        } else if (id == R.id.action_log_out) {
            setResult(Constants.LOG_OUT);
            finish();
            return true;

        } else if (id == R.id.action_statistics) {
            Intent intent = new Intent(this, StatisticsActivity.class);
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
            mode = MODE_COURSE.MATERIAL;
            mRelativeLayout.setVisibility(View.GONE);
            mFloatingButton.setVisibility((Constants.user_type == Constants.USER_TYPE.STUDENT) ? View.GONE : View.VISIBLE);
            getSupportActionBar().setSubtitle("Materials");
            mListView.setAdapter(materialAdapter);
            GetMaterials();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();

        } else if (id == R.id.nav_second) {
            mRelativeLayout.setVisibility(View.GONE);
            mode = MODE_COURSE.ASSIGNMENT;
            mFloatingButton.setVisibility((Constants.user_type == Constants.USER_TYPE.STUDENT) ? View.GONE : View.VISIBLE);
            getSupportActionBar().setSubtitle("Assignments");
            mListView.setAdapter(assignmentAdapter);
            GetAssignments();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();

        } else if (id == R.id.nav_third) {
            mRelativeLayout.setVisibility(View.GONE);
            mode = MODE_COURSE.PROJECT;
            mFloatingButton.setVisibility((Constants.user_type == Constants.USER_TYPE.STUDENT) ? View.GONE : View.VISIBLE);
            getSupportActionBar().setSubtitle("Projects");
            mListView.setAdapter(projectAdapter);
            GetProjectss();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();

        } else if (id == R.id.nav_fourth) {
            FillSpinner();
            GetTeams(mSelectorSpinner.getSelectedItemPosition());
            mRelativeLayout.setVisibility(View.VISIBLE);
            mode = MODE_COURSE.TEAMS;
            mFloatingButton.setVisibility((Constants.user_type == Constants.USER_TYPE.TEACHER) ? View.GONE : View.VISIBLE);
            getSupportActionBar().setSubtitle("Teams");
            mListView.setAdapter(teamAdapter);
            GetTeams(mSelectorSpinner.getSelectedItemPosition());
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();

        } else if (id == R.id.nav_fifth) {
            mRelativeLayout.setVisibility(View.GONE);
            mode = MODE_COURSE.OFFICE_HOURS;
            mFloatingButton.setVisibility((Constants.user_type == Constants.USER_TYPE.STUDENT) ? View.GONE : View.VISIBLE);
            getSupportActionBar().setSubtitle("Office hours");
            mListView.setAdapter(officeHourAdapter);
            GetOfficeHour();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(CourseActivity.this, SettingsActivity.class);
            startActivityForResult(intent, Constants.UPDAE_INFO_SUCCESSSFULLY);
        } else if (id == R.id.nav_log_out) {
            setResult(Constants.LOG_OUT);
            finish();
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        } else if (resultCode == Constants.CREATE_MATERIAL_RESULT_OK) {
            GetMaterials();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
        } else if (resultCode == Constants.CREATE_MATERIAL_RESULT_FAILED) {
            GetMaterials();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Material wasn't Created successfully", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.UPDATE_MATERIAL_RESULT_OK) {
            GetMaterials();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Constants.selectedMaterial = null;
        } else if (resultCode == Constants.UPDATE_MATERIAL_RESULT_FAILED) {
            GetMaterials();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Material wasn't updated successfully", Toast.LENGTH_SHORT).show();
            Constants.selectedMaterial = null;
        } else if (resultCode == Constants.CREATE_ASSIGNMENT_RESULT_OK) {
            GetAssignments();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
        } else if (resultCode == Constants.CREATE_ASSIGNMENT_RESULT_FAILED) {
            GetAssignments();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
        } else if (resultCode == Constants.UPDATE_ASSIGNMENT_RESULT_OK) {
            GetAssignments();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Constants.selectedAssignment = null;
        } else if (resultCode == Constants.UPDATE_ASSIGNMENT_RESULT_FAILED) {
            GetAssignments();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Assignment wasn't updated successfully", Toast.LENGTH_SHORT).show();
            Constants.selectedAssignment = null;
        } else if (resultCode == Constants.CREATE_PROJECT_RESULT_OK) {
            GetProjectss();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
        } else if (resultCode == Constants.CREATE_PROJECT_RESULT_FAILED) {
            GetProjectss();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Project wasn't Created successfully", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.UPDATE_PROJECT_RESULT_OK) {
            GetProjectss();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Constants.selectedProject = null;
        } else if (resultCode == Constants.UPDATE_PROJECT_RESULT_FAILED) {
            GetProjectss();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Project wasn't updated successfully", Toast.LENGTH_SHORT).show();
            Constants.selectedProject = null;
        } else if (resultCode == Constants.CREATE_OFFICE_HOUR_RESULT_OK) {
            GetOfficeHour();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
        } else if (resultCode == Constants.CREATE_OFFICE_HOUR_RESULT_FAILED) {
            GetOfficeHour();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Office hour wasn't Created successfully", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.UPDATE_OFFICE_HOUR_RESULT_OK) {
            GetOfficeHour();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Constants.selectedProject = null;
        } else if (resultCode == Constants.UPDATE_OFFICE_HOUR_RESULT_FAILED) {
            GetOfficeHour();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Office hour wasn't updated successfully", Toast.LENGTH_SHORT).show();
            Constants.selectedProject = null;
        } else if (resultCode == Constants.CREATE_TEAM_RESULT_OK) {
            GetTeams(mSelectorSpinner.getSelectedItemPosition());
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Team was Created successfully", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.CREATE_TEAM_RESULT_FAILED) {
            GetTeams(mSelectorSpinner.getSelectedItemPosition());
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Team wasn't Created successfully", Toast.LENGTH_SHORT).show();
        } else if (resultCode == Constants.UPDATE_TEAM_RESULT_OK) {
            GetTeams(mSelectorSpinner.getSelectedItemPosition());
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Team was updated successfully", Toast.LENGTH_SHORT).show();
            Constants.selectedProject = null;
        } else if (resultCode == Constants.UPDATE_TEAM_RESULT_FAILED) {
            GetTeams(mSelectorSpinner.getSelectedItemPosition());
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "Team wasn't updated successfully", Toast.LENGTH_SHORT).show();
            Constants.selectedProject = null;
        }
    }


    private void composeEmail(String addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{addresses});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openWeb(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        try {
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Invalid Link", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void Update(MODE_COURSE mode) {
        if (MODE_COURSE.MATERIAL == mode) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    GetMaterials();
                    ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
                }
            }, 1000);
        } else if (MODE_COURSE.ASSIGNMENT == mode) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    GetAssignments();
                    ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
                }
            }, 1000);
        } else if (MODE_COURSE.PROJECT == mode) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    GetProjectss();
                    ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
                }
            }, 1000);
        } else if (MODE_COURSE.OFFICE_HOURS == mode) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    GetOfficeHour();
                    ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
                }
            }, 1000);
        } else if (MODE_COURSE.TEAMS == mode) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    GetTeams(mSelectorSpinner.getSelectedItemPosition());
                    ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
                }
            }, 1000);
        }
    }


    static public enum MODE_COURSE {
        MATERIAL,
        ASSIGNMENT,
        PROJECT,
        TEAMS,
        OFFICE_HOURS
    }

    private void FillSpinner() {
        GetProjectss();
        String[] items = new String[mProjects.size()];
        for (int i = 0; i < mProjects.size(); i++) {
            items[i] = mProjects.get(i).getmTitle();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectorSpinner.setAdapter(adapter);
    }

    public void GetMaterials() {

        final String CourseId = Constants.selectedCourse.getmID();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.GET_MATERIALS_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray materials = jsonobject.getJSONArray("contents");
                            mMaterials.clear();
                            for (int i = materials.length() - 1; i >= 0; i--) {
                                String topic = materials.getJSONObject(i).getString("Topic");
                                String link = materials.getJSONObject(i).getString("Link");
                                String type = materials.getJSONObject(i).getString("Type");
                                mMaterials.add(new Material(CourseActivity.this, topic, link, DetremineMaterialType(type)));
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
                params.put(Constants.CourseId, CourseId);
                return params;
            }
        };
        //bind adapter
        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }

    public void GetAssignments() {

        final String CourseId = Constants.selectedCourse.getmID();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.GET_ASSIGNMENT_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray assignments = jsonobject.getJSONArray("Ass");
                            mAssignments.clear();
                            for (int i = assignments.length() - 1; i >= 0; i--) {
                                int number = Integer.parseInt(assignments.getJSONObject(i).getString("AssNum"));
                                String link = assignments.getJSONObject(i).getString("Link");
                                String deadline = assignments.getJSONObject(i).getString("Deadline");
                                String submit = assignments.getJSONObject(i).getString("SubmitEmail");
                                mAssignments.add(new Assignment(CourseActivity.this, number, link, deadline, submit));
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
                params.put(Constants.CourseId, CourseId);
                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }

    public void GetProjectss() {

        final String CourseId = Constants.selectedCourse.getmID();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.GET_PROJECTS_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray projects = jsonobject.getJSONArray("projects");
                            mProjects.clear();
                            for (int i = projects.length() - 1; i >= 0; i--) {
                                int number = Integer.parseInt(projects.getJSONObject(i).getString("MaxNum"));
                                String link = projects.getJSONObject(i).getString("Link");
                                String Title = projects.getJSONObject(i).getString("Title");
                                String submit = projects.getJSONObject(i).getString("SubmitEmail");
                                mProjects.add(new Project(CourseActivity.this, submit, Title, link, number, null));
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
                params.put(Constants.CourseId, CourseId);
                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }

    public void GetOfficeHour() {

        final String CourseId = Constants.selectedCourse.getmID();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.GET_OFFICE_HOURS_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray teachers = jsonobject.getJSONArray("teachers");
                            JSONArray officeHours = jsonobject.getJSONArray("OfficeHours");
                            mOfficeHours.clear();
                            for (int i = teachers.length() - 1; i >= 0; i--) {
                                for (int j = 0; j < officeHours.getJSONArray(i).length(); j++) {
                                    String dayName = officeHours.getJSONArray(i).getJSONObject(j).getString("Day");
                                    String start = officeHours.getJSONArray(i).getJSONObject(j).getString("Start");
                                    String until = officeHours.getJSONArray(i).getJSONObject(j).getString("Until");
                                    OfficeHours.DAYS_NAMES day = DetereminDayName(dayName);
                                    Teacher teacher = new Teacher(teachers.getJSONObject(i).getString("Fname"),
                                            teachers.getJSONObject(i).getString("Lname"),
                                            Integer.parseInt(teachers.getJSONObject(i).getString("Gyear")),
                                            Teacher.TeacherTypes.teacherAssistant, null);

                                    mOfficeHours.add(new OfficeHours(CourseActivity.this, teacher, day, start, until));
                                }
                                ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
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
                params.put(Constants.CourseId, CourseId);
                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(stringrequest);

    }

    public void GetTeams(int position) {

        if (mProjects.size()>0) {
            final String CourseId = Constants.selectedCourse.getmID();
            final String Title = mProjects.get(position).getmTitle();
            StringRequest stringrequest = new StringRequest(Request.Method.POST,
                    (Constants.GET_TEAMS_REQUEST),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonobject = new JSONObject(response);
                                JSONArray teams = jsonobject.getJSONArray("teams");
                                mTeams.clear();
                                for (int i = 0; i < teams.length(); i++) {
                                    String number = teams.getString(i);
                                    mTeams.add(new Team(CourseActivity.this, number, null, mSelectorSpinner.getSelectedItem().toString()));
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
                    params.put(Constants.CourseId, CourseId);
                    params.put("Title", Title);
                    return params;
                }
            };
            RequestQueue r = Volley.newRequestQueue(this);
            r.add(stringrequest);
        }

    }


    public Material.MATERIAL_TYPE DetremineMaterialType(String type) {
        String[] types = getResources().getStringArray(R.array.material_type);
        if (type.equals(types[0])) {
            return Material.MATERIAL_TYPE.LECTURE_SLIDES;
        } else if (type.equals(types[1])) {
            return Material.MATERIAL_TYPE.LECTURE_WRITTEN;
        } else if (type.equals(types[2])) {
            return Material.MATERIAL_TYPE.SHEET;
        } else if (type.equals(types[3])) {
            return Material.MATERIAL_TYPE.SHEET_SOLUTION;
        } else if (type.equals(types[4])) {
            return Material.MATERIAL_TYPE.SECTION;
        } else if (type.equals(types[5])) {
            return Material.MATERIAL_TYPE.NOTE;
        } else if (type.equals(types[6])) {
            return Material.MATERIAL_TYPE.VIDEO;
        } else if (type.equals(types[7])) {
            return Material.MATERIAL_TYPE.REFERENCE;
        } else {
            return Material.MATERIAL_TYPE.OTHER;
        }
    }

    private OfficeHours.DAYS_NAMES DetereminDayName(String day) {
        String[] types = getResources().getStringArray(R.array.day_names);
        if (day.equals("Sunday")) {
            return OfficeHours.DAYS_NAMES.Sunday;
        } else if (day.equals("Monday")) {
            return OfficeHours.DAYS_NAMES.Monday;
        } else if (day.equals("Tuesday")) {
            return OfficeHours.DAYS_NAMES.Tuesday;
        } else if (day.equals("Wednesday")) {
            return OfficeHours.DAYS_NAMES.Wednesday;
        } else {
            return OfficeHours.DAYS_NAMES.Thursday;
        }
    }

    private void DisplayTeamMembers(int position) {
        final String CourseId = Constants.selectedCourse.getmID();
        final String Title = mSelectorSpinner.getSelectedItem().toString();
        final String TeamNumber = mTeams.get(position).getmTeamNumber();
        StringRequest stringrequest = new StringRequest(Request.Method.POST,
                (Constants.GET_TEAM_MEMBER_REQUEST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray contancts = jsonobject.getJSONArray("students");
                            ArrayList<Student> mStudents = new ArrayList<>();
                            for (int i = contancts.length() - 1; i >= 0; i--) {
                                String Fnmae = contancts.getJSONObject(i).getString("Fname");
                                String Lname = contancts.getJSONObject(i).getString("Lname");
                                String Sec = contancts.getJSONObject(i).getString("Sec");
                                String Bn = contancts.getJSONObject(i).getString("Bn");
                                mStudents.add(new Student(Fnmae + " " + Lname, null, Integer.parseInt(Sec),Integer.parseInt( Bn), Constants.YearId));
                            }
                            LayoutInflater inflater = CourseActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.content_home_screen, null);
                            RelativeLayout e = (RelativeLayout) dialogView.findViewById(android.R.id.empty);
                            e.setBackgroundColor(getResources().getColor(R.color.white));
                            dialogView.setBackgroundColor(getResources().getColor(R.color.white));
                            ListView l = (ListView) dialogView.findViewById(R.id.list);
                            StudentAdapter d = new StudentAdapter(CourseActivity.this,mStudents);
                            l.setAdapter(d);
                            l.setEmptyView(e);
                            new AlertDialog.Builder(CourseActivity.this).setView(dialogView)
                                    .setTitle("Students")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CourseId", CourseId);
                params.put("Title", Title);
                params.put("TeamNumber", TeamNumber);

                return params;
            }
        };

        RequestQueue r = Volley.newRequestQueue(getBaseContext());
        r.add(stringrequest);


    }
}
