package com.tcy.app.netty3.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;


import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.logging.InternalLogLevel;
import org.jboss.netty.util.HashedWheelTimer;

import com.tcy.app.netty3.client.R80Message.R80Data;



import static org.jboss.netty.channel.Channels.*;





public class Client extends ClientBootstrap {
	
	

	private  static final HashedWheelTimer hbTimer = new HashedWheelTimer();
	
	
	
	 // Parse options.
	private final static String DEFAULT_HOST="localhost";
	private final static int DEFAULT_PORT=8090;
	private final static int HEARTBEAT_TIME = 120;
	static final int RECONNECT_DELAY = 10;
	static final int REPORT_INTVAL = 30;
    private String host;
    private int port;
    private String id;
    
    private int interval = 120;
    
    private R80Message hbMsg;
    
   
    
    private double step = 0.001;
    
    private double lat= 2232.4679;//GPSUtils.convertFromDegree(22.433558172246947);//2232.4679;
    private double lng =11356.7805;// GPSUtils.convertFromDegree(114.085170768338);//11356.7805;
    private  Random rand = new Random();
    public Client(String id){
    	this(Client.DEFAULT_HOST, Client.DEFAULT_PORT);

    	
    }
    public Client(String host,int port){
    	this.host = host;
    	this.port = port;
    }
    
    
    InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) getOption("remoteAddress");
    }
    
  
    
	public Channel start() {
		// TODO Auto-generated method stub
		
		final Client client = this;
		

     // Set up the pipeline factory.
        setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
            	
            	ChannelPipeline pipeline = pipeline();

                // Add the text line codec combination first,
            	pipeline.addLast("logger", new LoggingHandler(InternalLogLevel.INFO));
                pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(8192, 2, 2, -4, 0));
                pipeline.addLast("decoder", new R80MessageDecoder());
                pipeline.addLast("encoder", new R80MessageEncoder());
                //pipeline.addLast("idle", new IdleStateHandler(hbTimer, 0, 0, R80Client.HEARTBEAT_TIME));
                // and then business logic.
                pipeline.addLast("handler", new R80ClientHandler(client));

                return pipeline;
            	
            }
        });
        
        
        setOption(
                "remoteAddress", new InetSocketAddress(host, port));
        ChannelFuture future = connect();
        
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            releaseExternalResources();
            return null;
        }
        
        return channel;
              

	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public ChannelFuture connect(SocketAddress remoteAddress,
			SocketAddress localAddress) {
		// TODO Auto-generated method stub
		ChannelFuture future =  super.connect(remoteAddress, localAddress);
		
		future.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f){
				System.out.println("connected:"+f.getChannel().isConnected());
				try {
					User u=new User();
					u.setName("tcy");
					u.setPasswd("1234");					
					ChannelBuffer cb=ChannelBuffers.buffer(256);
					cb.writeBytes("Hello".getBytes());
					f.getChannel().write(cb);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		return future;
	}
	public R80Message getHbMsg() {
		return hbMsg;
	}
	
	
	public R80Message getCurrentStatus() {
		R80Data data = new R80Data();
		
		R80Message msg = null;
		try {
			msg = new R80Message(R80Message.CMD_REPORT_SINGLE, getId(), R80Data.encode(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}
	@Override
	public String toString() {
		return "R80Client [hbTimer=" + hbTimer + ", host=" + host + ", port="
				+ port + ", id=" + id + ", hbMsg=" + hbMsg + ", intval="
				+ interval + ", step=" + step + ", lat=" + lat + ", lng=" + lng
				+ ", rand=" + rand + "]";
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
}
