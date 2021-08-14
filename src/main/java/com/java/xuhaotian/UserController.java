package com.java.xuhaotian;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;


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
	 * @return 如果登陆失败返回错误信息，成功(200)则返回Token
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
}
