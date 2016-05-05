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
	    var categories = []; 
	    var datas1 = [];
	    var datas2 = [];  	    
		if (confCreateByMon!=null && confOpenByMon!=null){
			var myArray = new Array();
			var now = new Date();
			var createMap = array2map(confCreateByMon);
			var openMap = array2map(confOpenByMon);
			for ( var i = -12; i <0; i++) {
				var currentDate = addDate(now, 'm', i+1);
				var key = formatYearMonth(currentDate);
				var chartKey = "";
				chartKey = currentDate.getFullYear() + "."
				+ (currentDate.getMonth() + 1);
				categories.push(chartKey);
				datas1.push(parseInt(createMap[key]));
				datas2.push(parseInt(openMap[key]));
			}
		}
		return {category:categories, data1:datas1, data2:datas2};
	};
	
	function showConfRegisterChart(userRegByDay, userRegByDayOld) {
	    var categories = [];
	    var datas = [];  
		if (userRegByDay!=null && userRegByDayOld!=null){
			var map = {};
			mergeMap(map, userRegByDay);
			mergeMap(map, userRegByDayOld);
			var userArray = new Array();
			var now = new Date();
			for ( var i = -31; i < 0; i++) {
				var currentDate = addDate(now, 'd', i+1);
				var key = formatDate(currentDate);
				var value = map[key];
				var chartKey= (currentDate.getMonth() + 1) + "-"
				+ currentDate.getDate();
				if (typeof value == "undefined") {
					value = 0;
				}
//				userArray[i + 30] = new Array(chartKey, value);
				categories.push(chartKey);
				datas.push(value);
			}
		}
		return {category:categories, data:datas};
	};
	
	$(window).ready(function(){
		probe.getProductData("csp", function(json) {
	    	require.config({
	            paths: {
	                echarts: 'js'
	            }
	        });
			require(
		            [
		                'echarts',
		                'echarts/chart/line', // 使用柱状图就加载bar模块，按需加载
		                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		            ],
		            function (ec) {
		                var chartCspConfByMon = ec.init(document.getElementById('cspConfByMon')); 
		                var chartCspUserReg = ec.init(document.getElementById('cspUserReg')); 
		                chartCspConfByMon.showLoading({
		                    text: '正在读取数据中...',  
		                });
		                chartCspUserReg.showLoading({
		                    text: '正在读取数据中...',  
		                });
		    			var userRegNum = 0;
		    			var confStartByMon=null;
		    			var confCreateByMon=null;
		    			var userRegByDay=null;
		    			var userRegByDayOld=null;

		    			for ( var i = 0; i < json.length; i++) {
		    				var watchData = json[i];
		    				switch (watchData["collectorId"]) {
		    				case 7://userRegNum
		    					 if (watchData["data"][0]["count"]!=null)
                                     userRegNum = userRegNum + watchData["data"][0]["count"];
		    					 break;
		    				case 9://UserRegNumOld
		    					if (watchData["data"][0]["count"]!=null)
		    						userRegNum = userRegNum + watchData["data"][0]["count"];
		    					break;
		    				case 10://conf_num
		    					$("#csp_conf").text(watchData["data"][0]["count"]);
		    					break;
		    				case 11://inst_num
		    					$("#csp_inst").text(watchData["data"][0]["count"]);
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
		    			var arrrayConfNum = showConferenceChart(confCreateByMon, confStartByMon);
		    			var arrrayConfRegister = showConfRegisterChart(userRegByDay, userRegByDayOld);
		    			
		    			chartCspConfByMon.hideLoading();
		                chartCspUserReg.hideLoading();

		                option = {
		                	    title : {
		                	        text: '会议服务平台',
		                	        subtext: ''
		                	    },
		                	    tooltip : {
		                	        trigger: 'axis'
		                	    },
		                	    legend: {
		                	        data:['创建会议数','召开会议数']
		                	    },
		                	    grid: {
		                	    	x:'10%',
		                	    	y:'15%',
		                	    	y2:'25%',
		                	    	x2:'10%'
		                	    },
		                	    toolbox: {
		                	        show : false,
		                	        feature : {
		                	            mark : {show: true},
		                	            dataView : {show: true, readOnly: false},
		                	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
		                	            restore : {show: true},
		                	            saveAsImage : {show: true}
		                	        }
		                	    },
		                	    calculable : false,
		                	    xAxis : [
		                	        {
		                	            type : 'category',
		                	            data : arrrayConfNum.category,
		                	            axisLabel: {
		                	            	rotate: 60
		                	            }
		                	        }
		                	    ],
		                	    yAxis : [
		                	        {
		                	            type : 'value'
		                	        }
		                	    ],
		                	    series : [
				                	        {
				                	            name:'创建会议数',
				                	            type:'bar',
				                	            data:arrrayConfNum.data1,
				                	            markPoint : {
				                	                data : [
				                	                    {type : 'max', name: '最大值'},
				                	                ]
				                	            },
				                	            markLine : {
				                	                data : [
				                	                    {type : 'average', name : '平均值'}
				                	                ]
				                	            }
				                	        },
				                	        {
				                	            name:'召开会议数',
				                	            type:'bar',
				                	            data:arrrayConfNum.data2,
				                	            markPoint : {
				                	                data : [
				                	                    {type : 'max', name: '最大值'},
				                	                ]
				                	            },
				                	            markLine : {
				                	                data : [
				                	                    {type : 'average', name : '平均值'}
				                	                ]
				                	            }
				                	        },
				                	    ]
		                	};
		                
		                optionRegUser = {
		                	    title : {
		                	        text: '会议服务平台',
		                	        subtext: '',
		                	        show:false		                	        
		                	    },
		                	    tooltip : {
		                	        trigger: 'axis'
		                	    },
		                	    legend: {
		                	        data:['每日注册用户数']
		                	    },
		                	    grid: {
		                	    	x:'10%',
		                	    	y:'15%',
		                	    	y2:'25%',
		                	    	x2:'10%'
		                	    },
		                	    toolbox: {
		                	        show : false,
		                	        feature : {
		                	            mark : {show: true},
		                	            dataView : {show: true, readOnly: false},
		                	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
		                	            restore : {show: true},
		                	            saveAsImage : {show: true}
		                	        }
		                	    },
		                	    calculable : false,
		                	    xAxis : [
		                	        {
		                	            type : 'category',
		                	            boundaryGap : false,
		                	            data : arrrayConfRegister.category,
		                	            axisLabel: {
		                	            	rotate: 60
		                	            }
		                	        }
		                	    ],
		                	    yAxis : [
		                	        {
		                	            type : 'value'
		                	        }
		                	    ],
		                	    series : [
		                	        {
		                	            name:'每日注册用户数',
		                	            type:'line',
		                	            smooth:true,
		                	            itemStyle: {normal: {areaStyle: {type: 'default'}}},
		                	            data:arrrayConfRegister.data,
		                	            markPoint : {
		                	                data : [
		                	                    {type : 'max', name: '最大值'},
//		                	                    {type : 'min', name: '最小值'}
		                	                ]
		                	            },
		                	            markLine : {
		                	                data : [
		                	                    {type : 'average', name : '平均值'}
		                	                ]
		                	            }
		                	        }
		                	    ]
		                	};
		                
		                chartCspConfByMon.setOption(option);
		                chartCspUserReg.setOption(optionRegUser);
						});
		            });
		});
}(window.jQuery, window.probe);