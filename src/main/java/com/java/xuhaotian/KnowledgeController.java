package com.java.xuhaotian;

import java.util.*;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	 * @param label筛选标签，String，可选，默认不筛选
	 * @param sorted是否按照相关度排序，Boolean，可选，默认不排序
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回实体列表（List类型）
	 */
	@GetMapping(value = "/getInstanceList")
	public ResponseEntity<?> getInstanceList(@RequestParam(value="course") String course, 
			@RequestParam(value="searchKey") String searchKey, 
			@RequestParam(value="offset",required=false,defaultValue="0") Integer offset, 
			@RequestParam(value="limit",required=false,defaultValue="50") Integer limit, 
			@RequestParam(value="label",required=false,defaultValue="") String label, 
			@RequestParam(value="sorted",required=false,defaultValue="false") Boolean sorted, 
			@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		Object obj = BackendSystem.getInstanceList(course, searchKey, offset, limit, label, sorted);
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
	 * @param course学科，String
	 * @param name实体名，String
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回实体详情（JSONObject类型）
	 */
	@GetMapping(value = "/getInfoByInstanceName")
	public ResponseEntity<?> getInfoByInstanceName(@RequestParam(value="course") String course, 
			@RequestParam(value="name") String name, 
			@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		Object obj = BackendSystem.getInfoByInstanceName(course, name);
		if (obj instanceof Error) return new ResponseEntity<Error>((Error)obj, HttpStatus.NOT_ACCEPTABLE);
		else if (obj instanceof JSONObject) {
			user.addInstanceHistory(ImmutablePair.of(course, name));
			((JSONObject) obj).put("isFavourite", user.isFavourite(ImmutablePair.of(course, name)));
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
	 * @return 如果失败返回错误信息，成功（200）返回回答（JSONArray类型）
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
		else if (obj instanceof JSONArray) return new ResponseEntity<JSONArray>((JSONArray)obj, HttpStatus.OK);
		else {
			System.out.println("ERR: inputQuestion in KnowledgeController\n" + obj.getClass());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 知识识别
	 * @param param包括context,course和token，都是String，course可选
	 * @return 如果失败返回错误信息，成功（200）返回知识标注（List类型）
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
		else if (obj instanceof List) return new ResponseEntity<>(obj, HttpStatus.OK);
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
	 * @return 如果失败返回错误信息，成功（200）返回学科-实体名键值对数组(ImmutablePair<String, String>[]类型)
	 */
	@GetMapping(value = "/getInstanceHistory")
	public ResponseEntity<?> getInstanceHistory(@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		ImmutablePair<String, String>[] history;
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
	 * 清除实体访问历史记录
	 * @param token
	 * @return 成功（200）返回null
	 */
	@PutMapping(value = "/clearInstanceHistory")
	public ResponseEntity<?> clearInstanceHistory(@RequestBody JSONObject param) {
		String token = param.getString("token");
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		user.clearInstanceHistory();
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	/**
	 * 获取收藏实体列表
	 * @param token
	 * @return 成功（200）返回学科-实体名键值对数组(ImmutablePair<String, String>[]类型)
	 */
	@GetMapping(value = "/getFavourite")
	public ResponseEntity<?> getFavourite(@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		ImmutablePair<String, String>[] favourite = user.getFavouriteList();
		return new ResponseEntity<>(favourite, HttpStatus.OK);
	}
	
	/**
	 * 设置收藏
	 * @param param包括course,name和token，都是String
	 * @return 成功（200）返回null
	 */
	@PutMapping(value = "/setFavourite")
	public ResponseEntity<?> setFavourite(@RequestBody JSONObject param) {
		String course = param.getString("course");
		String name = param.getString("name");
		String token = param.getString("token");
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		if (course == null || course .isEmpty() || name == null || name.isEmpty()) {
			return new ResponseEntity<Error>(new Error(16, "Course and Name cannot be empty!"), HttpStatus.BAD_REQUEST);
		}
		user.setFavourite(ImmutablePair.of(course, name));
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	/**
	 * 取消收藏
	 * @param param包括course,name和token，都是String
	 * @return 成功（200）返回null
	 */
	@PutMapping(value = "/resetFavourite")
	public ResponseEntity<?> resetFavourite(@RequestBody JSONObject param) {
		String course = param.getString("course");
		String name = param.getString("name");
		String token = param.getString("token");
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		if (course == null || course .isEmpty() || name == null || name.isEmpty()) {
			return new ResponseEntity<Error>(new Error(16, "Course and Name cannot be empty!"), HttpStatus.BAD_REQUEST);
		}
		user.resetFavourite(ImmutablePair.of(course, name));
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	/**
	 * 判断实体是否被收藏
	 * @param course学科，String
	 * @param name实体名称，String
	 * @param token
	 * @return 成功（200）返回实体是否被收藏（Boolean类型）
	 */
	@GetMapping(value = "/isFavourite")
	public ResponseEntity<?> isFavourite(@RequestParam(value="course") String course, 
			@RequestParam(value="name") String name, 
			@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(user.isFavourite(ImmutablePair.of(course, name)), HttpStatus.OK);
	}
	
	/**
	 * 获取搜索历史记录
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回学科-关键字键值对数组(ImmutablePair<String, String>[]类型)
	 */
	@GetMapping(value = "/getSearchHistory")
	public ResponseEntity<?> getSearchHistory(@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		ImmutablePair<String, String>[] history;
		try {
			history = user.getSearchHistory();
		}
		catch (Throwable e) {
			System.out.println("ERR: getSearchHistory in KnowledgeController\n" + e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(history, HttpStatus.OK);
	}
	
	/**
	 * 清除搜索历史记录
	 * @param token
	 * @return 成功（200）返回null
	 */
	@PutMapping(value = "/clearSearchHistory")
	public ResponseEntity<?> clearSearchHistory(@RequestBody JSONObject param) {
		String token = param.getString("token");
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		user.clearSearchHistory();
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	/**
	 * 获取热门标签列表
	 * @param subject学科，String
	 * @param token
	 * @return 成功（200）返回热门标签数组(String[]类型)
	 */
	@GetMapping(value = "/getHotLabel")
	public ResponseEntity<?> getHotLabel(@RequestParam(value="subject") String subject,
			@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		ArrayList<String> hotLabel = DataSystem.getHotLabel(subject);
		return new ResponseEntity<>(hotLabel, HttpStatus.OK);
	}
	
	/**
	 * 获取热门实体列表
	 * @param subject学科
	 * @param token
	 * @return 成功（200）返回热门实体数组(String[]类型)
	 */
	@GetMapping(value = "/getHotInstance")
	public ResponseEntity<?> getHotInstance(@RequestParam(value="subject") String subject,
			@RequestParam(value="token") String token) {
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		ArrayList<String> hotInstance = DataSystem.getHotInstance(subject);
		return new ResponseEntity<>(hotInstance, HttpStatus.OK);
	}

	/**
	 * 返回专项训练题目列表
	 * @param limit题目数量，Integer，可选，缺省值10
	 * @param name实体名，String
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回题目列表(List<JSONObject>类型)
	 */
	@GetMapping(value = "/getSpecialTopicQuestionList")
	public ResponseEntity<?> getSpecialTopicQuestionList(@RequestParam(value="limit",required=false,defaultValue="10") Integer limit, 
			@RequestParam(value="name") String name, 
			@RequestParam(value="token") String token) {
		if (limit == null || limit < 0) limit = 10;
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		Set<Integer> questionIdSet = DataSystem.getQuestionIdSetOfInstance(name);
		if (questionIdSet == null) {
			BackendSystem.getQuestionListByUriName(name, 0, 10);
			questionIdSet = DataSystem.getQuestionIdSetOfInstance(name);
		}
		List<Integer> idList = new ArrayList<Integer>(questionIdSet);
		Collections.shuffle(idList);
		List<JSONObject> questionList = new ArrayList<>();
		for (int i = 0; i < idList.size() && i < limit; i++) {
			questionList.add(DataSystem.getQuestion(idList.get(i)));
		}
		return new ResponseEntity<>(questionList, HttpStatus.OK);
	}
	
	/**
	 * 根据做题及浏览历史记录推荐题目
	 * @param limit题目数量，Integer，可选，缺省值10
	 * @param token
	 * @return 如果失败返回错误信息，成功（200）返回题目列表(List<JSONObject>类型)
	 */
	@GetMapping(value = "/getRecommandQuestionList")
	public ResponseEntity<?> getRecommandQuestionList(@RequestParam(value="limit",required=false,defaultValue="10") Integer limit, 
			@RequestParam(value="token") String token) {
		if (limit == null || limit < 0) limit = 10;
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		Set<Integer> questionIdSet = new HashSet<>();
		Set<String> instanceNameSet = new HashSet<>(); 
		ImmutablePair<String, String>[] history = user.getInstanceHistory();
		double probability = 0.8;
		for (ImmutablePair<String, String> instance : history) {
			if (probability < 0.1 || questionIdSet.size() >= 3 * limit) break;
			String name = instance.getValue();
			if (instanceNameSet.contains(name)) continue;
			instanceNameSet.add(name);
			Set<Integer> set = DataSystem.getQuestionIdSetOfInstance(name);
			if (set == null) {
				BackendSystem.getQuestionListByUriName(name, 0, 10);
				set = DataSystem.getQuestionIdSetOfInstance(name);
			}
			final double copy = probability;
			set.forEach(id -> {
				if (Math.random() < copy) questionIdSet.add(id);
			});
			probability *= 0.9;
		}
		user.getErrorBook().forEach(id -> {
			if (Math.random() < 0.4) questionIdSet.add(id);
		});
		List<Integer> idList = new ArrayList<Integer>(questionIdSet);
		Collections.shuffle(idList);
		List<JSONObject> questionList = new ArrayList<>();
		for (int i = 0; i < idList.size() && i < limit; i++) {
			questionList.add(DataSystem.getQuestion(idList.get(i)));
		}
		
		return new ResponseEntity<>(questionList, HttpStatus.OK);
	}

	/**
	 * 记录答题对错情况
	 * @param param包括answer和token，answer是JSONArray类型，形如[{"id":10,"isCorrect":false},{"id":21,"isCorrect":true},...]
	 * @return 如果失败返回错误信息，成功（200）返回null
	 */
	@PostMapping(value = "/answerQuestion")
	public ResponseEntity<?> answer(@RequestBody JSONObject param) {
		String token = param.getString("token");
		User user = UserSystem.getUserByToken(token);
		if (user == null) return new ResponseEntity<Error>(new Error(9, "Require logged in."), HttpStatus.UNAUTHORIZED);
		JSONArray jsonArray = param.getJSONArray("answer");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			int id = obj.getIntValue("id");
			boolean isCorrect = obj.getBooleanValue("isCorrect");
			if (!isCorrect) {
				user.addError(id);
			}
			else {
				user.removeError(id);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
