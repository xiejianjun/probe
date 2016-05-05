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
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.probe.common.IOUtils;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stat")
public class RandomJsonController {
	private Random rand = new Random();

	@RequestMapping
	public void stat(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		JSONObject object = new JSONObject();
		object.put("Users", rand.nextInt());
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/json");
		PrintWriter writer = response.getWriter();
		try {
			object.writeJSONString(writer);
		} finally {
			IOUtils.closeQuitely(writer);
		}

	}
}
