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
package net.duckling.probe.service.login.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.probe.service.login.LoginService;

import org.apache.log4j.Logger;

import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.Oauth;
import cn.vlabs.umt.oauth.UMTOauthConnectException;
import cn.vlabs.umt.oauth.UserInfo;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;

public class LoginServiceImpl implements LoginService {
	private Properties prop;
	private static Logger log = Logger.getLogger(LoginService.class);

	public void setOauthProp(Map<?, ?> map) {
		this.prop = new Properties();
		this.prop.putAll(map);
	}

	@Override
	public void login(HttpServletRequest request, HttpServletResponse response,String returnUrl) {
		Oauth oauth = new Oauth(prop);
		try {

			String url = oauth.getAuthorizeURL(request); 
			if (returnUrl!=null){
				url = url+"&state="+ URLEncoder.encode(returnUrl, "UTF-8");
			}
			response.sendRedirect(url);
		} catch (UMTOauthConnectException | IOException e) {
			log.error("redirect to login server failed.",e);
		}
	}

	@Override
	public void commit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取了Code换成AccessToken
		Oauth oauth = new Oauth(prop);
		try {
			AccessToken token = oauth.getAccessTokenByRequest(request);
			UserInfo user = token.getUserInfo();
			UserPrincipal principal = new UserPrincipal(user.getCstnetId(), user.getTrueName(), user.getCstnetId(),user.getType());
			request.getSession().setAttribute("user", principal);
			response.sendRedirect(prop.getProperty("probe.baseUrl"));
		} catch (UMTOauthConnectException |OAuthProblemException e) {
			log.error("commit login info failed",e);
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			String logoutUrl = prop.getProperty("logout_URL");
			String baseUrl = prop.getProperty("probe.baseUrl");
			String url = logoutUrl+"?WebServerURL="+URLEncoder.encode(baseUrl, "UTF-8");
			request.getSession().invalidate();
			response.sendRedirect(url);
		} catch (IOException e) {
			log.error("logout failed",e);
		}
	}
}
