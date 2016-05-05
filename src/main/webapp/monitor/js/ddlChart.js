!function($, probe) {
	function addDate(date, type, amount) {
		var y = date.getFullYear(), m = date.getMonth(), d = date.getDate();
		if (type === 'y') {
			y += amount;
		}
		if (type === 'm') {
			m += amount;
		}
		if (type === 'd') {
			d += amount;
		}
		return new Date(y, m, d);
	};
	function formatDDLYearMonth(date) {
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		if (month < 10) {
			return year + "-0" + month;
		} else {
			return year + "-" + month;
		}
	};
	function formatVisistKey(date) {
		var month = date.getMonth() + 1;
		var day = date.getDate();
		var str = "" + date.getFullYear();
		if (month < 10) {
			str = str + "-0" + month;
		} else {
			str = str + "-" + month;
		}
		;
		if (day < 10) {
			str = str + "-0" + day;
		} else {
			str = str + "-" + day;
		}
		return str;
	};
	function showDDLActiveUser(activeUsers) {
		var myArray = new Array();
		var map = {};
		for ( var i = 0; i < activeUsers.length; i++) {
			map[activeUsers[i]["day"]] = activeUsers[i]["num"];
		}
		var now = new Date();
		for ( var i = -12; i < 0; i++) {
			var currentDate = addDate(now, 'm', i);
			var key = formatDDLYearMonth(currentDate);
			var chartKey= currentDate.getFullYear() + "-"
						+ (currentDate.getMonth() + 1);
			myArray[i + 12] = new Array(chartKey, map[key]);
		}
		var activeUser = {
			axisY : '按月活跃用户数',
			data : myArray
		};
		drawLineChart('activeUsers', activeUser);
	};

	function showDDLVisitByDay(visitUsers) {
		var myArray = new Array();
		var map = {};
		for ( var i = 0; i < visitUsers.length; i++) {
			map[visitUsers[i]["day"]] = visitUsers[i]["num"];
		}
		var now = new Date();
		for ( var i = -30; i < 0; i++) {
			var currentDate = addDate(now, 'd', i);
			var key = formatVisistKey(currentDate);
			var chartKey = "";
			chartKey = (currentDate.getMonth() + 1) + "-"
					+ currentDate.getDate();
			var value=map[key];
			if (typeof value=="undefined"){
				value=0;
			}
			myArray[i + 30] = new Array(chartKey, value);
		}
		var browsLog = {
			axisY : '每天访问量',
			data : myArray
		};
		drawLineChart('browsLog', browsLog);
	};
	$(document).ready(function(){
		probe.getProductData("ddl", function(json) {
			for ( var i = 0; i < json.length; i++) {
				var watchData = json[i];
				switch (watchData["collectorId"]) {
				case 1://usernum
					$("#ddl_usercount").text(watchData["data"][0]["usernum"]);
					break;
				case 2://teamnum
					$("#team_count").text(watchData["data"][0]["teamnum"]);
					break;
				case 3://resourcenum
					$("#resource_count").text(watchData["data"][0]["resourcenum"]);
					break;
				case 6://activeUserByMon
					showDDLActiveUser(watchData["data"]);
					break;
				case 5://visitByDay
					showDDLVisitByDay(watchData["data"]);
					break;
				}
			}
		});
	});
}(window.jQuery, window.probe);