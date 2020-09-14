package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class wordAdapter extends ArrayAdapter<word> {

    private int mcolor;


    public wordAdapter(Activity context, ArrayList<word> numbers,int color) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // going to use this second argument, so it can be any value. Here, we used 0.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        super(context, 0, numbers);
        mcolor=color;
    }
        @Override
        public View getView(int position,View convertView,ViewGroup parent){
            // Check if the existing view is being reused, otherwise inflate the view
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

            // Get the {@link AndroidFlavor} object located at this position in the list
            word currentword = getItem(position);

            // Find the TextView in the list_item.xml layout with the ID version_name
            TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok);
            // Get the version name from the current AndroidFlavor object and
            // set this text on the name TextView
            miwokTextView.setText(currentword.getMiwokTranslation());

            // Find the TextView in the list_item.xml layout with the ID version_number
            TextView englishTextView = (TextView) listItemView.findViewById(R.id.english);
            // Get the version number from the current AndroidFlavor object and
            // set this text on the number TextView
            englishTextView.setText(currentword.getDefaultTranslation());

            ImageView imageView =(ImageView) listItemView.findViewById(R.id.image);
            if(currentword.hasimage()){
           imageView.setImageResource(currentword.getmImage());
           imageView.setVisibility(View.VISIBLE);
            }
            else{
                imageView.setVisibility(View.GONE);
            }
            View textContainer = listItemView.findViewById(R.id.text_container);
            int color = ContextCompat.getColor(getContext(),mcolor);
            textContainer.setBackgroundColor(color);




            // Return the whole list item layout (containing 2 TextViews and an ImageView).
            // so that it can be shown in the ListView.
            return listItemView;
        }
    }

