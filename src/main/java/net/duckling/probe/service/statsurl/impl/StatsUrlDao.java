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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import net.duckling.probe.common.BaseDao;
import net.duckling.probe.service.statsurl.StatsUrl;

public class StatsUrlDao extends BaseDao {
	private RowMapper<StatsUrl> rowMapper = new RowMapper<StatsUrl>() {
		@Override
		public StatsUrl mapRow(ResultSet rs, int rowNum) throws SQLException {
			StatsUrl w = new StatsUrl();
			w.setCollectorId(rs.getInt("collectorId"));
			w.setProductId(rs.getInt("productId"));
			w.setAvgTime(rs.getFloat("avgTime"));
			w.setCalcTime(rs.getDate("calcTime"));
			w.setHealth(rs.getFloat("health"));
			w.setMethod(rs.getString("method"));
			return w;
		}
	};
	
	public List<StatsUrl> findDaysUrlData(int days,int collectorId){
		String sql="select * from calc_url_data where calcTime>=(select curdate() - interval ? day) and collectorId=?;";		
		return getJdbcTemplate().query(sql, new Object[] { days, collectorId},	rowMapper);
	}
	
	public List<StatsUrl> findMonthsUrlData(int months,int collectorId){
		String sql="select productId,collectorId,avg(health) as health,avg(avgTime) as avgTime,method,max(calcTime) as calcTime from calc_url_data where calcTime>=(select date_add(curdate() - interval ? month, interval - day(curdate()) + 1 day)) and collectorId=? group by date_format(calcTime,'%y-%m');";
		
		return getJdbcTemplate().query(sql, new Object[]{months-1,collectorId},rowMapper);	 
	}
	
	public List<StatsUrl> findYearsUrlData(int years,int collectorId){
		String sql="select productId,collectorId,avg(health) as health,avg(avgTime) as avgTime,method,max(calcTime) as calcTime from calc_url_data where calcTime>=(select DATE_ADD(CURDATE()-interval ? year,INTERVAL -dayofyear(now())+1 DAY)) and collectorId=? group by date_format(calcTime,'%y');";
		return getJdbcTemplate().query(sql, new Object[]{years-1,collectorId},rowMapper);	
	}
}
