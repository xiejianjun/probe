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
package net.duckling.probe.service.collector;

import java.util.List;
/**
 * 监测项服务
 * @author xiejj@cnic.cn
 *
 */
public interface CollectorService {
	/**
	 * 保存一个监测项
	 * @param productId	产品ID
	 * @param collector	监测项
	 * @return 新创建监测项的ID
	 */
	int save(int productId, Collector collector);
	/**
	 * 查找产品下的所有的监测项
	 * @param productId	产品ID
	 * @return 产品下所有的监测项
	 */
	List<Collector> findAll(int productId);
	/**
	 * 删除监测项
	 * @param collectorId
	 */
	void remove(int collectorId);
	/**
	 * 删除产品下的所有的监测项
	 * @param productId 产品的ID
	 */
	void removeAll(int productId);
	/**
	 * 根据监测项的ID，查找监测项信息
	 * @param collectorId	监测项ID
	 * @return 监测项信息
	 */
	Collector find(int collectorId);
	/**
	 * 查找所有在当前时间节点需要执行的监测
	 * @param interval	当前时间节点应该执行的时间间隔
	 * @return	所有匹配的监测项
	 */
	List<Collector> findShouldCollect(int interval);
	/**
	 * 根据产品和监测项名称查找监控项
	 * @param productId		产品ID
	 * @param name			监测项的名称
	 * @return				匹配的监测项
	 */
	Collector find(int productId, String name);
	/**
	 * 更新监测项
	 * @param productId		产品ID
	 * @param collector		监测项信息
	 */
	void update(int productId, Collector collector);
}
