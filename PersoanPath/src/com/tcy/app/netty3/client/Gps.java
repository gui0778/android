package com.tcy.app.netty3.client;

import java.io.Serializable;
import java.util.Date;


public  class Gps implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1170098578152082675L;

	private static final String CHARSET="US-ASCII";

	private double lat;
    private double lng;
    private double alt;
    private long time;
    private boolean valid;
    private double speed;
    private double heading;
    
    private int acc; 
	
	
	public Gps() {
		super();
		this.lat = 0;
		this.lng = 0;
		this.time = new Date().getTime()/1000;
		this.valid = false;
		this.speed = 0;
		this.heading = 0;
		this.acc = 0;
	}
	

	public double getLat() {
		return lat;
	}

	public void setLat(double latitude) {
		this.lat = latitude;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double longitude) {
		this.lng = longitude;
	}

	public long getTime() {
		return time;
	}

	public void setTime(Date dateTime) {
		this.time = dateTime.getTime()/1000;
	}
	public void setTime(long dateTime) {
		this.time = dateTime;
	}
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	


	public int getAcc() {
		return acc;
	}


	public void setAcc(int accuracy) {
		this.acc = accuracy;
	}


	@Override
	public String toString() {
		return "GPSInfo [latitude=" + lat + ", longitude=" + lng
				+ ", dateTime=" + time + ", valid=" + valid
				+ ", speed=" + speed + ", heading=" + heading
				+ ", accuracy=" + acc + "]";
	}

	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

}