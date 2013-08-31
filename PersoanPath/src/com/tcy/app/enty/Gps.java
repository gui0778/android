package com.tcy.app.enty;

import java.io.Serializable;

public class Gps implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double lat;
	private double lng;
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	@Override
	public String toString()
	{
		return "Gps[lat=]";
	}

}
