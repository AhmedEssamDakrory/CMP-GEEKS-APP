package com.example.android.cmpgeeks.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.cmpgeeks.DataBaseEntity.Material;
import com.example.android.cmpgeeks.R;

import java.util.ArrayList;

public class MaterialAdapter extends ArrayAdapter<Material> {


    public MaterialAdapter(@NonNull Context context, ArrayList<Material> materials) {
        super(context, 0, materials);
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
        Material material = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView topicText = (TextView) listItemView.findViewById(R.id.material_topic);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        topicText.setText(material.getTopic());

        TextView typeText = (TextView) listItemView.findViewById(R.id.material_type);

        String[] mArray;
        mArray = getContext().getResources().getStringArray(R.array.material_type);

        typeText.setText(mArray[material.getmMaterialType().ordinal()]);
        return listItemView;
    }
}
