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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.CollectorService;
import net.duckling.probe.service.product.Product;
import net.duckling.probe.service.product.ProductService;
import net.duckling.probe.service.statsurl.StatsUrl;
import net.duckling.probe.service.statsurl.StatsUrlService;
import net.duckling.probe.service.statsvalue.StatsValue;
import net.duckling.probe.service.statsvalue.StatsValueService;
import net.duckling.probe.service.watcheddata.WatchedData;
import net.duckling.probe.service.watcheddata.WatchedDataService;
import net.duckling.probe.service.watchedurl.WatchedUrl;
import net.duckling.probe.service.watchedurl.WatchedUrlService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 提供JSON API访问
 * @author xiejj@cnic.cn
 *
 */
@Controller
@RequestMapping("/api")
public class APIController {

	@Autowired
	private CollectorService collectorService;
	@Autowired
	private ProductService productService;

	@Autowired
	private WatchedDataService watchedDataService;

	@Autowired
	private WatchedUrlService watchedUrlService;
	
	@Autowired
	private StatsUrlService statsUrlService;
	
	@Autowired
	private StatsValueService statsValueService;
	
	private SimpleDateFormat createSimpleDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	private void insertAPIData(JSONArray array, WatchedData datum, SimpleDateFormat formater) {
		JSONObject object=new JSONObject();
		object.put("watchTime", formater.format(datum.getWatchTime()));
		object.put("collectorId", datum.getCollectorId());
		object.put("data", datum.toJSONArray());
		array.add(object);
	}
	
	private void insertURLData(JSONArray array, WatchedUrl datum, SimpleDateFormat formater) {
		JSONObject object=new JSONObject();	
		JSONObject object1=new JSONObject();
		JSONArray json = new JSONArray();
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("health", datum.getAvailability());
		data.put("responseTime", datum.getResponseTime());
		object1.putAll(data);
		json.add(object1);		
		object.put("watchTime",formater.format(datum.getWatchTime()));
		object.put("collectorId",datum.getCollectorId());
		object.put("data", json);		
		array.add(object);
	}
	private void insertValueData(JSONArray array, StatsValue datum, SimpleDateFormat formater) throws ParseException {
		JSONObject object=new JSONObject();	
		JSONObject object1=new JSONObject();
		JSONArray json = new JSONArray();
		Map<String,Object> data=new HashMap<String,Object>();
		JSONParser parser=new JSONParser();
		JSONArray array1=(JSONArray) parser.parse(datum.getData());
		for(Entry<String, Object> entry:((JSONObject) array1.get(0)).entrySet()){
			data.put("count", entry.getValue());
		}
		object1.putAll(data);
		json.add(object1);		
		object.put("watchTime",formater.format(datum.getCalcTime()));
		object.put("collectorId",datum.getCollectorId());
		object.put("data", json);		
		array.add(object);
	}
	
	private void insertStatsURlData(JSONArray array,StatsUrl datum,String productName,String collectorName,String type){
		JSONObject object=new JSONObject();	
		object.put("health", datum.getHealth());
		object.put("avgReponseTime", datum.getAvgTime());
		object.put("method", datum.getMethod());
		switch(type){
		case "day":
			object.put("day", datum.getCalcTime().toString());
			break;
		case "month":
			SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
			object.put("month", formatMonth.format(datum.getCalcTime()));
			break;
		case "year":
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			object.put("year", formatYear.format(datum.getCalcTime()));
			break;
		}
		
		array.add(object);
	}
	private void insertStatsValueData(JSONArray array,StatsValue datum,String productName,String collectorName,String type) throws ParseException{
		JSONObject object=new JSONObject();
		JSONParser parser=new JSONParser();
		JSONArray array1=(JSONArray) parser.parse(datum.getData());
		for(Entry<String, Object> entry:((JSONObject) array1.get(0)).entrySet()){
			object.put("count", entry.getValue());
		}
		switch(type){
		case "day":
			object.put("day", datum.getCalcTime().toString());
			break;
		case "month":
			SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
			object.put("month", formatMonth.format(datum.getCalcTime()));
			break;
		case "year":
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			object.put("year", formatYear.format(datum.getCalcTime()));
			break;
		}
		array.add(object);
	}
	private void writeJSON(HttpServletResponse response, JSONArray array)
			throws IOException {
		response.setContentType("text/javascript");
		response.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		response.setHeader("Access-Control-Allow-Origin", "*");
		array.writeJSONString(response.getWriter());
	}
	
	/**
	 * 直接访问某个监控项的上次监测值
	 * @param productName	监控产品的名称
	 * @param collectorName	监控项的名称
	 */
	@RequestMapping("/data/{productName}/{collectorName}")
	public void collectorData(@PathVariable("productName") String productName,
			@PathVariable("collectorName") String collectorName,
			HttpServletResponse response) throws IOException {
		Product product = productService.findByName(productName);
		SimpleDateFormat dateFormat = createSimpleDateFormat();
		JSONArray array = new JSONArray();
		if (product != null) {
			Collector collector = collectorService.find(product.getId(),
					collectorName);
			if (collector != null) {
				switch(collector.getReader().getType()){
				case "SQL":
					if(collector.getDatatype()!=null &&collector.getDatatype().equals("valuetype")){
						StatsValue value=statsValueService.findLastestData(collector.getId());
						try{
							insertValueData(array,value,dateFormat);
						}catch(ParseException pe) {
							System.out.println("position: " + pe.getPosition());
						    System.out.println(pe);
						}		
					}else{
						WatchedData datsql = watchedDataService.findLastestData(collector.getId());
						insertAPIData(array, datsql,dateFormat);
					}					
					break;
				case "API":
					if(collector.getDatatype()!=null &&collector.getDatatype().equals("valuetype")){
						StatsValue value=statsValueService.findLastestData(collector.getId());
						try{
							insertValueData(array,value,dateFormat);
						}catch(ParseException pe) {
							System.out.println("position: " + pe.getPosition());
						    System.out.println(pe);
						}		
					}else{
						WatchedData datum = watchedDataService.findLastestData(collector.getId());
						insertAPIData(array, datum,dateFormat);
					}				
					break;
				case "URL":
					WatchedUrl daturl=watchedUrlService.findLastestData(collector.getId());
					insertURLData(array, daturl,dateFormat);
					break;
				}
				
			}
		}
		writeJSON(response, array);
	}
	/**
	 * 查询某项监测产品的所有监控项
	 * @param productName	监测产品名称
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/list/collectors/{productName}")
	public void collectors(@PathVariable("productName") String productName,
			HttpServletResponse response) throws IOException {
		Product product = productService.findByName(productName);
		JSONArray array = new JSONArray();
		if (product != null) {
			List<Collector> collectors = collectorService.findAll(product
					.getId());
			for (Collector collector : collectors) {
				JSONObject json = new JSONObject();
				json.put("id", collector.getId());
				json.put("name", collector.getName());
				array.add(json);
			}
		}
		writeJSON(response, array);
	}
	/**
	 * 无参数访问url统计数据的平均值，默认返回最近10天
	 * @param productName   监测产品名称
	 * @param collectorName 监控项的名称
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/stats/{productName}/{collectorName}")
	public void statsData(@PathVariable("productName") String productName,
			@PathVariable("collectorName") String collectorName,
			HttpServletResponse response) throws IOException {
		int days=10; //无参数访问时默认返回最近10天
		Product product = productService.findByName(productName);
		JSONArray array = new JSONArray();
		JSONArray array1 = new JSONArray();
		JSONObject object=new JSONObject();	
		SimpleDateFormat dateFormat = createSimpleDateFormat();
		object.put("watchTime", dateFormat.format(new Date()));
		if (product != null) {
			Collector collector = collectorService.find(product.getId(),
					collectorName);
			if (collector != null) {
				if(collector.getReader().getType().equals("URL")){
					List<StatsUrl> data= statsUrlService.findDaysUrlData(days, collector.getId());
					for(StatsUrl datum:data){
						insertStatsURlData(array1,datum,productName,collectorName,"day");
					}
				}else if(collector.getDatatype()!=null &&collector.getDatatype().equals("valuetype")){
					List<StatsValue> statsValues=statsValueService.findDaysValueData(days, collector.getId());
					for(StatsValue statsValue:statsValues){
						try {
							insertStatsValueData(array1,statsValue,productName,collectorName,"day");
						} catch (ParseException pe) {
							System.out.println("position: " + pe.getPosition());
						    System.out.println(pe);
						}
					}
				}
			}
			object.put("data", array1);
			object.put("collectorId",collector.getId() );
		}
		array.add(object);
		writeJSON(response, array);
	}
	/**
	 * 根据天数访问stats统计数据的平均值
	 * @param productName   监测产品名称
	 * @param collectorName 监控项的名称
	 * @param days          查询url统计的天数
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/stats/{productName}/{collectorName}",params="day")
	public void statsDayData(@PathVariable("productName") String productName,
			@PathVariable("collectorName") String collectorName,
			@RequestParam("day") int days,HttpServletResponse response) throws IOException {
		Product product = productService.findByName(productName);
		JSONArray array = new JSONArray();
		JSONArray array1 = new JSONArray();
		JSONObject object=new JSONObject();	
		SimpleDateFormat dateFormat = createSimpleDateFormat();
		object.put("watchTime", dateFormat.format(new Date()));
		if (product != null) {
			Collector collector = collectorService.find(product.getId(),
					collectorName);
			if (collector != null) {
				if(collector.getReader().getType().equals("URL")){
					List<StatsUrl> data= statsUrlService.findDaysUrlData(days, collector.getId());
					for(StatsUrl datum:data){
						insertStatsURlData(array1,datum,productName,collectorName,"day");
					}
				}else if(collector.getDatatype()!=null &&collector.getDatatype().equals("valuetype")){
					List<StatsValue> statsValues=statsValueService.findDaysValueData(days, collector.getId());
					for(StatsValue statsValue:statsValues){
						try {
							insertStatsValueData(array1,statsValue,productName,collectorName,"day");
						} catch (ParseException pe) {
							System.out.println("position: " + pe.getPosition());
						    System.out.println(pe);
						}
					}
				}
			}
			object.put("data", array1);
			object.put("collectorId",collector.getId() );
		}
		array.add(object);
		writeJSON(response, array);
	}
	/**
	 * 根据月分访问stats每月统计数据的平均值
	 * @param productName   监测产品名称
	 * @param collectorName 监控项的名称
	 * @param months        查询url统计的月份数
	 * @param response
	 */
	@RequestMapping(value="/stats/{productName}/{collectorName}",params="month")
	public void statsMonthData(@PathVariable("productName") String productName,
			@PathVariable("collectorName") String collectorName,
			@RequestParam("month") int months,HttpServletResponse response)throws IOException{
		Product product = productService.findByName(productName);
		JSONArray array = new JSONArray();
		JSONArray array1 = new JSONArray();
		JSONObject object=new JSONObject();	
		SimpleDateFormat dateFormat = createSimpleDateFormat();
		object.put("watchTime", dateFormat.format(new Date()));
		if (product != null) {
			Collector collector = collectorService.find(product.getId(),
					collectorName);
			if (collector != null) {
				if(collector.getReader().getType().equals("URL")){
					List<StatsUrl> data= statsUrlService.findMonthsUrlData(months, collector.getId());
					for(StatsUrl datum:data){
						insertStatsURlData(array1,datum,productName,collectorName,"month");
					}
				}else if(collector.getDatatype()!=null &&collector.getDatatype().equals("valuetype")){
					List<StatsValue> statsValues=statsValueService.findMonthsValueData(months, collector.getId());
					for(StatsValue statsValue:statsValues){
						try {
							insertStatsValueData(array1,statsValue,productName,collectorName,"month");
						} catch (ParseException pe) {
							System.out.println("position: " + pe.getPosition());
						    System.out.println(pe);
						}
					}
				}				
			}
			object.put("data", array1);
			object.put("collectorId",collector.getId() );
		}
		array.add(object);
		writeJSON(response, array);
	}
	/**
	 * 根据月分访问stats每月统计数据的平均值
	 * @param productName   监测产品名称
	 * @param collectorName 监控项的名称
	 * @param years        查询url统计的年份数
	 * @param response
	 */
	@RequestMapping(value="/stats/{productName}/{collectorName}",params="year")
	public void statsYearData(@PathVariable("productName") String productName,
			@PathVariable("collectorName") String collectorName,
			@RequestParam("year") int years,HttpServletResponse response)throws IOException{
		Product product = productService.findByName(productName);
		JSONArray array = new JSONArray();
		JSONArray array1 = new JSONArray();
		JSONObject object=new JSONObject();	
		SimpleDateFormat dateFormat = createSimpleDateFormat();
		object.put("watchTime", dateFormat.format(new Date()));
		if (product != null) {
			Collector collector = collectorService.find(product.getId(),
					collectorName);
			if (collector != null) {
				if(collector.getReader().getType().equals("URL")){
					List<StatsUrl> data= statsUrlService.findYearsUrlData(years, collector.getId());
					for(StatsUrl datum:data){
						insertStatsURlData(array1,datum,productName,collectorName,"year");
					}
				}else if(collector.getDatatype()!=null &&collector.getDatatype().equals("valuetype")){
					List<StatsValue> statsValues=statsValueService.findYearsValueData(years, collector.getId());
					for(StatsValue statsValue:statsValues){
						try {
							insertStatsValueData(array1,statsValue,productName,collectorName,"year");
						} catch (ParseException pe) {
							System.out.println("position: " + pe.getPosition());
						    System.out.println(pe);
						}
					}
				}			
			}
			object.put("data", array1);
			object.put("collectorId",collector.getId() );
		}
		array.add(object);
		writeJSON(response, array);
	}
	/**
	 * 列出product下的所有的stats可访问的collector
	 * @param productName   监测产品名称
	 * @throws IOException 
	 */
	@RequestMapping(value="/stats/{productName}")
	public void statsCollectorList(@PathVariable("productName") String productName,HttpServletResponse response) throws IOException{
		Product product = productService.findByName(productName);
		List<Collector> collectors=collectorService.findAll(product.getId());
		JSONArray array = new JSONArray();
		for(Collector collector:collectors){
			if(collector.getReader().getType().equals("URL")){
				array.add(collector.getName());
			}else if(collector.getDatatype()!=null &&collector.getDatatype().equals("valuetype")){
				array.add(collector.getName());
			}
		}
		writeJSON(response, array);
	}
	
	/**
	 * 访问某个监测产品的所有监测值
	 * @param productName	监测产品名称
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/data/{productName}")
	public void productData(@PathVariable("productName") String productName,
			HttpServletResponse response) throws IOException, ParseException {
		Product product = productService.findByName(productName);
		SimpleDateFormat dateFormat = createSimpleDateFormat();
		JSONArray array = new JSONArray();
		if (product != null) {
			List<Collector> collectors=collectorService.findAll(product.getId());
			List<Collector> valueCollectors=new ArrayList<Collector>();
			for(int i=0;i<collectors.size();i++){
				if(collectors.get(i).getDatatype()!=null &&collectors.get(i).getDatatype().equals("valuetype"))
					valueCollectors.add(collectors.get(i));
			}
			List<WatchedData> data = watchedDataService.findAllLatestData(product.getId());			
			for (WatchedData datum : data) {
				int i=0;
				while(i<valueCollectors.size()){
					if(valueCollectors.get(i).getId()==datum.getCollectorId())
						break;
					i++;
				}
				if(i==valueCollectors.size())
					insertAPIData(array, datum,dateFormat);
			}
			List<WatchedUrl> urlData=watchedUrlService.findAllLatestData(product.getId());
			for(WatchedUrl urData:urlData){
				insertURLData(array,urData,dateFormat);
			}
			List<StatsValue> valueData=statsValueService.findAllLatestData(product.getId());
			for(StatsValue statsValue:valueData){
				insertValueData(array,statsValue,dateFormat);
			}		
		}
		writeJSON(response, array);
	}
	
	/**
	 * 查询所有的监控产品列表
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/list/products")
	public void products(HttpServletResponse response) throws IOException {
		List<Product> products = productService.readAll();
		JSONArray array = new JSONArray();
		for (Product p : products) {
			array.add(p.getName());
		}
		writeJSON(response, array);
	}
	
	/**
	 * 可视化检测项产品
	 * @param productName		产品名称
	 * @param collectorName		监测项名称
	 * @return
	 */
	@RequestMapping(value="/chart/{productName}/{collectorName}")
	public String monitorchart(@PathVariable("productName") String productName,
			@PathVariable("collectorName") String collectorName, Model model) {
		Product product = productService.findByName(productName);
		Collector collector = collectorService.find(product.getId(),
				collectorName);
		model.addAttribute("productName",productName);
		model.addAttribute("collector", collector);
		return "monitorChart";
	}
}
