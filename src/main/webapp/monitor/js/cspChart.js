!function($, probe){
	function formatDate(date) {
		var str = "" + date.getFullYear();
		var month = date.getMonth() + 1;
		if (month < 10) {
			str = str + "0" + month;
		} else {
			str = str + month;
		}
		var day = date.getDate();
		if (day < 10) {
			str = str + "0" + day;
		} else {
			str = str + day;
		}
		return str;
	};
	function mergeMap(map, data) {
		for ( var i = 0; i < data.length; i++) {
			var key = data[i]["day"];
			if (key != null) {
				var value = map[key];
				if (value != null) {
					map[key] = value + data[i]["num"];
				} else {
					map[key] = data[i]["num"];
				}
			}
		}
	};
	function formatYearMonth(date) {
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		if (month < 10) {
			return year + "0" + month;
		} else {
			return year + "" + month;
		}
	};
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
	function array2map(array) {
		var map = {};
		if (array){
			for ( var i = 0; i < array.length; i++) {
				map[array[i]["mon"]] = array[i]["confNum"];
			}
		}
		return map;
	};
	function showConferenceChart(confCreateByMon, confOpenByMon) {
		if (confCreateByMon!=null && confOpenByMon!=null){
			var myArray = new Array();
			var now = new Date();
			var createMap = array2map(confCreateByMon);
			var openMap = array2map(confOpenByMon);
			for ( var i = -12; i <0; i++) {
				var currentDate = addDate(now, 'm', i);
				var key = formatYearMonth(currentDate);
				var chartKey = "";
				chartKey = currentDate.getFullYear() + "."
				+ (currentDate.getMonth() + 1);
				myArray[i + 12] = new Array(chartKey, parseInt(createMap[key]),
						parseInt(openMap[key]));
			}
			var confData = {
					axisY : '按月会议数',
					series1Title : '按创建时间统计',
					series2Title : '按召开时间统计',
					data : myArray
			};
			draw2Series('conference', confData);
		}
	};
	function showConfRegisterChart(userRegByDay, userRegByDayOld) {
		if (userRegByDay!=null && userRegByDayOld!=null){
			var map = {};
			mergeMap(map, userRegByDay);
			mergeMap(map, userRegByDayOld);
			var userArray = new Array();
			var now = new Date();
			for ( var i = -30; i < 0; i++) {
				var currentDate = addDate(now, 'd', i);
				var key = formatDate(currentDate);
				var value = map[key];
				var chartKey= (currentDate.getMonth() + 1) + "-"
				+ currentDate.getDate();
				if (typeof value == "undefined") {
					value = 0;
				}
				userArray[i + 30] = new Array(chartKey, value);
			}
			var registerData = {
					axisY : '每日注册用户数',
					data : userArray
			};
			drawLineChart('confRegister', registerData);
		}
	};
	$(window).ready(function(){
		probe.getProductData("csp", function(json) {
			var userRegNum = 0;
			var confStartByMon=null;
			var confCreateByMon=null;
			var userRegByDay=null;
			var userRegByDayOld=null;
			for ( var i = 0; i < json.length; i++) {
				var watchData = json[i];
				switch (watchData["collectorId"]) {
				case 7://userRegNum
				case 9://UserRegNumOld
					userRegNum = userRegNum + watchData["data"][0]["usernum"];
					break;
				case 10://conf_num
					$("#csp_conf").text(watchData["data"][0]["num"]);
					break;
				case 11://inst_num
					$("#csp_inst").text(watchData["data"][0]["num"]);
					break;
				case 12://confCreateByMon
					confCreateByMon = watchData["data"];
					break;
				case 13://confStartByMon
					confStartByMon = watchData["data"];
					break;
				case 14://userRegByDay
					userRegByDay = watchData["data"];
					break;
				case 15://userRegByDayOld
					userRegByDayOld = watchData["data"];
					break;
				}
			}
			$("#csp_user").text(userRegNum);
			showConferenceChart(confCreateByMon, confStartByMon);
			showConfRegisterChart(userRegByDay, userRegByDayOld);
		});
	});
}(window.jQuery, window.probe);