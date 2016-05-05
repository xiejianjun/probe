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
package net.duckling.probe.service.watcheddata.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.duckling.probe.common.BaseDao;
import net.duckling.probe.service.watcheddata.WatchedData;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.jdbc.core.RowMapper;

public class WatchedDataDAO extends BaseDao {
	private static final Logger LOG=Logger.getLogger(WatchedDataDAO.class);
	private RowMapper<WatchedData> rowMapper = new RowMapper<WatchedData>() {
		@Override
		public WatchedData mapRow(ResultSet rs, int rowNum) throws SQLException {
			WatchedData w = new WatchedData();
			w.setCollectorId(rs.getInt("collectorId"));
			w.setProductId(rs.getInt("productId"));
			w.setWatchTime(rs.getTimestamp("watch_time"));
			try {
				w.parseValues(rs.getString("data"));
			} catch (ParseException e) {
				LOG.error("fail to load watch data", e);
			}
			return w;
		}
	};

	public List<WatchedData> findAllLatestData(int productId) {
		String sql ="select w.* from watched_data w,(select max(id) id from watched_data where productId=? group by collectorId) v where w.id=v.id";
		return getJdbcTemplate().query(sql, new Object[] { productId },
				rowMapper);
	}

	public WatchedData findLatestData(int collectorId) {
		String sql = "select * from watched_data where collectorId=? order by id desc limit 1";
		List<WatchedData> data = getJdbcTemplate().query(sql,
				new Object[] { collectorId }, rowMapper);
		if (data.size() > 0) {
			return data.get(0);
		} else {
			return null;
		}
	}

	public void save(List<WatchedData> data) {
		String sql = "call insertdata(?,?,?,?)";
		List<Object[]> args = new ArrayList<Object[]>();
		for (WatchedData w : data) {
			args.add(new Object[] { w.getProductId(), w.getCollectorId(),
					new Timestamp(w.getWatchTime().getTime()), w.toJSONString() });
		}
		getJdbcTemplate().batchUpdate(sql, args);
	}

	public void removeAll(int productId) {
		String sql = "delete from watched_data where productId=?";
		getJdbcTemplate().update(sql, new Object[]{productId});
	}

	public void removeCollectorData(int collectorId) {
		String sql = "delete from watched_data where collectorId=?";
		getJdbcTemplate().update(sql, new Object[]{collectorId});
	}
}
