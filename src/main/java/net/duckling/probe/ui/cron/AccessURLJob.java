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
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.URLDataReader;
import net.duckling.probe.service.mailalert.impl.AlertServiceImpl;
import net.duckling.probe.service.watchedurl.WatchedUrl;

public class AccessURLJob implements Job{

	private Collector collector;
	//private String encode;
	private String method;
	private String url;
	private static AlertServiceImpl alertServiceImpl;
	static{
		alertServiceImpl=new AlertServiceImpl();
	}
	public AccessURLJob(Collector collector){
		this.collector = collector;
		URLDataReader reader = (URLDataReader) collector.getReader();
		url = reader.getUrl();
		//this.encode = reader.getEncode();
		this.method=reader.getMethod();
	}
	
	@SuppressWarnings("finally")
	@Override
	public WatchedUrl call() throws JobException {
		// TODO Auto-generated method stub
		WatchedUrl watchedUrl=null;
		try {			
			watchedUrl=HttpTest();			
		}catch (IOException e) {
			throw new JobException("执行访问URL(" + url + ")的任务失败", e);
		}finally{
			return watchedUrl;
		}
		
	}
	
	@SuppressWarnings("finally")
	public WatchedUrl HttpTest() throws ClientProtocolException, IOException{
		WatchedUrl watchedUrl=new WatchedUrl();		 
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		long start = System.currentTimeMillis(); 
		try{
			switch(method){
			case "GET":
				HttpGet httpget = new HttpGet(url);
			    response = httpclient.execute(httpget);
			    break;
			case "POST":
				HttpPost httppost=new HttpPost(url);
				response = httpclient.execute(httppost);
				break;
			case "HEAD":
				HttpHead httphead=new HttpHead(url);
				response = httpclient.execute(httphead);
				break;	
			}			
		}finally{
			int responseTime=(int)(System.currentTimeMillis()-start);
			if(response!=null){
				watchedUrl.setStatusCode(response.getStatusLine().getStatusCode());				
				if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==302){
					watchedUrl.setAvailability(true);
					alertServiceImpl.conutSuccess(collector);
				}else{
					watchedUrl.setAvailability(false);
					alertServiceImpl.countFail(collector);
				}
				
			}else{
				watchedUrl.setAvailability(false);
				alertServiceImpl.countFail(collector);
			}
			watchedUrl.setResponseTime(responseTime);			
			watchedUrl.setCollectorId(collector.getId());
			watchedUrl.setProductId(collector.getProductId());
			watchedUrl.setMethod(method);
			watchedUrl.setWatchTime(new Date());			
			httpclient.close();
			return watchedUrl;
		}
		
	}

}
