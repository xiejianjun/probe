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
package net.duckling.probe.common;

import java.beans.PropertyVetoException;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * SQL处理工具
 * @author xiejj@cnic.cn
 *
 */
public final class SQLUtils {
	private static final String MYSQL_CONN_URL = "jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
	private static final Logger LOG = Logger.getLogger(SQLUtils.class);

	private SQLUtils() {
	};
	/**
	 * 将集合变成集合SQL
	 * @param values	集合值
	 * @return			结合SQL
	 */
	public static String makeSetString(int[] values) {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		if (values != null && values.length > 0) {
			boolean first = true;
			for (int v : values) {
				if (!first) {
					builder.append(",");
				} else {
					first = false;
				}
				builder.append(v);
			}
		}
		builder.append(")");
		return builder.toString();
	}
	/**
	 * 构造数据库连接池
	 * @param database	数据库名称
	 * @param host		数据库主机
	 * @param user		用户名
	 * @param password	密码
	 * @return			数据库连接池
	 */
	public static ComboPooledDataSource makeMysqlPool(String database,
			String host, String user, String password) {
		ComboPooledDataSource comboDataSource = new ComboPooledDataSource();
		try {
			comboDataSource.setDriverClass("com.mysql.jdbc.Driver");
			comboDataSource.setJdbcUrl(String.format(MYSQL_CONN_URL, host,
					database));
			comboDataSource.setUser(user);
			comboDataSource.setPassword(password);
			comboDataSource.setMinPoolSize(10);
		} catch (PropertyVetoException e) {
			LOG.error("Fail to create mysql connection",e);
		}
		return comboDataSource;
	}
}
