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
<link href="css/custom.css" rel="stylesheet" media="screen">

<style>
body {
	padding-top: 60px;
}
</style>
<title>管理监控项</title>
</head>
<body>
	<jsp:include page="../fragments/navbar.jsp" />
	<div class="container">
		<h1>管理监控项</h1>		
		<jsp:include page="../fragments/datasourceTable.jsp" />

		<jsp:include page="../fragments/collectorTable.jsp" />
	</div>
	<!-- /container -->
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/jquery.validate.min.js"></script>
	<script type="text/javascript" src="js/messages_zh.js"></script>
	<script type="text/javascript" src="js/probe_ajax.js"></script>
	<script type="text/javascript" src="js/monitorchart.js"></script>
	<jsp:include page="../views/modifyCollector.jsp"/>
	<jsp:include page="../views/deleteConfirm.jsp"/>
	<jsp:include page="../fragments/addDatasourceDialog.jsp" />
	<jsp:include page="../fragments/addCollectorDialog.jsp" />
	<jsp:include page="../fragments/addAPIDialog.jsp"/>
	<jsp:include page="../fragments/addURLDialog.jsp"/>

	
</body>
</html>