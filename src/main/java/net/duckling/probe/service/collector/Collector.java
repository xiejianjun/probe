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

public class Collector {
	private int id;
	private int interval;
	private int productId;
	private DataReader reader;
	private String name;
	private String description;
	private String datatype;
	public int getId() {
		return id;
	}

	public int getInterval() {
		return interval;
	}

	public DataReader getReader() {
		return reader;
	}

	public void setReader(DataReader reader) {
		this.reader = reader;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductId() {
		return productId;
	}
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getIntervalDesc(){
		switch (interval){
		case 5:
				return "5分钟";
		case 10:
				return "10分钟";
		case 30:
				return "30分钟";
		case 60:
				return "1个小时";
		case 180:
				return "3个小时";
		case 360:
				return "6个小时";
		case 720:
				return "12个小时";
		case 1440:
				return "1天";
		}
		return "";
	}
}
