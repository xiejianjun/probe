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

import java.util.HashMap;
import java.util.Map;

public class DataSource {
	private int id;
	private int productId;
	private Map<String, String> properties = new HashMap<String, String>();
	private String name;
	private DataSourceType type;

	public int getId() {
		return id;
	}

	public int getProductId() {
		return productId;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperty(String key, String value) {
		if (key != null) {
			if (value != null) {
				properties.put(key, value);
			} else {
				properties.remove(key);
			}
		}
	}

	public String getName() {
		return name;
	}

	public DataSourceType getType() {
		return type;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(DataSourceType type) {
		this.type = type;
	}
}
