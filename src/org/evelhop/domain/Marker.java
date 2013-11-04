package org.evelhop.domain;

public class Marker {
	
	private float lat;
	private float longi;
	private boolean isShop;
	private int availableBike;
	private boolean canCreditCard;
	
	private String name;
	
	public Marker()
	{
		
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLongi() {
		return longi;
	}

	public void setLongi(float longi) {
		this.longi = longi;
	}

	public boolean isShop() {
		return isShop;
	}

	public void setShop(boolean isShop) {
		this.isShop = isShop;
	}

	public int getAvailableBike() {
		return availableBike;
	}

	public void setAvailableBike(int availableBike) {
		this.availableBike = availableBike;
	}

	public boolean isCanCreditCard() {
		return canCreditCard;
	}

	public void setCanCreditCard(boolean canCreditCard) {
		this.canCreditCard = canCreditCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
