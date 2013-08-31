package com.tcy.app.socket.client;

import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient {
	private static final int PORT = 8888;
	public Socket socket;
	private static ExecutorService exec = Executors.newCachedThreadPool();


	public SocketClient(String host,int port) {
		try {
			socket = new Socket(host, port);
			exec.execute(new Sender(socket));
		} catch (Exception e) {

		}
	}
	public void sendMessage(String string,SocketClientResponese socketClientResponese)
	{
		try {
			OutputStream out=this.socket.getOutputStream();
			out.write(string.getBytes(CharsetUtil.UTF_8));
			socketClientResponese.getResponseSocket(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

    /**
	 * 客户端线程获取控制台输入消息
	 */
	static class Sender implements Runnable {
		private Socket socket;

		public Sender(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				String msg;

				while (true) {
					msg = br.readLine();
					pw.println(msg);

					if (msg.trim().equals("bye")) {
						pw.close();
						br.close();
						exec.shutdownNow();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

