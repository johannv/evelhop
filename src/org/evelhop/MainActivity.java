package org.evelhop;


import org.evelhop.domain.Marker;
import org.evelhop.singleton.Singleton;

import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

	private static final String MAP_URL = "http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html";
	//private static final String MAP_URL = "http://www.google.fr";
	
	private static final float LAT_STRAS = 48.578562f;
	private static final float LONG_STRAS = 7.745f;
	
	private static final String GREEN_ICON = "http://img11.hostingpics.net/pics/357428dispoicon.png";
	private static final String RED_ICON = "http://img11.hostingpics.net/pics/721522nondispoicon.png";
	
	private Location mostRecentLocation;
	
	
	private WebView webView;
	 
		private void getLocation() {
		    LocationManager locationManager =
		      (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		    Criteria criteria = new Criteria();
		    criteria.setAccuracy(Criteria.ACCURACY_FINE);
		    String provider = locationManager.getBestProvider(criteria,true);
		    //In order to make sure the device is getting the location, request updates.
		    locationManager.requestLocationUpdates(provider, 1, 0, this);
		    mostRecentLocation = locationManager.getLastKnownLocation(provider);
		  }
	
	  @Override
	  /** Called when the activity is first created. */
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    getLocation();
	    setupWebView();
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	  }
	  
	  /** Sets up the WebView object and loads the URL of the page **/
	  private void setupWebView(){
	    webView = (WebView) findViewById(R.id.webview);
	    webView.getSettings().setJavaScriptEnabled(true);
	    //Wait for the page to load then send the location information
	    webView.setWebChromeClient(new WebChromeClient());
	    
	  //Wait for the page to load then send the location information
	    webView.setWebViewClient(new WebViewClient(){
	      @Override
	      public void onPageFinished(WebView view, String url){
	        String centerURL = "centerAt(" + LAT_STRAS + "," + LONG_STRAS + ");";
	        webView.loadUrl("javascript:"+getStringJSProperty()+getCompleteMarkersString()+centerURL);
	      }
	    });
	    
	    webView.loadUrl(MAP_URL);
	  }
	  
	
	public String getStringJSProperty()
	{
		String loc = "var myLatlng = new google.maps.LatLng(" + LAT_STRAS + "," + LONG_STRAS + ");";
		String options = "var myMapOptions = {zoom: 13,center: myLatlng,mapTypeId: google.maps.MapTypeId.ROADMAP};";
		String map = "var myMap = new google.maps.Map(document.getElementById('map_canvas'),myMapOptions);";
		
		String color = "var red = \"http://img11.hostingpics.net/pics/374068nondispopin.png\";" +
					   "var green = \"http://img11.hostingpics.net/pics/311569dispopin.png\";" +
					   "var purple = \"http://img11.hostingpics.net/pics/193012boutiquepin.png\";";
		
		
		
		return loc+options+map+color;
	}
	
	public String getCompleteMarkersString()
	{
		String s = "";
		for(int i = 0;i <= Singleton.markerList.size() - 1;i++)
		{
			s += getMarkerString(Singleton.markerList.get(i),i);
		}
			
		return s;
	}
	  
	public String getMarkerString(Marker marker,int markerNumber)
	{
		float lat = marker.getLat();
		float longi = marker.getLongi();
		String image = "";
		String color = "green";
		
		String name = marker.getName();
		
		Log.e("name = ",name);
		
		if(marker.getAvailableBike() > 0)
		{
			image = GREEN_ICON;
			color = "green";
		}
		else
		{
			image = RED_ICON;
			color = "red";
		}
		
		String canCC = "";
		if(marker.isCanCreditCard())
			canCC = getString(R.string.yes);
		else
			canCC = getString(R.string.no);
		
		String mark = "var myMarker"+markerNumber+" = new google.maps.Marker({" +
				"position: new google.maps.LatLng("+lat+","+longi+")," +
				"map: myMap," +
				"icon: "+color+"," +
				"title: \"marker "+markerNumber+"\"});";
		
		String window = "var myWindows"+markerNumber+"= {content:'<div id=\"info_view_child\" style=\"height:55px !important;margin-top:-5px;\">" +
																		"<table style=\"font-size:14px;height:55px !important;\">" +
																		"<tr>" +
																			"<td rowspan=\"2\" style=\"width:45px;\"> <img src=\""+image+"\" style=\"margin-top:-15px;\" /> </td>" +
																			"<td><h4> "+name+" </h4></td></tr>" +
																		"<tr>" +
																			"<td><h5> "+getString(R.string.available_bike)+" "+marker.getAvailableBike()+"  - "+getString(R.string.CB)+" "+canCC+" </h5></td></tr></table>" +
																		"</div>'};";
		String infoWindow = "var myInfoWindow"+markerNumber+" = new google.maps.InfoWindow(myWindows"+markerNumber+");";
		String click = "google.maps.event.addListener(myMarker"+markerNumber+", 'click', function() {myInfoWindow"+markerNumber+".open(myMap,myMarker"+markerNumber+");});";
		return mark+window+infoWindow+click;
	}
	  
	// #### Functions to create the menu #####   
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try
		{
			MenuInflater inflater = getMenuInflater();
		     inflater.inflate(R.menu.menu, menu);
		    return super.onCreateOptionsMenu(menu);
		}catch (Exception e) {
			return true;
		} 
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        super.onOptionsItemSelected(item);
 
        switch(item.getItemId()){
            case R.id.about:
            	Intent intent = new Intent(this, DetailActivity.class);
        		this.startActivity(intent);
                break;
            case R.id.refresh:
            	setupWebView();
                break;
        }
        return true;
 
    }



	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
