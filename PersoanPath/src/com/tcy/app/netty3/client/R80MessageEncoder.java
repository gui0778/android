package com.tcy.app.netty3.client;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class R80MessageEncoder extends OneToOneEncoder {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(R80MessageEncoder.class);
	
	


	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		// TODO Auto-generated method stub
		if (!(msg instanceof R80Message)) {
	            return msg;
	    }
		
		R80Message rmsg = (R80Message)msg;
		
		
		int len = rmsg.getData().length + R80Message.HEADER_LEN + R80Message.TAIL_LEN;
		rmsg.setDir(R80Message.FROMSERVER);
		rmsg.setLen(len);
		ChannelBuffer buf = ChannelBuffers.buffer(rmsg.getLen());
		buf.writeShort(rmsg.getDir());
		buf.writeShort(rmsg.getLen());
		buf.writeBytes(R80Message.encodeId(rmsg.getId()));
		buf.writeShort(rmsg.getCmd());
		buf.writeBytes(rmsg.getData());
		int checksum = Utils.crc16(buf);
		buf.writeShort(checksum);
		buf.writeShort(R80Message.ENDLINE);
		LOGGER.info("encoder:"+buf);
		return buf;
	}

}