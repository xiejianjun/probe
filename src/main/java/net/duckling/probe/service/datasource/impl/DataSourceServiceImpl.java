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

import java.util.List;

import net.duckling.probe.service.datasource.DataSource;
import net.duckling.probe.service.datasource.DataSourceService;

public class DataSourceServiceImpl implements DataSourceService {
	private DataSourceDAO dataSourceDao;
	private DataPropertiesDAO propDao;
	private void fillProperties(DataSource dataSource) {
		List<PropertiesItem> props = propDao
				.findDataSourceProperties(dataSource.getId());
		for (PropertiesItem prop : props) {
			dataSource.setProperty(prop.getName(), prop.getValue());
		}
	}
	private DataSource lookupDataSource(List<DataSource> dataSources, int datasourceId){
		for (DataSource dataSource:dataSources){
			if (dataSource.getId() == datasourceId){
				return dataSource;
			}
		}
		return null;
	}
	@Override
	public DataSource find(int productId, String name) {
		DataSource dataSource = dataSourceDao.findByName(productId, name);
		fillProperties(dataSource);
		return dataSource;
	}
	@Override
	public DataSource findById(int dataSourceId) {
		DataSource dataSource = dataSourceDao.findById(dataSourceId);
		fillProperties(dataSource);
		return dataSource;
	}

	@Override
	public List<DataSource> findDataSources(int productId) {
		List<DataSource> dataSources = dataSourceDao.findDataSources(productId);
		List<PropertiesItem> items = propDao.findMuseumProperties(productId);
		DataSource current = null;
		for (PropertiesItem item:items){
			if (current==null || current.getId()!=item.getDatasourceId()){
				current = lookupDataSource(dataSources, item.getDatasourceId());
			}
			if (current!=null){
				current.setProperty(item.getName(), item.getValue());
			}
		}
		return dataSources;
	}
	@Override
	public void remove(int productId, String name) {
		dataSourceDao.remove(productId, name);
		propDao.removeAll(productId, name);
	}

	@Override
	public void save(DataSource datasource) {
		DataSource existSource = dataSourceDao.findByName(
				datasource.getProductId(), datasource.getName());
		int datasourceId;
		if (existSource != null) {
			propDao.removeAll(existSource.getProductId(),
					existSource.getName());
			datasourceId = existSource.getId();
		}else{
			datasourceId = dataSourceDao.createDataSource(datasource);
		}
		propDao.saveAll(datasourceId, datasource.getProperties());
	}

	public void setDataSourceDao(DataSourceDAO dataSourceDao){
		this.dataSourceDao = dataSourceDao;
	}
	public void setPropDao(DataPropertiesDAO propDao){
		this.propDao = propDao;
	}
	@Override
	public void remove(int datasourceId) {
		dataSourceDao.remove(datasourceId);
		propDao.remove(datasourceId);
	}
}