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

import java.util.ArrayList;
import java.util.List;

import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.CollectorService;
import net.duckling.probe.service.collector.SQLDataReader;
import net.duckling.probe.service.watcheddata.WatchedData;
import net.duckling.probe.service.watcheddata.WatchedDataService;
import net.duckling.probe.service.watchedurl.WatchedUrl;
import net.duckling.probe.service.watchedurl.WatchedUrlService;

import org.apache.log4j.Logger;

public class CronTask {
	private static final Logger LOG = Logger.getLogger(CronTask.class);
	private CronContext crontContext;
	private CollectorService collectorService;
	private int interval;
	private WatchedDataService watchDataService;
	private WatchedUrlService watchUrlService;
	private Job makeJob(Collector collector) {
		Job job = null;
		switch (collector.getReader().getType()) {
		case "SQL":
			SQLDataReader reader = (SQLDataReader) collector.getReader();
			if (reader != null) {
				String datasourceName = reader.getDatasourceName();
				try{
					job = new AccessDBJob(crontContext.lookupDataSource(
							collector.getProductId(), datasourceName), collector);
				}catch(Exception e){
					e.printStackTrace();
					return null;
				}				
			}
			break;
		case "API":
			job = new AccessAPIJob(collector);
			break;
		case "URL":
			job=new AccessURLJob(collector);
			break;
		default:
			job=null;
		}
		return job;
	}

	public void doCollect() {
		LOG.info(interval+"分钟定时任务开始执行");
		List<Collector> collectors = collectorService.findShouldCollect(interval);
		ArrayList<WatchedData> watchedData = new ArrayList<WatchedData>();
		ArrayList<WatchedUrl> watchedUrl = new ArrayList<WatchedUrl>();
		for (Collector collector : collectors) {
			Job job = makeJob(collector);
			switch(collector.getReader().getType()){
			case "SQL":
			case "API":
				try {
					if (job!=null){
						WatchedData datum = (WatchedData) job.call();
						if (datum != null) {
							watchedData.add(datum);
						}
					}
				} catch (Throwable e) {
					LOG.error("执行定期任务失败", e);
				}
				break;
			case "URL":
				WatchedUrl datum=null;
				try {
					if (job!=null){
						datum = (WatchedUrl) job.call();						
					}
				} catch (Throwable e) {
					LOG.error("执行定期任务失败", e);
				}finally{
					if (datum != null) {
						watchedUrl.add(datum);
					}
				}
				break;
			}
			
		}
		watchDataService.save(watchedData);
		watchUrlService.save(watchedUrl);
		LOG.info(interval+"分钟定时任务执行完成");
	}

	public void setCronContext(CronContext cronContext) {
		this.crontContext = cronContext;
	}

	public void setCollectorService(CollectorService collectorService) {
		this.collectorService = collectorService;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setWatchedDataService(WatchedDataService watchDataService) {
		this.watchDataService = watchDataService;
	}

	public void setWatchedUrlService(WatchedUrlService watchUrlService) {
		this.watchUrlService = watchUrlService;
	}

}
