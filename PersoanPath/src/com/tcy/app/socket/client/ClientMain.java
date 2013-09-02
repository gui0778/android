package com.tcy.app.socket.client;

import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final SocketClient sc = new SocketClient("192.168.1.229", 8766);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true)
					try

					{
						sc.sendMessage("data", new SocketClientResponese() {
							
							@Override
							public void getResponseSocket(Socket socket) {
								// TODO Auto-generated method stub
								System.out.println("socket"+socket.getLocalAddress());
								
							}
						});
						System.out.println(sc.sendMessage("admin"));
						Thread.sleep(1000);

					} catch (Exception e) {
						System.out.println("error");

					}
				

			}

		}).start();

	}
}
