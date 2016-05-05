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
package net.duckling.probe.service.statsurl.impl;

import java.util.List;

import net.duckling.probe.service.statsurl.StatsUrl;
import net.duckling.probe.service.statsurl.StatsUrlService;

public class StatsUrlServiceImpl implements StatsUrlService {

	private StatsUrlDao statsUrlDao;
	
	public void setStatsUrlDao(StatsUrlDao statsUrlDao) {
		this.statsUrlDao = statsUrlDao;
	}

	@Override
	public List<StatsUrl> findDaysUrlData(int days, int collectorId) {
		// TODO Auto-generated method stub
		return statsUrlDao.findDaysUrlData(days, collectorId);
	}

	@Override
	public List<StatsUrl> findMonthsUrlData(int months, int collectorId) {
		// TODO Auto-generated method stub
		return statsUrlDao.findMonthsUrlData(months, collectorId);
	}

	@Override
	public List<StatsUrl> findYearsUrlData(int years, int collectorId) {
		// TODO Auto-generated method stub
		return statsUrlDao.findYearsUrlData(years, collectorId);
	}


}
