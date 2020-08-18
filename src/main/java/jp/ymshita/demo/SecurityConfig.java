package jp.ymshita.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("UserDetailsServiceImpl")
	private UserDetailsService userDetailsService;
	
	@Bean // define Bean of PasswordEncoder
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private DataSource dataSource;

	private static final String USER_SQL = "SELECT user_id, password, enabled FROM m_user WHERE user_id=?";

	private static final String ROLE_SQL = "SELECT m_user.user_id, role.role_name "
			+ "FROM m_user "
			+ "INNER JOIN t_user_role AS user_role "
			+ "ON m_user.user_id=user_role.user_id "
			+ "INNER JOIN m_role AS role "
			+ "ON user_role.role_id=role.role_id "
			+ "WHERE m_user.user_id=?";

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("webjars/**", "/css/**"); // 静的リソースはセキュリティを無視
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/login/**").permitAll()
				.antMatchers("/signup/**").permitAll()
				.antMatchers("/rest/**").permitAll()
				.antMatchers("/admin").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated(); // 上記以外のリクエストに対しては認証済みの場合アクセス許可

		http.formLogin()
				.loginProcessingUrl("/login") //templete form action
				.loginPage("/login") //controller action
				.failureUrl("/login?error")
				.usernameParameter("userId") // input element's name attribute
				.passwordParameter("password") // input element's name attribute
				.defaultSuccessUrl("/home", true);

		http.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // for GET request
				.logoutUrl("/logout") // for POST request, general
				.logoutSuccessUrl("/login");

		RequestMatcher csrfMatcher = new RestMatcher("/rest/**");
		http.csrf().requireCsrfProtectionMatcher(csrfMatcher);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication() // Specify JdbcUserDetailsManager as the implementation of UserDetailSservice
//				.dataSource(dataSource)
//				.usersByUsernameQuery(USER_SQL)
//				.authoritiesByUsernameQuery(ROLE_SQL)
//				.passwordEncoder(passwordEncoder());
		
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder());
	}
}
