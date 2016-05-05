<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="addDatasource" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">&times;</button>
		<h3>创建数据源</h3>
	</div>
	<div class="modal-body">
		<form id="addDataSourceForm" action="collector.do" method="post"
			class="form-horizontal">
			<input type="hidden" name="productId" value="${productId}" /> <input
				type="hidden" name="act" value="addDataSource" />
			<div class="control-group">
				<label class="control-label">数据源名称</label>
				<div class="controls">
					<input name="name" type="text" class="required" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">服务器地址</label>
				<div class="controls">
					<input name="host" type="text" placeholder="例:localhost"
						class="required" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">数据库名称</label>
				<div class="controls">
					<input name="database" type="text" class="required" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">用户名</label>
				<div class="controls">
					<input name="user" type="text" class="required" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">密码</label>
				<div class="controls">
					<input id="password" name="password" type="password"
						class="required" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">再输一遍</label>
				<div class="controls">
					<input name="retype" type="password"/>
				</div>
			</div>
		</form>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
		<button id="addDataSourceSubmit" class="btn btn-primary">创建</button>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#addDataSourceForm").validate({
			rules : {
				retype : {required:true,equalTo:"#password"}
			}
		});
	});
	$("#addDataSourceSubmit").click(function() {
		$("#addDataSourceForm").submit();
	});
</script>