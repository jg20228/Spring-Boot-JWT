package com.cos.jwtex01.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.jwtex01.model.User;

public class PrincipalDetails implements UserDetails{
	//여기~
	private User user;
	public PrincipalDetails(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	//까지 만들고 오버라이드
	//setter는 세션할거면 필요하지만 
	//Userdetails가 Security~
	//로그인을 직접시키던가 UserDetails값의 객체를 수정하면 됨
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
		user.getRoleList().forEach(r ->{
			authorities.add(()->{return r;});
		});
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
