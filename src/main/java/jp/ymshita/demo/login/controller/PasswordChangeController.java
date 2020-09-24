package jp.ymshita.demo.login.controller;

import jp.ymshita.demo.login.domain.model.AppUserDetails;
import jp.ymshita.demo.login.domain.model.PasswordForm;
import jp.ymshita.demo.login.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;

@Controller
@Slf4j
public class PasswordChangeController {
    @Autowired
    UserDetailsServiceImpl service;

    @GetMapping("/password/change")
    public String getPasswordChange(Model model, @ModelAttribute PasswordForm form){
        return "password_change";
    }

    @PostMapping("/password/change")
    public String postPasswordChange(
            Model model,
            @ModelAttribute PasswordForm form,
            @AuthenticationPrincipal AppUserDetails user) throws ParseException{
        service.updatePasswordDate(user.getUserId(), form.getPassword());
        return "home";
    }
}
