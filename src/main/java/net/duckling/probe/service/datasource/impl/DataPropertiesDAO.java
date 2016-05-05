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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.duckling.probe.common.BaseDao;

import org.springframework.jdbc.core.RowMapper;

public class DataPropertiesDAO extends BaseDao {
	private RowMapper<PropertiesItem> rowmapper = new RowMapper<PropertiesItem>() {

		public PropertiesItem mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PropertiesItem item = new PropertiesItem();
			item.setDatasourceId(rs.getInt("dataSourceId"));
			item.setName(rs.getString("name"));
			item.setValue(rs.getString("value"));
			return item;
		}

	};

	public List<PropertiesItem> findDataSourceProperties(int datasourceId) {
		return getJdbcTemplate().query(
				"select * from ds_prop where datasourceId=?",
				new Object[] { datasourceId }, rowmapper);
	}

	public List<PropertiesItem> findMuseumProperties(int productId) {
		return getJdbcTemplate()
				.query("select ds_prop.* from ds_prop, datasource"
						+ " where datasource.productId=? and ds_prop.datasourceId=datasource.id"
						+ " order by datasourceId", new Object[] { productId },
						rowmapper);
	}

	public void removeAll(int productId, String name) {
		getJdbcTemplate()
				.update("delete from ds_prop where dataSourceId in (select id from datasource where productId=? and name=?)",
						new Object[] { productId, name });
	}

	public void saveAll(int datasourceId, Map<String, String> props) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Entry<String, String> entry : props.entrySet()) {
			batchArgs.add(new Object[] { datasourceId, entry.getKey(),
					entry.getValue() });
		}
		getJdbcTemplate()
				.batchUpdate(
						"insert into ds_prop(datasourceId, name, `value`) values(?,?,?)",
						batchArgs);
	}

	public void remove(int datasourceId) {
		getJdbcTemplate().update("delete from ds_prop where datasourceId=?",new Object[]{datasourceId});
	}
}