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
package net.duckling.probe.ui.permission;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.probe.ui.permission.annotation.RequirePermission;

public class ProbeDenyListener implements PermissionDenyListener {
	private String baseUrl;
	public void setBaseUrl(String url){
		this.baseUrl = url;
	}
	public void onDeny(HttpServletRequest request,
			HttpServletResponse response, RequirePermission requirePermission)
			throws IOException {
		if (request.getAttribute("user") != null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}else{
			response.sendRedirect(baseUrl+"/login.do");
		}
	}
}
