package com.java.xuhaotian;

import java.util.HashMap;

public class UserSystem {
	private static final HashMap<String, User> userMap = new HashMap<>();
	private static final HashMap<String, User> tokenMap = new HashMap<>();
	
	public static Error register(String userName, String password) {
		Error error = null;
		if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
			error = new Error(1, "Username and password cannot be empty!");
		}
		else if (userMap.containsKey(userName)) {
			error = new Error(2, "Username already exist. Please log in.");
		}
		else {
			userMap.put(userName, new User(userName, password));
		}
		return error;
	}
	
	public static Object login(String userName, String password) {
		if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
			return new Error(3, "Please input username and password.");
		}
		else if (!userMap.containsKey(userName)) {
			return new Error(4, "Username not exist. Please register.");
		}
		else {
			User user = userMap.get(userName);
			if (!user.getPassword().equals(password)) {
				return new Error(5, "Password incorrect. Please try again.");
			}
			else {
				tokenMap.remove(user.getToken(), user);
				user.setToken();
				tokenMap.put(user.getToken(), user);
				return user.getToken();
			}
		}
	}
	
	public static Error logout(User user) {
		if (user == null) {
			return new Error(9, "Require logged in.");
		}
		tokenMap.remove(user.getToken(), user);
		return null;
	}
	
	public static Object modifyPassword(User user, String oldPassword, String newPassword) {
		if (user == null) {
			return new Error(9, "Require logged in.");
		}
		else if (!user.getPassword().equals(oldPassword)) {
			return new Error(5, "Password incorrect. Please try again.");
		}
		else if (newPassword == null || newPassword.isEmpty()) {
			return new Error(14, "Password cannot be empty!");
		}
		user.modifyPassword(newPassword);
		user.setToken();
		return user.getToken();
	}
	
	public static User getUserByToken(String token) {
		return tokenMap.get(token);
	}
}
