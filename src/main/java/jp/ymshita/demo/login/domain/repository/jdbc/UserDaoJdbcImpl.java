package jp.ymshita.demo.login.domain.repository.jdbc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import jp.ymshita.demo.login.domain.model.User;
import jp.ymshita.demo.login.domain.model.User2;
import jp.ymshita.demo.login.domain.repository.UserDao;

@Repository("UserDaoJdbcImpl")
public class UserDaoJdbcImpl implements UserDao {

	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public int count() throws DataAccessException {
		int count = jdbc.queryForObject(
				"SELECT COUNT(*) FROM m_user",
				Integer.class);
		return count;
	}

	@Override
	public int insertOne(User user) throws DataAccessException {
//		String encryptedPassword = passwordEncoder.encode(user.getPassword());
//		int rowNumber = jdbc.update(
//				"INSERT INTO m_user(user_id, password, user_name, birthday, age, marriage, role) VALUES(?, ?, ?, ?, ?, ?, ?) ",
//				user.getUserId(),
//				encryptedPassword,
//				user.getUserName(),
//				user.getBirthday(),
//				user.getAge(),
//				user.isMarriage(),
//				user.getRole());

		return insertOne2(user);
	}
	
	public int insertOne2(User user) throws DataAccessException {
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		
		User2 user2 = new User2();
		user2.setUserId(user.getUserId());
		user2.setPassword(encryptedPassword);
		Timestamp timestamp = new Timestamp(user.getBirthday().getTime());
		user2.setPasswordUpdateDate(timestamp);
		user2.setLoginMissTimes(user.getAge());
		user2.setUnlock(true);
		user2.setTenantId("tenant");
		user2.setUserName(user.getUserName());
		user2.setMailAddress(user.getUserId());
		user2.setEnabled(true);
		user2.setUserDueDate(timestamp);
		
		String sql = "INSERT INTO m_user("
				+ "user_id, "
				+ "password,"
				+ " pass_update_date, "
				+ "login_miss_times, "
				+ "unlock, "
				+ "tenant_id, "
				+ "user_name, "
				+ "mail_address, "
				+ "enabled, "
				+ "user_due_date) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		int rowNumber = jdbc.update(
				sql,
				user2.getUserId(),
				encryptedPassword,
				user2.getPasswordUpdateDate(),
				user2.getLoginMissTimes(),
				user2.isUnlock(),
				user2.getTenantId(),
				user2.getUserName(),
				user2.getMailAddress(),
				user2.isEnabled(),
				user2.getUserDueDate());

		return rowNumber;
	}

	@Override
	public User selectOne(String userId) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM m_user WHERE user_id=?", userId);
		User user = new User();
		
		user.setUserId((String) map.get("user_id"));
		user.setPassword((String) map.get("password"));
		user.setUserName((String) map.get("user_name"));
		user.setBirthday((Date) map.get("birthday"));
		user.setAge((Integer) map.get("age"));
		user.setMarriage((Boolean) map.get("marriage"));
		user.setRole((String) map.get("role"));
		return user;
	}

	@Override
	public List<User> selectMany() throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM m_user");
		List<User> userList = new ArrayList<>();
		for (Map<String, Object> map : getList) {
			User user = new User();
			user.setUserId((String) map.get("user_id"));
			user.setPassword((String) map.get("password"));
			user.setUserName((String) map.get("user_name"));
			user.setBirthday((Date) map.get("birthday"));
			user.setAge((Integer) map.get("age"));
			user.setMarriage((Boolean) map.get("marriage"));
			user.setRole((String) map.get("role"));

			userList.add(user);
		}
		return userList;
	}

	@Override
	public int updateOne(User user) throws DataAccessException {
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		int rowNumber  = jdbc.update("UPDATE m_user SET password=?, user_name=?, birthday=?, age=?, marriage=? WHERE user_id=?",
				encryptedPassword,
				user.getUserName(),
				user.getBirthday(),
				user.getAge(),
				user.isMarriage(),
				user.getUserId());
//		if(rowNumber > 0) {
//			throw new DataAccessException(" transction test") {};
//		}
		return rowNumber;
	}

	@Override
	public int deleteOne(String userId) throws DataAccessException {
		int rowNumber = jdbc.update("DELETE FROM m_user WHERE user_id=?", userId);
		return rowNumber;
	}

	@Override
	public void userCsvOut() throws DataAccessException {
		String sql = "SELECT * FROM m_user";
		UserRowCallbackHandler handler = new UserRowCallbackHandler();
		jdbc.query(sql, handler);
	}
}
