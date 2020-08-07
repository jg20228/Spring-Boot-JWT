package com.cos.jwtex01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.jwtex01.config.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity //시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean//@EnableWebSecurity로 인해서 IOC될때 같이 된다.
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http //stateful 사용 안할려고 설정
				.csrf().disable()//csrf는 필요가 없음, 이게 있으면 postman 작동이 안함
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션 사용X
			.and()
				.formLogin().disable() //form 로긴 막음
				.httpBasic().disable() //http Jsession방식 사용안함
				//필터 추가
				.addFilter(new JwtAuthenticationFilter(authenticationManager())) //내가 만든 인증 필터 
				//.addFilter(null)
				.authorizeRequests()//모든 권한 요청에 대해서
				//antMatchers를 걸고 네거티브 방식 사용
				.antMatchers("/api/v1/manager/**")
					.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
				.antMatchers("/api/v1/admin/**")
					.access("hasRole('ROLE_ADMIN')")
				.anyRequest().permitAll();
				//.authenticated()을 걸면 위 페이지 외에 모두 인증이 필요함
	}
}
