!function($,probe){
	$(window).ready(function(){
		probe.getProductData("dhome", function(json) {
			for ( var i = 0; i < json.length; i++) {
				var watchData = json[i];
				switch (watchData["collectorId"]) {
				case 17://usernum
					$("#dhome_user").text(watchData["data"][0]["count"]);
					break;
				case 18://instnum
					$("#dhome_inst").text(watchData["data"][0]["count"]);
					break;
				}
			}
		});
	});
}(window.jQuery, window.probe);