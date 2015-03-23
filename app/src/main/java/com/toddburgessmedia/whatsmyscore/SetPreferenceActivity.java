package com.toddburgessmedia.whatsmyscore;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class SetPreferenceActivity extends Activity  {

	OnSharedPreferenceChangeListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = prefs.edit();
		
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				
					//Log.d("GetMyKloutScore","prefs changed" + key);
					
					editor.putBoolean("haschanged", true);
					editor.putFloat("rawkloutscore", 0);
					editor.commit();
				
			}
		};
		
		prefs.registerOnSharedPreferenceChangeListener(listener);
		
		
		
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new SettingsFragment()).commit();
	}

}
