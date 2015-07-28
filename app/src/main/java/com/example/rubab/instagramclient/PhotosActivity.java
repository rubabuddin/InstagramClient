package com.example.rubab.instagramclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "1eef72e2bf2d453ca03c87df09bdfb89";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        //Send out API request to Popular Photos
        photos = new ArrayList<>();
        //1. Create the adapter linking it to the source
        aPhotos = new InstagramPhotosAdapter(this, photos);
        //2. Find the listview from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //3. Set the adapter binding it to the listview
        lvPhotos.setAdapter(aPhotos);
        //Fetch the popular photos
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos()
    {
    //    Popular Photos: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN


        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        //Create network client
        AsyncHttpClient client = new AsyncHttpClient();

        //Trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler(){
            //onSuccess
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Expecting a JSON object


                //      iterate each of the photo items and decode the item into a java object
                JSONArray photosJSON = null;
                try{
                    photosJSON = response.getJSONArray("data"); //gets us array of posts

                    // iterate array of posts
                    for(int i=0; i<photosJSON.length(); i++){

                        //get the JSON object at that position in the array
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode the collected attributes of the JSON into our data model
                        InstagramPhoto photo = new InstagramPhoto();
                        //    - Username: { "data" => [x] => "user" => "username" }
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        //    - Caption: { "data" => [x] => "caption" => "text" }
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        //    - Type: { "data" => [x] => "type" } ("image" or "video")
                        //photo.type = photoJSON.getJSONObject("type").getString("text");
                        //    - Graphic: { "data" => [x] => "images" => "standard_resolution" => "url" }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        //Height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        //Likes count
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");

                        //Add each decoded object to the photo array
                        photos.add(photo);

                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
                //Log.i("DEBUG", response.toString());

                //triggers listview to refresh
                aPhotos.notifyDataSetChanged();
            }

            //onFailure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
