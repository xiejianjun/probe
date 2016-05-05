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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.duckling.probe.common.SQLUtils;
import net.duckling.probe.service.datasource.DataSource;
import net.duckling.probe.service.datasource.DataSourceService;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class CronContext {
	private DataSourceService dataSourceService;
	private Map<String, Object> variables;

	public void setDataSourceService(DataSourceService dataSourceService) {
		this.dataSourceService = dataSourceService;
	}
	private String makeDataSourceKey(int productId, String datasourceName){
		return String.format("%d.%s", productId, datasourceName);
	}
	public JdbcTemplate lookupDataSource(int productId,String datasourceName) {
		String key = makeDataSourceKey(productId, datasourceName);
		ComboPooledDataSource comboDataSource = (ComboPooledDataSource) variables
				.get(key);
		if (comboDataSource == null) {
			DataSource ds = dataSourceService.find(productId, datasourceName);
			Map<String, String> properties = ds.getProperties();
			comboDataSource = SQLUtils.makeMysqlPool(
					properties.get("database"), properties.get("host"),
					properties.get("user"), properties.get("password"));
			variables.put(key, comboDataSource);
		}

		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(comboDataSource);
		return jdbcTemplate;
	}

	public CronContext() {
		variables = Collections.synchronizedMap(new HashMap<String, Object>());
	}

	public void close() {
		for (Entry<String, Object> entry : variables.entrySet()) {
			if (entry.getValue() instanceof ComboPooledDataSource) {
				ComboPooledDataSource jdbc = (ComboPooledDataSource) entry
						.getValue();
				jdbc.close();
			}
		}
	}
}
