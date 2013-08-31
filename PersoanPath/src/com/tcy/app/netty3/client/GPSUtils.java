package com.tcy.app.netty3.client;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GPSUtils {

	public static double convertToDegree(double dm) {
	
		double degree, min;
		double adm = Math.abs(dm);
		
		degree= Math.floor(adm/100);
		
		min=(adm -(degree)*100)/60;

		degree = degree+min;
		
		if(dm < 0.0) {
			degree *= -1;
		}
		
		return degree;
	}
	
	public static double convertFromDegree(double dg){
		double degree = Math.abs(Math.floor(dg));
		double min = (dg - degree)*60;
		degree = degree*100 + min;
		if(dg < 0.0){
			degree *= -1;
		}
		return degree;
	}
	
}


