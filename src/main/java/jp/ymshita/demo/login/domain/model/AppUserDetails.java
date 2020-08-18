package jp.ymshita.demo.login.domain.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDetails implements UserDetails {
	
	private String userId;
	private String password;
	private Date passUpdateDate;
	private int loginMissTimes;
	private boolean unlock;
	private boolean enabled;
	private Date userDueDate;
	
	private Collection<? extends GrantedAuthority> authority;
	
	private String tenantId;
	private String appUserName;
	private String mailAddress;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authority;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return this.userId;
	}
	@Override
	public boolean isAccountNonExpired() {
		if(this.userDueDate.after(new Date())) {
			return true;
		}else {
			return false;
		}
	}
	@Override
	public boolean isAccountNonLocked() {
		return unlock;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		if(this.passUpdateDate.after(new Date())) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	
}
