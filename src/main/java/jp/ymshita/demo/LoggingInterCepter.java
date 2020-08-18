package jp.ymshita.demo;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Component("LoggingIntercepter")
@Slf4j
public class LoggingInterCepter implements HandlerInterceptor {

	private static final String USER_ID = "USER_ID";
	private static final String SESSION_ID = "SESSION_ID";

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) {

		Principal user = request.getUserPrincipal();
		String userId = null;
		if (user != null) {
			userId = user.getName();
		}

		if (userId != null && "".equals(userId) == false) {
			MDC.put(USER_ID, userId);
		} else {
			MDC.put(USER_ID, "");
		}

		HttpSession session = request.getSession(false);
		if (session != null) {
			MDC.put(SESSION_ID, session.getId());
		}

		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response,
			Object object,
			ModelAndView modelAndView) {
		MDC.remove(SESSION_ID);
		MDC.remove(USER_ID);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			Exception ex) {
		
	}
}
