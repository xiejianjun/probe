function parseData(data){
		var jsonData=$.parseJSON(data.jsonData);
		$("#name").val(data.name);
		$("#productId").val(data.productId);
		$("#collectorId").val(data.collectorId);
		$("#description").val(data.description);
		switch(data.type){
		case 'SQL':
			$("#act").val("modifyCollector");
			$("#sql").val(jsonData.sql);
			$("#encode").val(jsonData.encode);
			var dataSources=data.datasource;
			if(dataSources!=null){
				$.each(dataSources,function(index,element){
	                $("#datasourceName").append("<option value="+element+">"+element+"</option>");
	            });
			}			
			$("#datasourceName").val(data.datasourceName);
			$("#url_group").remove();
			$("#method_group").remove();
			$("#encode_group").remove();
			$("#email_group").remove();
			break;
		case 'URL':
			$("#act").val("modifyURLCollector");
			$("#url").val(jsonData.url);
			$("#method").val(jsonData.method);
			$("#encode").val(jsonData.encode);
			$("#email").val(jsonData.email);
			$("#interval option[value='180']").remove(); 
			$("#interval option[value='360']").remove(); 
			$("#interval option[value='720']").remove(); 
			$("#interval option[value='1440']").remove(); 
			$("#datasourceName_group").remove();
			$("#sql_group").remove();
			$("#encode_group").hide();
			break;
		case 'API':
			$("#act").val("modifyAPICollector");
			$("#url").val(jsonData.url);
			$("#encode").val(jsonData.encode);
			$("#datasourceName_group").remove();
			$("#sql_group").remove();
			$("#email_group").remove();
			break;
		}
		$("#interval").val(data.interval);		
				
		
	}
function modifyCollector(para1, para2) {
	var yz = $.ajax({
		type : 'get',
		url : 'collector.do?act=getCollector&productId=' + para1
				+ '&collectorId=' + para2,
		data : {},
		dataType : 'json',
		success : function(data) {
			$("#ModifyCol").modal('show');
			parseData(data);			
		},
		error : function(data) {
			$("#ModifyCol").modal('show');
			parseData(data);
		}
	});
}
function modifyProduct(para){
	var yz = $.ajax({
		type : 'get',
		url : 'product.do?act=getProduct&productId=' + para,
		data : {},
		dataType : 'json',
		success : function(data) {
			$("#ModifyPro").modal('show');
			$("#name").val(data.name);
			$("#productId").val(data.productId);
			$("#description").val(data.description);
		},
		error : function(data) {

		}
	});
}
function deleteConfirm(type,url){
	switch(type){
	case "product":
		$("#deletetitle").html("删除产品项");
		$("#modalcontent").html("该产品及其子项将全部删除,是否确认");
		$('#url').val(url);
		$("#deleteconfirm").modal('show');		
		break;
	case "collector":
		$('#url').val(url);
		$("#deleteconfirm").modal('show');
		break;
	}
}
function submitDelete(){
	var url=$.trim($("#url").val());
	window.location.href=url;
}


$(function () {
	  $('[data-toggle="tooltip"]').tooltip()
	});



		