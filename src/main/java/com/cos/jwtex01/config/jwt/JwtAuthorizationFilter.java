package com.cos.jwtex01.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwtex01.config.auth.PrincipalDetails;
import com.cos.jwtex01.config.auth.SessionUser;
import com.cos.jwtex01.model.User;
import com.cos.jwtex01.repository.UserRepository;

//인가 
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	// BasicAuthenticationFilter Header전부 분석함
	// 그러고 SecurityContextHolder에 result를 넣어줌
	private UserRepository userRepository;

	// private AuthenticationManager authenticationManager; protected로 getter가 있어서
	// 할필요없음

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// this.authenticationManager = authenticationManager;
	}

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("JwtAuthorizationFilter 진입확인");
		// 서명하는곳
		// 서명할때 첫번째로 HEADER값 확인
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			// 빈값 확인, TOKEN의 Bearer 확인
			chain.doFilter(request, response); // 아무것도 안하고 돌려보냄
		}

		String token = request.getHeader(JwtProperties.HEADER_STRING)
				// JWT에 들어가면 안되는 값들 (공백, == , =) =은 패딩값
				.replace(JwtProperties.TOKEN_PREFIX, "") // Bearer 날려야 진정한 토큰이라서
				.replace("=", "").replace(" ", "");

		// 5. 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
		//내가 SecurityContext에 직접 접근해서 세션을 만들때 자동으로 UserDetailsService에 있는 loadByusername이 호출된다.
		String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)// 서명
				.getClaim("username").asString();
		
		if(username != null) {
			User user = userRepository.findByUsername(username);
			SessionUser sessionUser = SessionUser.builder()
					.id(user.getId())
					.username(user.getUsername())
					.roles(user.getRoleList())
					.build();
			
			//세션을 이거 쓸건데 어노테이션 @를 쓰는법을 알려줄것이다. user에서 @SessionUser sessionUser
			HttpSession session = request.getSession();
			session.setAttribute("sessionUser", sessionUser);
		}
		chain.doFilter(request, response);
	}
}
