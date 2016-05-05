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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import net.duckling.probe.common.SQLUtils;
import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.SQLDataReader;
import net.duckling.probe.service.watcheddata.WatchedData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class FetchJobTest {
	private ComboPooledDataSource ds;
	@Before
	public void setUp(){
		ds = SQLUtils.makeMysqlPool("probe", "127.0.0.1", "root", "123456");
	}
	@After
	public void tearDown(){
		ds.close();
	}
	@Test
	public void test() throws Exception {
		JdbcTemplate jdbc= new JdbcTemplate(ds);
		
		Collector collector = new Collector();
		collector.setInterval(5);
		collector.setProductId(1);
		collector.setName("Count Of Page");
		SQLDataReader reader = new SQLDataReader();
		reader.setDatasourceName("本地数据源");
		reader.setSql("select count(*) groups, dataSourceId from ds_prop group by dataSourceId");
		collector.setReader(reader);
		
		
		AccessDBJob job=new AccessDBJob(jdbc,collector);
		WatchedData data = job.call();
		assertNotNull(data);
		List<Map<String, Object>> values = data.getValues();
		assertNotNull(values);
		assertNotNull(values.get(0).get("groups"));
		assertEquals(java.lang.Integer.class,values.get(0).get("groups").getClass());
	}
}
