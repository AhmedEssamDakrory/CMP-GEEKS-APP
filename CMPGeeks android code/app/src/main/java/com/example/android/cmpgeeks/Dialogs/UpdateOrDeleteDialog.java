package com.example.android.cmpgeeks.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.example.android.cmpgeeks.Constants;
import com.example.android.cmpgeeks.CourseActivity;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateAssignment;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateCourse;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateMaterial;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateOfficeHour;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdatePost;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateProject;
import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateTeam;
import com.example.android.cmpgeeks.HomeScreen;

@SuppressLint("ValidFragment")
public class UpdateOrDeleteDialog extends AppCompatDialogFragment {

    private HomeScreen.MODE mode;
    private CourseActivity.MODE_COURSE cmode;
    private boolean InHomeScreen;
    private DialogListenerHomeScreen listener;
    private DialogListenerActivity listenerC;

    @SuppressLint("ValidFragment")
    public UpdateOrDeleteDialog(HomeScreen.MODE mode) {
        this.mode = mode;
        InHomeScreen = true;
    }


    @SuppressLint("ValidFragment")
    public UpdateOrDeleteDialog(CourseActivity.MODE_COURSE mode) {
        this.cmode = mode;
        InHomeScreen = false;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        //View view = inflater.inflate();  layout reasors
        if (InHomeScreen) {
            builder.setTitle("Data Manipulation")
                    .setMessage("For the current long clicked...")
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(getActivity()).setTitle("Deleting irem")
                                    .setMessage("Are you sure you want to delet this item")
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Toast.makeText(getActivity(), "Deletion was canceled", Toast.LENGTH_SHORT).show();
                                            Constants.selectedPost = null;
                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //deleting post request here
                                            if (mode == HomeScreen.MODE.POST) {
                                                Constants.selectedPost.Delete();
                                                Constants.selectedPost = null;
                                                listener.Update(mode);
                                            } else if (mode == HomeScreen.MODE.COURSE) {
                                                Constants.selectedCourse.Delete();
                                                Constants.selectedCourse = null;
                                                listener.Update(mode);
                                            }
                                        }
                                    }).show();

                        }
                    })
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mode == HomeScreen.MODE.POST) {
                                Intent intent = new Intent(getContext(), CreateUpdatePost.class);
                                startActivityForResult(intent, Constants.UPDAE_POST_SUCCESSSFULLY);
                            } else if (mode == HomeScreen.MODE.COURSE) {
                                Intent intent = new Intent(getContext(), CreateUpdateCourse.class);
                                startActivityForResult(intent, Constants.UPDATE_COURSE_RESULT_OK);
                            }
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Constants.selectedPost = null;
                        }
                    });
        } else {
            builder.setTitle("Data Manipulation")
                    .setMessage("For the current long clicked...")
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(getActivity()).setTitle("Deleting item")
                                    .setMessage("Are you sure you want to delet this item")
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Toast.makeText(getActivity(), "Deletion was canceled", Toast.LENGTH_SHORT).show();
                                            if (cmode == CourseActivity.MODE_COURSE.MATERIAL) {
                                                Constants.selectedMaterial = null;
                                            } else if (cmode == CourseActivity.MODE_COURSE.ASSIGNMENT) {
                                                Constants.selectedAssignment = null;
                                            } else if (cmode == CourseActivity.MODE_COURSE.TEAMS) {
                                                Constants.selectedTeam = null;
                                            } else if (cmode == CourseActivity.MODE_COURSE.PROJECT) {
                                                Constants.selectedProject = null;
                                            } else if (cmode == CourseActivity.MODE_COURSE.OFFICE_HOURS) {
                                                Constants.selectedOfficeHour = null;
                                            }
                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //deleting post request here
                                            //Toast.makeText(getActivity(), "Deletion done successfully", Toast.LENGTH_SHORT).show();
                                            if (cmode == CourseActivity.MODE_COURSE.MATERIAL) {
                                                Constants.selectedMaterial.Delete();
                                                Constants.selectedMaterial = null;
                                                listenerC.Update(cmode);
                                            } else if (cmode == CourseActivity.MODE_COURSE.ASSIGNMENT) {
                                                Constants.selectedAssignment.Delete();
                                                Constants.selectedAssignment = null;
                                                listenerC.Update(cmode);
                                            } else if (cmode == CourseActivity.MODE_COURSE.TEAMS) {
                                                Constants.selectedTeam.Delete();
                                                Constants.selectedTeam = null;
                                                listenerC.Update(cmode);
                                            } else if (cmode == CourseActivity.MODE_COURSE.PROJECT) {
                                                Constants.selectedProject.Delete();
                                                Constants.selectedProject = null;
                                                listenerC.Update(cmode);
                                            } else if (cmode == CourseActivity.MODE_COURSE.OFFICE_HOURS) {
                                                Constants.selectedOfficeHour.Delete();
                                                Constants.selectedOfficeHour = null;
                                                listenerC.Update(cmode);
                                            }
                                        }
                                    }).show();

                        }
                    })
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (cmode == CourseActivity.MODE_COURSE.MATERIAL) {
                                Intent intent = new Intent(getContext(), CreateUpdateMaterial.class);
                                startActivityForResult(intent, Constants.UPDATE_MATERIAL_RESULT_OK);
                            } else if (cmode == CourseActivity.MODE_COURSE.ASSIGNMENT) {
                                Intent intent = new Intent(getContext(), CreateUpdateAssignment.class);
                                startActivityForResult(intent, Constants.UPDATE_ASSIGNMENT_RESULT_OK);
                            } else if (cmode == CourseActivity.MODE_COURSE.PROJECT) {
                                Intent intent = new Intent(getContext(), CreateUpdateProject.class);
                                startActivityForResult(intent, Constants.UPDATE_PROJECT_RESULT_OK);
                            } else if (cmode == CourseActivity.MODE_COURSE.OFFICE_HOURS) {
                                Intent intent = new Intent(getContext(), CreateUpdateOfficeHour.class);
                                startActivityForResult(intent, Constants.UPDATE_OFFICE_HOUR_RESULT_OK);
                            } else if (cmode == CourseActivity.MODE_COURSE.TEAMS) {
                                //Intent intent = new Intent(getContext(), CreateUpdateTeam.class);
                                //startActivityForResult(intent, Constants.UPDATE_TEAM_RESULT_OK);
                                Toast.makeText(getContext(),"Invalid operation",Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (cmode == CourseActivity.MODE_COURSE.MATERIAL) {
                                Constants.selectedMaterial = null;
                            } else if (cmode == CourseActivity.MODE_COURSE.ASSIGNMENT) {
                                Constants.selectedAssignment = null;
                            }
                        }
                    });
        }
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (InHomeScreen) {
                listener = (DialogListenerHomeScreen) context;
            } else {
                listenerC = (DialogListenerActivity) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogListener");
        }
    }

    public interface DialogListenerHomeScreen {
        void Update(HomeScreen.MODE mode);
    }

    public interface DialogListenerActivity {
        void Update(CourseActivity.MODE_COURSE mode);
    }
}
