package com.toddburgessmedia.whatsmyscore;

import com.toddburgessmedia.whatsmyscore.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FirstTimeActivity extends Activity {

	Button b;
	EditText et;
	SharedPreferences sp;
	SharedPreferences.Editor edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.first_time);
		
		b = (Button) findViewById(R.id.button1);
		et = (EditText) findViewById(R.id.twitterid);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		edit = sp.edit();
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String twitterid = et.getText().toString();

                if (twitterid.charAt(0) == '@') {
                    twitterid = twitterid.substring(1);
                }

				if (twitterid.equals("")) {
					createErrorDialog();
				} else {
					edit.putBoolean("haschanged", true);
					edit.putBoolean("firsttime", false);
					edit.putString("twitterid", twitterid);
					edit.putFloat("kloutrawscore",-1.0f);
					edit.commit();
					
					Intent i = new Intent("com.toddburgessmedia.whatsmyscore.NEWSCORE");
					i.putExtra("newscore", false);
					sendBroadcast(i);
					
					Intent go = new Intent (FirstTimeActivity.this,GetMyKloutScore.class);
					startActivity(go);
					finish();
				}
				
				
				
				
			}
			
			void createErrorDialog() {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(FirstTimeActivity.this);
				builder.setMessage("Please Enter Your Twitter ID").setTitle("D'oh");
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						
						dialog.dismiss();
						
					}
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		
	}

}
