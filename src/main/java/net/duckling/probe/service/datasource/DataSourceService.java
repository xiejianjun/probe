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
package net.duckling.probe.service.datasource;

import java.util.List;
/**
 * 数据源管理服务
 * @author xiejj@cnic.cn
 *
 */
public interface DataSourceService {
	/**
	 * 根据产品ID和数据源名称查找数据源
	 * @param productId		产品ID
	 * @param name			数据源名称
	 * @return				匹配的数据源
	 */
	DataSource find(int productId, String name);
	/**
	 * 查找产品下所有配置的数据源
	 * @param productId		产品ID
	 * @return				所有配置的数据源
	 */
	List<DataSource> findDataSources(int productId);
	/**
	 * 保存数据源
	 * @param datasource	数据源信息
	 */
	void save(DataSource datasource);
	/**
	 * 删除数据源
	 * @param productId		产品ID
	 * @param name			数据源名称
	 */
	void remove(int productId, String name);
	/**
	 * 通过ID查找数据源
	 * @param dataSourceId	数据源ID
	 * @return				数据源
	 */
	DataSource findById(int dataSourceId);
	/**
	 * 删除数据源
	 * @param datasourceId	数据源ID
	 */
	void remove(int datasourceId);
}
