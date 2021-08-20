package com.java.xuhaotian;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 后台系统的基本运行
 * @author xht13127
 *
 */
public class BackendSystem {
	
	private static String id;
	private static final String url = "http://open.edukg.cn/opedukg/api";
	
	public static String getId() {
		return id;
	}
	
	/**
	 * 后台登录平台
	 * @param password密码
	 * @param phone手机号
	 */
	public static void login(String password, String phone) {
		System.out.println("Initializing");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("password", password);
		map.add("phone", phone);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url + "/typeAuth/user/login", request, String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		JSONObject jsonObject = JSONObject.parseObject(response.getBody());
		id = (String)jsonObject.get("id");
		System.out.println("Initialization successful! ID=" + id);
	}
	
	/**
	 * 将键值对转化为GET请求的url
	 * @param map键值对
	 * @return 字符串url
	 */
	private static String format(Map<String, String> map) {
		final StringBuffer str = new StringBuffer("?");
		map.forEach( (k, v) -> {
			str.append(k + "=" + v + "&");
		});
		return str.toString();
	}
	
	/**
	 * 求名字与关键字的相关度
	 * @param name名字
	 * @param key关键字
	 * @return 相关度
	 */
	private static int getRelevancy(String name, String key) {
		int relevancy = 20;
		int pos = 0, next = 0;
		while ((next = name.indexOf(key, pos)) != -1) {
			pos = next + key.length();
			relevancy += 8;
		}
		relevancy -= 3 * (name.length() - relevancy / 10 * key.length());
		final String pattern = "[,.;/\s，。；、与和]";
		final Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(name);
		while (m.find()) relevancy -= 20;
		return relevancy;
	}
	
	/**
	 * 实体搜索接口
	 * @param course学科
	 * @param searchKey关键字
	 * @param offset偏移量
	 * @param limit返回个数
	 * @return JSONObject的List
	 */
	public static Object getInstanceList(String course, String searchKey, Integer offset, Integer limit, String label, Boolean sorted) {
		if (course == null || course.isEmpty() || searchKey == null || searchKey.isEmpty()) {
			return new Error(6, "Course and searchKey cannot be empty!");
		}
		if (offset == null) offset = 0;
		if (limit == null) limit = 50;
		if (offset < 0 || limit < 0) {
			return new Error(10, "Offset and limit cannot be negative!");
		}
		if (label == null) label = "";
		if (sorted == null) sorted = false;
		System.out.println("Getting Instance List");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> request = new HttpEntity<>(headers);
		Map<String, String> map= new HashMap<>();
		map.put("course", course);
		map.put("searchKey", searchKey);
		map.put("id", id);
		ResponseEntity<String> response = restTemplate.exchange(url + "/typeOpen/open/instanceList" + format(map), HttpMethod.GET, request, String.class);
		try {
			assertThat(response.getStatusCode(), is(HttpStatus.OK));
		}
		catch (Exception e) {
			System.out.println("ERR: getInstanceList in BackendSystem\n" + response);
			return new Error(-1, "Server Error.");
		}
		JSONArray jsonArray = JSONObject.parseObject(response.getBody()).getJSONArray("data");
		if (jsonArray == null) jsonArray = new JSONArray();
		
		System.out.println("Dealing data...");
		
		List<JSONObject> originalList = jsonArray.toJavaList(JSONObject.class);
		
		Set<String> hash = new HashSet<String>();
		ArrayList<JSONObject> list = new ArrayList<>();
		
		final String pattern = "http://edukb.org/knowledge/0.1/instance/.+";
		
		for (JSONObject obj : originalList) {
			if (Pattern.matches(pattern, obj.getString("uri")) && (label.equals("") || label.equals(obj.getString("category")))) {
				String name = obj.getString("label");
				if (!hash.contains(name)) {
					hash.add(name);
					JSONObject newObj = new JSONObject();
					newObj.put("label", name);
					newObj.put("category", obj.getString("category"));
					newObj.put("relevancy", getRelevancy(name, searchKey));
					list.add(newObj);
				}
			}
		}
		
		if (sorted) {
			list.sort(new Comparator<JSONObject>(){

				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					int flag = o2.getInteger("relevancy").compareTo(o1.getInteger("relevancy"));
					if (flag == 0) flag = o1.getString("label").compareTo(o2.getString("label"));
					return flag;
				}
				
			});
		}
		
		System.out.println("Getting Instance List successful!");
		return list.subList(Math.min(list.size(), Math.max(0, offset)), Math.min(list.size(), offset + limit));
	}
	
	/**
	 * 实体详情接口
	 * @param course学科（可选）
	 * @param name实体名称
	 * @return JSON格式的实体详情
	 */
	public static Object getInfoByInstanceName(String course, String name) {
		if (name == null || name.isEmpty()) {
			return new Error(7, "Name cannot be empty!");
		}
		if (course == null) course = "";
		System.out.println("Getting Info By Instance Name");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> request = new HttpEntity<>(headers);
		Map<String, String> map= new HashMap<>();
		map.put("course", course);
		map.put("name", name);
		map.put("id", id);
		ResponseEntity<String> response = restTemplate.exchange(url + "/typeOpen/open/infoByInstanceName" + format(map), HttpMethod.GET, request, String.class);
		try {
			assertThat(response.getStatusCode(), is(HttpStatus.OK));
		}
		catch (Exception e) {
			System.out.println("ERR: getInstanceList in BackendSystem\n" + response);
			return new Error(-1, "Server Error.");
		}
		JSONObject jsonObject = JSONObject.parseObject(response.getBody()).getJSONObject("data");
		if (jsonObject == null) {
			return new Error(15, "Invalid query.");
		}
		
		System.out.println("Dealing data...");
		
		List<JSONObject> originalProperty = jsonObject.getJSONArray("property").toJavaList(JSONObject.class);
		JSONArray property = new JSONArray();
		for (JSONObject obj : originalProperty) {
			if (!obj.getString("object").startsWith("http")) {
				JSONObject newObj = new JSONObject();
				newObj.put("predicateLabel", obj.get("predicateLabel"));
				newObj.put("object", obj.getString("object"));
				property.add(newObj);
			}
		}
		jsonObject.put("property", property);
		
		final String pattern = "http://edukb.org/knowledge/0.1/instance/.+";
		
		List<JSONObject> originalContent = jsonObject.getJSONArray("content").toJavaList(JSONObject.class);
		JSONArray content = new JSONArray();
		for (JSONObject obj : originalContent) {
			if (obj.containsKey("object") && Pattern.matches(pattern, obj.getString("object"))) {
				JSONObject newObj = new JSONObject();
				newObj.put("predicate_label", obj.get("predicate_label"));
				newObj.put("object_label", obj.getString("object_label"));
				content.add(newObj);
			}
			else if (obj.containsKey("subject") && Pattern.matches(pattern, obj.getString("subject"))) {
				JSONObject newObj = new JSONObject();
				newObj.put("predicate_label", obj.get("predicate_label"));
				newObj.put("subject_label", obj.getString("subject_label"));
				content.add(newObj);
			}
		}
		jsonObject.put("content", content);
		
		System.out.println("Getting Info By Instance Name successful!");
		return jsonObject;
	}
	
	/**
	 * 问答接口
	 * @param course学科（可选）
	 * @param inputQuestion问题
	 * @return JSON格式的回答
	 */
	public static Object inputQuestion(String course, String inputQuestion) {
		if (inputQuestion == null || inputQuestion.isEmpty()) {
			return new Error(8, "InputQuestion cannot be empty!");
		}
		if (course == null) course = "";
		System.out.println("Inputing Question");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("course", course);
		map.add("inputQuestion", inputQuestion);
		map.add("id", id);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url + "/typeOpen/open/inputQuestion", request, String.class);
		try {
			assertThat(response.getStatusCode(), is(HttpStatus.OK));
		}
		catch (Exception e) {
			System.out.println("ERR: inputQuestion in BackendSystem\n" + response);
			return new Error(-1, "Server Error.");
		}
		JSONObject jsonObject = JSONObject.parseObject(response.getBody());
		System.out.println("Inputing Question successful!");
		return jsonObject.get("data");
	}
	
	/**
	 * 知识链接接口
	 * @param context待识别文本
	 * @param course学科（可选）
	 * @return JSON格式的标注
	 */
	public static Object linkInstance(String context, String course) {
		if (context == null || context.isEmpty()) {
			return new Error(11, "Context cannot be empty!");
		}
		if (course == null) course = "";
		System.out.println("Linking Instance");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("context", context);
		map.add("course", course);
		map.add("id", id);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url + "/typeOpen/open/linkInstance", request, String.class);
		try {
			assertThat(response.getStatusCode(), is(HttpStatus.OK));
		}
		catch (Exception e) {
			System.out.println("ERR: linkInstance in BackendSystem\n" + response);
			return new Error(-1, "Server Error.");
		}
		JSONObject jsonObject = JSONObject.parseObject(response.getBody());
		System.out.println("Linking Instance successful!");
		return jsonObject.get("data");
	}
	
	/**
	 * 实体相关习题接口
	 * @param uriName实体名
	 * @param offset偏移量
	 * @param limit返回个数
	 * @return JSONObject的List
	 */
	public static Object getQuestionListByUriName(String uriName, Integer offset, Integer limit) {
		if (uriName == null || uriName.isEmpty()) {
			return new Error(12, "UriName cannot be empty!");
		}
		if (offset == null) offset = 0;
		if (limit == null) limit = 10;
		if (offset < 0 || limit < 0) {
			return new Error(13, "Offset and limit cannot be negative!");
		}
		System.out.println("Getting Question List By Uri Name");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> request = new HttpEntity<>(headers);
		Map<String, String> map= new HashMap<>();
		map.put("uriName", uriName);
		map.put("id", id);
		ResponseEntity<String> response = restTemplate.exchange(url + "/typeOpen/open/questionListByUriName" + format(map), HttpMethod.GET, request, String.class);
		try {
			assertThat(response.getStatusCode(), is(HttpStatus.OK));
		}
		catch (Exception e) {
			System.out.println("ERR: getQuestionListByUriName in BackendSystem\n" + response);
			return new Error(-1, "Server Error.");
		}
		JSONArray jsonArray = JSONObject.parseObject(response.getBody()).getJSONArray("data");
		System.out.println("Getting Question List By Uri Name successful!");
		
		return jsonArray.subList(Math.min(jsonArray.size(), Math.max(0, offset)), Math.min(jsonArray.size(), offset + limit));
	}
	
}
