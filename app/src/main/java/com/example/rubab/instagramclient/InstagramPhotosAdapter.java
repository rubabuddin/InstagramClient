package com.example.rubab.instagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    //data needed from activity
    //Parameters: Context, Data Source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //What our item looks like
    //Use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for this position
        InstagramPhoto photo = getItem(position);
        //Check if we are using a recycled view, if not, inflate
        if(convertView == null)
        {
            //recycled view does not exist, create a new view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        //Lookup the views for populating the data (image, caption)
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        //Insert the model data into each of the view items
        String heartUnicode = "\u2764";
        String formattedTitle = "<html><br><br><font color=\"#517fa4\"<b>" + photo.username + "</b></font></html>";
        String formattedLikes = "<html><font color=\"#517fa4\"<b>" + heartUnicode + " "  + photo.likesCount + " likes</b></font></html>";
        String formattedCaption = "<html><br><font color=\"#517fa4\"<b>" + photo.username + "</b></font> " + photo.caption + "</html>";
        tvTitle.setText(Html.fromHtml(formattedTitle));
        tvLikes.setText(Html.fromHtml(formattedLikes));
        tvCaption.setText(Html.fromHtml(formattedCaption));
        //Clear out the ImageView
        ivPhoto.setImageResource(0);
        //Insert the image using Picasso (send out async request)
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        //Return the created item as a view
        return convertView;
    }
}
