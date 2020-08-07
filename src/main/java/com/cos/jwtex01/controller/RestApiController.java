package com.cos.jwtex01.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jwtex01.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
// @CrossOrigin //CORS 허용
public class RestApiController {
	
	// 모든 사람이 접근 가능
	@GetMapping("home")
	public String home() {
		return "<h1>home</h1>";
	}
	
	
	// 매니저가 접근 가능
	@GetMapping("manager/reports")
	public String reports() {
		return "<h1>reports</h1>";
	}
	
	// 어드민이 접근 가능
	@GetMapping("admin/user")
	public List<User> users(){
		return null;
	}
}
