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
package net.duckling.probe.service.datasource.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.duckling.probe.common.BaseDao;
import net.duckling.probe.service.datasource.DataSource;
import net.duckling.probe.service.datasource.DataSourceType;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DataSourceDAO extends BaseDao {
	private RowMapper<DataSource> rowMapper = new RowMapper<DataSource>() {

		public DataSource mapRow(ResultSet rs, int rowNum) throws SQLException {
			DataSource ds = new DataSource();
			ds.setId(rs.getInt("id"));
			ds.setProductId(rs.getInt("productId"));
			ds.setName(rs.getString("name"));
			ds.setType(DataSourceType.valueOf(rs.getString("type")));
			return ds;
		}
	};

	public DataSource findByName(int productId, String name) {
		List<DataSource> dataSources = getJdbcTemplate().query(
				"select * from datasource where productId=? and name=?",
				new Object[] { productId, name }, rowMapper);
		if (dataSources.size() > 0) {
			return dataSources.get(0);
		} else {
			return null;
		}
	}

	public List<DataSource> findDataSources(int productId) {
		return getJdbcTemplate().query(
				"select * from datasource where productId=?",
				new Object[] { productId }, rowMapper);
	}

	public void remove(int productId, String name) {
		getJdbcTemplate().update(
				"delete from datasource where productId=? and name=?",
				new Object[] { productId, name });
	}

	public int createDataSource(final DataSource datasource) {
		KeyHolder holder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement pst = conn.prepareStatement(
						"insert into datasource(productId, name, type) values(?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				try {
					pst.setInt(1, datasource.getProductId());
					pst.setString(2, datasource.getName());
					pst.setString(3, datasource.getType().toString());
				} catch (SQLException e) {
					pst.close();
					throw e;
				}
				return pst;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	public DataSource findById(int dataSourceId) {
		List<DataSource> list = getJdbcTemplate().query(
				"select * from datasource where id=?",
				new Object[] { dataSourceId }, rowMapper);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void remove(int datasourceId) {
		getJdbcTemplate().update("delete from datasource where id=?",
				new Object[] { datasourceId });
	}
}
