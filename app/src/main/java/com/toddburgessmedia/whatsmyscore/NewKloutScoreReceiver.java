package com.toddburgessmedia.whatsmyscore;

import com.toddburgessmedia.whatsmyscore.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.NotificationCompat.Builder;


public class NewKloutScoreReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context c, Intent arg1) {
		
		Log.d("GetMyKloutScore", "we got a message!!!!!!!");
		
		boolean newscore = arg1.getBooleanExtra("newscore", false);
		if (!newscore) {
			StartUpReceiver.launchService(c);
			return;
		}
		
		float currentscore = arg1.getFloatExtra("rawkloutscore", 0.0f);
		
		Intent i = new Intent (c,GetMyKloutScore.class);
		PendingIntent pi = PendingIntent.getActivity(c, 0, i, 0);
		
		NotificationManager nm = (NotificationManager) c
				.getSystemService(c.NOTIFICATION_SERVICE);
		
		long pattern[]  = {0,500};
		Notification n;
		n = new Notification.Builder(c)
			.setContentTitle(c.getString(R.string.whatsmyscore))
			.setContentText(c.getString(R.string.you_have_a_new_klout_score_))
			.setSmallIcon(R.drawable.ic_stat_whatsthescorenotify)
			.setContentIntent(pi)
			.setAutoCancel(true) 
			.setVibrate(pattern)
			.build();
		
		nm.notify(0,n);
		
		float rawkloutscore = arg1.getFloatExtra("rawkloutscore", currentscore);
		
		StartUpReceiver.launchService(c,currentscore);

	}
	
	

}
