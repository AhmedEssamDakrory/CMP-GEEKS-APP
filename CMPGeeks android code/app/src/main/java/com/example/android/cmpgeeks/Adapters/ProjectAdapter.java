package com.example.android.cmpgeeks.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.cmpgeeks.DataBaseEntity.Project;
import com.example.android.cmpgeeks.R;

import java.util.ArrayList;

public class ProjectAdapter extends ArrayAdapter<Project> {


    public ProjectAdapter(@NonNull Context context, ArrayList<Project> projects) {
        super(context, 0, projects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list_layout, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Project project = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView topicText = (TextView) listItemView.findViewById(R.id.material_topic);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        topicText.setText(project.getmTitle());

        TextView typeText = (TextView) listItemView.findViewById(R.id.material_type);
        String string = "Max number of team's members "+Integer.toString(project.getmMaxStudent());

        typeText.setText(string);
        return listItemView;
    }
}
