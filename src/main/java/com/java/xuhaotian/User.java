package com.java.xuhaotian;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;


/**
 * 用户信息，包括用户名、密码、访问Token、访问历史
 * @author xht13127
 *
 */
public class User {
	private final String userName;
	private String password;
	private String token;
	private History<ImmutablePair<String, String>> instanceHistory;
	private Set<ImmutablePair<String, String>> favourite;
	private History<ImmutablePair<String, String>> searchHistory;
	private Set<Integer> errorBook;
	
	@SuppressWarnings("unchecked")
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.token = "";
		instanceHistory = new History<ImmutablePair<String, String>>(
				(Class<ImmutablePair<String, String>>) ImmutablePair.of("", "").getClass(), 30);
		favourite = new HashSet<ImmutablePair<String, String>>();
		searchHistory = new History<ImmutablePair<String, String>>(
				(Class<ImmutablePair<String, String>>) ImmutablePair.of("", "").getClass(), 30);
		errorBook = new HashSet<>();
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
	
	public void addInstanceHistory(ImmutablePair<String, String> instance) {
		instanceHistory.addHistory(instance);
	}
	
	public void clearInstanceHistory() {
		instanceHistory.clear();
	}
	
	public ImmutablePair<String, String>[] getInstanceHistory() {
		return instanceHistory.getHistory();
	}
	
	public void setFavourite(ImmutablePair<String, String> instance) {
		favourite.add(instance);
	}
	
	public void resetFavourite(ImmutablePair<String, String> instance) {
		favourite.remove(instance);
	}
	
	@SuppressWarnings("unchecked")
	public ImmutablePair<String, String>[] getFavouriteList() {
		return favourite.toArray((ImmutablePair<String, String>[])Array.newInstance(
				(Class<ImmutablePair<String, String>>) ImmutablePair.of("", "").getClass(), 0));
	}
	
	public boolean isFavourite(ImmutablePair<String, String> instance) {
		return favourite.contains(instance);
	}
	
	public void addSearchHistory(ImmutablePair<String, String> search) {
		searchHistory.addHistory(search);
	}
	
	public ImmutablePair<String, String>[] getSearchHistory() {
		return searchHistory.getHistory();
	}
	
	public void clearSearchHistory() {
		searchHistory.clear();
	}
	
	public void addError(int id) {
		errorBook.add(id);
	}
	
	public void removeError(int id) {
		errorBook.remove(id);
	}
	
	public Set<Integer> getErrorBook() {
		return errorBook;
	}
}