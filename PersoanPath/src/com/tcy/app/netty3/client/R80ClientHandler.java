package com.tcy.app.netty3.client;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcy.app.netty3.client.R80Message.R80Data;

import static org.jboss.netty.channel.Channels.*;








public class R80ClientHandler extends IdleStateAwareChannelUpstreamHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(R80ClientHandler.class);
	
	


	
	

	private final Client client;
	private  static final HashedWheelTimer reconnTimer = new HashedWheelTimer();
	private  static final HashedWheelTimer reportTimer = new HashedWheelTimer();
    private final AtomicLong transferredBytes = new AtomicLong();

    private String props = "";
  
    /**
     * Creates a client-side handler.
     */
    public R80ClientHandler(Client client) {
    	this.client = client;
    	
    }
    
   
    public long getTransferredBytes() {
        return transferredBytes.get();
    }

    @Override
    public void channelConnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) {
        // Send the first message.  Server will not send anything here
        // because the firstMessage's capacity is 0.
        //e.getChannel().write(testMsg);
    	//e.getChannel().write(new R80Message(R80Message.CMD_LOGIN, getId()));
    }

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        // Send back the received message to the remote peer.
        //transferredBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
        R80Message msg = (R80Message) e.getMessage();
        LOGGER.info(msg.toString());
        //e.getChannel().write(e.getMessage());
        if(msg.getCmd() == R80Message.CMD_LOGIN_CFM &&
        		msg.getData().length> 0 && msg.getData()[0] == 1) {
        	this.reportSingle(ctx, e);
        	//this.getPlainAddress(ctx, e);
        	//this.reportSetting(ctx,e);
        }
        
        int cmd = msg.getCmd();
		switch (cmd) {
		case R80Message.CMD_SET_ANY:
			{
				try {
					String prop = new String(msg.getData(), R80Message.CHARSET);
					LOGGER.info("set prop:"+prop);
					this.props = prop;
					
					R80Message rmsg = R80Message.newMessage(R80Message.CMD_SET_ANY, client.getId(), (byte)1);
					e.getChannel().write(rmsg);
					
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		case R80Message.CMD_GET_ANY:
			{
				R80Message rmsg = R80Message.newMessage(R80Message.CMD_GET_ANY, client.getId(), this.props);
				e.getChannel().write(rmsg);
			}
			break;
		case R80Message.CMD_SEND_SMS:
			{
				byte[] data = msg.getData();
				
				
				try {
					String pn = R80Data.decodePhoneNumber(Arrays.copyOfRange(data, 0, R80Message.MAX_PN));
					String sms = new String(Arrays.copyOfRange(data, R80Message.MAX_PN, data.length), R80Message.TEXT_CHARSET);
					LOGGER.info(pn+":"+sms);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			break;
		default:
			LOGGER.warn("invalid msg:"+msg);
			break;
		}
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
    	LOGGER.warn("Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
        
    }

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		 
		  LOGGER.warn("Sleeping for: " + Client.RECONNECT_DELAY + "s");
		  reconnTimer.newTimeout(new TimerTask() {
	            public void run(Timeout timeout) throws Exception {
	                LOGGER.info("Reconnecting to: " + client.getRemoteAddress());
	                client.connect();
	            }
	        }, Client.RECONNECT_DELAY, TimeUnit.SECONDS);
		
	}

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		if(e.getState() == IdleState.ALL_IDLE) {
			LOGGER.info("send heartbeat:" + client.getId());
			e.getChannel().write(client.getHbMsg());
		}
	}
	
	public void reportSetting(final ChannelHandlerContext ctx, final MessageEvent e) {
		String setting = "vibc:true";
		R80Message msg;
		try {
			msg = R80Message.newMessage(R80Message.CMD_REPORT_SETTING, client.getId(), setting.getBytes(R80Message.UTF8));
			e.getChannel().write(msg);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
	}
	public void getPlainAddress(final ChannelHandlerContext ctx, final MessageEvent e) {
		R80Data data = new R80Data();

		
		
		
		R80Message msg = null;
		try {
			byte[] buf = R80Data.encode(data);
			ByteBuffer buf1 = ByteBuffer.allocate(buf.length+R80Message.MAX_PN);
			String pn = "8613421376066";
			buf1.put(R80Data.encodePhoneNumber(pn));
			buf1.put(buf);
			buf1.flip();
			LOGGER.info("start get address"+pn);
			
			msg = new R80Message(R80Message.CMD_GET_ADDRESS, client.getId(), buf1.array());
			e.getChannel().write(msg);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	public void reportSingle(final ChannelHandlerContext ctx, final MessageEvent e) {
		LOGGER.info("client:"+client.getId() + "report to: " + client.getRemoteAddress());
		e.getChannel().write(client.getCurrentStatus());
		reportTimer.newTimeout(new TimerTask() {
			public void run(Timeout timeout) throws Exception {
				 if(e.getChannel().isWritable()) {
					 reportSingle(ctx, e);
				 }
				 
			}
		}, client.getInterval(), TimeUnit.SECONDS);
	}
	
	
}

