package com.example.android.cmpgeeks;

import com.example.android.cmpgeeks.DataBaseEntity.Assignment;
import com.example.android.cmpgeeks.DataBaseEntity.Course;
import com.example.android.cmpgeeks.DataBaseEntity.Material;
import com.example.android.cmpgeeks.DataBaseEntity.OfficeHours;
import com.example.android.cmpgeeks.DataBaseEntity.Post;
import com.example.android.cmpgeeks.DataBaseEntity.Project;
import com.example.android.cmpgeeks.DataBaseEntity.Teacher;
import com.example.android.cmpgeeks.DataBaseEntity.Team;
import com.example.android.cmpgeeks.DataBaseEntity.Year;

import java.util.ArrayList;

public class Constants {
    //data base strings
    static public final String PORT = "192.168.43.134";
    static public final String STUDENT_SIGN_UP_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=SignUpAsStudent";
    static public final String TEACHER_SIGN_UP_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=SignUpAsTeacher";
    static public final String SIGN_IN_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=SignInCheck";
    static public final String CERATE_POST_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AddPost";
    static public final String GET_POST_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=GetAllPosts";
    static public final String DELETE_POST_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=DeletePost";
    static public final String UPDATE_POST_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=UpdatePost";
    static public final String CONTACTS_STUDENT_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AllContactsInYearStudents";
    static public final String GET_DOCRTORS_INFO_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=DoctorsInfo";
    static public final String GET_TA_INFO_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=TeacherAssistantsInfo";
    static public final String CREATE_COURSE_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AddCourse";
    static public final String ASSIGN_TEACHER_TO_COURSE_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AssignTeacherToCourse";
    static public final String GET_COURSES_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AllCoursesInYear";
    static public final String GET_MATERIALS_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=ContentsOfMaterial";
    static public final String ADD_MATERIALS_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AddMaterial";
    static public final String DELETE_MATERIALS_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=DeleteMaterial";
    static public final String GET_ASSIGNMENT_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=Assignments";
    static public final String GET_PROJECTS_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=GetProjects";
    static public final String GET_OFFICE_HOURS_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=GetOfficeHoursOfCourse";
    static public final String GET_TEAMS_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=TeamsInProj";
    static public final String DELETE_COURSE_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=DeleteCourse";
    static public final String DELETE_ASSIGNMENT_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=DeleteAssignment";
    static public final String DELETE_OFFICE_HOUR_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=DeleteOfficeHours";
    static public final String DELETE_PROJECT_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=DeleteProject";
    static public final String DELETE_TEAM_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=DeleteTeam";
    static public final String ADD_ASSIGNMENT_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AddAssignment";
    static public final String UPDATE_ASSIGNMENT_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=UpdateAssignment";
    static public final String ADD_PROJECT_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AddProject";
    static public final String UPDATE_PROJECT_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=UpdateProject";
    static public final String ADD_OFFICE_HOUR_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AddOfficeHours";
    static public final String GET_TEACHER_IN_COURSE_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=TeacherInfoInCourse";
    static public final String ADD_TEAM_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=CreateTeam";
    static public final String GET_STUDENT_NOT_IN_TEAM_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AllStudentsNotInTeam";
    static public final String ASSIGN_STUDENT_TO_TEAM = "http://" + PORT + ":82/android/myphp/registeruser.php?function=AddMemberInTeam";
    static public final String GET_AVAILABLE_PROJECTS_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=GetAvailableProjects";
    static public final String GET_INFO_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=Count_Statistics";
    static public final String GET_INFO_GROUP_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=Statistics_group";
    static public final String GET_TEAM_MEMBER_REQUEST = "http://" + PORT + ":82/android/myphp/registeruser.php?function=StudentsInTeam";



    //sign up parameters

    static public final String Fname = "Fname";
    static public final String Lname = "Lname";
    static public final String Email = "Email";
    static public final String Password = "Password";
    static public final String Sec = "Sec";
    static public final String Bn = "Bn";
    static public final String YearNum = "YearNum";
    static public final String GraduationYear = "Gyear";
    static public final String TeacherType = "type";
    static public final String PostText = "Text";
    static public final String PostColor = "Color";
    static public final String PostDate = "Date";
    static public final String YearNumber = "YearNum";
    static public final String Posts = "Posts";
    static public final String Contacts = "contacts";
    static public final String PostId = "Id";
    static public final String CourseId = "CourseId";
    static public final String CourseName = "CourseName";
    static public final String Courses ="courses";

    //Results  Value
    static public final int FINISH = 0;
    static public final int CREATE_POST_RESULT_OK = 1;
    static public final int BACK_TO_HOMESCREEN = 2;
    static public final int SIGN_UP_SUCCESSFULLY = 3;
    static public final int SIGN_UP_FAILD = 4;
    static public final int LOG_OUT = 5;
    static public final int UPDAE_INFO_SUCCESSSFULLY = 6;
    static public final int UPDATE_INFO_FAILED = 7;
    static public final int CREATE_POST_RESULT_FAILD = 8;
    static public final int UPDAE_POST_SUCCESSSFULLY = 9;
    static public final int UPDATE_POST_FAILED = 10;
    static public final int CREATE_COURSE_RESULT_OK = 11;
    static public final int CREATE_COURSE_RESULT_FAILED = 12;
    static public final int UPDATE_COURSE_RESULT_OK = 13;
    static public final int UPDATE_COURSE_RESULT_FAILED = 14;
    static public final int CREATE_MATERIAL_RESULT_OK = 15;
    static public final int CREATE_MATERIAL_RESULT_FAILED = 16;
    static public final int UPDATE_MATERIAL_RESULT_OK = 17;
    static public final int UPDATE_MATERIAL_RESULT_FAILED = 18;
    static public final int CREATE_ASSIGNMENT_RESULT_OK = 19;
    static public final int CREATE_ASSIGNMENT_RESULT_FAILED = 20;
    static public final int UPDATE_ASSIGNMENT_RESULT_OK = 21;
    static public final int UPDATE_ASSIGNMENT_RESULT_FAILED = 22;
    static public final int CREATE_PROJECT_RESULT_OK = 23;
    static public final int CREATE_PROJECT_RESULT_FAILED = 24;
    static public final int UPDATE_PROJECT_RESULT_OK = 25;
    static public final int UPDATE_PROJECT_RESULT_FAILED = 26;
    static public final int CREATE_OFFICE_HOUR_RESULT_OK = 27;
    static public final int CREATE_OFFICE_HOUR_RESULT_FAILED = 28;
    static public final int UPDATE_OFFICE_HOUR_RESULT_OK = 29;
    static public final int UPDATE_OFFICE_HOUR_RESULT_FAILED = 30;
    static public final int CREATE_TEAM_RESULT_OK = 31;
    static public final int CREATE_TEAM_RESULT_FAILED = 32;
    static public final int UPDATE_TEAM_RESULT_OK = 33;
    static public final int UPDATE_TEAM_RESULT_FAILED = 34;


    static public final String INDEX = "INDEX";
    static public USER_TYPE user_type;
    static public User user;
    static public Post selectedPost;
    static public Course selectedCourse;
    static public Material selectedMaterial = null;
    static public Assignment selectedAssignment = null;
    static public Team selectedTeam = null;
    static public Project selectedProject = null;
    static public OfficeHours selectedOfficeHour = null;
    static public int YearId;

    public enum USER_TYPE {
        REPRESENTATIVE,
        TEACHER,
        STUDENT
    }

    static public class User {
        //student
        private String mName;
        private int mSEC;
        private int mBN;
        private int mYear;

        //common
        private String mEMail;
        private String mPassword;

        //teachers
        private String mFName;
        private String mLName;
        private int mGY;
        private Teacher.TeacherTypes mType;
        private String Id;

        public User(String Id,String mFName, String mLName, int mSEC, int mBN, int mYear, String mEMail, String mPassword) {
            this.mFName = mFName;
            this.mLName = mLName;
            this.mSEC = mSEC;
            this.mBN = mBN;
            this.mYear = mYear;
            this.mEMail = mEMail;
            this.mPassword = mPassword;
            mName = mFName + " " + mLName;
            this.Id =Id;
        }

        public User(String mEMail, String mPassword, String mFName, String mLName, int mGY, Teacher.TeacherTypes mType) {
            this.mEMail = mEMail;
            this.mPassword = mPassword;
            this.mFName = mFName;
            this.mLName = mLName;
            this.mGY = mGY;
            this.mType = mType;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getmName() {
            return mName;
        }

        public String getmPassword() {
            return mPassword;
        }

        public void setmPassword(String mPassword) {
            this.mPassword = mPassword;
        }

        public void setmName(String mName) {
            this.mName = mName;
        }

        public int getmSEC() {
            return mSEC;
        }

        public void setmSEC(int mSEC) {
            this.mSEC = mSEC;
        }

        public int getmBN() {
            return mBN;
        }

        public void setmBN(int mBN) {
            this.mBN = mBN;
        }

        public int getmYear() {
            return mYear;
        }

        public void setmYear(int mYear) {
            this.mYear = mYear;
        }

        public String getmEMail() {
            return mEMail;
        }

        public void setmEMail(String mEMail) {
            this.mEMail = mEMail;
        }

        public String getmFName() {
            return mFName;
        }

        public void setmFName(String mFName) {
            this.mFName = mFName;
        }

        public String getmLName() {
            return mLName;
        }

        public void setmLName(String mLName) {
            this.mLName = mLName;
        }

        public int getmGY() {
            return mGY;
        }

        public void setmGY(int mGY) {
            this.mGY = mGY;
        }

        public Teacher.TeacherTypes getmType() {
            return mType;
        }

        public void setmType(Teacher.TeacherTypes mType) {
            this.mType = mType;
        }
    }
}
