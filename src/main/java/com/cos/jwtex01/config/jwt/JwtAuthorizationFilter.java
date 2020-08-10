package com.cos.jwtex01.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwtex01.config.auth.PrincipalDetails;

//인가 
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	//BasicAuthenticationFilter Header전부 분석함
	//그러고 SecurityContextHolder에 result를 넣어줌
	
	//private AuthenticationManager authenticationManager; protected로 getter가 있어서 할필요없음

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		//this.authenticationManager = authenticationManager;
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
		
		String token = request.getHeader(JwtProperties.HEADER_STRING)
		//JWT에 들어가면 안되는 값들 (공백, == , =) =은 패딩값
		.replace(JwtProperties.TOKEN_PREFIX,"") //Bearer 날려야 진정한 토큰이라서
		.replace("=", "")
		.replace(" ", "");
		
		//5. 토큰 검증
		String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
				.build()
				.verify(token)//서명
				.getClaim("username").toString();
		
		if(username != null) {
			AuthenticationManager authenticationManager = 
					getAuthenticationManager();
			

		}
	}
}
