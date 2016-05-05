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

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import net.duckling.probe.ui.permission.annotation.RequirePermission;

import cn.vlabs.commons.principal.UserPrincipal;

public class ProbePermissionChecker implements PermissionChecker {
	private Set<String> adminUsers;

	public void setAdminUsers(String adminUserString) {
		adminUsers = new HashSet<String>();
		if (adminUserString != null) {
			String[] users = adminUserString.split(":");
			for (String user : users) {
				if (StringUtils.isNotEmpty(user)) {
					adminUsers.add(user);
				}
			}
		}
	}

	public boolean hasAccess(HttpServletRequest request,
			RequirePermission requirePermission) {
		UserPrincipal user = (UserPrincipal) request.getSession().getAttribute(
				"user");
		if (requirePermission.authenticated()) {
			return user != null;
		}
		String operation = requirePermission.operation();
		if ("admin".equals(operation) && user != null) {
			return adminUsers.contains(user.getName());
		}
		return false;
	}
}