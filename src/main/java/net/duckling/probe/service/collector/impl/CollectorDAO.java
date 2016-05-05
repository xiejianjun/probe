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
package net.duckling.probe.service.collector.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.duckling.probe.common.BaseDao;
import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.DataReader;
import net.duckling.probe.service.collector.DataReaderFactory;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class CollectorDAO extends BaseDao {
	private RowMapper<Collector> rowMapper = new RowMapper<Collector>() {
		@Override
		public Collector mapRow(ResultSet rs, int rowNum) throws SQLException {
			Collector collector = new Collector();
			collector.setId(rs.getInt("id"));
			collector.setInterval(rs.getInt("interval"));
			collector.setName(rs.getString("name"));
			collector.setProductId(rs.getInt("productId"));
			collector.setDescription(rs.getString("description"));
			collector.setDatatype(rs.getString("datatype"));
			String readerType = rs.getString("type");
			String property = rs.getString("prop");
			DataReader reader = DataReaderFactory.create(readerType);
			reader.setDatasourceName(rs.getString("datasourceName"));
			reader.fromJSON(property);
			collector.setReader(reader);
			return collector;
		}
	};

	public int save(final int productId, final Collector collector) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String sql = "insert into collector(productId,name,`interval`,datasourceName, type,prop,description) values(?,?,?,?,?,?,?)";
				PreparedStatement pst = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				try {
					pst.setInt(1, productId);
					pst.setString(2, collector.getName());
					pst.setInt(3, collector.getInterval());
					DataReader reader = collector.getReader();
					if (reader != null) {
						pst.setString(4, reader.getDatasourceName());
						pst.setString(5, reader.getType());
						pst.setString(6, reader.toJSON());
					} else {
						pst.setInt(4, -1);
						pst.setString(5, "");
						pst.setString(6, "");
					}
					pst.setString(7, collector.getDescription());
				} catch (SQLException e) {
					pst.close();
					throw e;
				}
				return pst;
			}

		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public List<Collector> findAll(int productId) {
		String sql = "select * from collector where productId=?";
		return getJdbcTemplate().query(sql, new Object[] { productId },
				rowMapper);
	}

	public void remove(int collectorId) {
		getJdbcTemplate().update("delete from collector where id=?",
				new Object[] { collectorId });
	}

	public Collector find(int collectorId) {
		List<Collector> items = getJdbcTemplate().query(
				"select * from collector where id=?",
				new Object[] { collectorId }, rowMapper);
		if (items.size() > 0) {
			return items.get(0);
		} else {
			return null;
		}
	}

	public void removeAll(int productId) {
		getJdbcTemplate().update("delete from collector where productId=?",
				new Object[] { productId });
	}

	public List<Collector> findShouCollect(int interval) {
		return getJdbcTemplate().query(
				"select * from collector where `interval` =?",
				new Object[] { interval }, rowMapper);
	}

	public Collector find(int productId, String name) {
		List<Collector> collectors = getJdbcTemplate().query(
				"select * from collector where productId=? and name=?",
				new Object[] { productId, name }, rowMapper);
		if (collectors.size() > 0) {
			return collectors.get(0);
		} else {
			return null;
		}
	}

	public void update(int productId, Collector collector) {
		// productId,name,`interval`,datasourceName, type,prop
		String sql = "update collector set name=?, `interval`=?, datasourceName=?,type=?, prop=? ,description=? where id=?";
		getJdbcTemplate().update(
				sql,
				new Object[] { collector.getName(), collector.getInterval(),
						collector.getReader().getDatasourceName(),
						collector.getReader().getType(),			
						collector.getReader().toJSON(),
						collector.getDescription(), collector.getId() });
	}
}
