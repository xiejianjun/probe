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
	
	function getShortDate(dateSrc) {
		var date = new Date(dateSrc);
		return date.getMonth()+1 + '-' + date.getDate();
	}
	
	function showDDLNumByMon(visitUsers) {
	    var categories = [];  
	    var datas = [];  
	    for ( var i = visitUsers.length-1; i >= 0; i--) {
	    	categories.push(visitUsers[i]["day"]);
	    	datas.push(visitUsers[i]["num"]);
		}
		return { category: categories, data: datas }; 
	};
	
	function showDDLNumByDay(visitUsers) {
	    var categories = [];  
	    var datas = [];  
	    for ( var i = visitUsers.length-1; i >= 0; i--) {
	    	categories.push(getShortDate(visitUsers[i]["day"]));
	    	datas.push(visitUsers[i]["num"]);
		}
		return { category: categories, data: datas }; 
	};
	
	$(window).ready(function(){
		probe.getProductData("ddl",function(json){
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
		                var myChart = ec.init(document.getElementById('ddlVisitByDay')); 
		                var chartActiveUserByMon = ec.init(document.getElementById('ddlActiveUser')); 
		                myChart.showLoading({
		                    text: '正在读取数据中...',  
		                });
		                chartActiveUserByMon.showLoading({
		                    text: '正在读取数据中...',  
		                });
		    			for ( var i = 0; i < json.length; i++) {
		    				var arrayData, arrayDataActiveUser;
		    				var watchData = json[i];
		    				switch (watchData["collectorId"]) {
		    				case 1://usernum
		    					$("#ddl_usercount").text(watchData["data"][0]["count"]);
		    					break;
		    				case 2://teamnum
		    					$("#team_count").text(watchData["data"][0]["count"]);
		    					break;
		    				case 3://resourcenum
		    					$("#resource_count").text(watchData["data"][0]["count"]);
		    					break;
		    				case 6://activeUserByMon
		    					arrayDataActiveUser = showDDLNumByMon(watchData["data"]);
		    					break;
		    				case 5://visitByDay
		    					arrayData = showDDLNumByDay(watchData["data"]);
		    					break;
		    				}
		    			}
		                myChart.hideLoading();
		                chartActiveUserByMon.hideLoading();
		               		                

		                optionActiveUser = {
		                	    title : {
		                	        text: '团队文档库',
		                	        show:true,
		                	        subtext: ''
		                	    },
		                	    tooltip : {
		                	        trigger: 'axis'
		                	    },
		                	    legend: {
		                	        data:['月活跃用户']
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
		                	            data : arrayDataActiveUser.category,
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
		                	            name:'月活跃用户',
		                	            type:'bar',
		                	            data:arrayDataActiveUser.data,
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

		                option = {
		                	    title : {
		                	        text: '团队文档库',
		                	        subtext: '',
		                	        show:false
		                	    },
		                	    tooltip : {
		                	        trigger: 'axis'
		                	    },
		                	    legend: {
		                	        data:['日访问量']
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
		                	            data : arrayData.category,
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
		                	            name:'日访问量',
		                	            type:'line',
		                	            smooth:true,
		                	            itemStyle: {normal: {areaStyle: {type: 'default'}}},
		                	            data:arrayData.data,
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
		                
						myChart.setOption(option);
						chartActiveUserByMon.setOption(optionActiveUser);
						});
		            });
		});
}(window.jQuery,window.probe);