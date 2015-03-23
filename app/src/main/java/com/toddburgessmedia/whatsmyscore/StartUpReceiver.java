package com.toddburgessmedia.whatsmyscore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class StartUpReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
	
		/*
		Sha/redPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		float rawklout = sp.getFloat("rawkloutscore", 0);
		String twitterid = sp.getString("twitterid", "klout");
		
		Intent intent = new Intent(context, UpdateKloutScoreService.class);
		intent.putExtra("rawkloutscore", rawklout);
		intent.putExtra("twitterid",twitterid);
		
		context.startService(intent);
		*/
		
		
	
		launchService(context);
		
	}
	
	public static void launchService (Context c) {
		
		Intent die = new Intent(c,UpdateKloutScoreService.class);
		c.stopService(die);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		float rawklout = sp.getFloat("rawkloutscore", -1.0f);
		String twitterid = sp.getString("twitterid", "null23");
		
		Intent intent = new Intent(c, UpdateKloutScoreService.class);
		intent.putExtra("rawkloutscore", rawklout);
		intent.putExtra("twitterid",twitterid);
		
		c.startService(intent);
		
		
	}
	
	public static void launchService (Context c, float rawkloutscore) {
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		SharedPreferences.Editor edit = sp.edit();
		//float rawklout = sp.getFloat("rawkloutscore", 0);
		String twitterid = sp.getString("twitterid", "null23");
		
		
		edit.putFloat("rawkloutscore", rawkloutscore);
		edit.commit();
		
		Intent die = new Intent(c,UpdateKloutScoreService.class);
		c.stopService(die);
		
		Intent intent = new Intent(c, UpdateKloutScoreService.class);
		intent.putExtra("rawkloutscore", rawkloutscore);
		intent.putExtra("twitterid",twitterid);
		
		c.startService(intent);
		
		
		//StartUpReceiver.ServiceStart ss = new StartUpReceiver.ServiceStart();
		
		
		
	}
	
	public static void launchService(Context c, final long sleeptime) {
		
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			
		};
		
		Thread t = new Thread(r);
		t.start();
		
		StartUpReceiver.launchService(c);
		
	}
	
	public class ServiceStart extends AsyncTask<Void, Void, Void> {

		Context c;
		
		public Context getC() {
			return c;
		}

		public void setC(Context c) {
			this.c = c;
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
			float rawklout = sp.getFloat("rawkloutscore", 0);
			String twitterid = sp.getString("twitterid", "klout");
			
			Intent intent = new Intent(c, UpdateKloutScoreService.class);
			intent.putExtra("rawkloutscore", rawklout);
			intent.putExtra("twitterid",twitterid);
			
			c.startService(intent);
			
			
			return null;
		}
		
	}

}
