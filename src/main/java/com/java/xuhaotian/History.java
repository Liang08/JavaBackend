package com.java.xuhaotian;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class History<T> {
	private T[] history;
	private final int size;
	private int pos;
	private Class<T> componentType;
	
	@SuppressWarnings("unchecked")
	History(Class<T> componentType, int size) {
		this.componentType = componentType;
		history = (T[]) Array.newInstance(componentType, size);
		this.size = size;
		pos = 0;
	}
	
	void addHistory(T newHistory) {
		history[pos++] = newHistory;
		if (pos == size) pos = 0;
	}
	
	@SuppressWarnings("unchecked")
	T[] getHistory() {
		System.out.println("getHistory");
		ArrayList<T> res = new ArrayList<T>();
		for (int i = 1; i <= size; i++) {
			T tmp = history[(pos - i + size) % size];
			if (tmp == null) break;
			res.add(tmp);
		}
		System.out.println("getHistory successful");
		return res.toArray((T[]) Array.newInstance(componentType, 0));
	}

	public void clear() {
		for (int i = 0; i < size; i++)
			history[i] = null;
		pos = 0;
	}
}
