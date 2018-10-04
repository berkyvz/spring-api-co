package com.catchopportunity.springapico.infos;

public class Info {

	private String methodType;
	private String url;
	private String description;

	public Info(String methodType, String url, String description) {
		super();
		this.methodType = methodType;
		this.url = url;
		this.description = description;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
