<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link href="/probe/css/custom.css" rel="stylesheet" media="screen">
<title>监控图表</title>
</head>
<body>
	<div class="main">
		<div class="setview yyy" id="setview1">
			<span class="seth">粒度</span> <span id="day_grading1"
				class="day_grading setting spanactive">日粒度</span> <span
				id="week_grading1" class="week_grading setting">周粒度</span> <span
				id="month_grading1" class="month_grading setting">月粒度</span> <span
				id="year_grading1" class="year_grading setting">年粒度</span>
		</div>
		<div id="table1" class="monitortable"></div>
	</div>
	<input type="hidden" id="productName" value="${productName}" />
	<input type="hidden" id="collectorName" value="${collector.name }" />
	<input type="hidden" id="collectorDesc" value="${collector.description}"/>
	<script type="text/javascript" src="/probe/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="/probe/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/probe/js/echarts-all.js"></script>
	<script type="text/javascript" src="/probe/js/monitorchart.js"></script>
</body>
</html>