!function($,probe){
	$(window).ready(function(){
		probe.getProductData("vmt",function(json){
			for ( var i = 0; i < json.length; i++) {
				var watchData = json[i];
				switch (watchData["collectorId"]) {
				case 23://vmt_orgCount
					$("#vmt_orgCount").text(watchData["data"][0]["count"]);
					break;
				case 24://vmt_groupCount
					$("#vmt_groupCount").text(watchData["data"][0]["count"]);
					break;
				case 25://vmt_orgDchat
					$("#vmt_orgDchat").text(watchData["data"][0]["count"]);
					break;
				}
			}
		});
	});
}(window.jQuery, window.probe);