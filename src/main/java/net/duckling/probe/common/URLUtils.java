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
package net.duckling.probe.common;

import javax.servlet.http.HttpServletRequest;
/**
 * URL工具，拿出请求URL的根域名部分
 * @author xiejj@cnic.cn
 *
 */
public final class URLUtils {
	private URLUtils(){};
	/**
	 * 执行截取操作
	 * @param request	请求对象
	 * @return			主域名部分
	 */
	public static String getBaseUrl(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		return getBaseUrl(url.toString());
	}
	/**
	 * 执行截取操作
	 * @param url		原始请求URL
	 * @return			主域名部分
	 */
	public static String getBaseUrl(String url) {
		int colonIndex = url.indexOf(':');
		int index = url.indexOf('/', colonIndex+3);
		return url.substring(0, index);
	}
}