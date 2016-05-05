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
package net.duckling.probe.service.collector.impl;

import java.util.List;

import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.CollectorService;

public class CollectorServiceImpl implements CollectorService {
	private CollectorDAO collectorDao;

	public void setCollectorDao(CollectorDAO collectorDao) {
		this.collectorDao = collectorDao;
	}

	@Override
	public int save(int productId, Collector probe) {
		return collectorDao.save(productId, probe);
	}

	@Override
	public List<Collector> findAll(int productId) {
		return collectorDao.findAll(productId);
	}

	@Override
	public void remove(int probeId) {
		collectorDao.remove(probeId);
	}

	@Override
	public void removeAll(int productId) {
		collectorDao.removeAll(productId);
	}

	@Override
	public Collector find(int probeId) {
		return collectorDao.find(probeId);
	}

	@Override
	public List<Collector> findShouldCollect(int interval) {
		return collectorDao.findShouCollect(interval);
	}

	@Override
	public Collector find(int productId, String name) {
		return collectorDao.find(productId, name);
	}

	@Override
	public void update(int productId, Collector collector) {
		collectorDao.update(productId, collector);
	}
}
