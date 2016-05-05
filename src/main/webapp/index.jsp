<%@ page language="java" pageEncoding="UTF-8"%>
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
<title>API帮助</title>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar">
	<jsp:include page="WEB-INF/fragments/navbar.jsp" />
	<div class="container">
		<div class="row">
			<div class="span3 bs-docs-sidebar">
				<ul class="nav nav-list bs-docs-sidenav affix">
		            <li><a href="index.jsp#">★帮助列表★</a></li>
				    <li><a href="index.jsp#allProducts">查询所有产品</a></li>
					<li><a href="#allCollector">所有配置的监测项</a></li>
					<li><a href="#allDatas">所有监测项的最新值</a></li>
					<li><a href="#productData">某个监测项的最新值</a></li>
					<li><a href="#statsData">某个监测项的统计项</a></li>
					<li><a href="#chartData">监测项可视化图表</a></li>
					<li>-------------------------</li>
					<li><a href="changelog.jsp">★更新日志★</a></li>
				</ul>
			</div>
			<div class="span9">
				<section id="allProducts">
					<div class="page-header">
						<h3>查询所有产品</h3>
					</div>
					<p>查询所有的产品API可以从系统获得已配置的所有产品的列表，该接口中列举的产品的名称在后续的调用中将会被用到。
					访问入口：http://{hostname}/probe/api/list/products</p>
					<pre class="lang-javascript">["csp","ddl"]</pre>
					<p>这个列表中每一个数据都是一个已配置好的产品的名称</p>
				</section>
				<section id="allCollector">
					<div class="page-header">
						<h3>所有配置的监测项</h3>
					</div>
					<p>查询产品中配置的所有监测的项。访问入口：http://{hostname}/probe/api/list/collectors/{productName}</p>
<pre class="lang-javascript">
[ {
	id : 1,
	name : "vmtgroups"
}, {
	id : 2,
	name : "vmtusers"
} ]
</pre>
				</section>
				<section id="allDatas">
					<div class="page-header">
						<h3>所有监测项的最新值</h3>
					</div>
					<p>访问入口：http://{hostname}/probe/api/data/{productName}</p>
<pre class="lang-javascript">
[ {
	"watchTime" : "2013-07-11 16:00:10.0",
	"data" : [{
		"count" : 21
	}],
	"collectorId" : 1
}, {
	"watchTime" : "2013-07-11 15:50:10.0",
	"data" :[{
		"count" : 3
	}],
	"collectorId" : 4
}, {
	"watchTime" : "2013-07-11 16:00:10.0",
	"data" : [{
		"count" : 3
	}],
	"collectorId" : 5
} ]
</pre>
					<p>这其中watchTime是这条数据的获取时间。data字段是监控值，在目前我们只支持SQL的情况下，里边是数据库查询记录。每个字段值对应着查询SQL中的列名。</p>
				</section>
				<section id="productData">
					<div class="page-header">
						<h3>某个监测项的最新值</h3>
					</div>
					<p>访问入口：http://{hostname}/probe/api/data/{productName}/{collectorName}</p>
<pre class="lang-javascript">
[ {
	"watchTime" : "2013-07-11 16:00:10.0",
	"data" : [{
		"count" : 21
	}],
	"collectorId" : 1
}]
</pre>
					<p>这其中watchTime是这条数据的获取时间。data字段是监控值，在目前我们只支持SQL的情况下，里边是数据库查询记录。每个字段值对应着查询SQL中的列名。</p>
				</section>
				<section id="statsData">
				<div class="page-header">
					<h3>某个监测项的统计值</h3>
				</div>
				<p>访问入口：http://{hostname}/probe/api/stats/{productName}/{collectorName}?[day|month|year]=value</p>
<pre class="lang-javascript">
[ {
	"watchTime" : "2013-07-11 16:00:10.0",
	"data" : [
              {"average" : 0.998, "day": "20130710"},
              {"average" : 0.967, "day": "20130709"},
              {"average" : 0.978, "day": "20130708"},
              {"average" : 0.929, "day": "20130707"},
              {"average" : 0.997, "day": "20130706"},
              {"average" : 0.999, "day": "20130705"},
              {"average" : 1.000, "day": "20130704"}
         ],
	"collectorId" : 1
}]
</pre>
				<p>关于数值类检测项的统计，其中参数“day|month|year”表示“天|月|年”为单位，例如day=30表示之前30天的数值统计。如果监测项是URL监测，即布尔值的统计，则返回的是该时间内的平均值，即当天/月/年可用性或服务正常率百分比；如果监测项是值类型，则返回的是截止到当前天/月/年的最后值数据。</p>
			</section>

			<section id="chartData">
			<div class="page-header">
				<h3>某个监测项的可视化图表</h3>
			</div>
			<p>访问入口：http://{hostname}/probe/api/chart/{productName}/{collectorName}</p>
			<p><img src="img/chart-ex.jpg" /></p>
			<p>关于某个监测项自动生成可视化图表。</p>
		</section>
			
			</div>
		</div>
	</div>
	
	<!-- /container -->
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="prettify.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$(pre).addClass("prettyprint");
			prettyPrint();
		});
	</script>
</body>
</html>