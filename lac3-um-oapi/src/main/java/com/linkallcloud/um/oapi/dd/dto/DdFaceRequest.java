package com.linkallcloud.um.oapi.dd.dto;

import com.linkallcloud.core.face.message.request.ObjectFaceRequest;

public class DdFaceRequest<T> extends ObjectFaceRequest<T> {
	private static final long serialVersionUID = 7787096680482638434L;

	private String accessToken;

	public DdFaceRequest() {
		super();
	}

	public DdFaceRequest(T data) {
		super(data);
	}

	public DdFaceRequest(String accessToken, T data) {
		super(data);
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
