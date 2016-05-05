!function($,probe){
	$(window).ready(function(){
		probe.getCollectorData("passport","userNum",function(data, textStatus){
			if (data && data.length>0){
				$("#lastUpdate").text(data[0]["watchTime"]);
				$("#passport_count").text(data[0]["data"][0]["count"]);
			}
		});
	});
}(window.jQuery, window.probe);