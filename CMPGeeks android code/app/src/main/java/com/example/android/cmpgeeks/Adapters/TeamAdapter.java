package com.example.android.cmpgeeks.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.cmpgeeks.DataBaseEntity.Student;
import com.example.android.cmpgeeks.DataBaseEntity.Team;
import com.example.android.cmpgeeks.R;

import java.util.ArrayList;

public class TeamAdapter extends ArrayAdapter<Team> {

    public TeamAdapter(@NonNull Context context, ArrayList<Team> teams) {
        super(context, 0,teams);
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
        Team team = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView teacherName = (TextView) listItemView.findViewById(R.id.name);
        TextView teacherType = (TextView) listItemView.findViewById(R.id.type);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        teacherName.setText("Team number "+team.getmTeamNumber());
       return listItemView;
    }

}
