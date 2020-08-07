package com.cos.jwtex01.config.jwt;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cos.jwtex01.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	//모든것을 오버라이드해서 sysout 찍으면 순서를 알 수 있음
	
	//@RequiredArgsConstructor로 DI했음
	private final AuthenticationManager authenticationManager;

	
	//1번 Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager (필터체인으로 가서 또 다른 것을 타게된다는것)
	//여기서 request랑 response를 변형도 가능
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		//request에서 username , password를 파싱해서 자바 Object로 받기
		//objectmapper를 쓰면 꺼내짐
		ObjectMapper om = new ObjectMapper();
		LoginRequestDto loginRequestDto = null;
		
		//리퀘스트 안에 있는 정보 파싱
		try {
			//InputStream으로 받은 데이터를 Dto로 바꿔줌
			//블로그에 ObjectMapper에 대한 정리
			//Username password가 저장됨
			loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//유저네임 패스워드 토큰 생성
		//principal = 인증 주체
		//credentials = 비밀번호
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getUsername(), 
						loginRequestDto.getPassword() 
						);
				
		//Authentication 객체는 자동으로 UserDetailsService를 통해서 만들어진다. 
		Authentication authentication = 
				authenticationManager.authenticate(authenticationToken);
		
		
		return authentication;
	}

	
	//JWT Token 생성해서 response에 담아주기(header)
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
}
