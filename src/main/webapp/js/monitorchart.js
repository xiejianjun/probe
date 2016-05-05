//产品名称
var productName = $("#productName").val();
var collectorName = $("#collectorName").val();
var collectorDesc=$("#collectorDesc").val()==undefined?collectorName:$("#collectorDesc").val();
// 存放获取到的以日、月、年为单位的json数据
var dayData = [];
var monthData = [];
var yearData = [];
// 存放解析后的以日、月、年为单位的数据，防止重复解析
var parseDayData = [];
var parseMonthData = [];
var parseYearData = [];
// 设置图表各项名称
var chartNameArr = [ collectorDesc ];
var doubleYaxisArr = [];
// 存放option对象的数组
var option;
// 存放Echarts对象的数组
var mychart = [];
var nowtime = [];
//y坐标的最大长度
var maxValue=0;
mychart[0] = echarts.init(document.getElementById("table1"));

// 获取当前装载表格的对象

$.ajax({
	type : 'get',
	url : 'http://probe.escience.cn/probe/api/stats/' + productName + '/'
			+ collectorName + '?day=30',
	data : {},
	async : true,
	dataType : 'json',
	success : function(val) {
		$.each(val, function(i, val) {
			dayData = val.data;
			// 渲染以日为单位的表
			reverseJson(dayData);
			parseEchartData(dayData, 'day');
			drawEchart(parseDayData);

		});
	},
});

// 初始化Echart，并画表格
function drawEchart(data) {
	option = {
		title : {
			text : maxValue==0?'服务正常率':'服务统计',
			subtext : ''
		},
		tooltip : {
			trigger : 'axis'
		},
		grid : {
			x : '10%',
			y : '20%',
			y2 : '20%',
			x2 : '10%'
		},
		toolbox : {
			show : true,
			feature : {
				mark : {
					show : false
				},
				dataView : {
					show : true,
					readOnly : false
				},
				magicType : {
					show : false,
					type : [ 'line', 'bar' ]
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		dataZoom : {
			show : true,
			start : 0,
		},
		legend : {
			data : [ chartNameArr[0] ],
			padding : 35,
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			data : data.x,
		} ],
		yAxis : [ {
			type : 'value',
			scale : true,
			min : 0,
			max : maxRange(maxValue),
			splitNumber : maxValue==0?6:5,
			name : '百分比%',
		} ],
		series : [ {
			name : chartNameArr[0],
			type : "line",
			data : data.y,
			markPoint : {
				data : [ {
					type : 'max',
					name : '最大值'
				}, {
					type : 'min',
					name : '最小值'
				} ]
			},
			markLine : {
				data : [ {
					type : 'average',
					name : '平均值'
				} ]
			}
		} ]
	};
	nowtime = data.x;
	mychart[0].clear();
	mychart[0].setOption(option, false);
	mychart[0].restore();
}

function maxRange(maxValue){
	if(maxValue==0)
		return 120;
	else{
		var i=100;
		while(maxValue/i>1){
			i*=10;
		}
		return i;
	}
}



// 按年月日类型ajax请求，并画表
function ajaxRequest(collectorName, type) {
	switch (type) {
	case 'day':
		$.ajax({
			type : 'get',
			url : 'http://probe.escience.cn/probe/api/stats/' + productName
					+ '/' + collectorName + '?day=30',
			data : {},
			async : true,
			dataType : 'json',
			success : function(val) {
				$.each(val, function(i, val) {
					dayData = val.data;
					// 渲染以日为单位的表
					reverseJson(dayData);
					parseEchartData(dayData, 'day');
					drawEchart(parseDayData);

				});
			},
		});
		clickFunction(document.getElementById("day_grading1"), "yy-mm-dd");
		break;
	case 'month':
		$.ajax({
			type : 'get',
			url : 'http://probe.escience.cn/probe/api/stats/' + productName
					+ '/' + collectorName + '?month=12',
			data : {},
			dataType : 'json',
			success : function(data) {
				$.each(data, function(i, val) {
					monthData = val.data;
					reverseJson(monthData);
					parseEchartData(monthData, 'month');
					drawEchart(parseMonthData);
				});
			},
		});
		clickFunction(document.getElementById("month_grading1"), "yy-mm");
		break;
	case 'year':
		$.ajax({
			type : 'get',
			url : 'http://probe.escience.cn/probe/api/stats/' + productName
					+ '/' + collectorName + '?year=1',
			data : {},
			dataType : 'json',
			success : function(data) {
				$.each(data, function(i, val) {
					yearData = val.data;
					reverseJson(yearData);
					parseEchartData(yearData, 'year');
					drawEchart(parseYearData);
				});
			},
			error : function(data) {

			}
		});
		clickFunction(document.getElementById("year_grading1"), "yy");
		break;
	}
}
// 翻转json数据的顺序
function reverseJson(data) {
	for ( var i = data.length - 1, j = 0; i >= j; i--, j++) {
		var temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}
}

// 将json对象里的数据分离提取
function parseEchartData(data, type) {
	var x = [];
	var y = [];
	var j = 0;
	var size = data != null ? data.length : 0;
	switch (type) {
	case 'day':
		for ( var i = 0; i < size; i++) {		
			y[i] = Number((parseFloat(data[size - i - 1]['health']) * 100)
					.toFixed(2));
			if(data[size - i - 1]['health']==undefined){
				y[i]=parseInt(data[size - i - 1]['count']);
				if(maxValue<y[i])
					maxValue=y[i];
			}				
			x[i] = data[size - i - 1]['day'];
		}
		
		parseDayData = [];
		parseDayData.x = x;
		nowtime = x;
		parseDayData.y = y;
		break;
	case 'week':
		var j = 0;
		for ( var i = 0; i < parseDayData.x.length; i += 7) {
			y.push(getArrAve(parseDayData.y.slice(i, i + 6)));
			x.push(++j);
		}
		nowtime = x;
		break;
	case 'month':
		for ( var i = 0; i < size; i++) {
			y[i] = Number((parseFloat(data[size - i - 1]['health']) * 100)
					.toFixed(2));
			if(data[size - i - 1]['health']==undefined){
				y[i]=parseInt(data[size - i - 1]['count']);
				if(maxValue<y[i])
					maxValue=y[i];
			}
			x[i] = data[size - i - 1]['month'];
		}
		nowtime = x;
		parseMonthData = [];
		parseMonthData.x = x;
		parseMonthData.y = y;
		break;
	case 'year':
		for ( var i = 0; i < size; i++) {
			y[i] = Number((parseFloat(data[size - i - 1]['health']) * 100)
					.toFixed(2));
			if(data[size - i - 1]['health']==undefined){
				y[i]=parseInt(data[size - i - 1]['count']);
				if(maxValue<y[i])
					maxValue=y[i];
			}
			x[i] = data[size - i - 1]['year'];
		}
		parseYearData = [];
		parseYearData.x = x;
		parseYearData.y = y;
		nowtime = x;
		break;
	}

	return {
		time : x,
		data : y
	};
}
// 年月日视图切换
function changeEchartView(type) {
	switch (type) {
	case 'day':
		drawEchart(parseDayData);
		break;
	case 'week':
		var parseWeekData = [];
		parseWeekData = [];
		parseWeekData.x = parseEchartData(parseDayData, 'week').time;
		parseWeekData.y = parseEchartData(parseDayData, 'week').data;
		drawEchart(parseWeekData);
		break;
	case 'month':
		if (parseMonthData[0] == undefined) {
			ajaxRequest(collectorName,  'month');
		} else {
			drawEchart(parseMonthData);
		}
		break;
	case 'year':
		if (parseYearData[0] == undefined) {
			ajaxRequest(collectorName, 'year');
		} else {
			drawEchart(parseYearData);
		}
		break;
	}
}
// 为数组中每个元素设置index值
function setIndex(arr) {
	for ( var i = 0; i < arr.length; i++) {
		arr[i].index = i;
	}
}
// 获取用户输入的时间在时间条的位置
function getTimeIndex(time, arr) {
	for ( var key in arr) {
		if (arr[key] == time)
			return key;
	}
	return -1;
}

function getArrSum(arr) {
	var sum = 0;
	for ( var i in arr) {
		sum += arr[i];
	}
	return sum;
}

function getArrAve(arr) {
	var sum = 0;
	for ( var i in arr) {
		sum += arr[i];
	}
	return Math.round(sum / arr.length);
	// return sum.toFixed(2);
}

function getArrTop(arr) {
	arr.sort(sortNumber);
	return arr[0];
}

function sortNumber(a, b) {
	return b - a;
}

// 为按钮绑定点击事件
function clickFunction(that, placeholder) {
	var index = parseInt((that.id).slice(-1));
	clickInitial(that, index);
	$("#userinput" + index + " input").attr("placeholder", "格式" + placeholder);
}
$(".day_grading").click(function() {
	clickFunction(this, "yy-mm-dd");
	changeEchartView('day');
});
$(".week_grading").click(function() {
	clickFunction(this, "number(第几周)");
	changeEchartView('week');
});
$(".month_grading").click(function() {
	clickFunction(this, "yy-mm");
	changeEchartView('month');
});
$(".year_grading").click(function() {
	clickFunction(this, "yy");
	changeEchartView('year');
});

$(".setting").click(function() {
	$("input").val("");
});

function clickInitial(that, index) {
	$("#setview" + index + " .spanactive").removeClass("spanactive");
	$(that).addClass("spanactive");
	$("#sum_y" + index).addClass("spanactive");
}
