package rmo5087.edu.psu.soundscape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class SongActivity extends Activity{
	
	private static final String url_product_details = "http://pascal.hbg.psu.edu:6917/bin/getMusicInfo";
	
	MediaPlayer mediaPlayer;
	String Id;
	int raw = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_activity);
		
		//set background color using saved preferences
        PreferenceManager.setDefaultValues(this, R.layout.preferences, false);
    	SharedPreferences sharedPref = getSharedPreferences(getString(R.string.saved_background_color), Context.MODE_PRIVATE);//??
         
        String defaultValue = getResources().getString(R.string.pref_backgroundColor_default);
        String s = getString(R.string.saved_background_color);
        String tmp = sharedPref.getString(s, defaultValue);
        
        View v = findViewById(R.id.songname);
        View root = v.getRootView();
        
        if(tmp.equals("White"))
        	root.setBackgroundColor(getResources().getColor(R.color.white));
        else if(tmp.equals("Gray"))
        	root.setBackgroundColor(getResources().getColor(R.color.gray));
        else if(tmp.equals("Blue"))
        	root.setBackgroundColor(getResources().getColor(R.color.blue));
        else if(tmp.equals("Green"))
        	root.setBackgroundColor(getResources().getColor(R.color.green));
        else if(tmp.equals("Yellow"))
        	root.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(tmp.equals("Purple"))
        	root.setBackgroundColor(getResources().getColor(R.color.purple));
		Intent i = getIntent();
		Id = i.getStringExtra("id");

		//Corresponding song based off of id:
		//  1: Get Lucky
		//  2: Instant Crush
		//  3: Fragments of Time
		//  4: Bohemian Rhapsody
		//  5: Don't Stop Me Now
		//  6: Killer Queen
		if(Id.equals("1"))
			raw = R.raw.get_lucky;
		else if(Id.equals("2"))
			raw = R.raw.instant_crush;
		else if(Id.equals("3"))
			raw = R.raw.fragments_of_time;
		else if(Id.equals("4"))
			raw = R.raw.bohemian_rhapsody;
		else if(Id.equals("5"))
			raw = R.raw.dont_stop_me_now;
		else if(Id.equals("6"))
			raw = R.raw.killer_queen;
		
		mediaPlayer = MediaPlayer.create(SongActivity.this, raw);
		if(mediaPlayer.isPlaying()) mediaPlayer.stop();
		mediaPlayer.start(); //Can do this on the UI thread because it takes very little time to grab the file
		
		new GetSongDetails().execute();
	}
	
    //Action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()){
		case R.id.back_button:
			this.finish();
			return true;
		case R.id.action_settings:
			Intent i = new Intent(SongActivity.this, Settings.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
    protected void onResume()
    {
    	super.onResume();
    	SharedPreferences sharedPref = getSharedPreferences(getString(R.string.saved_background_color), Context.MODE_PRIVATE);//??
         
        String defaultValue = getResources().getString(R.string.pref_backgroundColor_default);
        String s = getString(R.string.saved_background_color);
        String tmp = sharedPref.getString(s, defaultValue);
         
        int bgcolor = getResources().getColor(R.color.white);
        
        View v = findViewById(R.id.songname);
        View root = v.getRootView();
        
        if(tmp.equals("White"))
        	root.setBackgroundColor(getResources().getColor(R.color.white));
        else if(tmp.equals("Gray"))
        	root.setBackgroundColor(getResources().getColor(R.color.gray));
        else if(tmp.equals("Blue"))
        	root.setBackgroundColor(getResources().getColor(R.color.blue));
        else if(tmp.equals("Green"))
        	root.setBackgroundColor(getResources().getColor(R.color.green));
        else if(tmp.equals("Yellow"))
        	root.setBackgroundColor(getResources().getColor(R.color.yellow));
        else if(tmp.equals("Purple"))
        	root.setBackgroundColor(getResources().getColor(R.color.purple));
    }

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;
	}
	
	public void playButton(View view)
	{
		if(!mediaPlayer.isPlaying())
			mediaPlayer.start();
	}
	public void pauseButton(View view)
	{
		if(mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}
	public void replayButton(View view)
	{
		if(mediaPlayer.isPlaying()) mediaPlayer.stop();
		mediaPlayer.reset();
		mediaPlayer = MediaPlayer.create(SongActivity.this, raw);
		mediaPlayer.start();
	}
	
	
	
    class GetSongDetails extends AsyncTask<Void, Void, Void> {
    	private String sSong;
    	private String sArtist;
    	private String sAlbum;
        private ProgressDialog progressDialog;

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SongActivity.this);
            progressDialog.setMessage("Loading song...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        @Override
        protected Void doInBackground(Void... args) {

            try {
            	JSONObject json = JSONParser.makeHttpRequest(url_product_details, "GET", new ArrayList<NameValuePair>());
                  
                if (json == null) return null;
                        
                if (json.getString("result").equals("success")) {
                    // successfully received product details
                    JSONArray productObj = json.getJSONArray("records");
 
                    JSONObject entry;
                    if(Id.equals("1"))
                    	entry = productObj.getJSONObject(0);
                    else if(Id.equals("2"))
                    	entry = productObj.getJSONObject(1);
                    else if(Id.equals("3"))
                    	entry = productObj.getJSONObject(2);
                    else if(Id.equals("4"))
                    	entry = productObj.getJSONObject(3);
                    else if(Id.equals("5"))
                    	entry = productObj.getJSONObject(4);
                    else if(Id.equals("6"))
                    	entry = productObj.getJSONObject(5);
                    else
                    	entry = productObj.getJSONObject(0); //default to first object
 
                    sSong = entry.getString("song");
                    sArtist = entry.getString("artist");
                    sAlbum = entry.getString("album");
 
                }
            } catch (JSONException e) {
                Log.d(MainActivity.LOG_TAG, e.getMessage());
            } 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(Void result) {
            // dismiss the dialog once got all details
            progressDialog.dismiss();
            String uri = "";
            int imageResource;
            
            if (sSong != null && sArtist != null) {
	            // display product data in TextView
	            TextView txtPrefix = (TextView) findViewById(R.id.songname);
	            TextView txtSuffix = (TextView) findViewById(R.id.artistname);
	            TextView txtCredits = (TextView) findViewById(R.id.albumname);
	            ImageView albumCover = (ImageView) findViewById(R.id.albumimageframe);

	            txtPrefix.setText(sSong);
	            txtSuffix.setText(sArtist);
	            txtCredits.setText(sAlbum);
	            
	            //based off of album name, get drawable resource
	            if(sAlbum.equals("Queen's Greatest Hits"))
	            	uri = "@drawable/queen_greatest_hits";
	            else if(sAlbum.equals("Random Access Memories"))
	            	uri = "@drawable/random_access_memories";
	            else
	            	uri = "@drawable/ic_launcher.png";
	            
	            
	            //set the albumCover to drawable resource
            	imageResource = getResources().getIdentifier(uri, null, getPackageName());
            	Drawable d = getResources().getDrawable(imageResource);
            	albumCover.setImageDrawable(d);
            }
        }
    }
}
