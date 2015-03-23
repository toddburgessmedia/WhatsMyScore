package com.toddburgessmedia.whatsmyscore;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class WhatsMyScoreApplication extends Application {

	@Override
	public void onCreate() {
		
		// TODO Auto-generated method stub
		super.onCreate();
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		float rawklout = sp.getFloat("rawkloutscore", 0);
		String twitterid = sp.getString("twitterid", "klout");
		
		Intent intent = new Intent(this, UpdateKloutScoreService.class);
		intent.putExtra("rawkloutscore", rawklout);
		intent.putExtra("twitterid",twitterid);
		
		//startService(intent);
		
		Log.d("GetMyKloutScore","application starting service");
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

}
