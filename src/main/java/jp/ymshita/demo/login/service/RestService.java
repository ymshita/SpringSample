package jp.ymshita.demo.login.service;

import java.util.List;

import jp.ymshita.demo.login.domain.model.User;

public interface RestService {
	
	public boolean insert(User user);
	
	public User selectOne(String userId);
	
	public List<User> selectMany();
	
	public boolean updateOne(User user);
	
	public boolean delete(String userId);

}
