package com.tcy.app.netty3.client;


import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class R80MessageDecoder extends OneToOneDecoder {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(R80MessageDecoder.class);
	
    
	/* (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.oneone.OneToOneDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)
	 */
		
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		// TODO Auto-generated method stub
		 if (!(msg instanceof ChannelBuffer)) {
	            return msg;
	     }
		 
		
		ChannelBuffer frame = (ChannelBuffer)msg;
	    
		if(frame.readableBytes() < R80Message.MIN_FRAME) {
			LOGGER.info("invalid command");
			return null;
		}
		
		Utils.Crc16 crc = new Utils.Crc16();
		for(int i = 0; i < frame.readableBytes() - R80Message.TAIL_LEN; i++) {
			crc.update(frame.getByte(i));
		}
		R80Message cmd = new R80Message(); 
		
		try{
			
			int dir = frame.readUnsignedShort();
			cmd.setDir(dir);
			int len = frame.readUnsignedShort();
			cmd.setLen(len);
			
			byte[]  id = new byte[R80Message.ID_LEN];
			frame.readBytes(id);
			String sid = R80Message.decodeId(id);
			cmd.setId(sid);
			int cd = frame.readUnsignedShort();
			cmd.setCmd(cd);
			
			byte[] data = new byte[len - R80Message.HEADER_LEN - R80Message.TAIL_LEN];
			frame.readBytes(data);
			
			cmd.setData(data);
			int checksum = frame.readUnsignedShort();
			if(checksum != crc.getResult()) {
				LOGGER.info("crc error");
				//return null;
			}
			cmd.setChecksum(checksum);
			frame.readUnsignedShort();
			LOGGER.info(cmd.toString());
		}catch(IndexOutOfBoundsException e) {
			LOGGER.info(e.getMessage());
		}
		
		/*
		if(cmd.getDir() != R80Message.FROMDEVICE) {
			LOGGER.error("invalid message dir!");
			return null;
		}*/
		
		return cmd;
	}
	
	

}