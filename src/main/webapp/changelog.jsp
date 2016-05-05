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
					<li><a href="index.jsp#allCollector">所有配置的监测项</a></li>
					<li><a href="index.jsp#allDatas">所有监测项的最新值</a></li>
					<li><a href="index.jsp#productData">某个监测项的最新值</a></li>
					<li><a href="index.jsp#statsData">某个监测项的统计项</a></li>
					<li>-------------------------</li>
					<li  class="active"><a href="changelog.jsp#log">★更新日志★</a></li>
				</ul>
			</div>
			<div class="span9">
				<section id="log">
					<div class="page-header">
						<h3>新增功能及bug修复</h3>
					</div>			
					<p><strong>2016年3月9日(probe v1.0.7p1)</strong></p>
					<ul>
					<li>修复了一些数据项无法拉取信息的bug(Bug Fix)。</li>
					<li>修复了修改collector界面的bug(Bug Fix)。</li>
					</ul>
					<p><strong>2016年1月26日(probe v1.0.7)</strong></p>
					<ul>
					<li>增加了api/chart/{productName}/{collectorName}可视化监测项接口，对数据自动生成可视化图表。</li>
					<li>添加了产品、监测项、数据源的删除确认(Bug Fix)。</li>
					</ul>
					<p><strong>2015年11月24日(probe v1.0.6p2)</strong></p>
					<ul>
					<li>根据数值类型的统计方式调整，修复了monitor展示的js代码(Bug Fix)。</li>
					<li>修复watch_time时间显示的问题(Bug Fix)。</li>
					<li>修复了访问api/data/{productName}数据重复的问题(Bug Fix)。</li>
					</ul>
					<p><strong>2015年11月16日(probe v1.0.6p1)</strong></p>
					<ul>
					<li>修复了api/data无法访问value类型数据的问题(Bug Fix)。</li>
					<li>修复了stats/{productName}列出的collector name有误的问题(Bug Fix)。</li>
					</ul>
					<p><strong>2015年11月16日(probe v1.0.6)</strong></p>
					<ul>
					<li>增加对值（Value）类型数据的统计数据API支持。</li>
					<li>添加了api/stats/{productName}的接口，返回该产品下所有能用stats访问的collector的name。</li>
					</ul>
					<p><strong>2015年10月28日(probe v1.0.5)</strong></p>
					<ul>
					<li>合并了monitor监控可视化模块，在probe中统一发布。</li>
					<li>科技云服务监控页面，增加了对于服务正常率的可视化展示。</li>
					<li>增加了对组织通讯录的监控数据（值）展示。</li>
					</ul>
					<p><strong>2015年10月22日(probe v1.0.4)</strong></p>
					<ul>
					<li>增加了无年月日参数访问api/stats/{productName}/{collectorName}的数据接口</li>
					<li>修复了api/stats/{productName}/{collectorName}访问月份的数据出现的问题(Bug Fix)</li>
					</ul>
					<p><strong>2015年10月14日(probe v1.0.3)</strong></p>
					<ul>
					<li>增加了针对url监控的邮件预警功能（包括故障和恢复通知）</li>
					<li>对管理监控项增加了“描述”字段，对监控项进行中文描述</li>
					<li>修复了管理监控项时无法修改name的问题(Bug Fix)</li>
					</ul>
					<p><strong>2015年9月18日(probe v1.0.2p2)</strong></p>
					<ul>
					<li>统一了data和stats接口返回的json格式(Bug Fix)</li>
					<li>增加版本更新日志(Bug Fix)</li>
					</ul>
					<p><strong>2015年9月15日(probe v1.0.2p1)</strong></p>
					<ul>
					<li>修复了productName/collectorName无返回数据的问题(Bug Fix)</li>
					</ul>
					<p><strong>2015年9月15日(probe v1.0.2)</strong></p>
					<ul>
					<li>增加对网站正常率监控（URL监控），并根据监控记录每天生成统计数据</li>
					<li>增加对数值型监控数据的统计规则</li>
					</ul>
					<p><strong>2015年1月15日(probe v1.0.1)</strong></p>
					<ul>
					<li>对API接口获取的数据顺序进行了保留
					<li>修改了数据库查询语句，对查询数据效率进行了优化</li>
					</ul>
					<p><strong>2014年probe项目启动(probe v1.0.0)</strong></p>
					<ul>
					<li>分为probe和monitor项目，前者抓取数据，提供JSON访问，后者可视化图表</li>
					<li>probe可分为SQL监控和API监测项，连接SQL监控或抓取JSON数据监控</li>
					</ul>
					<hr />
					<p><strong>END</strong></p>
				</section>
			</div>
		</div>
	</div>
	<!-- /container -->
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>

</body>
</html>