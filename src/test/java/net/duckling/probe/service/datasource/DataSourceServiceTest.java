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
package net.duckling.probe.service.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import net.duckling.probe.service.datasource.DataSource;
import net.duckling.probe.service.datasource.DataSourceService;
import net.duckling.probe.service.datasource.DataSourceType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:root-context.xml")
public class DataSourceServiceTest {
    @Resource
    private DataSourceService dataSourceService;
    @Before
    public void setUp(){
		DataSource dataSource= new DataSource();
		dataSource.setProductId(1);
		dataSource.setName("数据库");
		dataSource.setType(DataSourceType.database);
		dataSource.setProperty("database.ip", "localhost");
		dataSourceService.save(dataSource);
    }
    @After
    public void tearDown(){
		dataSourceService.remove(1, "数据库");
    }
	@Test
	public void testFind() {
		DataSource dataSource = dataSourceService.find(1, "数据库");
		assertNotNull(dataSource);
		assertEquals("localhost",dataSource.getProperties().get("database.ip"));
	}

	@Test
	public void testFindDataSources() {
		List<DataSource> datasources = dataSourceService.findDataSources(1);
		assertNotNull(datasources);
	}
}
