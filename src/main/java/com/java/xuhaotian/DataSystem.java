package com.java.xuhaotian;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;

import com.opencsv.CSVReader;

public class DataSystem {
	
	private static final String[] subjects = {
			"chinese",
			"english",
			"math",
			"physics",
			"chemistry",
			"biology",
			"history",
			"geo",
			"politics"
	};
	
	private static final Map<String, Map<String, Integer>> labelCount = new HashMap<>();
	private static Map<String, String> mathClassLabel;
	private static final Map<String, Map<String, String>> classLabel = new HashMap<>();
	
	private static void load(String subject) {
		System.out.println("Loading " + subject + "...");
		Map<String, Integer> count = new HashMap<>();
		Map<String, String> label = new HashMap<>();
		try {
			CSVReader reader = new CSVReader(new FileReader("data/" + subject + ".csv", Charset.forName("utf-8")));
			String [] nextLine;
			String pattern0 = "http://edukb.org/knowledge/0.1/class/" + subject + "#(.+)";
			Pattern r0 = Pattern.compile(pattern0);
			String pattern1 = "http://www.w3.org/2000/01/rdf-schema#label";
			
			while ((nextLine = reader.readNext()) != null) {
				Matcher m = r0.matcher(nextLine[0]);
				if (m.find() && pattern1.equals(nextLine[1])) {
					label.put(m.group(1), nextLine[2]);
					count.put(nextLine[2], 1);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (Map.Entry<String, String> entry : label.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
		try {
			
			CSVReader reader = new CSVReader(new FileReader("data/" + subject + ".csv", Charset.forName("utf-8")));
			String [] nextLine;
			String pattern0 = "http://edukb.org/knowledge/0.1/instance/math#(.+)";
			Pattern r0 = Pattern.compile(pattern0);
			String pattern1 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
			String pattern2 = "http://edukb.org/knowledge/0.1/class/math#(.+)";
			Pattern r2 = Pattern.compile(pattern2);
			
			while ((nextLine = reader.readNext()) != null) {
				Matcher m0 = r0.matcher(nextLine[0]), m2 = r2.matcher(nextLine[2]);
				if (m0.find() && pattern1.equals(nextLine[1]) && m2.find()) {
					String name = label.get(m2.group(1));
					if (count.containsKey(name)) {
						count.put(name, count.get(name) + 1);
					}
					else {
						System.out.println("ERR: Class(" + m2.group(1) + ":" + name + ") not found.");
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		labelCount.put(subject, count);
		classLabel.put(subject, label);
		System.out.println("Load " + subject + " successful.");
	}
	
	public static void loadData() {
		for (String subject : subjects) {
			load(subject);
		}
		mathClassLabel = classLabel.get("math");
	}
	
	public static String getMathClassLabel(String uri) {
		String pattern = "http://edukb.org/knowledge/0.1/class/math#(.+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(uri);
		if (m.find()) {
			String name =  mathClassLabel.get(m.group(1));
			if (name != null) return name;
		}
		System.out.println("ERR: Class(" + m.group(0) + ") not found.");
		return "其它";
	}
	
	public static ArrayList<String> getHotLabel(String subject) {
		ArrayList<Entry<String, Integer>> list = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : labelCount.get(subject).entrySet()) {
			list.add(entry);
		}
		
		list.sort(new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				int flag = o2.getValue().compareTo(o1.getValue());
				if (flag == 0) flag = o1.getKey().compareTo(o2.getKey());
				return flag;
			}
			
		});
		
		ArrayList<String> res = new ArrayList<>();
		for (int i = 0; i < Math.min(10, list.size()); i++) {
			res.add(list.get(i).getKey());
		}
		return res;
	}
}
