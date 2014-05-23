package rmo5087.edu.psu.soundscape;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rmo5087.edu.psu.soundscape.JSONParser;
//import edu.psu.mob_apps.android_remotedb.AllEntriesActivity;
//import edu.psu.mob_apps.android_remotedb.AppConstants;
//import edu.psu.mob_apps.android_remotedb.EditEntryActivity;
//import edu.psu.mob_apps.android_remotedb.JSONParser;
//import edu.psu.mob_apps.android_remotedb.NewEntryActivity;
//import edu.psu.mob_apps.android_remotedb.R;
//import edu.psu.mob_apps.android_remotedb.AllEntriesActivity.LoadAllEntries;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	public static int UPDATE_ENTRY_RESULT = 100;
	static final String LOG_TAG = "Songscape";
    ArrayList<HashMap<String, String>> listAdapterData;
    ListView lv;
    
    // url to get all songs list
    private static String url_all_records = "http://pascal.hbg.psu.edu:6917/bin/getMusicInfo";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        PreferenceManager.setDefaultValues(this, R.layout.preferences, false);
        
        //set background color using saved preferences
    	SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_backgroundColor), Context.MODE_PRIVATE);//??
         
        String defaultValue = getResources().getString(R.string.pref_backgroundColor_default);
        String s = getString(R.string.saved_background_color);
        String tmp = sharedPref.getString(s, defaultValue);
        
        View v = findViewById(R.id.listSongs);
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
        
        
        // Get song data from database
        // For ListView
        listAdapterData = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllEntries().execute();
 
        // Get listview
        lv = (ListView) findViewById(R.id.listSongs);
 
        // on selecting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
            	TextView txtId = (TextView) view.findViewById(R.id.id);
                String uniqueId = txtId.getText().toString();
 
                // Starting new intent
                Intent in = new Intent(MainActivity.this, SongActivity.class);
                // sending id to next activity
                in.putExtra("id", uniqueId);
 
                // starting new activity and expecting some response back
                startActivityForResult(in, UPDATE_ENTRY_RESULT);
            }
        });
 
    }
 
    @Override
    protected void onResume()
    {
    	super.onResume();
    	//set background color using saved preferences
    	SharedPreferences sharedPref = getSharedPreferences(getString(R.string.saved_background_color), Context.MODE_PRIVATE);//??
         
        String defaultValue = getResources().getString(R.string.pref_backgroundColor_default);
        String s = getString(R.string.saved_background_color);
        String tmp = sharedPref.getString(s, defaultValue);
        
        View v = findViewById(R.id.listSongs);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UPDATE_ENTRY_RESULT) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
 
    /**
     * Background AsyncTask to Load all product by making HTTP Request
     * */
    class LoadAllEntries extends AsyncTask<Void, Void, Boolean> {
    	
        // Progress Dialog
        private ProgressDialog progressDialog;
        
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading songs. Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
 
        @Override
        protected Boolean doInBackground(Void... args) {

            JSONObject json = JSONParser.makeHttpRequest(url_all_records, "GET", new ArrayList<NameValuePair>());
            if (json == null) return false;
 
            try {
 
                if (json.getString("result").equals("success")) {
                    // records returned
                    JSONArray entries = json.getJSONArray("records");
 
                    // looping through all records
                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject c = entries.getJSONObject(i);
 
                        // creating new HashMap for this entry in List
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", Integer.toString(c.getInt("id")));
                        map.put("song", c.getString("song"));
                        map.put("artist", c.getString("artist"));
                        map.put("album", c.getString("album"));
 
                        // adding HashMap to collection for all entries in ListView
                        listAdapterData.add(map);
                    }
                }
            } catch (JSONException e) {
                Log.d(LOG_TAG, e.getMessage());
                return false;
            }
 
            return true;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog after getting all records
        	try {
                progressDialog.dismiss();
        	}
        	catch (Exception e) {}
        	
            if (result == false) return;
            
	        /**
	         * Updating parsed JSON data into ListView
	         * */
	        ListAdapter adapter = new SimpleAdapter(
	                MainActivity.this, listAdapterData,
	                R.layout.list_item, new String[] { "id",
	                        "song", "artist", "album"},
	                new int[] { R.id.id, R.id.song, R.id.artist, R.id.album });
	
	        lv.setAdapter(adapter); 
        }
 
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
			Intent i = new Intent(MainActivity.this, Settings.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
}
