<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fieldset>
	<legend>已添加的数据源</legend>
	<p class="text-right"><a href="#addDatasource" role="button" class="btn" data-toggle="modal">创建数据源</a></p>
	<table class="table table-striped">
		<tr>
			<th>名称</th>
			<th>服务器地址</th>
			<th>数据库</th>
			<th>用户名</th>
			<th>操作</th>
		</tr>
		<c:forEach items="${datasources}" var="datasource">
			<tr>
				<td>${datasource.name}</td>
				<td>${datasource.properties.host}</td>
				<td>${datasource.properties.database}</td>
				<td><c:out value="${datasource.properties.user}" /></td>
				<td><a href="javascript:void(0);" onclick="deleteConfirm('collector','collector.do?act=delDataSource&productId=${productId}&datasourceId=${datasource.id}')">删除</a></td>
			</tr>
		</c:forEach>
	</table>
</fieldset>