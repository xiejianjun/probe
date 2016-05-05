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
package net.duckling.probe.service.collector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class URLDataReader extends DataReader {

	private String url;
	private String encode = "UTF-8";
	private String method=null;
	private String email=null;
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("url", url);
		jsonObject.put("encode", encode);
		jsonObject.put("method", method);
		jsonObject.put("email", email);
		return jsonObject.toJSONString();
	}

	@Override
	public void fromJSON(String json) {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			this.url = (String) object.get("url");
			this.method=(String) object.get("method");
			if (object.get("encode") != null) {
				this.encode = (String) object.get("encode");
			}
			if (object.get("email") != null) {
				this.email = (String) object.get("email");
			}
		} catch (ParseException e) {
			this.url = null;
		}
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		
		return "URL";
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}
}
