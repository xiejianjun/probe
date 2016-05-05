!function($,probe) {
	function _createChart(canvasId, drawData, axisY) {
		var myChart = new JSChart(canvasId, 'bar');
		myChart.setTitle("");
		myChart.setAxisNameX('');
		myChart.setAxisNameY(axisY);
		myChart.setAxisNameColorY('#999');
		myChart.setAxisNameColorX('#000');
		myChart.setAxisValuesColor('#000000');
		myChart.setAxisColor('#B5B5B5');
		myChart.setAxisWidth(1);
		myChart.setBarValues(false);
		myChart.setBarValuesColor('#000000');
		myChart.setAxisPaddingBottom(80);
		myChart.setAxisPaddingLeft(40);
		myChart.setDataArray(drawData);
		myChart.setShowXValues(true);
		myChart.setAxisValuesNumberX(5);
		myChart.setAxisPaddingTop(5);
		myChart.setAxisNameFontSizeX(10);
		myChart.setAxisValuesAngle(90);
		myChart.setTitleFontSize(11);
		myChart.setBarBorderWidth(0);
		myChart.setBarSpacingRatio(50);
		myChart.setBarOpacity(0.9);
		myChart.setFlagRadius(6);
		myChart.setTooltipPosition('nw');
		myChart.setTooltipOffset(3);
		myChart.setSize(680, 200);
		myChart.setGridColor('#C6C6C6');
		return myChart;
	};
	function parseDate(str){
		var year = str.substr(0,4);
		var month= str.substr(4);
		return year+"年"+month+"月";
	};
	function map2Array(data, sequence){
		var json = new Array();
		if ( typeof sequence !="undefined"){
			for (var i=0;i<sequence.length;i++){
				var key = sequence[i];
				json.push([key, data[key]]);
			}
		}else{
			for (var key in data){
				json.push([key,data[key]]);
			}
		}
		return json;
	};
	$(window).ready(function(){
		probe.getProductData("scgrid",function(json){
			var data = json[0].data[0];
			
			$("#compute_user_count").text(data.userNum);
			
			var updateTime = parseDate(data.updateTime.toString());
			$("#compute_watch_time").text(updateTime);
			$("#compute_chart_watch_time").text(updateTime);
			
			var chart = _createChart("supercompute", map2Array(data.usage,data.usageOrder), "利用率%");
			chart.draw();
		});
	});
}(window.jQuery,window.probe);