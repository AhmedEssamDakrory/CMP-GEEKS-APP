package com.example.android.cmpgeeks.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cmpgeeks.DataBaseEntity.Post;
import com.example.android.cmpgeeks.DataBaseEntity.Team;
import com.example.android.cmpgeeks.R;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> {
    public PostAdapter(@NonNull Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.post_item_layout, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Post post = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView postText = (TextView) listItemView.findViewById(R.id.post_text);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        postText.setText(post.getmText());

        TextView postNmae = (TextView) listItemView.findViewById(R.id.post_name);
        postNmae.setText(post.getmFName()+ " "+post.getmLName());

        TextView postDate =(TextView)listItemView.findViewById(R.id.post_date);
        postDate.setText(post.getmDate());

        return listItemView;
    }

}
