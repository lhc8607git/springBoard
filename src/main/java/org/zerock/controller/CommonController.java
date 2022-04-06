package org.zerock.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class CommonController {

	@GetMapping("/accessError")
	public void accessDenied(Authentication auth, Model model) {

		log.info("■ access에러 발생! " + auth);

		model.addAttribute("msg", "access 에러발생...");
	}

	@GetMapping("/customLogin")
	public void loginInput(String error, String logout, Model model) {

		log.info("error: " + error);
		log.info("logout: " + logout);

		if (error != null) {
			model.addAttribute("error", "로그인 에러!!");
		}

		if (logout != null) {
			model.addAttribute("logout", "Logout!!");
		}
	}



	@GetMapping("/customLogout")
	public void logoutGET() {
		log.info("■ customlogout 요청 들어옴");
	}

	@PostMapping("/customLogout")
	public void logoutPost() {
		log.info("■ post custom logout");
	}

}
