package com.weblab.chatroom.controller;

import com.weblab.chatroom.util.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	/** 用于随机选的数字 */
	public static final String BASE_CHARACTER = "abcdefghijklmnopqrstuvwxyz0123456789";

	@GetMapping("/index")
	public ModelAndView index(){
		ModelAndView mav = new ModelAndView("socket");
		mav.addObject("uid", RandomUtils.randomString(BASE_CHARACTER,6));
		return mav;
	}
}
