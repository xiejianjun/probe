!function($){
	window.probe={
		baseUrl:"http://probe.escience.cn/probe/api/data",
		getCollectorData:function(product, collector, callback){
			var url = this.baseUrl+"/"+product+"/"+collector;
			$.getJSON(url, callback);
		},
		getProductData:function(product, callback){
			var url = this.baseUrl+"/"+product;
			$.getJSON(url, callback);
		}
	};
}(window.jQuery);