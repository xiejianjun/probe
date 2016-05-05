<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="ModifyPro" class="modal hide fade">
	<div id="addCollector" class="modal">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>修改监测项</h3>
		</div>
		<div class="modal-body">
			<form id="addProductForm" action="product.do" method="post"
				class="form-horizontal">
				<input type="hidden" name="act" value="ModifyProduct" /> 
				<input type="hidden" name="productId" id="productId" />
				<div class="control-group">
					<label class="control-label">名称</label>
					<div class="controls">
						<input name="name" type="text" id="name" class="required" />
					</div>
				</div>

				<div class="control-group">
					<label class="control-label">简介</label>
					<div class="controls">
						<textarea name="description" id="description" class="required"></textarea>
					</div>
				</div>
			</form>
		</div>
		<div class="modal-footer">
			<button id="addProductSubmit" class="btn btn-primary">保存</button>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$("#addProductForm").validate();
	});
	$("#addProductSubmit").click(function() {
		$("#addProductForm").submit();
	});
</script>
