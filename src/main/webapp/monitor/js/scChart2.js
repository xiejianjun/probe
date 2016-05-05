!function($,probe) {
	function parseDate(str){
		var year = str.substr(0,4);
		var month= str.substr(4);
		return year+"年"+month+"月";
	};
	function formatNoGroupData(data, sequence) {
	    var categories = [];  
	    var datas = [];  
	    if ( typeof sequence !="undefined"){
			for (var i=0;i<sequence.length;i++){
				var key = sequence[i];
				categories.push(key);
				datas.push(data[key]);
			}
		}else{
			for (var key in data){
				categories.push(key);
				datas.push(data[key]);
			}
		}
	    return { category: categories, data: datas }; 
	}
	
	function getActiveApp(data) {
	    var categories = [];  
	    var datas = new Array();
	    for (var j=0;j<4; j++) {
	    	datas[j] = [];
	    }
	    for (var i=0;i<data.length;i++){
			categories.push(data[i].appname);
			datas[0].push(data[i].run.numJob);
			datas[1].push(data[i].run.numCore);
			datas[2].push(data[i].pend.numJob);
			datas[3].push(data[i].pend.numCore);
		}
	    return { category: categories, data: datas }; 
	}
	
	$(window).ready(function(){
		probe.getProductData("scgrid",function(json){
	    	require.config({
	            paths: {
	                echarts: 'js'
	            }
	        });
			require(
		            [
		                'echarts',
		                'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
		                'echarts/chart/line'
		            ],
		            function (ec) {
		                var myChart = ec.init(document.getElementById('supercompute'));
		                var chartSCActiveApp = ec.init(document.getElementById('scActiveApp')); 
		                myChart.showLoading({
		                    text: '正在读取数据中...',  
		                });
		                chartSCActiveApp.showLoading({
		                    text: '正在读取数据中...',  
		                });		                
		                var data = null;
		    			for ( var i = 0; i < json.length; i++) {
		    				var watchData = json[i];
		    				switch (watchData["collectorId"]) {
		    				case 19://total Usage
		    					data = watchData["data"][0];
		    					break;
		    				case 21://ActiveApp
		    					dataActiveApp = watchData["data"][0]; 
		    					break;		    					
		    				}
		    			}
						$("#compute_user_count").text(data.userNum);
						var updateTime = parseDate(data.updateTime.toString());
						$("#compute_watch_time").text(updateTime);
						$("#compute_chart_watch_time").text(updateTime);
						var arrayData = formatNoGroupData(data.usage,data.usageOrder);
						var arrarActiveApp = getActiveApp(dataActiveApp.appJobList);
						
		                myChart.hideLoading();
		                chartSCActiveApp.hideLoading();
		                
		                option = {
		                	    title : {
		                	        text: '超级计算环境',
		                	        subtext: '利用率%('+updateTime+')',
		                	        subtextStyle: {
		                	        	color: '#F3A43B',
		                	        }
		                	    },
		                	    tooltip : {
		                	        trigger: 'axis'
		                	    },
		                	    legend: {
		                	        data:['利用率(%)'],
		                	        show:false,
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
		                	            magicType : {show: true, type: ['line', 'bar']},
		                	            restore : {show: true},
		                	            saveAsImage : {show: true}
		                	        }
		                	    },
		                	    calculable : false,
		                	    xAxis : [
		                	        {
		                	            type : 'category',
		                	            data : arrayData.category,
		                	            show: true,
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
		                	            name:'利用率(%)',
		                	            type:'bar',
		                	            itemStyle: {
		                	                normal: {
		                	                    color: function(params) {
		                	                        // build a color map as your need.
		                	                        var colorList = [
		                	                          '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
		                	                           '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
		                	                           '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0',
		                	                           '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B'
		                	                        ];
		                	                        return colorList[params.dataIndex]
		                	                    },
		                	                    label: {
		                	                        show: true,
		                	                        position: 'top',
		                	                        formatter: '{c}'
//		                	                        formatter: '{b}\n{c}'
		                	                    }
		                	                }
		                	            },
		                	            data:arrayData.data,
		                	            markLine : {
		                	                data : [
		                	                    {type : 'average', name: '平均值'}
		                	                ]
		                	            }
		                	        }
		                	    ]
		                	};
		                optionSCActiveApp = {
		                	    tooltip : {
		                	        trigger: 'axis',
		                	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		                	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		                	        }
		                	    },
		                	    legend: {
		                	        data: ['运行作业','排队作业','运行核数','排队核数'],
		                	    },
		                	    grid: {
		                	    	x:'10%',
		                	    	y:'15%',
		                	    	y2:'25%',
		                	    	x2:'10%'
		                	    },
		                	    toolbox: {
		                	        show : false,
		                	        orient: 'vertical',
		                	        x: 'right',
		                	        y: 'center',
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
		                	            name: '应用名',
		                	            nameLocation: 'start',
		                	        	type : 'category',
		                	            data : arrarActiveApp.category,
		                	            axisLabel: {
		                	            	rotate: 60
		                	            }
		                	        }
		                	    ],
		                	    yAxis : [
		                	        {
		                	            type : 'value',
		                	            name:'作业数(个)',
		                	            axisLabel : {
		                	                formatter: '{value}'
		                	            }
		                	        },
		                	        {
		                	            type : 'value',
		                	            name:'核数(个)',
		                	            axisLabel : {
		                	                formatter: '{value}'
		                	            }
		                	        }
		                	    ],
		                	    series : [
		                	        {
		                	            name:'运行作业',
		                	            type:'bar',
		                	            data:arrarActiveApp.data[0],
		                	        },
		                	        {
		                	            name:'排队作业',
		                	            type:'bar',
		                	            data:arrarActiveApp.data[1],
		                	        },
		                	        {
		                	            name:'运行核数',
		                	            type:'line',
		                	            yAxisIndex: 1,
		                	            data:arrarActiveApp.data[2],
		                	        },
		                	        {
		                	            name:'排队核数',
		                	            type:'line',
		                	            yAxisIndex: 1,
		                	            data:arrarActiveApp.data[3],
		                	        },
		                	    ]
		                	};
		                
						myChart.setOption(option);
						chartSCActiveApp.setOption(optionSCActiveApp);
						});
		            });
		});
}(window.jQuery,window.probe);