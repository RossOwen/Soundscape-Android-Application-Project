package rmo5087.edu.psu.soundscape;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Settings extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
	}
	
	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			addPreferencesFromResource(R.layout.preferences);
		}
		
		@Override
		public void onPause()
		{
			super.onPause();
			//save background color in shared preferences
			ListPreference lp = (ListPreference) findPreference("pref_backgroundColor");
			
			SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_background_color), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(getString(R.string.saved_background_color), lp.getEntry()+"");

			editor.commit();
			
	        String defaultValue = getResources().getString(R.string.pref_backgroundColor_default);
	        String s = getString(R.string.saved_background_color);
	        String tmp = sharedPref.getString(s, defaultValue);
	         
	        int bgcolor = getResources().getColor(R.color.white);
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
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
