package com.java.xuhaotian;

import java.util.ArrayList;

public class History {
	private final String[] history;
	private final int size;
	private int pos;
	
	History(int size) {
		history = new String[size];
		this.size = size;
		pos = 0;
	}
	
	void addHistory(String newHistory) {
		history[pos++] = newHistory;
		if (pos == size) pos = 0;
	}
	
	String[] getHistory() throws Throwable {
		System.out.println("getHistory");
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 1; i <= size; i++) {
			String tmp = history[(pos - i + size) % size];
			if (tmp == null) break;
			res.add(tmp);
		}
		System.out.println("getHistory successful" + res);
		return res.toArray(new String[0]);
	}
}
