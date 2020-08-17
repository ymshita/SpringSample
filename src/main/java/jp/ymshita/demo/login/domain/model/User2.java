package jp.ymshita.demo.login.domain.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class User2 {
	private String userId;
	private String password;
	private Timestamp passwordUpdateDate;
	private int loginMissTimes;
	private boolean unlock;
	private String tenantId;
	private String userName;
	private String mailAddress;
	private boolean enabled;
	private Timestamp userDueDate;
}

