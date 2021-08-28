package com.java.xuhaotian;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;

import org.apache.commons.lang3.tuple.Pair;

import com.alibaba.fastjson.JSONObject;
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
	
	private static final Map<Integer, JSONObject> questions = new HashMap<>();
	private static final Map<String, Set<Integer>> instanceQuestions = new HashMap<>();
	
	private static final Map<String, ArrayList<String>> hotInstance = new HashMap<>();
	
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

	public static void addQuestion(JSONObject obj) {
		questions.put(obj.getIntValue("id"), obj);
	}

	public static JSONObject getQuestion(int id) {
		return questions.get(id);
	}
	
	public static void initQuestionIdSetOfInstance(String name) {
		if (!instanceQuestions.containsKey(name)) {
			instanceQuestions.put(name, new LinkedHashSet<>());
		}
	}
	
	public static void addQuestionIdOfInstance(String name, int id) {
		instanceQuestions.get(name).add(id);
	}
	
	public static Set<Integer> getQuestionIdSetOfInstance(String name) {
		return instanceQuestions.get(name);
	}
	
	public static void initHotInstance() {
		ArrayList<String> list;
		list = new ArrayList<>();
		list.add("唐宋八大家");
		list.add("兰亭集序");
		list.add("阿房宫赋");
		list.add("苏轼");
		list.add("李白");
		list.add("文言虚词");
		list.add("倒装句");
		list.add("修辞手法");
		list.add("成语");
		list.add("对联");
		hotInstance.put("chinese", list);
		
		list = new ArrayList<>();
		list.add("三角形");
		list.add("正弦函数");
		list.add("余弦函数");
		list.add("不等式");
		list.add("向量");
		list.add("求导");
		list.add("矩阵");
		list.add("方程");
		list.add("分数");
		list.add("数学归纳法");
		hotInstance.put("math", list);
		
		list = new ArrayList<>();
		list.add("介词");
		list.add("虚拟语气");
		list.add("定语从句");
		list.add("被动语态");
		list.add("现在分词");
		list.add("语气");
		list.add("abandon");
		list.add("同位语");
		list.add("元音");
		list.add("助动词");
		hotInstance.put("english", list);
		
		list = new ArrayList<>();
		list.add("力");
		list.add("牛顿第三定律");
		list.add("牛顿第二定律");
		list.add("能量");
		list.add("物理量");
		list.add("滑轮");
		list.add("杠杆");
		list.add("电路");
		list.add("弹簧测力计");
		list.add("自由落体运动");
		hotInstance.put("physics", list);
		
		list = new ArrayList<>();
		list.add("元素");
		list.add("氧气");
		list.add("化学式");
		list.add("氢氧化钠");
		list.add("复分解反应");
		list.add("置换反应");
		list.add("有机物");
		list.add("苯");
		list.add("燃烧");
		list.add("氧化还原反应");
		hotInstance.put("chemistry", list);
		
		list = new ArrayList<>();
		list.add("动物");
		list.add("植物");
		list.add("生物多样性");
		list.add("基因");
		list.add("性状");
		list.add("遗传病");
		list.add("免疫");
		list.add("细胞膜");
		list.add("病毒");
		list.add("遗传");
		hotInstance.put("biology", list);
		
		list = new ArrayList<>();
		list.add("唐朝");
		list.add("北宋");
		list.add("南宋");
		list.add("元朝");
		list.add("明朝");
		list.add("清朝");
		list.add("鸦片战争");
		list.add("辛亥革命");
		list.add("抗日战争");
		list.add("新中国的成立");
		hotInstance.put("history", list);
		
		list = new ArrayList<>();
		list.add("中国");
		list.add("美国");
		list.add("气候");
		list.add("温带大陆性气候");
		list.add("地中海气候");
		list.add("天气");
		list.add("等温线");
		list.add("地形");
		list.add("人口");
		list.add("区域");
		hotInstance.put("geo", list);
		
		list = new ArrayList<>();
		list.add("马克思主义哲学");
		list.add("中国共产党");
		list.add("人民代表大会制度");
		list.add("价值观");
		list.add("科教兴国");
		list.add("改革开放");
		list.add("哲学");
		list.add("世界观");
		list.add("三个代表");
		list.add("综合国力");
		hotInstance.put("politics", list);
	}
	
	public static ArrayList<String> getHotInstance(String subject) {
		return hotInstance.get(subject);
	}
}
