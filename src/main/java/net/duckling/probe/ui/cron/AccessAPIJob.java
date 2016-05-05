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
package net.duckling.probe.ui.cron;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLConnection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.duckling.probe.common.HttpUtils;
import net.duckling.probe.common.IOUtils;
import net.duckling.probe.service.collector.APIDataReader;
import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.watcheddata.WatchedData;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AccessAPIJob implements Job {
	private static 	ContainerFactory factory = new ContainerFactory(){
		@Override
		public List<Object> creatArrayContainer() {
			return new LinkedList<Object>();
		}

		@Override
		public Map<String,Object> createObjectContainer() {
			return new LinkedHashMap<String,Object>();
		}
		
	};
	
	private static LinkedList<String> retriveOrders(Object field) {
		@SuppressWarnings("unchecked")
		LinkedHashMap<String,Object> fieldMap=(LinkedHashMap<String,Object>)field;
		LinkedList<String> fieldOrder = new LinkedList<String>();
		for (String key:fieldMap.keySet()){
			fieldOrder.add(key);
		}
		return fieldOrder;
	}
	private Collector collector;
	private String encode;

	private String url;
	
	public AccessAPIJob(Collector collector) {
		this.collector = collector;
		APIDataReader reader = (APIDataReader) collector.getReader();
		url = reader.getUrl();
		this.encode = reader.getEncode();
	}
	private Map<String,Object> calcKeysOrder(LinkedHashMap<String,Object> jsonData) {
		LinkedHashMap<String,Object> keysOrder =new LinkedHashMap<String,Object>();
		for (String key:jsonData.keySet()){
			Object field = jsonData.get(key);
			if (field instanceof LinkedHashMap){
				keysOrder.put(key+"Order", retriveOrders(field));
			}
		}
		return keysOrder;
	}

	@Override
	public WatchedData call() throws JobException {
		try {
			LinkedHashMap<String,Object> jsonData =parseOrderedJSON();
			Map<String,Object> map = calcKeysOrder(jsonData);
			jsonData.putAll(map);
			WatchedData data = new WatchedData();
			data.setProductId(collector.getProductId());
			data.setCollectorId(collector.getId());
			data.addValue(jsonData);
			data.setWatchTime(new Date());
			return data;
		} catch (IOException | ParseException e) {
			throw new JobException("执行访问API(" + url + ")的任务失败", e);
		}
	}

	@SuppressWarnings("unchecked")
	private LinkedHashMap<String,Object>  parseOrderedJSON() throws IOException, ParseException{
		URLConnection conn = HttpUtils.createUrlconnection(url);
		Reader in = null;
		try {
			conn.connect();
			in = new InputStreamReader(conn.getInputStream(), encode);
			JSONParser parser = new JSONParser();
			return (LinkedHashMap<String,Object>) parser.parse(in, factory);
		} finally {
			IOUtils.closeQuitely(in);
		}
	}
}
