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
package net.duckling.probe.service.watchedurl.impl;

import java.util.List;

import net.duckling.probe.service.watchedurl.WatchedUrl;
import net.duckling.probe.service.watchedurl.WatchedUrlService;

public class WatchedUrlServiceImpl implements WatchedUrlService {
	private WatchedUrlDao watchedUrlDao;
	public void setWatchedUrlDao(WatchedUrlDao watchedUrlDao) {
		this.watchedUrlDao = watchedUrlDao;
	}

	@Override
	public List<WatchedUrl> findAllLatestData(int productId) {
		// TODO Auto-generated method stub				
		return watchedUrlDao.findAllLatestData(productId);
	}

	@Override
	public WatchedUrl findLastestData(int collectorId) {
		// TODO Auto-generated method stub
		return watchedUrlDao.findLatestData(collectorId);
	}

	@Override
	public void save(List<WatchedUrl> data) {
		// TODO Auto-generated method stub
		watchedUrlDao.save(data);
		
	}

	@Override
	public void removeAll(int productId) {
		// TODO Auto-generated method stub
		watchedUrlDao.removeAll(productId);
		
	}

	@Override
	public void removeCollectorData(int collectorId) {
		// TODO Auto-generated method stub
		watchedUrlDao.removeCollectorData(collectorId);
		
	}

}
