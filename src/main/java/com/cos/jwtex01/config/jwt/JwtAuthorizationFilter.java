package com.cos.jwtex01.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//인가 
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	//BasicAuthenticationFilter Header전부 분석함
	//그러고 SecurityContextHolder에 result를 넣어줌

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//서명하는곳
		//서명할때 첫번째로 HEADER값 확인
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			 //빈값 확인, TOKEN의 Bearer 확인
			chain.doFilter(request, response); //아무것도 안하고 돌려보냄
		}
		
		String token = request.getHeader(JwtProperties.HEADER_STRING);
		//JWT에 들어가면 안되는 값들 (공백, == , =) =은 패딩값
	}
}
