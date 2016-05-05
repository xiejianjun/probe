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
package net.duckling.probe.service.watchedurl;

import java.util.Date;

import net.duckling.probe.service.dbdataplate.DbDataPlate;

public class WatchedUrl extends DbDataPlate{
	public Date getWatchTime() {
		if (this.watchTime != null) {
			return new Date(watchTime.getTime());
		} else {
			return null;
		}
	}

	public void setWatchTime(Date watchTime) {
		if (watchTime != null) {
			this.watchTime = new Date(watchTime.getTime());
		}
	}
	private Date watchTime;
	private int collectorId;
	private int productId;	
	private boolean availability;
	private int statusCode;
	private String method;
	private int responseTime;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(int collectorId) {
		this.collectorId = collectorId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}
	
	public short getAvailability(){
		if(availability){
			return 1;
		}else{
			return 0;
		}
	}
	
}
