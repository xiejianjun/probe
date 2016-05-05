CREATE TABLE `collector` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `description` text,
  `productId` int(11) default NULL,
  `datasourceName` varchar(255) default NULL,
  `interval` int(11) default NULL,
  `type` varchar(255) default NULL,
  `prop` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `datasource` (
  `id` int(11) NOT NULL auto_increment,
  `productId` int(11) default NULL,
  `name` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `ds_prop` (
  `Id` int(11) NOT NULL auto_increment,
  `datasourceId` int(11) default NULL,
  `name` varchar(255) default NULL,
  `value` varchar(255) default NULL,
  PRIMARY KEY  (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `product` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `description` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `watched_data` (
  `id` int(11) NOT NULL auto_increment,
  `productId` int(11) default NULL,
  `collectorId` int(11) default NULL,
  `watch_time` timestamp NULL default NULL,
  `data` text,
  PRIMARY KEY  (`id`),
  KEY `Collector` (`collectorId`),
  KEY `multiIndex` (`productId`,`collectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `url_watch_data` (
  `id` int(11) NOT NULL auto_increment,
  `productId` int(11) default NULL,
  `collectorId` int(11) default NULL,
  `availability` tinyint(1) default NULL,
  `watch_time` timestamp NULL default NULL,
  `method` varchar(255) default NULL,
  `statuscode`  smallint(3) default NULL,
  `responsetime` int unsigned default NULL,
  PRIMARY KEY  (`id`),
  KEY `Collector` (`collectorId`),
  KEY `multiIndex` (`productId`,`collectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `calc_url_data` (
  `id` int(11) NOT NULL auto_increment,
  `productId` int(11) default NULL,
  `collectorId` int(11) default NULL,
  `calcTime` date  default NULL,
  `method` varchar(255) default NULL,
  `health` float default NULL,
  `avgTime` float default NULL,
  PRIMARY KEY  (`id`),
  KEY `Collector` (`collectorId`),
  KEY `multiIndex` (`productId`,`collectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET GLOBAL event_scheduler = 1;
DELIMITER // 
CREATE PROCEDURE `calc_url_data` ()
BEGIN
insert into calc_url_data (productId,collectorId,health,avgTime,method,calcTime) (select productId,collectorId,avg(availability),avg(responsetime),method,curdate()-1 from url_watch_data where date_format(watch_time,'%y-%m-%d')=(select curdate() - INTERVAL 1 DAY ) group by collectorId);
END //
CREATE  EVENT  IF NOT EXISTS  calc_url
ON  SCHEDULE  EVERY  1  day  STARTS date_add(curdate(),interval 1 hour)
ON  COMPLETION  PRESERVE  ENABLE
DO
BEGIN
CALL calc_url_data();
END //
DELIMITER ;
#增加的value插入的存储过程
DELIMITER // 
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertdata`(productId int ,collectorId int,watchTime timestamp,
	jsondata text charset utf8)
BEGIN
    declare count int default 0;
    declare length int default 0;
    set length=LENGTH(jsondata);
    set count=INSTR(jsondata,'}');
	if(count+1=length)
	then update collector set datatype='valuetype' where id=collectorId;
	insert into value_watch_data(productId, collectorId, watch_time, data) values(productId,collectorId,watchTime,jsondata);
	else update collector set datatype='texttype' where id=collectorId;
	insert into watched_data(productId, collectorId, watch_time, data) values(productId,collectorId,watchTime,jsondata);
	end if;
END //
DELIMITER ;
#2015-11-04
#修改存储过程calc_url_data为以下内容：
drop procedure calc_url_data;
DELIMITER //
CREATE PROCEDURE `calc_stats_data`()
BEGIN
insert into calc_url_data (productId,collectorId,health,avgTime,method,calcTime) (select productId,collectorId,avg(availability),avg(responsetime),method,curdate()-1 from url_watch_data where date_format(watch_time,'%y-%m-%d')=(select curdate() - INTERVAL 1 DAY ) group by collectorId);
insert into calc_value_data(productId,collectorId,calcTime,data) (select productId,collectorId,date_format(watch_time,'%y-%m-%d') as calcTime,data from value_watch_data as a  join (select max(id) as id from value_watch_data where watch_time<(select curdate() - INTERVAL 0 hour) group by collectorId) as b on a.id=b.id);
END //
DELIMITER ;
#修改event事件为以下内容：
drop event calc_url;
DELIMITER //
CREATE  EVENT  IF NOT EXISTS  calc_stats
ON  SCHEDULE  EVERY  1  day  STARTS date_add(curdate(),interval 1 hour)
ON  COMPLETION  PRESERVE  ENABLE
DO
BEGIN
CALL calc_stats_data();
END //
DELIMITER ;
#collector表添加了datatype列：
alter table collector add datatype varchar(255) default null;
#添加了value_watch_data表
CREATE TABLE `value_watch_data` (
  `id` int(11) NOT NULL auto_increment,
  `productId` int(11) default NULL,
  `collectorId` int(11) default NULL,
  `watch_time` timestamp NULL default NULL,
  `data` text,
  PRIMARY KEY  (`id`),
  KEY `Collector` (`collectorId`),
  KEY `multiIndex` (`productId`,`collectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#添加了calc_value_data表
CREATE TABLE `calc_value_data` (
  `id` int(11) NOT NULL auto_increment,
  `productId` int(11) default NULL,
  `collectorId` int(11) default NULL,
  `calcTime` date  default NULL,
  `data` text,
  PRIMARY KEY  (`id`),
  KEY `Collector` (`collectorId`),
  KEY `multiIndex` (`productId`,`collectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#添加了watched_data导入到value_watch_data以及并计算到calc_value_data的存储过程,参数格式为('%y-%m-%d')表示导入数据的起始日期
DELIMITER //
CREATE PROCEDURE `import_data`(beginDate date)
BEGIN
declare i int default 1; 
declare len int default 0;
select to_days(now()) - to_days(beginDate) into len;
insert into value_watch_data(productId,collectorId,watch_time,data)(select productId,collectorId,watch_time,data from watched_data where date_format(watch_time,'%y-%m-%d')>(select curdate()-interval len day) and isValue(data)=1);
set i=len;
while i >0 do 
 insert into calc_value_data(productId,collectorId,calcTime,data) (select productId,collectorId,date_format(watch_time,'%y-%m-%d') as calcTime,data from value_watch_data as a  join (select max(id) as id from value_watch_data where watch_time<(select (curdate()-interval i day ) - INTERVAL 0 hour) and watch_time>(select (curdate()-interval i+1 day ) - INTERVAL 0 hour) group by collectorId) as b on a.id=b.id);
set i = i -1;
end while;
END //
DELIMITER ;
#判断json数据是否是数值类型的mysql function
DELIMITER //
CREATE DEFINER=`root`@`localhost` FUNCTION `isValue`(jsondata text) RETURNS tinyint(1)
BEGIN
	declare isValue boolean default false;
	declare count int default 0;
    declare length int default 0;
    set length=LENGTH(jsondata);
    set count=INSTR(jsondata,'}');
    if(count+1=length)
    then set isValue=true;
    end if;
RETURN isValue;
END //
DELIMITER ;