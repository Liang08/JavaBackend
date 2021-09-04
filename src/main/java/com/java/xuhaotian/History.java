package com.java.xuhaotian;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class History<T> {
	private T[] history;
	private final int size;
	private int count;
	private Class<T> componentType;
	
	@SuppressWarnings("unchecked")
	History(Class<T> componentType, int size) {
		this.componentType = componentType;
		history = (T[]) Array.newInstance(componentType, size);
		this.size = size;
		count = 0;
	}
	
	void addHistory(T newHistory) {
		T current = newHistory;
		for (int i = 0; i < count; i++) {
			T tmp = history[i];
			history[i] = current;
			current = tmp;
			if (current.equals(newHistory)) break;
		}
		if (count < size && (count == 0 || !current.equals(newHistory))) history[count++] = current;
	}
	
	@SuppressWarnings("unchecked")
	T[] getHistory() {
		System.out.println("getHistory");
		ArrayList<T> res = new ArrayList<T>();
		for (int i = 0; i < count; i++) {
			res.add(history[i]);
		}
		System.out.println("getHistory successful");
		return res.toArray((T[]) Array.newInstance(componentType, 0));
	}

	public void clear() {
		count = 0;
	}
}
