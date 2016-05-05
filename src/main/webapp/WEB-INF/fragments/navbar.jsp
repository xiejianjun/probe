<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
			<button type="button" class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="brand" href="index.jsp">统计数据监控服务</a>
			<div class="nav-collapse collapse">
				<ul class="nav">
					<li><a href="index.jsp">API帮助</a></li>
					<c:if test="${user!=null}">
						<li><a href="product.do">监控产品管理</a></li>
						<li><a href='<c:url value="/logout.do"/>'>注销</a></li>
						<li><a href="#">欢迎：${user.displayName}(${user.name})</a></li>
					</c:if>
					
					<c:if test="${user==null}">
						<li><a href='<c:url value="/login.do"/>'>登录</a></li>
					</c:if>
				</ul>
			</div>
		</div>
	</div>
</div>