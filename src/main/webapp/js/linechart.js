function _createChart(canvasId, drawData){
	var myChart = new JSChart(canvasId, 'bar');
	myChart.setDataArray(drawData.data);
	myChart.setTitle("");
	myChart.setAxisNameX('');
	myChart.setAxisNameY(drawData.axisY);
	myChart.setAxisNameColor('#999');
	myChart.setAxisValuesColor('#777');
	myChart.setAxisColor('#B5B5B5');
	myChart.setAxisWidth(1);
	myChart.setBarValues(false);
	myChart.setBarValuesColor('#2F6D99');
	myChart.setAxisPaddingBottom(60);
	myChart.setAxisPaddingLeft(45);
	myChart.setTitleFontSize(11);
	myChart.setBarBorderWidth(0);
	myChart.setBarSpacingRatio(50);
	myChart.setBarOpacity(0.9);
	myChart.setFlagRadius(6);
	myChart.setTooltipPosition('nw');
	myChart.setTooltipOffset(3);
	myChart.setSize(500, 321);
	myChart.setGridColor('#C6C6C6');
	return myChart;
}
function draw2Series(canvasId, drawData){
	var myChart = _createChart(canvasId, drawData);
	myChart.setLegendShow(true);
	myChart.setLegendPosition("top");
	myChart.setBarColor('#2D6B96', 1);
	myChart.setBarColor('#9CCEF0', 2);
	myChart.setLegendForBar(1, drawData.series1Title);
	myChart.setLegendForBar(2, drawData.series2Title);
	myChart.draw();
}

function drawLineChart(canvasId, drawData){
	var myChart = new JSChart(canvasId, 'line');
	myChart.setDataArray(drawData.data, 'red');
	myChart.setAxisPaddingBottom(40);
	myChart.setTextPaddingBottom(10);
	myChart.setAxisValuesNumberY(5);
	myChart.setIntervalStartY(0);
	myChart.setIntervalEndY(200);
	myChart.setAxisValuesNumberX(5);
	myChart.setAxisNameY(drawData.axisY);
	myChart.setAxisNameX('');
	myChart.setAxisPaddingLeft(50);
	myChart.setTitle('');
	myChart.setShowXValues(false);
	myChart.setTitleColor('#454545');
	myChart.setAxisValuesColor('#454545');
	myChart.setLineColor('#A4D314', 'green');
	myChart.setLineColor('#BBBBBB', 'gray');
	for (var i=0;i<31;i++){
		myChart.setTooltip([i]);
	}
	myChart.setFlagColor('#9D16FC');
	myChart.setFlagRadius(4);
	myChart.setSize(616, 321);
	myChart.draw();
}
function draw1Series(canvasId, drawData){
	var myChart = _createChart(canvasId, drawData);
	myChart.setBarColor('#2D6B96', 1);
	myChart.setLegendForBar(1, drawData.series1Title);
	myChart.draw();
}