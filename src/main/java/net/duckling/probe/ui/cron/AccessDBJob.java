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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.SQLDataReader;
import net.duckling.probe.service.watcheddata.WatchedData;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AccessDBJob implements Job{
	private JdbcTemplate jdbc;
	private String sql;
	private Collector collector;

	private class WatchedDataExtractor implements
			ResultSetExtractor<WatchedData> {
		private Date watchTime = new Date();

		private boolean isInteger(String className) {
			return ("java.lang.Long".equals(className) || "java.lang.Integer"
					.equals(className));
		}

		@Override
		public WatchedData extractData(ResultSet rs) throws SQLException{
			ResultSetMetaData metaData = rs.getMetaData();
			WatchedData datum = new WatchedData();
			datum.setProductId(collector.getProductId());
			datum.setCollectorId(collector.getId());
			datum.setWatchTime(watchTime);
			while (rs.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					if (isInteger(metaData.getColumnClassName(i))) {
						map.put(metaData.getColumnName(i), rs.getInt(i));
					} else {
						map.put(metaData.getColumnName(i),
								rs.getString(i));
					}
				}
				if (map.size()>0){
					datum.addValue(map);
				}
			}
			return datum;
		}
	};

	public AccessDBJob(JdbcTemplate jdbc, Collector exhibits) {
		this.jdbc = jdbc;
		SQLDataReader reader = (SQLDataReader) exhibits.getReader();
		this.sql = reader.getSql();
		this.collector = exhibits;
	}
	
	@Override
	public WatchedData call(){
		WatchedDataExtractor extractor = new WatchedDataExtractor();
		return jdbc.query(sql, extractor);
	}
}