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
package net.duckling.probe.service.statsvalue.impl;

import java.util.List;

import net.duckling.probe.service.statsvalue.StatsValue;
import net.duckling.probe.service.statsvalue.StatsValueService;

public class StatsValueServiceImpl implements StatsValueService{

	private StatsValueDao statsValueDao;
	
	
	public void setStatsValueDao(StatsValueDao statsValueDao) {
		this.statsValueDao = statsValueDao;
	}

	@Override
	public List<StatsValue> findDaysValueData(int days, int collectorId) {
		// TODO Auto-generated method stub
		return statsValueDao.findDaysValueData(days, collectorId);
	}

	@Override
	public List<StatsValue> findMonthsValueData(int months, int collectorId) {
		// TODO Auto-generated method stub
		return statsValueDao.findMonthsValueData(months, collectorId);
	}

	@Override
	public List<StatsValue> findYearsValueData(int years, int collectorId) {
		// TODO Auto-generated method stub
		return statsValueDao.findYearsValueData(years, collectorId);
	}

	@Override
	public List<StatsValue> findAllLatestData(int productId) {
		// TODO Auto-generated method stub
		return statsValueDao.findAllLatestData(productId);
	}

	@Override
	public StatsValue findLastestData(int collectorId) {
		// TODO Auto-generated method stub
		return statsValueDao.findLastestData(collectorId);
	}

}
