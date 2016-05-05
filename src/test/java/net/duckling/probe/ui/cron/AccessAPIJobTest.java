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

import static org.junit.Assert.*;
import net.duckling.probe.service.collector.APIDataReader;
import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.watcheddata.WatchedData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccessAPIJobTest {
	private Collector collector;
	@Before
	public void setUp(){
		collector = new Collector();
		collector.setId(1);
		collector.setInterval(1);
		collector.setName("API");
		collector.setProductId(1);
		APIDataReader reader = new APIDataReader();
		reader.setDatasourceName("");
		reader.setEncode("UTF-8");
		reader.setUrl("http://localhost/probe/stat");
		collector.setReader(reader);
	}
	@After
	public void tearDown(){
		collector=null;
	}
	@Test
	public void test() throws JobException {
		AccessAPIJob job = new AccessAPIJob(collector);
		WatchedData data = job.call();
		assertNotNull(data);
		assertNotNull(data.getValues());
		assertTrue(data.getValues().size()>0);
		assertNotNull(data.getValues().get(0).get("Users"));
	}

}
