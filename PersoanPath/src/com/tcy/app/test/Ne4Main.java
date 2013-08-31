package com.tcy.app.test;

import com.tcy.app.netty4.Ne4Client;

public class Ne4Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new Ne4Client("127.0.0.1",8766,256).run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
