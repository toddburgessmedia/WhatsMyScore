package com.toddburgessmedia.whatsmyscore;




import com.toddburgessmedia.whatsmyscore.R;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateKloutScoreService extends Service {


	final int sleeptime = 7200000;
	//final int sleeptime = 60000;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		// App stuff
		String kloutappid = getString(R.string.kloutappid);
		
		// Intent stuff
		if (intent == null) {
			return START_REDELIVER_INTENT;
		}
		final float rawklout = intent.getFloatExtra("rawkloutscore", 0);
		String twitterid = intent.getStringExtra("twitterid");
		
		final KloutScoreConnector ksc = new KloutScoreConnector(twitterid, kloutappid);
		
		//Log.d("GetMyKloutScore","intent float extra " + rawklout);
		
		Runnable r = new Runnable () {
			
			public void run () {
				
				float currentklout = rawklout;
				float newrawklout = currentklout;
			
				try {
					if (currentklout == -1.0f) {
						Thread.sleep (60000);
					} else {
						Thread.sleep(sleeptime);
						
						String newklout = ksc.getKloutScore();
						newrawklout = ksc.getRawKloutScore();
					}
					//Log.d("GetMyKloutScore","service thread working");
					
					//Log.d("GetMyKloutScore","current: " + currentklout + " new: " + newrawklout);
					
					Intent i = new Intent("com.toddburgessmedia.whatsmyscore.NEWSCORE");
					if ((newrawklout != currentklout) && 
							(currentklout != -1.0f) &&
							(newrawklout != 0.0)) {
						currentklout = newrawklout;
						i.putExtra("newscore",true);		
					} else {
						i.putExtra("newscore", false);
					}
					
					i.putExtra("rawkloutscore",currentklout);
						
					sendBroadcast(i);
					
				} catch (InterruptedException e) {
					
					Intent i = new Intent("com.toddburgessmedia.whatsmyscore.NEWSCORE");
					i.putExtra("newscore", false);
					i.putExtra("rawkloutscore",currentklout);	
					sendBroadcast(i);
					
					return;
					
				} catch (KloutException ke) {

					Intent i = new Intent("com.toddburgessmedia.whatsmyscore.NEWSCORE");
					i.putExtra("newscore", false);
					i.putExtra("rawkloutscore",currentklout);	
					sendBroadcast(i);
					return;
				}
			}
		};
	
		Thread t = new Thread(r);
		t.start();
		
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onCreate() {
		
		super.onCreate();
		
		//Log.d("GetMyKloutScore","service started");
	}

	@Override
	public void onDestroy() {
		
		//Log.d("GetMyKloutScore","service destoyed :(");
		
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	
		
	
	

}
