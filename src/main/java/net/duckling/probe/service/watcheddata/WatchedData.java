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
package net.duckling.probe.service.watcheddata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.duckling.probe.service.dbdataplate.DbDataPlate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WatchedData extends DbDataPlate{
	public Date getWatchTime() {
		if (this.watchTime != null) {
			return new Date(watchTime.getTime());
		} else {
			return null;
		}
	}

	public void setWatchTime(Date watchTime) {
		if (watchTime != null) {
			this.watchTime = new Date(watchTime.getTime());
		}
	}

	private int collectorId;
	private int productId;
	private Date watchTime;
	private List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

	public int getCollectorId() {
		return collectorId;
	}

	public int getProductId() {
		return productId;
	}

	public List<Map<String, Object>> getValues() {
		return values;
	}

	public void parseValues(String json) throws ParseException {
		values.clear();
		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray) parser.parse(json);
		for (Object o : array) {
			JSONObject map = (JSONObject) o;
			values.add(map);
		}
	}

	public void addValue(Map<String, Object> value) {
		values.add(value);
	}

	public void setCollectorId(int collectorId) {
		this.collectorId = collectorId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setValues(List<Map<String, Object>> values) {
		this.values = values;
	}

	public JSONArray toJSONArray() {
		JSONArray json = new JSONArray();
		if (values != null) {
			for (Map<String,Object> v:values){
				JSONObject jsonObject = new JSONObject();
				jsonObject.putAll(v);
				json.add(jsonObject);
			}
		}
		return json;
	}

	public String toJSONString() {
		return toJSONArray().toJSONString();
	}
}
