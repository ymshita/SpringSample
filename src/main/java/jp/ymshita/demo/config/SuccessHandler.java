package jp.ymshita.demo.config;

import jp.ymshita.demo.login.domain.model.AppUserDetails;
import jp.ymshita.demo.login.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component("SuccessHandler")
@Slf4j
public class SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserDetailsServiceImpl service;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("ログイン成功イベント開始");
        AppUserDetails user = (AppUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        log.info(user.toString());

        String redirectPath = request.getContextPath();

        if(user.getPassUpdateDate().after(new Date())){
            log.info("遷移先: ホーム");
            redirectPath += "/home";
        }else{
            log.info("遷移先: パスワード変更");
            redirectPath += "/password/change";
        }
        log.info("ログイン成功イベント終了");

        response.sendRedirect(redirectPath);
    }
}
