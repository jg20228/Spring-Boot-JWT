package com.cos.jwtex01.config.auth;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.jwtex01.model.User;
import com.cos.jwtex01.repository.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService : 진입");
		User user = userRepository.findByUsername(username);
		//여기서 못찾으면 null이 아니라 throws로 Exception이 발생되는데 Global로 잡아서 해도 된다.
		
		if(user != null) {
			System.out.println("해당 유저를 찾았어요!");
			//session.setAttribute("loginUser",user);
			//이때 내부적으로 로그인이 성공되어서 Authentication가 생성됨
			return new PrincipalDetails(user);
		}
		System.out.println("해당 유저를 못찾았어요!");
		return null;
	}

}
