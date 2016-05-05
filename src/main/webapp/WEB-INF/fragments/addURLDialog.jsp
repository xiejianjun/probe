<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="addURL" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">&times;</button>
		<h3>创建URL监测项</h3>
	</div>
	<div class="modal-body">
		<form id="addURLForm" action="collector.do" method="post"
			class="form-horizontal">
			<input type="hidden" name="act" value="addURLCollector" /> <input
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
						<option value="30">半小时</option>
						<option value="60">一小时</option>
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
				<label class="control-label">预警邮箱</label>
				<div class="controls">
					<textarea name="email"  placeholder="输入预警邮箱地址" ></textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">请求方法</label>
				<div class="controls">
					<select name="method">
						<option value="GET" selected>GET</option>
						<option value="POST">POST</option>
						<option value="HEAD">HEAD</option>
					</select>
				</div>
			</div>
			<div class="control-group" style="display: none">
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
		<button id="addURLSubmit" class="btn btn-primary">创建</button>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#addURLForm").validate();
	});
	$("#addURLSubmit").click(function() {
		$("#addURLForm").submit();
	});
</script>