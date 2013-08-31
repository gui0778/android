package com.tcy.app.netty3.client;

import java.io.Serializable;

public interface IMessage extends Serializable {
	
	public int getType();
	
}
