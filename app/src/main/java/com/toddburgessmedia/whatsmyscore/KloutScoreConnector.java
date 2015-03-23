package com.toddburgessmedia.whatsmyscore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class KloutScoreConnector {
	
	String twitterid;
	int kloutscore;
	String kloutid= "-1";
	String kloutappid;
	
	float daychange;
	float weekchange;
	float monthchange;
	
	
	public float getDaychange() {
		return daychange;
	}

	public void setDaychange(String daychange) {
		float newchange = Float.parseFloat(daychange);
		int tempchange = (int) (newchange * 100);
		this.daychange = tempchange / 100f;
	}

	public float getWeekchange() {
		return weekchange;
	}

	public void setWeekchange(String weekchange) {
		float newchange = Float.parseFloat(weekchange);
		int tempchange = (int) (newchange * 100);
		this.weekchange = tempchange / 100f;
	}

	public float getMonthchange() {
		return monthchange;
	}

	public void setMonthchange(String monthchange) {
		float newchange = Float.parseFloat(monthchange);
		int tempchange = (int) (newchange * 100);
		this.monthchange = tempchange / 100f;
	}

	
	private float rawkloutscore = 0.0f;
	
	public float getRawKloutScore() {
		return rawkloutscore;
	}

	public void setRawKloutScore(float rawkloutscore) {
		this.rawkloutscore = rawkloutscore;
	}
	
	public void setRawKloutScore (String rawkloutscore) {
		this.rawkloutscore = Float.parseFloat(rawkloutscore);
		
		int tmp = (int) (this.rawkloutscore * 100000);
		this.rawkloutscore = tmp / 100000f;
	}

	public KloutScoreConnector(String twitterid,String kloutappid) {
		this.twitterid = twitterid;
		this.kloutappid = kloutappid;
	}

	private void getKloutId () throws KloutException  {
		
		URL url;
		
		if (twitterid.equals("null23")) {
			throw new KloutException();
		}
		
		try {
			
			url = new URL ("http://api.klout.com/v2/identity.json/twitter?screenName=" + twitterid + "&key=" + kloutappid);
			URLConnection uc = url.openConnection();
			
			BufferedReader in = new BufferedReader (
					new InputStreamReader(uc.getInputStream()));
			
			String line = in.readLine();
			
			//Log.d("GetMyKloutScore",line);
			
			JSONObject jo = new JSONObject (line);
			kloutid = jo.getString("id");
			
			//Log.d("GetMyKloutScore","kloutid " + kloutid);
			
			
		} catch (MalformedURLException e) {
			//Log.d("GetMyKloutScore",e.toString());
			throw new KloutException();
		} catch (IOException e) {
			//Log.d("GetMyKloutScore",e.toString());
			throw new KloutException();
		} catch (JSONException e) {
			//Log.d("GetMyKloutScore",e.toString());
			throw new KloutException();
		}
		
	
	}
	
	public String getKloutScore () throws KloutException {
		String kloutscore = "";
		int myscore = 0;
		
		
		if (twitterid.equals("null23")) {
			throw new KloutException();
			
		}
		
		
		if (kloutid.equals("-1")) {
			getKloutId();
		}
		
		try {
			URL url = new URL ("http://api.klout.com/v2/user.json/" + kloutid + "?key=" + kloutappid);
			URLConnection uc = url.openConnection();
			
			BufferedReader in = new BufferedReader (
					new InputStreamReader(uc.getInputStream()));
			
			String line = in.readLine();
			//Log.d("GetMyKloutScore","first: " + line);
			
			JSONObject jo = new JSONObject (line);
			JSONObject score = jo.getJSONObject("score");
			JSONObject scoredeltas = jo.getJSONObject("scoreDeltas");
			
			kloutscore = score.getString("score");
			
			//Log.d("GetMyKloutScore","score string " + kloutscore);
			
			setRawKloutScore(kloutscore);
			
			
			setDaychange(scoredeltas.getString("dayChange"));
			setWeekchange(scoredeltas.getString("weekChange"));
			setMonthchange(scoredeltas.getString("monthChange"));
			
			
			//Log.d("GetMyKloutScore","after setting " + getRawKloutScore());
		
			myscore = (int) Math.round(Double.parseDouble(kloutscore));
			
			//Log.d ("GetMyKloutScore", "score " + myscore);
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new KloutException();
		} catch (IOException e) {
			throw new KloutException();
		} catch (JSONException e) {
			throw new KloutException();
		}
		
		
		
		return String.valueOf(myscore);
		
	}
	
}
