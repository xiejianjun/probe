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
package net.duckling.probe.service.boostrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.ResourceUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class BootstrapDao{

	private static final Logger LOG = Logger.getLogger(BootstrapDao.class);
	private String checkTable;
	private ComboPooledDataSource createDataSource;

	private String database;
	private String sqlPath;

	private JdbcTemplate getCreatJdbc() {
		return new JdbcTemplate(createDataSource);
	}
	/**
	 * 关闭创建数据库连接池
	 */
	public void close() {
		createDataSource.close();
	}

	public void createDatabase() {
		getCreatJdbc().execute(
				"CREATE database IF NOT EXISTS " + database
						+ " CHARACTER SET utf8");
	}

	public void createTables() {
		JdbcTemplate jdbcTemplate = getCreatJdbc();
		jdbcTemplate.execute("use "+database);
		File file = null;
		SQLReader reader = null;
		try {
			file = ResourceUtils.getFile(sqlPath);
			if (file.exists()) {
				reader = new SQLReader(new FileInputStream(file), "UTF-8");
				String sql;
				while ((sql = reader.next()) != null) {
					jdbcTemplate.execute(sql);
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Init sql file is not found", e);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Unsupported encode for UTF-8", e);
		} catch (DataAccessException e) {
			LOG.error("Data access exception", e);
		} catch (WrongSQLException e) {
			LOG.error("Init SQL has problem", e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	public boolean isDatabaseExisted() {
		String sql = "show databases";
		final List<String> results = getCreatJdbc().queryForList(sql,
				String.class);
		if (results == null) {
			return false;
		}
		return results.contains(database);
	}

	public boolean isTableExisted() {
		JdbcTemplate jdbcTemplate = getCreatJdbc();
		jdbcTemplate.execute("use "+database);
		return jdbcTemplate.query(
				"show tables like '" + this.checkTable + "'",
				new ResultSetExtractor<Boolean>() {
					public Boolean extractData(ResultSet rs)
							throws SQLException {
						return rs.next();
					}
				});
	}

	public void setCheckTable(String checkTable) {
		this.checkTable = checkTable;
	}

	public void setCreateDataSource(ComboPooledDataSource createDataSource) {
		this.createDataSource = createDataSource;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public void setSqlPath(String sqlPath) {
		this.sqlPath = sqlPath;
	}

}
