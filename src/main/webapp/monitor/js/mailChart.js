!function($, probe){
	function drawMailCharts() {
		var json = {
				bardata : [ [ "一月", 505, 2406 ], [ "二月", 378, 1465 ],
				            [ "三月", 600, 3761 ], [ "四月", 569, 3327 ],
				            [ "五月", 609, 4373 ], [ "六月", 575, 4705 ],
				            [ "七月", 551, 5285 ] ],
				            piedata : [ [ "正常邮件", 13.3 ], [ "病毒邮件", 0 ], [ "无效邮件", 11.8 ],
				                        [ "垃圾邮件", 74.2 ], [ "策略邮件", 0.7 ] ]
		};
		var pieData = {
				axisY : '邮件类型分布',
				data : json.piedata
		};
		drawPieChart('mailPie', pieData);
		var barData = {
				axisY : '邮件类型分布',
				series1Title : '正常邮件',
				series2Title : '垃圾邮件',
				paddingLeft : 60,
				data : json.bardata
		};
		draw2Series('mailBar', barData);
	};
	$(window).ready(function(){
		drawMailCharts();	
	});
}(window.jQuery, window.probe);