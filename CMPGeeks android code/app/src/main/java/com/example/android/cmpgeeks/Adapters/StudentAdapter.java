package com.example.android.cmpgeeks.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.cmpgeeks.Constants;
import com.example.android.cmpgeeks.DataBaseEntity.Student;
import com.example.android.cmpgeeks.DataBaseEntity.Teacher;
import com.example.android.cmpgeeks.R;

import java.util.ArrayList;

public class StudentAdapter extends ArrayAdapter<Student> {

    private Context mContext;
    public StudentAdapter(@NonNull Context context, ArrayList<Student> students) {
        super(context, 0,students);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contact_item_layout, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Student student = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView studentName = (TextView) listItemView.findViewById(R.id.name);
        TextView studentInfo = (TextView) listItemView.findViewById(R.id.type);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        studentName.setText(student.getmName());
        //studentName.setTextColor(ContextCompat.getColor(mContext,(Constants.mYear.getmRepresentative()==student)?R.color.red:R.color.black));

        studentInfo.setText(("SEC : "+student.getmSEC()+"    BN : "+student.getmBN()));
        return listItemView;
    }

}
