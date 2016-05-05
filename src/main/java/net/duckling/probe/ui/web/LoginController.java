/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.probe.ui.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.probe.service.login.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 登录/登出
 * @author xiejj@cnic.cn
 *
 */
@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;

	/**
	 * 接受SSO登录结果
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/login.do", params = "act=commit")
	public void commit(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		loginService.commit(request, response);
	}
	/**
	 * 登出
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/logout.do")
	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		loginService.logout(request,response);
	}
	/**
	 * 开始登录操作
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/login.do")
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		loginService.login(request, response, null);
	}
}
