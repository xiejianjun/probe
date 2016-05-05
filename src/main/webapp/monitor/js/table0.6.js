//collecor名称
var collectorName = ['cstcloud', 'scgrid', 'datacloud', 'passport', 'vmt', 'ddl', 'csp', 'egeoscience', 'astrocloud', 'cctcloud', 'starnet', 'ihepcloud', 'imcloud', 'dnayun', 'gaohansc'];
//产品名称
var productName = 'cstcloud';
//存放获取到的以日、月、年为单位的json数据
var dayData = [[]];
var monthData = [[]];
var yearData = [[]];
//存放解析后的以日、月、年为单位的数据，防止重复解析
var parseDayData = new Array(collectorName.length);
var parseMonthData = new Array(collectorName.length);
var parseYearData = new Array(collectorName.length);

//设置图表各项名称
var chartNameArr = [['科技云门户'], ['超算云服务'], ['数据云存储'], ['科技网通行证'], ['组织通讯录'], ['团队文档库'], ['会议服务平台'], ['地理生态云'], ['天文领域云'], ['洁净煤领域云'], ['空间领域云'], ['高能领域云'], ['微生物领域云'], ['干细胞领域云'], ['高寒领域云']];
var doubleYaxisArr = [];
//存放option对象的数组
var option;
//存放Echarts对象的数组
var mychart = [];
var table = $(".table");
var nowtime = [];
//动态加载select中的option
for(var i=0;i<chartNameArr.length;i++){
	$('#legendSelector').append("<option value='"+chartNameArr[i][0]+"'>"+chartNameArr[i][0]+"</option>");
}

//获取当前装载表格的对象
for (var i = 0; i < table.length; i++) {
	mychart[i] = echarts.init(document.getElementById("table" + (i + 1)));
}

//初始化Echart，画第一条线
function drawEchart(data) {
	// mychart[0].showLoading({
	// 	text: "图表数据正在努力加载..."
	// });  
	option = {
		title: {
			text: '服务正常率',
			subtext: ''
		},
		tooltip: {
			trigger: 'axis'
		},
		grid: {
			x: '10%',
			y: '30%',
			y2: '20%',
			x2: '10%'
		},
		toolbox: {
			show: true,
			feature: {
				mark: {
					show: false
				},
				dataView: {
					show: true,
					readOnly: false
				},
				magicType: {
					show: false,
					type: ['line', 'bar']
				},
				restore: {
					show: true
				},
				saveAsImage: {
					show: true
				}
			}
		},
		dataZoom: {
			show: true,
			start: 0,
		},
		legend: {
			data: [chartNameArr[0][0]],
			padding: 35,
		},
		calculable: true,
		xAxis: [{
			type: 'category',
			data: data[0].x,
		}],
		yAxis: [{
			type: 'value',
			scale: true,
			min: 0,
			max: 120,
			splitNumber: 6,
			name: '百分比%',
		}],
		series: [{
			name: chartNameArr[0][0],
			type: "line",
			data: data[0].y,
			markPoint: {
				data: [{
					type: 'max',
					name: '最大值'
				}, {
						type: 'min',
						name: '最小值'
					}]
			},
			markLine: {
				data: [{
					type: 'average',
					name: '平均值'
				}]
			}
		}]
	};
	nowtime=data[0].x;
	mychart[0].clear();
	mychart[0].setOption(option, false);
	mychart[0].restore();
}
//ajax请求获取日月年的数据,并画表
for(var i=0;i<collectorName.length;i++){
	ajaxRequest(collectorName[i],i, 'day');		
}
function drawAllEcharts(data, index) {
	option.legend.data.push(chartNameArr[index][0]);
	option.series.push({
		name: chartNameArr[index][0],
		type: "line",
		data: data[index].y,
		markPoint: {
			data: [{
				type: 'max',
				name: '最大值'
			}, {
					type: 'min',
					name: '最小值'
				}]
		},
		markLine: {
			data: [{
				type: 'average',
				name: '平均值'
			}]
		}
	});
	mychart[0].setOption(option);
}

//按年月日类型ajax请求，并画表
function ajaxRequest(collectorName, index,type) {
	switch (type) {
		case 'day':
			$.ajax({
				type: 'get',
				url: 'http://probe.escience.cn/probe/api/stats/' + productName + '/' + collectorName+ '?day=30',
				data: {},
				async: true,
				dataType: 'json',
				success: function (val) {
					$.each(val, function (i, val) {
						dayData[index] = val.data;
						//渲染以日为单位的表
						reverseJson(dayData[index]);
						parseData(dayData[index], 'day', index);
						if (index == 0) {
							drawEchart(parseDayData);
						} else {
							drawAllEcharts(parseDayData, index);
						}
					});
				},
			});
			clickFunction(document.getElementById("day_grading1"), "yy-mm-dd");
			break;
		case 'month':
			$.ajax({
				type: 'get',
				url: 'http://probe.escience.cn/probe/api/stats/' + productName + '/'
				+ collectorName+ '?month=12',
				data: {},
				dataType: 'json',
				success: function (data) {
					$.each(data, function (i, val) {
						monthData[index] = val.data;
						reverseJson(monthData[index]);						
						parseData(monthData[index], 'month', index);
						if (index == 0) {
							drawEchart(parseMonthData);
						} else {
							drawAllEcharts(parseMonthData, index);
						}
					});
				},
			});
			clickFunction(document.getElementById("month_grading1"), "yy-mm");
			break;
		case 'year':
			$.ajax({
				type: 'get',
				url: 'http://probe.escience.cn/probe/api/stats/' + productName + '/'
				+ collectorName + '?year=1',
				data: {},
				dataType: 'json',
				success: function (data) {
					$.each(data, function (i, val) {
						yearData[index] = val.data;
						reverseJson(yearData[index]);
						parseData(yearData[index], 'year', index);
						if (index == 0) {
							drawEchart(parseYearData);
						} else {
							drawAllEcharts(parseYearData, index);
						}
					});
				},
				error: function (data) {

				}
			});
			clickFunction(document.getElementById("year_grading1"), "yy");
			break;
	}
}
//翻转json数据的顺序
function reverseJson(data) {
	for (var i = data.length - 1, j = 0; i >= j; i-- , j++) {
		var temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}
}

//将json对象里的数据分离提取
function parseData(data, type, index) {
	var x = [];
	var y = [];
	var j = 0;
	var size = data != null ? data.length : 0;
	switch (type) {
		case 'day':
			for (var i = 0; i < size; i++) {
				y[i] = Number((parseFloat(data[size - i - 1]['health']) * 100)
					.toFixed(2));
				x[i] = data[size - i - 1]['day'];
			}
			parseDayData[index] = [];
			parseDayData[index].x = x;
			nowtime = x;
			parseDayData[index].y = y;
			break;
		case 'week':
			var j = 0;
			for (var i = 0; i < parseDayData[index].x.length; i += 7) {
				y.push(getArrAve(parseDayData[index].y.slice(i, i + 6)));
				x.push(++j);
			}
			nowtime = x;
			break;
		case 'month':
			for (var i = 0; i < size; i++) {
				y[i] = Number((parseFloat(data[size - i - 1]['health']) * 100)
					.toFixed(2));
				x[i] = data[size - i - 1]['month'];
			}
			nowtime = x;
			parseMonthData[index] = [];
			parseMonthData[index].x = x;
			parseMonthData[index].y = y;
			break;
		case 'year':
			for (var i = 0; i < size; i++) {
				y[i] = Number((parseFloat(data[size - i - 1]['health']) * 100)
					.toFixed(2));
				x[i] = data[size - i - 1]['year'];
			}
			parseYearData[index] = [];
			parseYearData[index].x = x;
			parseYearData[index].y = y;
			nowtime = x;
			break;
	}

	return {
		time: x,
		data: y
	};
}
//年月日视图切换
function changeEchartView(type){
	switch(type){
		case 'day':
			for (var i = 0; i < parseDayData.length; i++) {
				if (i == 0) {
					drawEchart(parseDayData);
				} else {
					drawAllEcharts(parseDayData, i);
				}
			}
			break;
		case 'week':
			var parseWeekData=[[]];
			for (var i = 0; i < parseDayData.length; i++) {
				parseWeekData[i]=[];
				parseWeekData[i].x=parseData(parseDayData,'week',i).time;
				parseWeekData[i].y=parseData(parseDayData,'week',i).data;
				if (i == 0) {
					drawEchart(parseWeekData);
				} else {
					drawAllEcharts(parseWeekData, i);
				}
			}
			break;
		case 'month':
			if (parseMonthData[0] == undefined) {
				for (var i = 0; i < collectorName.length; i++) {
					ajaxRequest(collectorName[i], i, 'month');
				}
			} else {
				for (var i = 0; i < parseMonthData.length; i++) {
					if (i == 0) {
						drawEchart(parseMonthData);
					} else {
						drawAllEcharts(parseMonthData, i);
					}
				}
			}
			break;
		case 'year':
			if(parseYearData[0]==undefined){
				for (var i = 0; i < collectorName.length; i++) {
					ajaxRequest(collectorName[i], i, 'year');
				}
			}else{
				for (var i = 0; i < parseYearData.length; i++) {
					if (i == 0) {
						drawEchart(parseYearData);
					} else {
						drawAllEcharts(parseYearData, i);
					}
				}
			}
			break;
	}
}
//为数组中每个元素设置index值
function setIndex(arr) {
	for (var i = 0; i < arr.length; i++) {
		arr[i].index = i;
	}
}
//获取用户输入的时间在时间条的位置
function getTimeIndex(time, arr) {
	for (var key in arr) {
		if (arr[key] == time)
			return key;
	}
	return -1;
}


function getArrSum(arr) {
	var sum = 0;
	for (var i in arr) {
		sum += arr[i];
	}
	return sum;
}

function getArrAve(arr) {
	var sum = 0;
	for (var i in arr) {
		sum += arr[i];
	}
	return Math.round(sum / arr.length);
	//return sum.toFixed(2);
}

function getArrTop(arr) {
	arr.sort(sortNumber);
	return arr[0];
}

function sortNumber(a, b) {
	return b - a;
}

//为按钮绑定点击事件
function clickFunction(that, placeholder) {
	var index = parseInt((that.id).slice(-1));
	clickInitial(that, index);
	$("#userinput" + index + " input").attr("placeholder", "格式" + placeholder);
}
$(".day_grading").click(function () {	
	clickFunction(this,  "yy-mm-dd");
	changeEchartView('day');
});
$(".week_grading").click(function () {	
	clickFunction(this, "number(第几周)");
	changeEchartView('week');
});
$(".month_grading").click(function () {	
	clickFunction(this, "yy-mm");
	changeEchartView('month');
});
$(".year_grading").click(function () {	
	clickFunction(this, "yy");
	changeEchartView('year');
});

$(".setting").click(function () {
	$("input").val("");
});

function clickInitial(that, index) {
	$("#setview" + index + " .spanactive").removeClass("spanactive");
	$(that).addClass("spanactive");
	$("#sum_y" + index).addClass("spanactive");
}

function changeLegendView(legendName){
	var legend= mychart[0].chart['line'].component.legend;
	if(legendName=='0'){
		return;
	}
	for(var i=0;i<chartNameArr.length;i++){
		if(legendName==chartNameArr[i][0]){
			legend.setSelected(chartNameArr[i][0],true);
		}else{
			legend.setSelected(chartNameArr[i][0],false);
		}
	}
}
