package com.java.xuhaotian;

/**
 * 请求错误类型，包括错误代码以及错误信息
 * @author xht13127
 * 
 */
public class Error {
	private int code;
	private String message;

	public int getCode() {
    	return code;
	}

	public void setCode(int code) {
    	this.code = code;
	}

	public String getMessage() {
    	return message;
	}

	public void setMessage(String message) {
    	this.message = message;
	}
	
	public Error(int code, String message) {
		this.code = code;
		this.message = message;
	};
}
