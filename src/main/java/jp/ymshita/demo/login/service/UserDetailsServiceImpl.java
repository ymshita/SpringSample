package jp.ymshita.demo.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import jp.ymshita.demo.login.domain.repository.LoginUserRepository;

@Component("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private LoginUserRepository repository;

	/**
	 * @param: String userneme: user id
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = repository.selectOne(username);
		return user;
	}

}
