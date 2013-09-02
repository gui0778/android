package com.tcy.app.socket.client;

import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSender implements Runnable {
	private Socket socket;
	private int heart;
	private Boolean isheart = false;

	public ClientSender(Socket socket, int heart, Boolean isheart) {
		this.socket = socket;
		this.heart = heart;
		this.isheart = isheart;
	}
	public void run() {
		// TODO Auto-generated method stub

		while (this.isheart) {
			try {
				OutputStream out = this.socket.getOutputStream();
				out.write("heart".getBytes(CharsetUtil.UTF_8));
				out.flush();
				// TODO Auto-generated method stub
				messageRecive();
				Thread.sleep(this.heart);

			} catch (Exception e) {
				try {
					this.socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;

			}
		}

	}
	public synchronized String messageRecive()
	{
		String data = null;
		InputStream in = null;
		try {
			in = this.socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		try {
			int b = 0;
			byte[] buf = new byte[1024];
			while (((b = in.read(buf)) != -1)) {
				data = new String(buf).trim();
				int len = data.getBytes().length;
				System.out.println(data);
				if (len < 1024) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}

