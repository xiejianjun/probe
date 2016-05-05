<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="addREST" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">&times;</button>
		<h3>创建API监测项</h3>
	</div>
	<div class="modal-body">
		<form id="addRestForm" action="collector.do" method="post"
			class="form-horizontal">
			<input type="hidden" name="act" value="addAPICollector" /> <input
				type="hidden" name="productId" value="${productId}" />
			<div class="control-group">
				<label class="control-label">名称</label>
				<div class="controls">
					<input name="name" type="text" class="required" placeholder="监测项名称（须英文）" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">简介</label>
				<div class="controls">
					<textarea name="description" id="description"  placeholder="监测项介绍"></textarea>
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
				<label class="control-label">监测地址</label>
				<div class="controls">
					<textarea name="url" class="required" placeholder="请输入以http://或https://开头的url"></textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">字符集</label>
				<div class="controls">
					<select name="encode">
						<option value="UTF-8" selected>UTF-8</option>
						<option value="GBK">GBK</option>
					</select>
				</div>
			</div>
		</form>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
		<button id="addRestSubmit" class="btn btn-primary">创建</button>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#addRestForm").validate();
	});
	$("#addRestSubmit").click(function() {
		$("#addRestForm").submit();
	});
</script>