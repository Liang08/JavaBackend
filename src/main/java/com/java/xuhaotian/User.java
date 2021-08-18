package com.java.xuhaotian;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 用户信息，包括用户名、密码、访问Token、访问历史
 * @author xht13127
 *
 */
public class User {
	private final String userName;
	private String password;
	private String token;
	History instanceHistory;
	Set<String> favourite;
	
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.token = "";
		instanceHistory = new History(50);
		favourite = new HashSet<String>();
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void modifyPassword(String password) {
		this.password = password;
	}
	
	public void setToken() {
		token = RandomStringUtils.randomAlphanumeric(32);
	}
	
	public String getToken() {
		return token;
	}
	
	public void addInstanceHistory(String instance) {
		instanceHistory.addHistory(instance);
	}
	
	public String[] getInstanceHistory() throws Throwable {
		return instanceHistory.getHistory();
	}
	
	public void setFavourite(String instance) {
		favourite.add(instance);
	}
	
	public void resetFavourite(String instance) {
		favourite.remove(instance);
	}
	
	public String[] getFavouriteList() {
		return favourite.toArray(new String[0]);
	}
}