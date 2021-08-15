package com.java.xuhaotian;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
	 * 实体搜索接口
	 * @param course学科
	 * @param searchKey关键字
	 * @param offset偏移量
	 * @param limit返回个数
	 * @return JSONArray格式的数组
	 */
	public static Object getInstanceList(String course, String searchKey, Integer offset, Integer limit) {
		if (course == null || course.isEmpty() || searchKey == null || searchKey.isEmpty()) {
			return new Error(6, "Course and searchkey cannot be empty!");
		}
		if (offset == null) offset = 0;
		if (limit == null) limit = 50;
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
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		JSONObject jsonObject = JSONObject.parseObject(response.getBody());
		System.out.println("Getting Instance List successful!");
		return ((JSONArray)jsonObject.get("data")).subList(offset, offset + limit);
	}
	
	/**
	 * 实体详情接口
	 * @param course学科（可选，无效）
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
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		JSONObject jsonObject = JSONObject.parseObject(response.getBody());
		System.out.println("Getting Info By Instance Name successful!");
		return jsonObject.get("data");
	}
	
	/**
	 * 问答接口
	 * @param course学科（可选，无效）
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
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		System.out.print(response.getBody());
		JSONObject jsonObject = JSONObject.parseObject(response.getBody());
		System.out.println("Inputing Question successful!");
		return jsonObject.get("data");
	}
}
