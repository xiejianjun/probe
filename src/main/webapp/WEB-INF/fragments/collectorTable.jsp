<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fieldset>
	<legend>已添加的监控项</legend>
	<p class="text-right">
		<a href="#addCollector" role="button" class="btn" data-toggle="modal">创建数据库监测项</a>
		<a href="#addREST" role="button" class="btn" data-toggle="modal">创建API监测项</a>
		<a href="#addURL" role="button" class="btn" data-toggle="modal">创建URL监测项</a>
	</p>
	<table class="table table-striped">
		<tr>
			<th class="nametd">名称</th>
			<th class="intervaltd">时间间隔</th>
			<th class="datasourcetd">数据源</th>
			<th>SQL/URL地址</th>
			<th class="typetd">类型</th>
			<th class="operationtd">操作</th>
		</tr>
		<c:forEach items="${collectors}" var="collector">
			<tr>
				<td id="nametool" class="nametd" data-toggle="tooltip" data-placement="top" title="${collector.description}" data-container="body">${collector.name}</td>
				<td class="intervaltd">${collector.intervalDesc}</td>
				<td class="datasourcetd">${collector.reader.datasourceName}</td>
				<td class="adresstd" ><c:if test="${collector.reader.type=='SQL'}">
						<c:out value="${collector.reader.sql}" escapeXml="true" />
					</c:if> <c:if test="${collector.reader.type=='API'}">
						<c:out value="${collector.reader.url}" escapeXml="true" />
					</c:if>
					<c:if test="${collector.reader.type=='URL'}">
						<c:out value="${collector.reader.url}" escapeXml="true" />
					</c:if></td>
				<td class="typetd"><c:if test="${collector.reader.type=='SQL'}">
						数据库监测
					</c:if> <c:if test="${collector.reader.type=='API'}">
						API监测
					</c:if>
					<c:if test="${collector.reader.type=='URL'}">
						URL监测
					</c:if></td>
				<td class="operationtd">
					<a href="javascript:void(0);" onclick="modifyCollector('${productId}','${collector.id}')">修改</a>
					 <a	href="javascript:void(0);" onclick="deleteConfirm('collector','collector.do?act=delCollector&productId=${productId}&collectorId=${collector.id}')">删除</a>
					 <c:if test="${collector.datatype=='valuetype' ||collector.reader.type=='URL'}">
					 <a href="./api/chart/${productname}/${collector.name}">图表</a>
					 </c:if>			
				</td>
			</tr>
		</c:forEach>
	</table>
</fieldset>
