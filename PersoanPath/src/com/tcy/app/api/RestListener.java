package com.tcy.app.api;

import java.net.URI;

public interface RestListener {
	public abstract void onBeforeRequest(URI url);
	public abstract void onResponse(String response);
	public abstract void onError(Integer errorCode, String errorMessage);
}
