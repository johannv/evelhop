package org.evelhop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.evelhop.singleton.Singleton;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


public class IntroductionActivity extends Activity
{
	static final String PREFERENCES_EULA = "eula";
	static final String PREFERENCE_EULA_ACCEPTED = "eula.accepted";
	static final String ASSET_EULA = "EULA";
	private Timer timer;
	
	private LoadStationTask LST;

	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		this.setContentView(R.layout.introduction);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		this.timer = new Timer();
		this.timer.schedule(new PendingIntroductionTask(), 1000);
		
		this.LST = new LoadStationTask();
		this.LST.execute();
	}

	protected void startLoginActivity()
	{
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivity(intent);
	}
	
	private class LoadStationTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params) {
			URL url;
			try {
				url = new URL("http://velhop.strasbourg.eu/tvcstations.xml");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String inputLine;
				String input = "";
	
				while ((inputLine = in.readLine()) != null)
				    input += inputLine;
	
				in.close();
				
				Singleton.loadMarkerlistWithString(input);
				
				startLoginActivity();
				finish();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}

	private class PendingIntroductionTask extends TimerTask
	{
		@Override
		public void run()
		{
			try
			{
				//startLoginActivity();
				timer.cancel();
				//finish();
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}
}
