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
package net.duckling.probe.service.mailalert.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import net.duckling.probe.service.collector.Collector;
import net.duckling.probe.service.collector.URLDataReader;
import net.duckling.probe.service.mailalert.AlertService;
import net.duckling.probe.service.mailalert.Mail;
import net.duckling.probe.service.mailalert.MailService;

public class AlertServiceImpl implements AlertService {
	private static final Logger LOG = Logger.getLogger(AlertServiceImpl.class);
	private static HashMap<Integer,Integer> countFail;
	private static HashMap<Integer,Integer> countSuccess;
	private static MailService mailService;
	private SimpleDateFormat createSimpleDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	static{
		countFail=new HashMap<Integer,Integer>();
		countSuccess=new HashMap<Integer,Integer>();
		mailService=new MailServiceImpl();
	}
	
	@Override
	public void countFail(Collector collector) {
		// TODO Auto-generated method stub
		URLDataReader reader=(URLDataReader) collector.getReader();
		if(reader.getEmail()!=null){
			if(countFail.containsKey(collector.getId())){
				if(countFail.get(collector.getId())+1<=3){
					countFail.put(collector.getId(), countFail.get(collector.getId())+1);
					LOG.info("访问失败"+countFail.get(collector.getId())+"次");
					if(countFail.get(collector.getId())==3){
						mailAlertFail(collector);
					}
				}
			}else{
				countFail.put(collector.getId(), 1);
				LOG.info("访问失败"+countFail.get(collector.getId())+"次");
				if(countSuccess.containsKey(collector.getId())){
					countSuccess.remove(collector.getId());
					
				}
			}
		}				
	}
	@Override
	public void conutSuccess(Collector collector) {
		// TODO Auto-generated method stub
		if(countFail.containsKey(collector.getId())){
			if(countFail.get(collector.getId())==3){
				countSuccess.put(collector.getId(), 1);
				LOG.info("访问失败后恢复"+countSuccess.get(collector.getId())+"次");
			}	
			countFail.remove(collector.getId());
		}else if(countSuccess.containsKey(collector.getId())){
			if(countSuccess.get(collector.getId())+1<=3){
				countSuccess.put(collector.getId(), countSuccess.get(collector.getId())+1);
				LOG.info("访问失败后恢复"+countSuccess.get(collector.getId())+"次");
				if(countSuccess.get(collector.getId())==3){
					mailAlertRecover(collector);					
					countSuccess.remove(collector.getId());
				}
			}
		}
	}

	/*
	 * 发送邮件预警通知
	 * @see net.duckling.probe.service.mailalert.AlertService#mailAlertFail()
	 */
	@Override
	public void mailAlertFail(Collector collector) {
		// TODO Auto-generated method stub
		LOG.info(collector.getName()+"3次失败,发送邮件预警");
		Mail mail=new Mail();
		SimpleDateFormat dateFormat = createSimpleDateFormat();
		mail.setSubject("科技云监控预警通知");
		URLDataReader reader=(URLDataReader) collector.getReader();
		mail.setRecipient(reader.getEmail());
		String content="["+collector.getName()+"]监测项"+"已在"+dateFormat.format(new Date()).toString()+"连续三次访问失败！" +
				"请您及时检查和修复此监测项。<br/><br/>科技云监控服务vlab@cnic.cn";
		mail.setTemplate(content);
		mailService.init();
		mailService.sendMail(mail);
	}

	/*
	 * 发送邮件恢复通知
	 * @see net.duckling.probe.service.mailalert.AlertService#mailAlertRecover()
	 */
	@Override
	public void mailAlertRecover(Collector collector) {
		// TODO Auto-generated method stub
		LOG.info(collector.getName()+"3次成功,发送邮件恢复通知");
		Mail mail=new Mail();
		SimpleDateFormat dateFormat = createSimpleDateFormat();
		mail.setSubject("科技云监控恢复访问通知");
		URLDataReader reader=(URLDataReader) collector.getReader();
		mail.setRecipient(reader.getEmail());
		String content="["+collector.getName()+"]监测项"+"已在"+dateFormat.format(new Date()).toString()+"恢复访问！<br/><br/>科技云监控服务vlab@cnic.cn" ;
		mail.setTemplate(content);
		MailServiceImpl mailServiceImpl=new MailServiceImpl();
		mailServiceImpl.init();
		mailServiceImpl.sendMail(mail);
	}

}
