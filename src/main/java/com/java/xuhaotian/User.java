package com.java.xuhaotian;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 用户信息，包括用户名、密码、访问Token
 * @author xht13127
 *
 */
public class User {
	private final String userName;
	private final String password;
	private String token;

	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.token = "";
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setToken() {
		token = RandomStringUtils.randomAlphanumeric(32);
	}
	
	public String getToken() {
		return token;
	}
	
}