package jp.ymshita.demo.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jp.ymshita.demo.login.domain.repository.LoginUserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private LoginUserRepository repository;

	@Autowired
	private PasswordEncoder encoder;


	/**
	 * @param: String userneme: user id
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = repository.selectOne(username);
		return user;
	}

	/**
	 * パスワード更新
	 * @param userId
	 * @param password
	 * @throws ParseException
	 */
	public void updatePasswordDate(String userId, String password) throws ParseException {
		String encryptPass = encoder.encode(password);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = sdf.parse("2099/12/31");

		repository.updatePassword(userId, encryptPass, date);
	}

}
