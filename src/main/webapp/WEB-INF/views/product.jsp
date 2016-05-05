<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<style>
body {
	padding-top: 60px;
}
</style>
<title>管理监控产品</title>
</head>
<body>
	<jsp:include page="../fragments/navbar.jsp"/>
	<div class="container">
		<h1>管理监控产品</h1>
		<form action="product.do" method="post" id="productForm">
			<fieldset>
				<legend>添加新的监控产品</legend>
				<input type="hidden" name="act" value="add" />
				<label>产品名称</label>
				<input name="name" type="text"  id="productName" placeholder="产品名称（须英文）" />
				<input name="description" type="text" placeholder="输入监控产品的描述" />
				<button class="btn"  style="margin-bottom:10px" id="creatProduct">创建</button>
			</fieldset>
		</form>
		<fieldset>
			<legend>已监控的产品</legend>
			<table class="table table-striped">
				<tr>
					<th>名称</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
				<c:forEach items="${products}" var="product">
					<tr>
						<td><a href="collector.do?productId=${product.id}">${product.name}</a></td>
						<td style="width:700px">${product.description}</td>
						<td><a href="javascript:void(0);" onclick="modifyProduct('${product.id}')">修改</a>
							<a href="javascript:void(0);" onclick="deleteConfirm('product','product.do?act=del&productId=${product.id}')">删除</a></td>
					</tr>
				</c:forEach>
			</table>
		</fieldset>
	</div>
	<!-- /container -->
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/jquery.validate.min.js"></script>
	<script type="text/javascript" src="js/messages_zh.js"></script>
	<script type="text/javascript" src="js/probe_ajax.js"></script>
	<jsp:include page="../views/modifyProduct.jsp"/>
	<jsp:include page="../views/deleteConfirm.jsp"/>
	<script type="text/javascript">
	
	$("#creatProduct").click(function() {
		if($("#productName").val()==""){
			$("#productName").focus().select();
			return false;
		}
		$("#productForm").submit();
	});
</script>
</body>
</html>
