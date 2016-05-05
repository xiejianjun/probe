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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.probe.service.collector.APIDataReader;
import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.CollectorService;
import net.duckling.probe.service.collector.SQLDataReader;
import net.duckling.probe.service.collector.URLDataReader;
import net.duckling.probe.service.datasource.DataSource;
import net.duckling.probe.service.datasource.DataSourceService;
import net.duckling.probe.service.datasource.DataSourceType;
import net.duckling.probe.service.product.ProductService;
import net.duckling.probe.service.watcheddata.WatchedDataService;
import net.duckling.probe.service.watchedurl.WatchedUrlService;
import net.duckling.probe.ui.permission.annotation.RequirePermission;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 管理监控项
 * @author xiejj@cnic.cn
 *
 */
@Controller
@RequestMapping("/collector.do")
@RequirePermission(operation="admin")
public class CollectorController {

	private static final String PARAM_PRODUCT_ID = "productId";

	private static final String REDRECT_URI = "redirect:collector.do?productId=";

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private CollectorService collectorService;

	@Autowired
	private WatchedDataService watchedDataService;
	
	@Autowired
	private WatchedUrlService watchedUrlService;
	
	@Autowired
	private ProductService productService;
	/**
	 * 添加数据源
	 * @param pid		产品的ID
	 * @param name		数据源名称
	 * @param host		数据源的主机
	 * @param database	访问的数据库
	 * @param user		数据库用户
	 * @param password	数据库密码
	 * @return
	 */
	@RequestMapping(params = "act=addDataSource")
	public String addDataSource(@RequestParam(PARAM_PRODUCT_ID) int pid,
			@RequestParam("name") String name,
			@RequestParam("host") String host,
			@RequestParam("database") String database,
			@RequestParam("user") String user,
			@RequestParam("password") String password) {
		DataSource dataSource = new DataSource();
		dataSource.setName(name);
		dataSource.setProductId(pid);
		dataSource.setProperty("host", host);
		dataSource.setProperty("database", database);
		dataSource.setProperty("user", user);
		dataSource.setProperty("password", password);
		dataSource.setType(DataSourceType.database);
		dataSourceService.save(dataSource);
		return REDRECT_URI + pid;
	}
	/**
	 * 删除数据源
	 * @param productId		产品ID
	 * @param datasourceId	数据源ID
	 * @return
	 */
	@RequestMapping(params = "act=delDataSource")
	public String delDataSource(@RequestParam(PARAM_PRODUCT_ID) int productId,
			@RequestParam("datasourceId") int datasourceId) {
		dataSourceService.remove(datasourceId);
		return REDRECT_URI + productId;
	}
	/**
	 * 装载监测项的信息
	 * @param collectorId	监测项的ID
	 * @param productId		产品ID
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "act=getCollector", method = RequestMethod.GET)
	public void getCollector(@RequestParam("collectorId") int collectorId,@RequestParam(PARAM_PRODUCT_ID)int productId,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject object=new JSONObject();	
		object.put("productId", productId);
		if(!dataSourceService.findDataSources(productId).isEmpty()){
			JSONArray array = new JSONArray();
			for(DataSource dataSource : dataSourceService.findDataSources(productId)){
				array.add(dataSource.getName());
			}
			object.put("datasource", array);
		}		
		Collector collector=collectorService.find(collectorId);
		object.put("collectorId", collector.getId());
		object.put("interval", collector.getInterval());
		object.put("intervalDesc",collector.getIntervalDesc() );
		object.put("name", collector.getName());
		object.put("description", collector.getDescription());
		object.put("datasourceName", collector.getReader().getDatasourceName());
		object.put("jsonData", collector.getReader().toJSON());
		object.put("productId", collector.getProductId());
		object.put("type", collector.getReader().getType());
		try {
	        //设置页面不缓存
	        response.setContentType("application/json");
	        response.setHeader("Pragma", "No-cache");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out= null;
	        out = response.getWriter();
	        out.print(object.toJSONString());
	        out.flush();
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }		
	}
	
	/**
	 * 保存监测项修改结果
	 * @param productId			产品ID
	 * @param collectorId		监测项ID
	 * @param name				监测项名称
	 * @param sql				监测SQL
	 * @param interval			执行监测的时间间隔
	 * @param datasourceName	使用的数据源
	 * @return
	 */
	@RequestMapping(params = "act=modifyCollector")
	public String modifyCollector(@RequestParam(PARAM_PRODUCT_ID) int productId,
			@RequestParam("collectorId") int collectorId,
			@RequestParam("name") String name, @RequestParam("sql") String sql,
			@RequestParam("interval") int interval,
			@RequestParam("description") String description,
			@RequestParam("datasourceName") String datasourceName) {
		//Collector collector = collectorService.find(productId, name);
		Collector collector=collectorService.find(collectorId);
		if (collector != null) {
			collector = new Collector();
			collector.setId(collectorId);
			collector.setInterval(interval);
			collector.setName(name);
			collector.setDescription(description);
			collector.setProductId(productId);
			SQLDataReader reader = new SQLDataReader();
			reader.setDatasourceName(datasourceName);
			reader.setSql(sql);
			collector.setReader(reader);
			collectorService.update(productId, collector);
		}
		return REDRECT_URI + productId;
	}
	@RequestMapping(params = "act=modifyAPICollector")
	public String modifyAPICollector(@RequestParam(PARAM_PRODUCT_ID) int productId,
			@RequestParam("collectorId") int collectorId,
			@RequestParam("name") String name, @RequestParam("url") String url,
			@RequestParam("interval") int interval,
			@RequestParam("description") String description,
			@RequestParam("encode") String encode) {
		//Collector collector = collectorService.find(productId, name);
		Collector collector=collectorService.find(collectorId);
		if (collector != null) {
			collector = new Collector();
			collector.setId(collectorId);
			collector.setInterval(interval);
			collector.setName(name);
			collector.setDescription(description);
			collector.setProductId(productId);
			APIDataReader reader = new APIDataReader();
			reader.setUrl(url);
			reader.setEncode(encode);
			collector.setReader(reader);
			collectorService.update(productId, collector);
		}
		return REDRECT_URI + productId;
	}
	@RequestMapping(params = "act=modifyURLCollector")
	public String modifyURLCollector(@RequestParam(PARAM_PRODUCT_ID) int productId,
			@RequestParam("collectorId") int collectorId,
			@RequestParam("name") String name, @RequestParam("url") String url,
			@RequestParam("interval") int interval,
			@RequestParam("email") String email,
			@RequestParam("description") String description,
			@RequestParam("encode") String encode,@RequestParam("method") String method ) {
		//Collector collector = collectorService.find(productId, name);
		Collector collector=collectorService.find(collectorId);
		if (collector != null) {
			collector = new Collector();
			collector.setId(collectorId);
			collector.setInterval(interval);
			collector.setName(name);
			collector.setDescription(description);
			collector.setProductId(productId);
			URLDataReader reader = new URLDataReader();
			reader.setUrl(url);
			reader.setMethod(method);
			reader.setEncode(encode);
			reader.setEmail(email);
			collector.setReader(reader);
			collectorService.update(productId, collector);
		}
		return REDRECT_URI + productId;
	}
	/**
	 * 添加监测项
	 * @param productId			产品ID
	 * @param name				监测项名称
	 * @param sql				监测SQL
	 * @param interval			执行监测的时间间隔
	 * @param datasourceName	使用的数据源名称
	 * @return
	 */
	@RequestMapping(params = "act=addDBCollector")
	public String addSQLCollector(@RequestParam(PARAM_PRODUCT_ID) int productId,
			@RequestParam("name") String name, @RequestParam("sql") String sql,
			@RequestParam("interval") int interval,
			@RequestParam("description") String description,
			@RequestParam("datasourceName") String datasourceName) {
		Collector collector = collectorService.find(productId, name);
		if (collector == null) {
			collector = new Collector();
			collector.setInterval(interval);
			collector.setName(name);
			collector.setDescription(description);
			collector.setProductId(productId);
			SQLDataReader reader = new SQLDataReader();
			reader.setDatasourceName(datasourceName);
			reader.setSql(sql);
			collector.setReader(reader);
			collectorService.save(productId, collector);
		}

		return REDRECT_URI + productId;
	}
	/**
	 * 添加API监测项
	 * @param productId			产品ID
	 * @param name				监测项名称
	 * @param url				监测url
	 * @param interval			执行监测的时间间隔
	 * @param datasourceName	使用的数据源名称
	 * @return
	 */
	@RequestMapping(params = "act=addAPICollector")
	public String addAPICollector(@RequestParam(PARAM_PRODUCT_ID) int productId,
			@RequestParam("name") String name, @RequestParam("url") String url,
			@RequestParam("description") String description,
			@RequestParam("encode") String encode, @RequestParam("interval") int interval) {
		Collector collector = collectorService.find(productId, name);
		if (collector == null) {
			collector = new Collector();
			collector.setInterval(interval);
			collector.setName(name);
			collector.setDescription(description);
			collector.setProductId(productId);
			APIDataReader reader = new APIDataReader();
			reader.setDatasourceName("");
			reader.setUrl(url);
			reader.setEncode(encode);
			collector.setReader(reader);
			collectorService.save(productId, collector);
		}

		return REDRECT_URI + productId;
	}

	
	/**
	 * 添加URL监测项
	 * @param productId			产品ID
	 * @param name				监测项名称
	 * @param url				监测url
	 * @param interval			执行监测的时间间隔
	 * @param datasourceName	使用的数据源名称
	 * @return
	 */
	@RequestMapping(params = "act=addURLCollector")
	public String addURLCollector(@RequestParam(PARAM_PRODUCT_ID) int productId,
			@RequestParam("name") String name, @RequestParam("url") String url,
			@RequestParam("description") String description,
			@RequestParam("encode") String encode, @RequestParam("interval") int interval,
			@RequestParam("email") String email,
			@RequestParam("method") String method ) {
		Collector collector = collectorService.find(productId, name);
		if (collector == null) {
			collector = new Collector();
			collector.setInterval(interval);
			collector.setName(name);
			collector.setDescription(description);
			collector.setProductId(productId);
			URLDataReader reader = new URLDataReader();
			reader.setDatasourceName("");
			reader.setUrl(url);
			reader.setMethod(method);
			reader.setEncode(encode);
			reader.setEmail(email);
			collector.setReader(reader);
			collectorService.save(productId, collector);
		}
		return REDRECT_URI + productId;
	}

	/**
	 * 删除监测项
	 * @param productId		产品ID
	 * @param collectorId	监测项ID
	 * @return
	 */
	@RequestMapping(params = "act=delCollector")
	public String delCollector(@RequestParam(PARAM_PRODUCT_ID) int productId,
			@RequestParam("collectorId") int collectorId) {
		collectorService.remove(collectorId);
		watchedDataService.removeCollectorData(collectorId);
		watchedUrlService.removeCollectorData(collectorId);
		return REDRECT_URI + productId;
	}
	
	/**
	 * 展示监测产品的所有监测项清单
	 * @param productId		产品ID
	 * @param model			
	 * @return
	 */
	@RequestMapping
	public String home(@RequestParam(PARAM_PRODUCT_ID) int productId, Model model) {
		model.addAttribute(PARAM_PRODUCT_ID, productId);
		model.addAttribute("productname",productService.read(productId).getName());
		model.addAttribute("datasources",
				dataSourceService.findDataSources(productId));
		model.addAttribute("collectors", collectorService.findAll(productId));
		return "collector";
	}
}
