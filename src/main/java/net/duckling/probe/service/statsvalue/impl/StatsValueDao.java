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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import net.duckling.probe.common.BaseDao;
import net.duckling.probe.service.statsvalue.StatsValue;
import net.duckling.probe.service.watcheddata.WatchedData;

import org.springframework.jdbc.core.RowMapper;

public class StatsValueDao extends BaseDao{
	private RowMapper<StatsValue> rowMapper = new RowMapper<StatsValue>() {
		@Override
		public StatsValue mapRow(ResultSet rs, int rowNum) throws SQLException {
			StatsValue w = new StatsValue();
			w.setCollectorId(rs.getInt("collectorId"));
			w.setProductId(rs.getInt("productId"));
			w.setCalcTime(rs.getDate("calcTime"));
			w.setData(rs.getString("data"));
			return w;
		}
	};
	private RowMapper<StatsValue> dataRowMapper = new RowMapper<StatsValue>() {
		@Override
		public StatsValue mapRow(ResultSet rs, int rowNum) throws SQLException {
			StatsValue w = new StatsValue();
			w.setCollectorId(rs.getInt("collectorId"));
			w.setProductId(rs.getInt("productId"));
			w.setCalcTime(rs.getTimestamp("watch_time"));
			w.setData(rs.getString("data"));
			return w;
		}
	};
	public List<StatsValue> findDaysValueData(int days,int collectorId){
		String sql="select productId,collectorId,data,a.calcTime from calc_value_data as a join(select max(id) as id from calc_value_data where calcTime>=(select curdate() - interval ? day) and collectorId=? group by calcTime) as b where a.id=b.id ;";		
		return getJdbcTemplate().query(sql, new Object[] { days, collectorId},	rowMapper);
	}
	
	public List<StatsValue> findMonthsValueData(int months,int collectorId){
		String sql="select productId,collectorId,data,a.calcTime from calc_value_data as a join (select max(id) as id from calc_value_data where collectorId=? AND calcTime>=(select (curdate() - interval ? month) -interval 1 day) group by date_format(calcTime,'%y-%m') ) as b where a.id=b.id;";		
		return getJdbcTemplate().query(sql, new Object[] { collectorId,months-1},	rowMapper);
	}
	public List<StatsValue> findYearsValueData(int years,int collectorId){
		String sql="select productId,collectorId,data,a.calcTime from calc_value_data as a join (select max(id) as id from calc_value_data where collectorId=? AND calcTime>=(select (curdate() - interval ? year) -interval 1 day) group by date_format(calcTime,'%y') ) as b where a.id=b.id;";		
		return getJdbcTemplate().query(sql, new Object[] { collectorId,years-1},	rowMapper);
	}
	public StatsValue findLastestData(int collectorId){
		String sql = "select * from value_watch_data where collectorId=? order by id desc limit 1";
		List<StatsValue> data = getJdbcTemplate().query(sql,
				new Object[] { collectorId }, dataRowMapper);
		if (data.size() > 0) {
			return data.get(0);
		} else {
			return null;
		}
	}
	public List<StatsValue> findAllLatestData(int productId) {
		// TODO Auto-generated method stub
		String sql ="select w.* from value_watch_data w,(select max(id) id from value_watch_data where productId=? group by collectorId) v where w.id=v.id";
		return getJdbcTemplate().query(sql, new Object[] { productId },
				dataRowMapper);
	}
}
