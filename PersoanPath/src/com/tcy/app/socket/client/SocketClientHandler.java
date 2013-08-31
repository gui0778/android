package com.tcy.app.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClientHandler {
	public static Socket socket;
	public static OutputStream out;
	public static InputStream in;
	public SocketClientHandler(Socket socket)
	{
		this.socket=socket;
		try {
			this.out=socket.getOutputStream();
			this.in=socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String sendMessage(String string)
	{
		try {
			out.write(string.getBytes());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			InputStream in=socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		byte[] buf=new byte[256];
		StringBuffer sb=new StringBuffer();
		try {
			int b = 0;
			while (((b = in.read(buf)) != -1)) {
				System.out.println(new String(buf));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	public InputStream sendMessageForInput(String string)
	{
		return null;
	}


}
