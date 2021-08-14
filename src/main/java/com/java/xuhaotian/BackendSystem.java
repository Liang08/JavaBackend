package com.java.xuhaotian;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
	
	
}
