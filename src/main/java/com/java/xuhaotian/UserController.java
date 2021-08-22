package com.java.xuhaotian;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
public class UserController {
	/**
	 * 用户注册
	 * @param param包括userName和password，都是String类型，要求非空
	 * @return 如果注册失败返回错误信息，成功(201)返回空
	 */
	@PostMapping(value = "/register")
	public ResponseEntity<?> register(@RequestBody JSONObject param) {
		String userName = param.getString("userName");
		String password = param.getString("password");
		Error error = UserSystem.register(userName, password);
		if (error != null) return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
		else return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
	
	/**
	 * 用户登录
	 * @param param包括userName和password，都是String类型，要求非空
	 * @return 如果登陆失败返回错误信息，成功(200)则返回Token(String类型)
	 */
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody JSONObject param) {
		String userName = param.getString("userName");
		String password = param.getString("password");
		Object obj = UserSystem.login(userName, password);
		if (obj instanceof Error) return new ResponseEntity<Error>((Error)obj, HttpStatus.UNAUTHORIZED);
		else if (obj instanceof String) return new ResponseEntity<String>((String)obj, HttpStatus.OK);
		else return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 用户登出
	 * @param param包括token，String类型，要求非空
	 * @return 如果登出失败返回错误信息，成功(200)返回Token(String类型)
	 */
	@PutMapping(value = "/logout")
	public ResponseEntity<?> logout(@RequestBody JSONObject param) {
		String token = param.getString("token");
		Error error = UserSystem.logout(UserSystem.getUserByToken(token));
		if (error == null) return new ResponseEntity<>(null, HttpStatus.OK);
		else if (error instanceof Error) return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
		else return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 用户修改密码
	 * @param param包括oldPassword，newPassword和token，都是String类型，要求非空
	 * @return 如果登陆失败返回错误信息，成功(200)则返回空
	 */
	@PutMapping(value = "/modifyPassword")
	public ResponseEntity<?> modifyPassword(@RequestBody JSONObject param) {
		String oldPassword = param.getString("oldPassword");
		String newPassword = param.getString("newPassword");
		String token = param.getString("token");
		Object obj = UserSystem.modifyPassword(UserSystem.getUserByToken(token), oldPassword, newPassword);
		if (obj instanceof Error) return new ResponseEntity<Error>((Error)obj, HttpStatus.UNAUTHORIZED);
		else if (obj instanceof String) return new ResponseEntity<String>((String)obj, HttpStatus.OK);
		else return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
