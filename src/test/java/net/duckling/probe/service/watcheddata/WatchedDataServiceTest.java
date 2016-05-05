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
package net.duckling.probe.service.watcheddata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:root-context.xml")
public class WatchedDataServiceTest {
	@Resource
	private WatchedDataService watchedDataService;
	private WatchedData createWatchedData(int collectorId) {
		WatchedData datum = new WatchedData();
		datum.setCollectorId(collectorId);
		datum.setProductId(1);
		datum.setWatchTime(new Date());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("totalSocer", "100");
		datum.addValue(map);
		return datum;
	}

	@Before
	public void setUp() throws InterruptedException {
		List<WatchedData> data = new ArrayList<WatchedData>();
		for (int i=0;i<10;i++){
			for (int collectorId=1;collectorId<=100;collectorId++){
				data.add(createWatchedData(collectorId));
			}
		}
		watchedDataService.save(data);
	}

	@After
	public void tearDown() {
		this.watchedDataService.removeAll(1);
	}

	@Test
	public void testFindLatest() {
		List<WatchedData> data = watchedDataService.findAllLatestData(1);
		assertNotNull(data);
		assertEquals(100, data.size());
		
		WatchedData datum = watchedDataService.findLastestData(1);
		assertNotNull(datum);
	}
}
