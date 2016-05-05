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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.probe.ui.permission.annotation.RequirePermission;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOG = Logger
			.getLogger(SecurityInterceptor.class);
	private PermissionChecker checker = null;

	private PermissionDenyListener listener = null;

	private String param;

	private PermissionResolver resolver = new PermissionResolver();

	private boolean isProcessed(Object returnValue) {
		if (returnValue == null) {
			return true;
		}
		return (Boolean) returnValue;
	}

	private Object getControllerBean(Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod m = (HandlerMethod) handler;
			return m.getBean();
		} else {
			return handler;
		}
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws SecurityInceptorException {
		Object controller = getControllerBean(handler);
		String methodName = request.getParameter(param);
		if (LOG.isDebugEnabled()) {
			LOG.debug("handler=" + controller.getClass() + " method="
					+ methodName + " called.");
		}
		RequirePermission permission = resolver.findPermission(controller,
				methodName);
		if (permission == null) {
			return true;
		}

		if (checker == null || checker.hasAccess(request, permission) ) {
			return true;
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("check permission failed");
			}
			Method denyMethod = resolver.findDenyProcessor(handler, methodName);
			if (denyMethod != null) {
				Object returnValue = callCustomDenyMethod(request, response,
						handler, methodName, denyMethod);
				if (isProcessed(returnValue)) {
					return false;
				}
			}

			try {
				listener.onDeny(request, response, permission);
			} catch (IOException e) {
				throw new SecurityInceptorException("调用缺省的onDeny方法失败", e);
			}
			return false;
		}
	}

	private Object callCustomDenyMethod(HttpServletRequest request,
			HttpServletResponse response, Object handler, String methodName,
			Method denyMethod) throws SecurityInceptorException {
		Object returnValue;
		try {
			returnValue = denyMethod.invoke(handler, new Object[] { methodName,
					request, response });
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new SecurityInceptorException("调用定制的onDeny方法失败", e);
		}
		return returnValue;
	}

	public void setListener(PermissionDenyListener listener) {
		this.listener = listener;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setPermissionChecker(PermissionChecker checker) {
		this.checker = checker;
	}
}
