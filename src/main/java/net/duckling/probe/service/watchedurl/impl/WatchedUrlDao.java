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
package net.duckling.probe.service.watchedurl.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;



import net.duckling.probe.common.BaseDao;
import net.duckling.probe.service.watchedurl.WatchedUrl;


public class WatchedUrlDao extends BaseDao {
	
	private RowMapper<WatchedUrl> rowMapper = new RowMapper<WatchedUrl>() {
		@Override
		public WatchedUrl mapRow(ResultSet rs, int rowNum) throws SQLException {
			WatchedUrl w = new WatchedUrl();
			w.setCollectorId(rs.getInt("collectorId"));
			w.setProductId(rs.getInt("productId"));
			w.setWatchTime(rs.getTimestamp("watch_time"));
			w.setAvailability((rs.getShort("availability")==1));
			w.setStatusCode(rs.getShort("statuscode"));
			w.setResponseTime(rs.getInt("responsetime"));
			w.setMethod(rs.getString("method"));
			return w;
		}
	};
	
	public List<WatchedUrl> findAllLatestData(int productId) {
		String sql="select * from url_watch_data u,(select max(id) id from url_watch_data where productId=? group by collectorId) m where u.id=m.id; ";
			
		return getJdbcTemplate().query(sql, new Object[] { productId },	rowMapper);
	}
	
	
	public WatchedUrl findLatestData(int collectorId) {
		String sql="select * from url_watch_data where collectorId=? order by id desc limit 1;";
		List<WatchedUrl> data = getJdbcTemplate().query(sql,new Object[] { collectorId }, rowMapper);
		if (data.size() > 0) {
			return data.get(0);
		} else {
			return null;
		}
	}

	public void save(List<WatchedUrl> data) {
		String sql = "insert into url_watch_data(productId, collectorId, watch_time, availability, statuscode, responsetime, method) values(?,?,?,?,?,?,?)";
		List<Object[]> args = new ArrayList<Object[]>();
		for (WatchedUrl w : data) {
			args.add(new Object[] { w.getProductId(), w.getCollectorId(),new Timestamp(w.getWatchTime().getTime()),
					 w.getAvailability(),w.getStatusCode(),w.getResponseTime(),w.getMethod()});
		}
		getJdbcTemplate().batchUpdate(sql, args);
	}

	public void removeAll(int productId) {
		String sql = "delete from url_watch_data where productId=?";
		getJdbcTemplate().update(sql, new Object[]{productId});
	}

	public void removeCollectorData(int collectorId) {
		String sql = "delete from url_watch_data where collectorId=?";
		getJdbcTemplate().update(sql, new Object[]{collectorId});
	}
}
