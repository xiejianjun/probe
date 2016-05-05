<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="addCollector" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">&times;</button>
		<h3>创建数据库监测项</h3>
	</div>
	<div class="modal-body">
		<form id="addCollectorForm" action="collector.do" method="post"
			class="form-horizontal">
			<input type="hidden" name="act" value="addDBCollector" /> <input
				type="hidden" name="productId" value="${productId}" />
			<div class="control-group">
				<label class="control-label">名称</label>
				<div class="controls">
					<input name="name" type="text" class="required" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">简介</label>
				<div class="controls">
					<textarea name="description" id="description" class="required"></textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">监测间隔</label>
				<div class="controls">
					<select name="interval">
						<option value="5" selected>5分钟</option>
						<option value="10">10分钟</option>
						<option value="30">30分钟</option>
						<option value="60">1小时</option>
						<option value="180">3小时</option>
						<option value="360">6小时</option>
						<option value="720">12小时</option>
						<option value="1440">1天</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">监测数据源</label>
				<div class="controls">
					<select name="datasourceName">
						<c:forEach items="${datasources}" var="datasource">
							<option value="${datasource.name}">${datasource.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">监测语句</label>
				<div class="controls">
					<textarea name="sql" class="required"></textarea>
				</div>
			</div>
		</form>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
		<button id="addCollectorSubmit" class="btn btn-primary">创建</button>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#addCollectorForm").validate();
	});
	$("#addCollectorSubmit").click(function() {
		$("#addCollectorForm").submit();
	});
</script>