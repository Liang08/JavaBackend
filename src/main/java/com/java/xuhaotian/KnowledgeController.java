package com.java.xuhaotian;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RestController
public class KnowledgeController {

	/**
	 * 获取实体列表
	 * @param course学科，String
	 * @param searchKey关键字，String
	 * @param offset偏移量，Integer，可选，缺省值0
	 * @param limit数量，Integer，可选，缺省值50
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回实体列表（List类型）
	 */
	@GetMapping(value = "/getInstanceList")
	public ResponseEntity<?> getInstanceList(@RequestParam(value="course") String course, 
			@RequestParam(value="searchKey") String searchKey, 
			@RequestParam(value="offset",required=false,defaultValue="0") Integer offset, 
			@RequestParam(value="limit",required=false,defaultValue="50") Integer limit, 
			@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		Object obj = BackendSystem.getInstanceList(course, searchKey, offset, limit);
		if (obj instanceof Error) return new ResponseEntity<Error>((Error)obj, HttpStatus.NOT_ACCEPTABLE);
		else if (obj instanceof List) {
			user.addSearchHistory(ImmutablePair.of(course, searchKey));
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
		else {
			System.out.println("ERR: getInstanceList in KnowledgeController\n" + obj.getClass());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 获取实体详情
	 * @param course学科，String，可选
	 * @param name实体名，String
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回实体详情（JSONObject类型）
	 */
	@GetMapping(value = "/getInfoByInstanceName")
	public ResponseEntity<?> getInfoByInstanceName(@RequestParam(value="course",required=false,defaultValue="") String course, 
			@RequestParam(value="name") String name, 
			@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		Object obj = BackendSystem.getInfoByInstanceName(course, name);
		if (obj instanceof Error) return new ResponseEntity<Error>((Error)obj, HttpStatus.NOT_ACCEPTABLE);
		else if (obj instanceof JSONObject) {
			user.addInstanceHistory(name);
			return new ResponseEntity<JSONObject>((JSONObject)obj, HttpStatus.OK);
		}
		else {
			System.out.println("ERR: getInfoByInstanceName in KnowledgeController\n" + obj.getClass());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 问答
	 * @param param包括course,inputQuestion和token，都是String，course可选
	 * @return 如果失败返回错误信息，成功（200）返回回答（JSONObject类型）
	 */
	@PostMapping(value = "/inputQuestion")
	public ResponseEntity<?> inputQuestion(@RequestBody JSONObject param) {
		String course = param.getString("course");
		String inputQuestion = param.getString("inputQuestion");
		String token = param.getString("token");
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		Object obj = BackendSystem.inputQuestion(course, inputQuestion);
		if (obj instanceof Error) return new ResponseEntity<Error>((Error)obj, HttpStatus.NOT_ACCEPTABLE);
		else if (obj instanceof JSONArray) return new ResponseEntity<JSONObject>((JSONObject)((JSONArray)obj).get(0), HttpStatus.OK);
		else {
			System.out.println("ERR: inputQuestion in KnowledgeController\n" + obj.getClass());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 知识识别
	 * @param param包括context,course和token，都是String，course可选
	 * @return 如果失败返回错误信息，成功（200）返回知识标注（JSONObject类型）
	 */
	@PostMapping(value = "/linkInstance")
	public ResponseEntity<?> linkInstance(@RequestBody JSONObject param) {
		String context = param.getString("context");
		String course = param.getString("course");
		String token = param.getString("token");
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		Object obj = BackendSystem.linkInstance(context, course);
		if (obj instanceof Error) return new ResponseEntity<Error>((Error)obj, HttpStatus.NOT_ACCEPTABLE);
		else if (obj instanceof JSONObject) return new ResponseEntity<JSONObject>((JSONObject)obj, HttpStatus.OK);
		else {
			System.out.println("ERR: linkInstance in KnowledgeController\n" + obj.getClass());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 获取实体相关习题列表
	 * @param uriName实体名，String
	 * @param offset偏移量，Integer，可选，缺省值0
	 * @param limit数量，Integer，可选，缺省值10
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回习题列表（List类型）
	 */
	@GetMapping(value = "/getQuestionListByUriName")
	public ResponseEntity<?> getQuestionListByUriName(@RequestParam(value="uriName") String uriName, 
			@RequestParam(value="offset",required=false,defaultValue="0") Integer offset, 
			@RequestParam(value="limit",required=false,defaultValue="10") Integer limit, 
			@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		Object obj = BackendSystem.getQuestionListByUriName(uriName, offset, limit);
		if (obj instanceof Error) return new ResponseEntity<Error>((Error)obj, HttpStatus.NOT_ACCEPTABLE);
		else if (obj instanceof List) return new ResponseEntity<>(obj, HttpStatus.OK);
		else {
			System.out.println("ERR: getQuestionListByUriName in KnowledgeController\n" + obj.getClass());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 获取实体访问历史记录
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回实体名数组(String[]类型)
	 */
	@GetMapping(value = "/getInstanceHistory")
	public ResponseEntity<?> getInstanceHistory(@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		String []history;
		try {
			history = user.getInstanceHistory();
		}
		catch (Throwable e) {
			System.out.println("ERR: getInstanceHistory in KnowledgeController\n" + e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(history, HttpStatus.OK);
	}
	
	/**
	 * 获取收藏实体列表
	 * @param token
	 * @return 成功（200）返回实体名数组(String[]类型)
	 */
	@GetMapping(value = "/getFavourite")
	public ResponseEntity<?> getFavourite(@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		String []favourite = user.getFavouriteList();
		return new ResponseEntity<>(favourite, HttpStatus.OK);
	}
	
	/**
	 * 设置收藏
	 * @param param包括name和token，都是String
	 * @return 成功（200）返回null
	 */
	@PutMapping(value = "/setFavourite")
	public ResponseEntity<?> setFavourite(@RequestBody JSONObject param) {
		String name = param.getString("name");
		String token = param.getString("token");
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		user.setFavourite(name);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	/**
	 * 取消收藏
	 * @param param包括name和token，都是String
	 * @return 成功（200）返回null
	 */
	@PutMapping(value = "/resetFavourite")
	public ResponseEntity<?> resetFavourite(@RequestBody JSONObject param) {
		String name = param.getString("name");
		String token = param.getString("token");
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		user.resetFavourite(name);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	/**
	 * 获取搜索历史记录
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回学科-关键字键值对数组(String[]类型)
	 */
	@GetMapping(value = "/getSearchHistory")
	public ResponseEntity<?> getSearchHistory(@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		ImmutablePair<String, String>[]history;
		try {
			history = user.getSearchHistory();
		}
		catch (Throwable e) {
			System.out.println("ERR: getSearchHistory in KnowledgeController\n" + e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(history, HttpStatus.OK);
	}
}
