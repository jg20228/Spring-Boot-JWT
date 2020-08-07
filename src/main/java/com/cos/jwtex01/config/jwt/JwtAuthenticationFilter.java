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
	
	//인증 요청시에 실행되는 함수(attemptAuthentication) => /login 일때만 
	
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
				
		//authenticate() 함수가 호출 되면 인증 provider가 userDetailService의 
		//loadUserByUsername(토큰의 첫번째 파라메터)를 호출하고
		//UserDetails를 리턴 받아서 토큰의 두번째 파라메터(credential)과
		//UserDetails(DB값)의 getPassword() 함수로 비교해서 동일하면
		// Authentication 객체를 만들어서 필터체인으로 리턴해준다.
		
		//TIP : 인증 프로바이더의 디폴트 서비스는 UserDetailsServie 타입
		//TIP : 인증 프로바이더의 디폴트 암호화 방식은 BCryptePassword
		//결론은 인증 프로바이더에게 알려줄 필요가 없음.
		
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
