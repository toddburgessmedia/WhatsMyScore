package com.toddburgessmedia.whatsmyscore;



import com.toddburgessmedia.whatsmyscore.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetMyKloutScore extends Activity {


	Button b;
	TextView tv;
	TextView userid;
	String kloutappid;
	private String twitterid;
	ImageView iv;
	
	float kloutscore;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        kloutappid = getString(R.string.kloutappid);
        
        tv = (TextView) findViewById(R.id.textView11);
        userid = (TextView) findViewById(R.id.textView20);
        iv = (ImageView) findViewById(R.id.imageView1);
        
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		boolean firsttime = sharedPref.getBoolean("firsttime", true);
		
		if (firsttime) {
			Intent i = new Intent(GetMyKloutScore.this,FirstTimeActivity.class);
			startActivity(i);
			finish();
			return;
		}
		twitterid = sharedPref.getString("twitterid", "null");
        GetKloutScore ks = new GetKloutScore();
		ks.execute();
		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	//Log.d("WhatsMyScore","menu item " + item.getTitle());
    	
    	if (item.getTitle().toString().equals("Settings")) {
	    	Intent intent = new Intent();
	        intent.setClass(GetMyKloutScore.this, SetPreferenceActivity.class);
	        startActivityForResult(intent, 0); 
    	} else if (item.getTitle().toString().equals("Notification")) {
            Intent i = new Intent("com.toddburgessmedia.whatsmyscore.NEWSCORE");
            i.putExtra("newscore", true);
            sendBroadcast(i);
        } else if (item.getTitle().toString().equals("Test")) {
            Intent i = new Intent(GetMyKloutScore.this, Main2Activity.class);
            startActivityForResult(i,0);
    	} else { 
    		createAboutDialog();
    	}
   
         return true;
    }
    
    void createAboutDialog() {
		
    	TextView atv = new TextView(this);
    	atv.setText("Todd Burgess\nTodd Burgess Media\n\ntodd@toddburgessmedia.com\n@tburgess57");
    	atv.setGravity(Gravity.CENTER_HORIZONTAL);

		AlertDialog.Builder builder = new AlertDialog.Builder(GetMyKloutScore.this);
		builder.setView(atv).setTitle("About");
		
		builder.setPositiveButton("Nifty", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
    
    private class GetKloutScore extends AsyncTask <Void, Void, String> {

    	KloutScoreConnector ksc; 
    	double rawklout;
    	
		@Override
		protected String doInBackground(Void... params) {
			
			String myklout = "";
			
			ksc = new KloutScoreConnector(twitterid,kloutappid);

			try {
				myklout = ksc.getKloutScore();
			} catch (Exception e) {

			}
			kloutscore = ksc.getRawKloutScore();
			setRawKloutScorePreference();
			
			return String.valueOf(myklout);

		}

		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);	
			
			Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/DINBold.ttf");
			tv.setTypeface(tf);

			tv.setText(result);
			
			char sign;
			if (ksc.getDaychange() > 0) {
				sign = '+';
			} else { 
				sign = ' ';
			}
			
			userid.setText("@"+twitterid+" "+sign+ksc.getDaychange());
		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
		}

    }
    
    @Override
    protected void onResume() {
    	
    	super.onResume();
    	
    	boolean haschanged;
    	GetKloutScore ks = new GetKloutScore();
    	
    	//Log.d("GetMyKloutScore","onresume");
    	
    	//Log.d("GetMyKloutScore", "raw klout score " + kloutscore);
    	
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	haschanged = prefs.getBoolean("haschanged",false);

    	if (haschanged) {
    		twitterid = prefs.getString("twitterid", "null23");
    		ks.execute();
    		SharedPreferences.Editor editor = prefs.edit();
    		editor.putBoolean("haschanged", false);
    		//editor.putFloat("rawkloutscore", kloutscore);
    		editor.commit();
    	}
    }
    
    private void setRawKloutScorePreference () {
    	
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("haschanged", false);
		editor.putFloat("rawkloutscore", kloutscore);
		editor.commit();
    	
    }
    
    
    
}
