package com.java.xuhaotian;

import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaBackendApplication.class, args);
		BackendSystem.login("g4dadfh4", "18551209967");
		DataSystem.loadData();
		DataSystem.initHotInstance();
	}

}
