package org.evelhop.singleton;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.evelhop.domain.Marker;

import android.util.Log;


public class Singleton {

	public static ArrayList<Marker> markerList;
	
	public static void loadMarkerlistWithString(String s)
	{
		markerList = new ArrayList<Marker>();
		
		s = s.replace("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>", "");
		s = s.replace("<vcs ver=\"1\">", "");
		s = s.replace("</vcs>", "");
		s = s.replace("<sl>", "");
		s = s.replace("</sl>", "");
		
		String[] myList = s.split("</si>");

		for (String subString : myList) {
			Marker m = new Marker();
			
			String name = getStringBetween("na=\"","\"",subString).replace(getStringBetween("id=\"","\"",subString)+" ", "");
			name = name.replace("�", "é");
			name = name.replace("'", " ");

			m.setName(name);
			
			String av = getStringBetween("av=\"","\"",subString);
			m.setAvailableBike(Integer.parseInt(av));
			
			String lat = getStringBetween("la=\"","\"",subString);
			m.setLat(Float.parseFloat(lat));
			
			String lg = getStringBetween("lg=\"","\"",subString);
			m.setLongi(Float.parseFloat(lg));
			
			if(getStringBetween("cb=\"","\"",subString).equals(""))
				m.setCanCreditCard(false);
			else
				m.setCanCreditCard(true);
			
			markerList.add(m);
		}
		
		
		
	}
	
	public static String getStringBetween(String s1,String s2,String myString)
	{
		try
		{
			Pattern p = Pattern.compile(s1+"(.*?)"+s2);
			Matcher m = p.matcher(myString);
			m.find();
			
			return m.group(1);
		}catch (Exception e) {
			return "";
		}
	}
	
}
