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
package net.duckling.probe.service.collector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.CollectorService;
import net.duckling.probe.service.collector.SQLDataReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:root-context.xml")
public class CollectorServiceTest {
	@Resource
	private CollectorService collectorService;

	@Before
	public void setUp() {
		Collector e = createCollector();
		collectorService.save(1, e);
	}

	private Collector createCollector() {
		Collector e = new Collector();
		e.setInterval(10);
		e.setName("Test");
		SQLDataReader reader = new SQLDataReader();
		reader.setDatasourceName("本地数据源");
		reader.setSql("select * from vwb_properties");
		e.setReader(reader);
		return e;
	}

	@After
	public void tearDown() {
		collectorService.removeAll(1);
	}

	@Test
	public void testFindAll() {
		List<Collector> all = collectorService.findAll(1);
		assertNotNull(all);
		assertEquals(1, all.size());
		assertEquals("Test", all.get(0).getName());
	}

	@Test
	public void testRemove() {
		Collector collector = createCollector();
		int id=collectorService.save(1, collector);
		Collector e2=this.collectorService.find(id);
		assertNotNull(e2);
		assertEquals(10, e2.getInterval());
	}
}
