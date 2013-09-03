package com.tcy.app.netty3.client;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





public class R80Message implements IMessage {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(R80Message.class);
	
	

	public static final String TEXT_CHARSET="UTF-16LE";
	public static final String CHARSET="US-ASCII";
	public static final String UTF8="UTF-8";
	
	public static final int MIN_FRAME = 15;
	public static final int HEADER_LEN = 13;
	public static final int ID_LEN = 7;
	public static final int TAIL_LEN = 4;
	public static final int FROMSERVER = 0x4040;
	public static final int FROMDEVICE = 0x2424;
	public static final int ENDLINE = 0x0d0a;
	public static final int MAX_PN = 16;
	public static final int MAX_SMS = 70;
	
	public static final int CMD_HEARTBEAT = 0x0001; //heartbeat message
	public static final int CMD_GET_IPPORT = 0x0002; //get ip:port message
	public static final int CMD_LOGIN_CFM = 0x4000; //"  Server confirms tracker��s login                                                                           "),
	public static final int CMD_REGISTER=0x4133; //register by SMS
	public static final int	CMD_LOGIN = 0x5000;  //"  Tracker��s login                                                                                           "),
	public static final int	CMD_GET_SINGLE = 0x4101; //"  Request one single location report                                                                        ") ,
	public static final int	CMD_SET_INTERVAL = 0x4102; //"  Set time interval for continuous tracking                                                                 ") ,
	public static final int	CMD_SET_AUTH_NUM = 0x4103; //"  Set authorized phone number                                                                               ") ,
	public static final int	CMD_RESERVED = 0x4104; //"  Reserved                                                                                                  ") ,
	public static final int	CMD_SET_OVERSPEED = 0x4105; //,"  Set speed limit for over speed alarm                                                                      ") ,
	public static final int	CMD_SET_MOVE_ALERT = 0x4106; //"  Set movement alert                                                                                        ") ,
	public static final int	CMD_SET_EXT_FUNC = 0x4108; //"  Set extended functions                                                                                    ") ,
	public static final int	CMD_INIT_ALL = 0x4110; //"  Initialize all parameters except for password, IP/PORT/APN, ID and time interval for continuous tracking. ") ,
	public static final int	CMD_SET_GPRS_ALERT = 0x4116; //" Set GPRS alert for buttons or inputs                                                                       ") ,
	public static final int	CMD_SET_TAPPING_NUM = 0x4130; //" Set telephone number for wiretapping                                                                       ") ,
	public static final int	CMD_SET_TIMEZONE = 0x4132;//," Set time zone                                                                                              ") ,
	public static final int	CMD_GET_INTERVAL = 0x9002; //" Read time interval of continuous tracking                                                                  ") ,
	public static final int	CMD_GET_AUTH_NUM = 0x9003; //" Read authorized phone number                                                                               ") ,
	public static final int	CMD_REPORT_SINGLE = 0x9955; //" Single location report                                                                                     ") ,
	public static final int	CMD_REPORT_BLACK = 0x9956; //" Black box report                                                                                           ") ,
	public static final int	CMD_SEND_SMS = 0x9957; //" Send SMS                                                                                                   ") ,
	public static final int	CMD_GET_ADDRESS = 0x9958; //" Get plain address                                                                                          ") ,
	public static final int	CMD_GET_GPRMC = 0x9959; //" Get GPRMC form GSM Cell                                                                                    ") ,
	public static final int	CMD_REPORT_ANSWER = 0x9960; //" Report answer phone number and location (For Italy customer only)                                          ") ,
	public static final int	CMD_SET_ANSWER = 0x9961; //" Set answer phone mode (For Italy customer only)                                                            ") ,
	public static final int	CMD_GET_BAT_SIGNAL = 0x9962;//," Get battery voltage and signal strengh                                                                     ") ,
	public static final int	CMD_REPORT_CALL_DURATION = 0x9963; //," Report call duration (For Italy customer only)                                                             ") ,
	public static final int	CMD_REPORT_SMS_SENT = 0x9964; //" Report the content of SMS sent before (For Italy customer only)                                            ") ,
	public static final int	CMD_ALARM_COMMAND = 0x9999;//," Alarm command    
	
	public static final int CMD_SET_ANY = 0x9965; //set any setting
	public static final int CMD_GET_ANY = 0x9966; //get any setting
	public static final int CMD_REPORT_SETTING = 0x9967; //upload any setting
	
	private int dir;
	private int len;
	private String id;
	private int cmd;
	private byte[] data;
	private int  checksum;
	
	
	public static class R80Data {
		
		private static final int MAX_LEN = 1024;
		private Gps gps;
		private double hdop = 0;
		private double altitude = 0;
		private Cell cell;
		private int state = 0;
		private int alarm = 0;
		private int battery = 0;
		private int signal = 0;
		
		
		public static byte[] encodePhoneNumber(String pn){
			
			byte[] bytes = new byte[MAX_PN];
			Arrays.fill(bytes, (byte)0x0);
			
			byte[] rpn;
			try {
				rpn = pn.getBytes(CHARSET);
				System.arraycopy(rpn, 0, bytes, 0, rpn.length);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			return bytes;
		}
		
		public static String decodePhoneNumber(byte[] data){
			StringBuilder builder = new StringBuilder();
			for(int i=0; i < data.length; i++) {
				char c = (char) ((data[i] & 0x0f) + 0x30);
				builder.append(c);
			}
			
			return builder.toString();
		}
		public static Cell decodeCell(String data) throws Exception{
			Cell cell = new Cell();
			
			String[] items = data.split(",");
			if(items.length < 4){
				throw new Exception("invalid cell data"+data);
			}
			
			cell.setCid(Integer.parseInt(items[0]));
			cell.setLac(Integer.parseInt(items[1]));
			cell.setMnc(Integer.parseInt(items[2]));
			cell.setMcc(Integer.parseInt(items[3]));
			
			
			return cell;
		}
		
		public static byte[] encodeCell(Cell cell){
			
			String cstr = String.format("%d,%d,%d,%d", cell.getCid(), cell.getLac(), cell.getMnc(), cell.getMcc());
			byte[] buf = null;
			try {
				buf = cstr.getBytes(CHARSET);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return buf;
		}
		
		public static Gps decodeGPS(String sentence) throws Exception {
			Gps gprmc = new Gps();
			if(sentence.length() <= 0 ){
				return gprmc;
			}
			String[] parts = sentence.split(",");
			if (parts.length < 9 ) {
				throw new Exception("Expected 9 words but found "
						+ sentence);
			}

			double lat = Double.valueOf(parts[2]);
			String latDir = parts[3];
			if (latDir.compareTo("S") == 0) {
				lat *= -1;
			} else if (latDir.compareTo("N") != 0) {
				throw new Exception("Invalid latitude direction: " + latDir);
			}
			
			lat = GPSUtils.convertToDegree(lat);
			gprmc.setLat(lat);

			double lon = Double.valueOf(parts[4]);
			String lonDir = parts[5];
			if (lonDir.compareTo("W") == 0) {
				lon *= -1;
			} else if (lonDir.compareTo("E") != 0) {
				throw new Exception("Invalid longitude direction: " + latDir);
			}
			
			lon = GPSUtils.convertToDegree(lon);
			gprmc.setLng(lon);

			String nrw = parts[1];
			if (nrw.compareTo("A") != 0 && nrw.compareTo("V") != 0) {
				throw new Exception("Invalid navigational receiver warning: "
						+ nrw);
			}
			if(nrw.compareTo("A") == 0) {
				gprmc.setValid(true);
			} else {
				gprmc.setValid(false);
			}

			String sd = String.format("%s %s", parts[8], parts[0]);
			LOGGER.info("date:"+sd);
			
			SimpleDateFormat sdf = new SimpleDateFormat(
					"ddMMyy HHmmss.SSS");

			
			sdf.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
			
			Date d = sdf.parse(sd);
			gprmc.setTime(d);

			if(parts[6].length() > 0) {
				gprmc.setSpeed(Double.valueOf(parts[6]));
			} else {
				gprmc.setSpeed(0);
			}
			
			if(parts[7].length() > 0) {
				gprmc.setHeading(Double.valueOf(parts[7]));
			} else {
				gprmc.setHeading(0);
			}
			

			return gprmc;
		}

		public static byte[] encodeGPS(Gps gprmc) {
			StringBuffer sbuf = new StringBuffer();
			Date date = new Date();
			date.setTime(gprmc.getTime() * 1000);
			SimpleDateFormat dfh = new SimpleDateFormat("HHmmss.SSS");
			dfh.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
			sbuf = dfh.format(date,sbuf, new FieldPosition(0));
			sbuf.append(",");
			
			sbuf.append(gprmc.isValid()?"A":'V');
			sbuf.append(",");
			
			double lat = gprmc.getLat();
			double lng = gprmc.getLng();
			
			String other = String.format("%4.4f,%S,%5.4f,%S,%.2f,%.2f,", 
					Math.abs(lat),
					lat > 0? "N":"S",
					Math.abs(lng),
					lng > 0? "E": "W",
					gprmc.getSpeed(),
					gprmc.getHeading()
					);
			
			sbuf.append(other);
			
			dfh = new SimpleDateFormat("ddMMyy");
			dfh.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
			sbuf = dfh.format(date, sbuf, new FieldPosition(0));
			
			byte[] bytes;
			try {
				bytes = sbuf.toString().getBytes(CHARSET);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			
			return bytes;
			
		}
		public static R80Data decode(byte[] data) throws Exception{
			R80Data rdata = new R80Data();
			String sdata = new String(data, CHARSET);
			
			LOGGER.info("DATA:"+sdata);
			
			String[] items = sdata.split("\\|");
			if(items.length < 0) {
				return null;
			}
			
			 
			Gps	gprmc = decodeGPS(items[0]);
			
			rdata.setGps(gprmc);
			
			if(items.length > 1 && items[1].length() > 0 ){
				rdata.setHdop(Double.parseDouble(items[1]));
			}
			
			if(items.length > 2 && items[2].length() > 0){
				rdata.setAltitude(Double.parseDouble(items[2]));
			}
			
			if(items.length > 3 && items[3].length() > 0){
				rdata.setCell(decodeCell(items[3]));
			}
			
			if(items.length > 4 && items[4].length() > 0){
				rdata.setState(Integer.parseInt(items[4], 16));
			}
			
			if(items.length > 5 && items[5].length() > 0){
				rdata.setAlarm(Integer.parseInt(items[5], 16));
			}
			
			if(items.length > 6 && items[6].length() > 0){
				rdata.setBattery(Integer.parseInt(items[6]));
			}
			
			if(items.length > 7 && items[7].length() > 0){
				rdata.setSignal(Integer.parseInt(items[7]));
			}
			return rdata;
		}
		
		public static byte[] encode(R80Data data) throws Exception{
			ByteBuffer buf = ByteBuffer.allocate(MAX_LEN);
			
			
			buf.put(encodeGPS(data.getGps()));
			buf.put((byte) '|');
			buf.put(String.format("%.1f", data.getHdop()).getBytes(CHARSET));
			buf.put((byte) '|');
			buf.put(String.format("%.2f", data.getAltitude()).getBytes(CHARSET));
			buf.put((byte) '|');
			buf.put(encodeCell(data.getCell()));
			buf.put((byte) '|');
			buf.put(String.format("%d", data.getState()).getBytes(CHARSET));
			buf.put((byte) '|');
			buf.put(String.format("%d", data.getAlarm()).getBytes(CHARSET));
			buf.put((byte) '|');
			buf.put(String.format("%d", data.getBattery()).getBytes(CHARSET));
			buf.put((byte) '|');
			buf.put(String.format("%d", data.getSignal()).getBytes(CHARSET));
			
			
			buf.flip();
			
			byte[] bytes = Arrays.copyOfRange(buf.array(), buf.position(), buf.limit());
			
			return bytes;
		}
		public Gps getGps() {
			return gps;
		}
		public void setGps(Gps gprmc) {
			this.gps = gprmc;
		}
		public double getHdop() {
			return hdop;
		}
		public void setHdop(double hdop) {
			this.hdop = hdop;
		}
		public double getAltitude() {
			return altitude;
		}
		public void setAltitude(double altitude) {
			this.altitude = altitude;
		}
		public Cell getCell() {
			return cell;
		}
		public void setCell(Cell cell) {
			this.cell = cell;
		}
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		public int getAlarm() {
			return alarm;
		}
		public void setAlarm(int alarm) {
			this.alarm = alarm;
		}
		public int getBattery() {
			return battery;
		}
		public void setBattery(int battery) {
			this.battery = battery;
		}
		public int getSignal() {
			return signal;
		}
		public void setSignal(int signal) {
			this.signal = signal;
		}

		@Override
		public String toString() {
			return "R80Data [gprmc=" + gps + ", hdop=" + hdop + ", altitude="
					+ altitude + ", cell=" + cell + ", state=" + state
					+ ", alarm=" + alarm + ", battery=" + battery + ", signal="
					+ signal + "]";
		}
		
		
		
	}

	
	public R80Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public R80Message(int cmd, String id, byte[] data) {
		this.cmd = cmd;
		this.id = id;
		this.data = data;
	}
	public R80Message(int cmd, String id) {
		this(cmd, id, new byte[0]);
	}

	public static R80Message newMessage(int cmd, String id, byte flag) {
		
		return new R80Message(cmd, id, new byte[]{flag});
	}
	
	public static R80Message newMessage(int cmd, String id, byte[] data) {
		
		
		return new R80Message(cmd, id, data);
	}
	
	
	
	public static R80Message newMessage(int cmd, String id, String data){
		byte[] bytes;
		try {
			bytes = data.getBytes(CHARSET);
			return new R80Message(cmd, id, bytes);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static R80Message newMessage(int cmd, String id, String data, String cs){
		byte[] bytes;
		try {
			bytes = data.getBytes(cs);
			return new R80Message(cmd, id, bytes);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public int getDir() {
		return dir;
	}
	public void setDir(int dir) {
		this.dir = dir;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
		
	}
	public int getChecksum() {
		return checksum;
	}
	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}
	
	public int getType() {
		// TODO Auto-generated method stub
		return getCmd();
	}
	
	
	@Override
	public String toString() {
		return "R80Message [dir=" + dir + ", len=" + len + ", id=" + id
				+ ", cmd=0x" + Utils.toHexString(cmd) + ", data=" + data + ", checksum=" + checksum
				+ "]";
	}
	
	public static String decodeId(byte[] ids) {
		return hexToString(ids);
	}
	public static byte[] encodeId(String ids) {
		return stringToHex(ids);
	}
	public static String hexToString(byte[] ids){
		StringBuffer id = new StringBuffer();
		for(int i = 0; i < ids.length; i ++) {
			char c = (char)(((ids[i] >> 4) & 0x0f) + 0x30);
			id.append(c);
			c = (char)(((ids[i]) & 0x0f) + 0x30);
			id.append(c);
		}
		
		return id.toString();
	}
	
	public static byte[] stringToHex(String ids){
		byte[] buf = new byte[ids.length()/2];
		
		for(int i = 0, j=0; i < buf.length; i ++,j+=2) {
			byte h = (byte) ((byte) ids.charAt(j) - 0x30);
			byte l = (byte) ((byte) ids.charAt(j+1) - 0x30);
			buf[i] = (byte) (h << 4 | l);
		}
		
		return buf;
	}

	
}
