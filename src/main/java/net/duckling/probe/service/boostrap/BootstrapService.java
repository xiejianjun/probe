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

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 系统初始化服务，负责在初始化时创建数据库
 * @author xiejj@cnic.cn
 * 
 */
public class BootstrapService {
	private BootstrapDao bootstrapDao;

	public BootstrapService(ComboPooledDataSource pool, String database,
			String checkTable, String sqlFile) {
		bootstrapDao = new BootstrapDao();
		bootstrapDao.setCreateDataSource(pool);
		bootstrapDao.setDatabase(database);
		bootstrapDao.setSqlPath(sqlFile);
		bootstrapDao.setCheckTable(checkTable);
	}

	/**
	 * 检查并创建数据库
	 */
	public void bootstrap() {
		if (!bootstrapDao.isDatabaseExisted()) {
			bootstrapDao.createDatabase();
			bootstrapDao.createTables();
		}
		bootstrapDao.close();
	}
}
