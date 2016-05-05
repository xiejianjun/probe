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
package net.duckling.probe.service.watcheddata.impl;

import java.util.List;

import net.duckling.probe.service.watcheddata.WatchedData;
import net.duckling.probe.service.watcheddata.WatchedDataService;

public class WatchedDataServiceImpl implements WatchedDataService {
	private WatchedDataDAO watchedDataDao;

	public void setWatchedDataDao(WatchedDataDAO dao) {
		this.watchedDataDao = dao;
	}

	@Override
	public List<WatchedData> findAllLatestData(int productId) {
		return watchedDataDao.findAllLatestData(productId);
	}

	@Override
	public WatchedData findLastestData(int collectorId) {
		return watchedDataDao.findLatestData(collectorId);
	}

	@Override
	public void save(List<WatchedData> data) {
		watchedDataDao.save(data);
	}

	@Override
	public void removeAll(int productId) {
		watchedDataDao.removeAll(productId);
	}

	@Override
	public void removeCollectorData(int collectorId) {
		watchedDataDao.removeCollectorData(collectorId);
	}
}
