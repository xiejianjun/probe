<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="deleteconfirm" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="deleteconfirmmodal">
	<div class="modal modal-dialog">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 id="deletetitle">删除监测项</h4>
		</div>
		<div class="modal-body" style="height:45px">
			<label id="modalcontent" class="center" style="text-align:center;line-height:40px;font-size: x-large;">是否确认删除该项</label>
		</div>
		<div class="modal-footer">
			<input type="hidden" id="url"/>  
			<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        	<a onclick="submitDelete()" class="btn btn-danger" data-dismiss="modal">删除</a>
		</div>
	</div>
</div>