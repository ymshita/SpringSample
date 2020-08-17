
package jp.ymshita.demo.login.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jp.ymshita.demo.login.domain.model.SignupForm;
import jp.ymshita.demo.login.domain.model.User;
import jp.ymshita.demo.login.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	@Autowired
	UserService userService;

	private Map<String, String> radioMarriage;

	private Map<String, String> initRadioMarriage() {
		Map<String, String> radio = new LinkedHashMap<>();
		radio.put("既婚", "true");
		radio.put("未婚", "false");
		return radio;
	}

	@GetMapping("/home")
	public String getHome(Model model,
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
		log.info("HomeController Start");
		log.info("user: " +user.toString());
		log.info("HomeController End");
		model.addAttribute("contents", "login/home :: home_contents");
		return "login/homeLayout";
	}

	@GetMapping("/home2")
	public String getHome2(Model model, Principal principal) {
		Authentication authentication = (Authentication) principal;
		org.springframework.security.core.userdetails.User user1 = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
		
		log.info("user1: " + user1.toString());
		org.springframework.security.core.userdetails.User user2 = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		log.info("user2: " + user2.toString());

		model.addAttribute("contents", "login/home :: home_contents");
		return "login/homeLayout";
	}

	@GetMapping("/userList")
	public String getUserList(Model model) {
		model.addAttribute("contents", "/login/userList :: userList_contents");
		List<User> userList = userService.selectMany();

		model.addAttribute("userList", userList);
		int count = userService.count();
		model.addAttribute("userListCount", count);

		return "/login/homeLayout";
	}

	@GetMapping("/userDetail/{id:.+}")
	public String getUserDetail(
			@ModelAttribute SignupForm form,
			Model model,
			@PathVariable("id") String userId) {

		System.out.println("userId = " + userId);
		model.addAttribute("contents", "login/userDetail :: userDetail_contents");
		radioMarriage = initRadioMarriage();
		model.addAttribute("radioMarriage", radioMarriage);
		if (userId != null && userId.length() > 0) {
			User user = userService.selectOne(userId);

			form.setUserId(user.getUserId());
			form.setUserName(user.getUserName());
			form.setBirthday(user.getBirthday());
			form.setAge(user.getAge());
			form.setMarriage(user.isMarriage());

			model.addAttribute("signupForm", form);
		}
		return "login/homeLayout";
	}

	@PostMapping(value = "/userDetail", params = "update")
	public String postUserDetailUpdate(@ModelAttribute SignupForm form, Model model) {
		System.out.println("更新ボタンの処理");
		User user = new User();

		user.setUserId(form.getUserId());
		user.setPassword(form.getPassword());
		user.setUserName(form.getUserName());
		user.setBirthday(form.getBirthday());
		user.setAge(form.getAge());
		user.setMarriage(form.isMarriage());

		try {
			boolean result = userService.updateOne(user);
			if (result) {
				model.addAttribute("result", "更新成功");
			} else {
				model.addAttribute("result", "更新失敗");
			}
		} catch (Exception e) {
			model.addAttribute("result", "更新失敗(" + e.toString() + ")");
		}

		return getUserList(model);
	}

	@PostMapping(value = "/userDetail", params = "delete")
	public String postUserDeteilDelete(@ModelAttribute SignupForm form, Model model) {
		System.out.println("削除ボタンの処理");

		boolean result = userService.deleteOne(form.getUserId());
		if (result) {
			model.addAttribute("result", "削除成功");
		} else {
			model.addAttribute("result", "削除失敗");
		}
		return getUserList(model);
	}

	@PostMapping("/logout")
	public String postLogout() {
		return "redirect:/login";
	}

	@GetMapping("/userList/csv")
	public ResponseEntity<byte[]> getUserListCsv(Model model) {
		userService.userCsvOut();
		byte[] bytes = null;

		try {
			bytes = userService.getFile("sample.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}

		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", "sample.csv");

		return new ResponseEntity<>(bytes, header, HttpStatus.OK);
	}

	@GetMapping("/admin")
	public String getAdmin(Model model) {
		model.addAttribute("contents", "login/admin :: admin_contents");
		return "login/homeLayout";
	}
}
