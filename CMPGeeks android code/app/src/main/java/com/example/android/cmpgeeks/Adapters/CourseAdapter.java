package com.example.android.cmpgeeks.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.cmpgeeks.DataBaseEntity.Course;
import com.example.android.cmpgeeks.DataBaseEntity.Post;
import com.example.android.cmpgeeks.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CourseAdapter extends ArrayAdapter<Course> {


    public CourseAdapter(@NonNull Context context, ArrayList<Course> posts) {
        super(context, 0, posts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.course_item_layout, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Course course = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView courseText = (TextView) listItemView.findViewById(R.id.course_name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        courseText.setText(course.getmName());

        return listItemView;
    }
}
