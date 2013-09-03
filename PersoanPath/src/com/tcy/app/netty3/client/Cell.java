package com.tcy.app.netty3.client;

import java.io.Serializable;

public class Cell implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1289120451191133868L;

	public Cell(int cid, int lac, int mnc, int mcc) {
		super();
		this.cid = cid;
		this.lac = lac;
		this.mnc = mnc;
		this.mcc = mcc;
	}
	private static final String CHARSET="US-ASCII";
	private final static int MAX_BUF=1024;
	private int cid;
	private int lac;
	private int mnc;
	private int mcc;
	
	public Cell() {
		super();
		// TODO Auto-generated constructor stub
		cid = 0;
		lac = 0;
		mnc = 0;
		mcc = 0;
	}
	
	
	public boolean isVaild() {
		return cid + lac + mnc + mcc > 0;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getLac() {
		return lac;
	}
	public void setLac(int lac) {
		this.lac = lac;
	}
	public int getMnc() {
		return mnc;
	}
	public void setMnc(int mnc) {
		this.mnc = mnc;
	}
	public int getMcc() {
		return mcc;
	}
	public void setMcc(int mcc) {
		this.mcc = mcc;
	}
	@Override
	public String toString() {
		return "Cell [cid=" + cid + ", lac=" + lac + ", mnc=" + mnc
				+ ", mcc=" + mcc + "]";
	}
	
}