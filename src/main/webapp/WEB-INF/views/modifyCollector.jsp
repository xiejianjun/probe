<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="ModifyCol" class="modal hide fade">
	<div id="ModifyCollector" class="modal">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>修改监测项</h3>
		</div>
		<div class="modal-body">
			<form id="modifyCollectorForm" action="collector.do" method="post"
				class="form-horizontal">
				<input type="hidden" name="act" id="act" /> <input type="hidden"
					name="productId" id="productId" /> <input type="hidden"
					name="collectorId" id="collectorId" />
				<div class="control-group">
					<label class="control-label">名称</label>
					<div class="controls">
						<input name="name" type="text" id="name" class="required" />
					</div>
				</div>

				<div class="control-group">
					<label class="control-label">简介</label>
					<div class="controls">
						<textarea name="description" id="description" ></textarea>
					</div>
				</div>

				<div class="control-group">
					<label class="control-label">监测间隔</label>
					<div class="controls">
						<select name="interval" id="interval">
							<option value="5">5分钟</option>
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

				<div class="control-group" id="datasourceName_group">
					<label class="control-label">监测数据源</label>
					<div class="controls">
						<select name="datasourceName" id="datasourceName">
							<option value="">选择数据源</option>
						</select>
					</div>
				</div>
				<div class="control-group" id="sql_group">
					<label class="control-label">监测语句</label>
					<div class="controls">
						<textarea name="sql" id="sql" class="required"></textarea>
					</div>
				</div>

				<div class="control-group" id="url_group">
					<label class="control-label">监测地址</label>
					<div class="controls">
						<textarea name="url" id="url" class="required"></textarea>
					</div>
				</div>

				<div class="control-group" id="email_group">
					<label class="control-label">预警邮箱</label>
					<div class="controls">
						<textarea name="email"  id="email"></textarea>
					</div>
				</div>
				<div class="control-group" id="encode_group">
					<label class="control-label">字符集</label>
					<div class="controls">
						<select name="encode" id="encode">
							<option value="UTF-8">UTF-8</option>
							<option value="GBK">GBK</option>
						</select>
					</div>
				</div>
				<div class="control-group" id="method_group">
					<label class="control-label">请求方法</label>
					<div class="controls">
						<select name="method" id="method">
							<option value="GET">GET</option>
							<option value="POST">POST</option>
							<option value="HEAD">HEAD</option>
						</select>
					</div>
				</div>
			</form>
		</div>
		<div class="modal-footer">
			<button id="modifyCollectorSubmit" class="btn btn-primary">保存</button>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$("#modifyCollectorForm").validate();
	});
	$("#modifyCollectorSubmit").click(function() {
		$("#modifyCollectorForm").submit();
	});
</script>
