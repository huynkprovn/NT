package com.vht.sms.content.pinaco;

public class HttpHeader {
	
	public String key;

	public String value;

	public boolean unicode;

	public HttpHeader() {
		this.key = "";
		this.value = "";
		this.unicode = false;
	}

	public HttpHeader(String myKey, String myValue, boolean myUnicode) {
		this.key = myKey;
		this.value = myValue;
		this.unicode = myUnicode;
	}
}
